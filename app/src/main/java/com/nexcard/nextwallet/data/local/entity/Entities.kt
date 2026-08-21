package com.nexcard.nextwallet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nexcard.nextwallet.domain.model.CardBrand
import com.nexcard.nextwallet.domain.model.CardStatus
import com.nexcard.nextwallet.domain.model.CardType
import com.nexcard.nextwallet.domain.model.InvoiceStatus
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.domain.model.TransactionStatus

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val password: String,
)

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String,
    val productId: String,
    val holderName: String,
    val maskedNumber: String,
    val lastFourDigits: String,
    val brand: CardBrand,
    val type: CardType,
    val expirationDate: String,
    val totalLimitCents: Long,
    val usedLimitCents: Long,
    val availableLimitCents: Long,
    val status: CardStatus,
    val isFavorite: Boolean,
    val colorStyle: String,
    val isVirtual: Boolean,
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val cardId: String,
    val description: String,
    val amountCents: Long,
    val category: TransactionCategory,
    val dateEpochMillis: Long,
    val status: TransactionStatus,
)

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey val id: String,
    val cardId: String,
    val referenceMonth: String,
    val totalAmountCents: Long,
    val dueDate: String,
    val status: InvoiceStatus,
)

@Entity(tableName = "products")
data class CardProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brand: CardBrand,
    val category: String,
    val annualFeeCents: Long,
    val initialLimitCents: Long,
    val benefits: String,
    val style: String,
)

@Entity(tableName = "action_history")
data class ActionHistoryEntity(
    @PrimaryKey val id: String,
    val action: String,
    val timestamp: Long,
)
