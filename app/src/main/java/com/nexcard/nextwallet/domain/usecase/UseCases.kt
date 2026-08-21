package com.nexcard.nextwallet.domain.usecase

import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.domain.repository.AuthRepository
import com.nexcard.nextwallet.domain.repository.SettingsRepository
import com.nexcard.nextwallet.domain.repository.WalletRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, remember: Boolean) =
        authRepository.login(email, password, remember)
}

class SignupUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String) =
        authRepository.signup(name, email, password)
}

class RegisterPurchaseUseCase @Inject constructor(private val walletRepository: WalletRepository) {
    suspend operator fun invoke(
        cardId: String,
        description: String,
        amountCents: Long,
        category: TransactionCategory,
        dateEpochMillis: Long,
    ) = walletRepository.registerPurchase(cardId, description, amountCents, category, dateEpochMillis)
}

class ChangeCardLimitUseCase @Inject constructor(private val walletRepository: WalletRepository) {
    suspend operator fun invoke(cardId: String, newLimitCents: Long) =
        walletRepository.changeCardLimit(cardId, newLimitCents)
}

class ToggleCardBlockUseCase @Inject constructor(private val walletRepository: WalletRepository) {
    suspend operator fun invoke(cardId: String) = walletRepository.toggleBlock(cardId)
}

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() {
        settingsRepository.setLastCard("")
        authRepository.logout()
    }
}
