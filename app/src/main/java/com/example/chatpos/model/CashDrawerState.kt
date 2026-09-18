package com.example.chatpos.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CashDenomination(
    val value: Long,
    val isCoin: Boolean = false,
    val count: Int = 0
) {
    val total: Long get() = value * count
}

data class CashDrawerSummary(
    val initialCash: Long = 500_000L,
    val totalCashSales: Long = 0L,
    val totalExpenses: Long = 0L,
    val actualCashCounted: Long = 0L,
    val isShiftClosed: Boolean = false,
    val shiftOpenedAt: String = "08:00 WIB",
    val shiftClosedAt: String? = null,
    val cashierName: String = "Kasir Utama"
) {
    val expectedCash: Long get() = initialCash + totalCashSales - totalExpenses
    val difference: Long get() = actualCashCounted - expectedCash
}

object DefaultDenominations {
    fun createDefaultList(): List<CashDenomination> = listOf(
        CashDenomination(100_000L, isCoin = false, count = 0),
        CashDenomination(50_000L, isCoin = false, count = 0),
        CashDenomination(20_000L, isCoin = false, count = 0),
        CashDenomination(10_000L, isCoin = false, count = 0),
        CashDenomination(5_000L, isCoin = false, count = 0),
        CashDenomination(2_000L, isCoin = false, count = 0),
        CashDenomination(1_000L, isCoin = false, count = 0),
        CashDenomination(1_000L, isCoin = true, count = 0),
        CashDenomination(500L, isCoin = true, count = 0),
        CashDenomination(200L, isCoin = true, count = 0),
        CashDenomination(100L, isCoin = true, count = 0)
    )
}
