package com.nexcard.nextwallet.ui.screens.financial

import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.domain.model.Invoice
import com.nexcard.nextwallet.domain.model.Transaction
import com.nexcard.nextwallet.domain.model.TransactionCategory
import com.nexcard.nextwallet.util.ScreenLoadState

data class FinancialUiState(
    val loadState: ScreenLoadState = ScreenLoadState.Idle,
    val cards: List<Card> = emptyList(),
    val invoices: List<Invoice> = emptyList(),
    val selectedCardId: String = "",
    val selectedReferenceMonth: String = "",
    val transactions: List<Transaction> = emptyList(),
    val selectedCategory: TransactionCategory? = null,
    val sortByValue: Boolean = false,
    val showPurchaseSheet: Boolean = false,
    val showLimitSheet: Boolean = false,
    val message: String? = null,
)
