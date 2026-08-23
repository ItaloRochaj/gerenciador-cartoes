package com.nexcard.nextwallet.domain.repository

import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.domain.model.CardProduct
import com.nexcard.nextwallet.domain.model.Invoice
import com.nexcard.nextwallet.domain.model.ThemeMode
import com.nexcard.nextwallet.domain.model.Transaction
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String, remember: Boolean): Result<User>
    suspend fun signup(name: String, email: String, password: String): Result<Unit>
    suspend fun logout()
    fun isSessionActive(): Flow<Boolean>
    fun currentUser(): Flow<User?>
}

interface WalletRepository {
    fun observeCards(): Flow<List<Card>>
    fun observeTransactions(): Flow<List<Transaction>>
    fun observeInvoices(): Flow<List<Invoice>>
    fun observeProducts(): Flow<List<CardProduct>>
    suspend fun refreshCards(): Result<Unit>
    suspend fun refreshTransactions(): Result<Unit>
    suspend fun refreshProducts(): Result<Unit>
    suspend fun setFavorite(cardId: String, favorite: Boolean)
    suspend fun toggleBlock(cardId: String): Result<Unit>
    suspend fun deleteCard(cardId: String): Result<Unit>
    suspend fun requestCard(productId: String, style: String): Result<Unit>
    suspend fun registerPurchase(
        cardId: String,
        description: String,
        amountCents: Long,
        category: TransactionCategory,
        dateEpochMillis: Long,
    ): Result<Unit>
    suspend fun changeCardLimit(cardId: String, newLimitCents: Long): Result<Unit>
}

interface SettingsRepository {
    val themeMode: Flow<ThemeMode>
    val notificationsEnabled: Flow<Boolean>
    val lastCardId: Flow<String>
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setLastCard(cardId: String)
}

