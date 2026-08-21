package com.nexcard.nextwallet.ui.screens.cards

import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.util.ScreenLoadState

data class CardsUiState(
    val loadState: ScreenLoadState = ScreenLoadState.Idle,
    val cards: List<Card> = emptyList(),
    val deletingCardId: String? = null,
)
