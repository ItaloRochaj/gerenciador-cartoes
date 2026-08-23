package com.nexcard.nextwallet.ui.navigation

import android.net.Uri

sealed class AppRoute(val route: String) {
    data object Login : AppRoute("login")
    data object Signup : AppRoute("signup")
    data object Home : AppRoute("home")
    data object Cards : AppRoute("cards")
    data object Financial : AppRoute("financial") {
        const val ARG_CARD_ID = "cardId"
        const val ARG_MONTH = "month"
        val routeWithArgs = "$route?$ARG_CARD_ID={$ARG_CARD_ID}&$ARG_MONTH={$ARG_MONTH}"

        fun create(cardId: String = "", month: String = ""): String {
            val args = buildList {
                if (cardId.isNotBlank()) add("$ARG_CARD_ID=${Uri.encode(cardId)}")
                if (month.isNotBlank()) add("$ARG_MONTH=${Uri.encode(month)}")
            }
            return if (args.isEmpty()) route else "$route?${args.joinToString("&")}"
        }
    }
    data object Consolidated : AppRoute("consolidated") {
        const val ARG_CARD_ID = "cardId"
        const val ARG_MONTH = "month"
        val routeWithArgs = "$route?$ARG_CARD_ID={$ARG_CARD_ID}&$ARG_MONTH={$ARG_MONTH}"

        fun create(cardId: String = "", month: String = ""): String {
            val args = buildList {
                if (cardId.isNotBlank()) add("$ARG_CARD_ID=${Uri.encode(cardId)}")
                if (month.isNotBlank()) add("$ARG_MONTH=${Uri.encode(month)}")
            }
            return if (args.isEmpty()) route else "$route?${args.joinToString("&")}"
        }
    }
    data object AddCard : AppRoute("add_card")
    data object CardDetail : AppRoute("card_detail/{cardId}") {
        fun create(cardId: String) = "card_detail/$cardId"
    }
    data object Settings : AppRoute("settings")
}
