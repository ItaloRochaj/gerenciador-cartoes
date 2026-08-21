package com.nexcard.nextwallet.domain.usecase

import com.nexcard.nextwallet.domain.model.CardStatus

object WalletRules {
    fun availableLimit(totalLimitCents: Long, usedLimitCents: Long): Long =
        (totalLimitCents - usedLimitCents).coerceAtLeast(0)

    fun canPurchase(status: CardStatus, availableLimitCents: Long, amountCents: Long): Result<Unit> {
        if (status == CardStatus.BLOCKED) {
            return Result.failure(IllegalStateException("Cartões bloqueados não podem realizar compras."))
        }
        if (amountCents > availableLimitCents) {
            return Result.failure(IllegalArgumentException("O valor da compra ultrapassa o limite disponível."))
        }
        return Result.success(Unit)
    }

    fun sortFavoritesFirst(favorite: Boolean): Int = if (favorite) 0 else 1
}
