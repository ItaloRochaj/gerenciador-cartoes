package com.nexcard.nextwallet

import com.nexcard.nextwallet.domain.model.CardStatus
import com.nexcard.nextwallet.domain.usecase.WalletRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WalletRulesTest {
    @Test
    fun `calcula limite disponivel`() {
        assertEquals(456100, WalletRules.availableLimit(700000, 243900))
    }

    @Test
    fun `compra dentro do limite`() {
        val result = WalletRules.canPurchase(CardStatus.ACTIVE, 10000, 5000)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `compra acima do limite`() {
        val result = WalletRules.canPurchase(CardStatus.ACTIVE, 1000, 5000)
        assertTrue(result.isFailure)
    }

    @Test
    fun `compra em cartao bloqueado`() {
        val result = WalletRules.canPurchase(CardStatus.BLOCKED, 10000, 5000)
        assertTrue(result.isFailure)
    }

    @Test
    fun `ordenacao de favoritos`() {
        assertTrue(WalletRules.sortFavoritesFirst(true) < WalletRules.sortFavoritesFirst(false))
    }
}
