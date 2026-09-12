package com.example.chatpos.ui.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val indonesianLocale = Locale("id", "ID")
    private val formatter = NumberFormat.getNumberInstance(indonesianLocale)

    fun formatRupiah(amount: Long): String {
        return "Rp " + formatter.format(amount)
    }

    fun formatShortDenom(amount: Long): String {
        return if (amount >= 1000) {
            "${amount / 1000}K"
        } else {
            amount.toString()
        }
    }
}
