package com.nexcard.nextwallet.ui.screens.home

import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.domain.model.Transaction
import com.nexcard.nextwallet.util.ScreenLoadState

data class HomeUiState(
    val loadState: ScreenLoadState = ScreenLoadState.Idle,
    val userName: String = "Usuário",
    val primaryCard: Card? = null,
    val recentTransactions: List<Transaction> = emptyList(),
)
