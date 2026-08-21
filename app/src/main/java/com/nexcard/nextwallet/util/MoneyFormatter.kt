package com.nexcard.nextwallet.util

import java.text.NumberFormat
import java.util.Locale

object MoneyFormatter {
    private val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    fun format(cents: Long): String = format.format(cents / 100.0)
}
