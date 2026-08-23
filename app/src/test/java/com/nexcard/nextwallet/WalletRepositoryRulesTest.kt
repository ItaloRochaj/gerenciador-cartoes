package com.nexcard.nextwallet

import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.domain.model.CardBrand
import com.nexcard.nextwallet.domain.model.CardStatus
import com.nexcard.nextwallet.domain.model.CardType
import com.nexcard.nextwallet.domain.model.Invoice
import com.nexcard.nextwallet.domain.model.Transaction
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.domain.model.TransactionStatus
import com.nexcard.nextwallet.domain.repository.WalletRepository
import com.nexcard.nextwallet.domain.usecase.ChangeCardLimitUseCase
import com.nexcard.nextwallet.domain.usecase.ToggleCardBlockUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WalletRepositoryRulesTest {
    @Test
    fun `bloqueio e desbloqueio`() = runTest {
        val repo = FakeWalletRepository()
        ToggleCardBlockUseCase(repo)("card")
        assertEquals(CardStatus.BLOCKED, repo.card.status)
        ToggleCardBlockUseCase(repo)("card")
        assertEquals(CardStatus.ACTIVE, repo.card.status)
    }

    @Test
    fun `alteracao de limite`() = runTest {
        val repo = FakeWalletRepository()
        ChangeCardLimitUseCase(repo)("card", 900000)
        assertEquals(900000, repo.card.totalLimitCents)
        assertEquals(800000, repo.card.availableLimitCents)
    }

    @Test
    fun `tratamento de erro api`() = runTest {
        val repo = FakeWalletRepository(apiFail = true)
        assertTrue(repo.refreshCards().isFailure)
    }
}

private class FakeWalletRepository(private val apiFail: Boolean = false) : WalletRepository {
    var card = Card(
        id = "card",
        productId = "p",
        holderName = "h",
        maskedNumber = "****",
        lastFourDigits = "1234",
        brand = CardBrand.VISA,
        type = CardType.CREDIT,
        expirationDate = "12/30",
        totalLimitCents = 700000,
        usedLimitCents = 100000,
        availableLimitCents = 600000,
        status = CardStatus.ACTIVE,
        isFavorite = false,
        colorStyle = "x",
        isVirtual = true,
    )
    override fun observeCards(): Flow<List<Card>> = MutableStateFlow(listOf(card))
    override fun observeTransactions(): Flow<List<Transaction>> = flowOf(emptyList())
    override fun observeInvoices(): Flow<List<Invoice>> = flowOf(emptyList())
    override fun observeProducts() = flowOf(emptyList<com.nexcard.nextwallet.domain.model.CardProduct>())
    override suspend fun refreshCards(): Result<Unit> = if (apiFail) Result.failure(Exception("erro")) else Result.success(Unit)
    override suspend fun refreshTransactions() = Result.success(Unit)
    override suspend fun refreshProducts() = Result.success(Unit)
    override suspend fun setFavorite(cardId: String, favorite: Boolean) { card = card.copy(isFavorite = favorite) }
    override suspend fun toggleBlock(cardId: String): Result<Unit> {
        card = card.copy(status = if (card.status == CardStatus.ACTIVE) CardStatus.BLOCKED else CardStatus.ACTIVE)
        return Result.success(Unit)
    }
    override suspend fun deleteCard(cardId: String): Result<Unit> = Result.success(Unit)
    override suspend fun requestCard(productId: String, style: String): Result<Unit> = Result.success(Unit)
    override suspend fun registerPurchase(cardId: String, description: String, amountCents: Long, category: TransactionCategory, dateEpochMillis: Long): Result<Unit> = Result.success(Unit)
    override suspend fun changeCardLimit(cardId: String, newLimitCents: Long): Result<Unit> {
        card = card.copy(totalLimitCents = newLimitCents, availableLimitCents = newLimitCents - card.usedLimitCents)
        return Result.success(Unit)
    }
}
