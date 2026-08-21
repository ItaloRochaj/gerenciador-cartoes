package com.nexcard.nextwallet.ui.navigation

sealed class AppRoute(val route: String) {
    data object Login : AppRoute("login")
    data object Signup : AppRoute("signup")
    data object Home : AppRoute("home")
    data object Cards : AppRoute("cards")
    data object Financial : AppRoute("financial")
    data object AddCard : AppRoute("add_card")
    data object CardDetail : AppRoute("card_detail/{cardId}") {
        fun create(cardId: String) = "card_detail/$cardId"
    }
    data object Settings : AppRoute("settings")
}
