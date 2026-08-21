package com.nexcard.nextwallet.data.repository

import com.nexcard.nextwallet.data.local.dao.ActionHistoryDao
import com.nexcard.nextwallet.data.local.dao.CardDao
import com.nexcard.nextwallet.data.local.dao.InvoiceDao
import com.nexcard.nextwallet.data.local.dao.ProductDao
import com.nexcard.nextwallet.data.local.dao.TransactionDao
import com.nexcard.nextwallet.data.local.entity.ActionHistoryEntity
import com.nexcard.nextwallet.data.local.entity.TransactionEntity
import com.nexcard.nextwallet.data.mapper.toDomain
import com.nexcard.nextwallet.data.mapper.toEntity
import com.nexcard.nextwallet.data.remote.api.NextWalletApi
import com.nexcard.nextwallet.data.remote.dto.PatchLimitRequestDto
import com.nexcard.nextwallet.data.remote.dto.PatchStatusRequestDto
import com.nexcard.nextwallet.data.remote.dto.PostCardRequestDto
import com.nexcard.nextwallet.data.remote.dto.PostTransactionRequestDto
import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.domain.model.CardStatus
import com.nexcard.nextwallet.domain.model.CardProduct
import com.nexcard.nextwallet.domain.model.Transaction
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.domain.repository.WalletRepository
import com.nexcard.nextwallet.domain.usecase.WalletRules
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class WalletRepositoryImpl @Inject constructor(
    private val api: NextWalletApi,
    private val cardDao: CardDao,
    private val transactionDao: TransactionDao,
    private val invoiceDao: InvoiceDao,
    private val productDao: ProductDao,
    private val actionHistoryDao: ActionHistoryDao,
) : WalletRepository {

    override fun observeCards(): Flow<List<Card>> = cardDao.observeAll().map { list -> list.map { it.toDomain() } }
    override fun observeTransactions(): Flow<List<Transaction>> = transactionDao.observeAll().map { list -> list.map { it.toDomain() } }
    override fun observeProducts(): Flow<List<CardProduct>> = productDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun refreshCards(): Result<Unit> = runCatching {
        cardDao.upsertAll(api.getCards().map { it.toEntity() })
    }

    override suspend fun refreshTransactions(): Result<Unit> = runCatching {
        transactionDao.upsertAll(api.getTransactions().map { it.toEntity() })
    }

    override suspend fun refreshProducts(): Result<Unit> = runCatching {
        productDao.upsertAll(api.getProducts().map { it.toEntity() })
        invoiceDao.upsertAll(api.getInvoices().map { it.toEntity() })
    }

    override suspend fun setFavorite(cardId: String, favorite: Boolean) {
        val card = cardDao.findById(cardId) ?: return
        cardDao.upsert(card.copy(isFavorite = favorite))
        actionHistoryDao.insert(ActionHistoryEntity(UUID.randomUUID().toString(), "favorite:$cardId:$favorite", System.currentTimeMillis()))
    }

    override suspend fun toggleBlock(cardId: String): Result<Unit> = runCatching {
        val card = cardDao.findById(cardId) ?: error("Cartão não encontrado.")
        val status = if (card.status == CardStatus.ACTIVE) CardStatus.BLOCKED else CardStatus.ACTIVE
        api.patchCardStatus(cardId, PatchStatusRequestDto(status.name))
        cardDao.upsert(card.copy(status = status))
        actionHistoryDao.insert(ActionHistoryEntity(UUID.randomUUID().toString(), "block:$cardId:$status", System.currentTimeMillis()))
    }

    override suspend fun deleteCard(cardId: String): Result<Unit> = runCatching {
        if (cardDao.count() <= 1) error("Não é possível excluir o único cartão.")
        cardDao.deleteById(cardId)
        actionHistoryDao.insert(ActionHistoryEntity(UUID.randomUUID().toString(), "delete:$cardId", System.currentTimeMillis()))
    }

    override suspend fun requestCard(productId: String, style: String): Result<Unit> = runCatching {
        val existing = cardDao.observeAll()
        api.createCard(PostCardRequestDto(productId, style)).also {
            cardDao.upsert(it.toEntity())
        }
        existing
    }

    override suspend fun registerPurchase(
        cardId: String,
        description: String,
        amountCents: Long,
        category: TransactionCategory,
        dateEpochMillis: Long,
    ): Result<Unit> = runCatching {
        val card = cardDao.findById(cardId) ?: error("Cartão não encontrado.")
        if (card.status == CardStatus.BLOCKED) error("Cartões bloqueados não podem realizar compras.")
        if (description.isBlank()) error("Descrição obrigatória.")
        if (amountCents <= 0) error("Informe um valor maior que zero.")
        WalletRules.canPurchase(card.status, card.availableLimitCents, amountCents).getOrThrow()

        val updated = card.copy(
            usedLimitCents = card.usedLimitCents + amountCents,
            availableLimitCents = card.availableLimitCents - amountCents,
        )
        cardDao.upsert(updated)

        val tx = api.createTransaction(
            PostTransactionRequestDto(cardId, description, amountCents, category.name, dateEpochMillis),
        )
        transactionDao.upsert(
            TransactionEntity(
                id = tx.id,
                cardId = tx.cardId,
                description = tx.description,
                amountCents = tx.amountCents,
                category = category,
                dateEpochMillis = tx.dateEpochMillis,
                status = com.nexcard.nextwallet.domain.model.TransactionStatus.APPROVED,
            ),
        )
        actionHistoryDao.insert(ActionHistoryEntity(UUID.randomUUID().toString(), "purchase:$cardId:$amountCents", System.currentTimeMillis()))
    }

    override suspend fun changeCardLimit(cardId: String, newLimitCents: Long): Result<Unit> = runCatching {
        val card = cardDao.findById(cardId) ?: error("Cartão não encontrado.")
        if (newLimitCents < 10000L) error("O limite mínimo é R$ 100,00.")
        if (newLimitCents > 2_000_000L) error("O limite máximo é R$ 20.000,00.")
        api.patchCardLimit(cardId, PatchLimitRequestDto(newLimitCents))
        cardDao.upsert(
            card.copy(
                totalLimitCents = newLimitCents,
                availableLimitCents = WalletRules.availableLimit(newLimitCents, card.usedLimitCents),
            ),
        )
        actionHistoryDao.insert(ActionHistoryEntity(UUID.randomUUID().toString(), "limit:$cardId:$newLimitCents", System.currentTimeMillis()))
    }
}
