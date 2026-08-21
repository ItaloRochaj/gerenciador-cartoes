package com.nexcard.nextwallet.ui.screens.carddetail

import com.nexcard.nextwallet.domain.model.Card
import com.nexcard.nextwallet.util.ScreenLoadState

data class CardDetailUiState(
    val loadState: ScreenLoadState = ScreenLoadState.Loading,
    val card: Card? = null,
    val showDeleteConfirm: Boolean = false,
    val showBlockConfirm: Boolean = false,
    val showVirtualCard: Boolean = false,
    val showCardBack: Boolean = false,
    val revealNumber: Boolean = false,
    val revealCvv: Boolean = false,
    val message: String? = null,
)
