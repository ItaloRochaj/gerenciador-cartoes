package com.nexcard.nextwallet.domain.model

enum class CardStatus { ACTIVE, BLOCKED }
enum class CardBrand { VISA, MASTERCARD, ELO }
enum class CardType { CREDIT, DEBIT_CREDIT }
enum class InvoiceStatus { OPEN, PAID, OVERDUE }
enum class TransactionStatus { APPROVED, DECLINED }
enum class ThemeMode { SYSTEM, LIGHT, DARK }

enum class TransactionCategory {
    MERCADO, TRANSPORTE, VIAGEM, ASSINATURAS, OUTROS
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String = "",
)

data class Card(
    val id: String,
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

data class Transaction(
    val id: String,
    val cardId: String,
    val description: String,
    val amountCents: Long,
    val category: TransactionCategory,
    val dateEpochMillis: Long,
    val status: TransactionStatus,
)

data class Invoice(
    val id: String,
    val cardId: String,
    val referenceMonth: String,
    val totalAmountCents: Long,
    val dueDate: String,
    val status: InvoiceStatus,
)

data class CardProduct(
    val id: String,
    val name: String,
    val brand: CardBrand,
    val category: String,
    val annualFeeCents: Long,
    val initialLimitCents: Long,
    val benefits: List<String>,
    val style: String,
)

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val biometricEnabled: Boolean = false,
    val lastCardId: String = "",
    val sessionActive: Boolean = false,
)

