package com.nexcard.nextwallet.ui.screens.addcard

import com.nexcard.nextwallet.domain.model.CardProduct
import com.nexcard.nextwallet.util.ScreenLoadState

data class AddCardUiState(
    val loadState: ScreenLoadState = ScreenLoadState.Idle,
    val products: List<CardProduct> = emptyList(),
    val selectedProductId: String = "",
    val selectedStyle: String = "purple_wave_26",
    val isRequesting: Boolean = false,
    val message: String? = null,
)
