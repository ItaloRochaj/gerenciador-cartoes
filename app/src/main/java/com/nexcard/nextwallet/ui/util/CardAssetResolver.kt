package com.nexcard.nextwallet.ui.util

/**
 * Centralized mapping to keep card artwork consistent across all screens.
 */
fun resolveCardAssetPath(
    colorStyle: String,
    productId: String? = null,
    cardId: String? = null,
): String {
    return when (colorStyle) {
        "purple_black" -> "cards/Cart  Geometric  34.png"
        "graphite" -> "cards/Cart 25.png"
        "blue_dark" -> "cards/Cart 24.jpg"
        "purple_wave_26" -> "cards/Cart 26.png"
        else -> when {
            productId == "product_wave26" -> "cards/Cart 26.png"
            cardId == "card_01" -> "cards/Cart  Geometric  34.png"
            cardId == "card_02" -> "cards/Cart 25.png"
            else -> "cards/Cart 24.jpg"
        }
    }
}

