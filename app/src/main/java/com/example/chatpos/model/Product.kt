package com.example.chatpos.model

data class ProductItem(
    val code: String,
    val name: String,
    val category: String,
    val operator: String,
    val denomination: Long,
    val modalPrice: Long,
    val sellPrice: Long,
    val validity: String = "Aktif 30 Hari",
    val isAvailable: Boolean = true
)

data class SavedContact(
    val name: String,
    val phoneNumber: String,
    val tag: String = "Pelanggan"
)

fun formatSavedContactDestination(phoneNumber: String): String {
    val normalized = phoneNumber.filter(Char::isDigit)
    val contact = SampleProducts.savedContacts.firstOrNull {
        it.phoneNumber.filter(Char::isDigit) == normalized
    }
    return if (contact == null) phoneNumber else "$phoneNumber (${contact.name})"
}

object SampleProducts {
    // Quick product list including numeric codes requested by user: 5, 50, 500, etc.
    val allProducts = listOf(
        ProductItem("5", "Pulsa 5.000", "Pulsa", "Reguler", 5_000L, 5_650L, 7_000L),
        ProductItem("10", "Pulsa 10.000", "Pulsa", "Reguler", 10_000L, 10_450L, 12_000L),
        ProductItem("20", "Pulsa 20.000", "Pulsa", "Reguler", 20_000L, 20_150L, 22_000L),
        ProductItem("25", "Pulsa 25.000", "Pulsa", "Reguler", 25_000L, 25_000L, 27_000L),
        ProductItem("50", "Pulsa 50.000", "Pulsa", "Reguler", 50_000L, 49_800L, 52_000L),
        ProductItem("100", "Pulsa 100.000", "Pulsa", "Reguler", 100_000L, 98_500L, 102_000L),
        ProductItem("500", "Voucher Saldo 500.000", "E-Wallet", "Fintech", 500_000L, 498_000L, 502_000L),
        ProductItem("S5", "Telkomsel 5K", "Pulsa", "Telkomsel", 5_000L, 5_650L, 7_000L),
        ProductItem("S10", "Telkomsel 10K", "Pulsa", "Telkomsel", 10_000L, 10_450L, 12_000L),
        ProductItem("S20", "Telkomsel 20K", "Pulsa", "Telkomsel", 20_000L, 20_150L, 22_000L),
        ProductItem("T5", "Tri 5K", "Pulsa", "Three", 5_000L, 5_150L, 7_000L),
        ProductItem("T10", "Tri 10K", "Pulsa", "Three", 10_000L, 10_100L, 12_000L),
        ProductItem("PLN20", "Token Listrik 20K", "PLN", "PLN", 20_000L, 20_500L, 23_000L),
        ProductItem("PLN50", "Token Listrik 50K", "PLN", "PLN", 50_000L, 50_500L, 53_000L),
        ProductItem("DANA10", "Saldo DANA 10K", "E-Wallet", "DANA", 10_000L, 10_500L, 12_000L)
    )

    // Saved contacts in system / phone
    val savedContacts = listOf(
        SavedContact("Budi Santoso", "089512345678", "Pelanggan Tetap"),
        SavedContact("Siti Konter 2", "089512399881", "Konter Cabang"),
        SavedContact("Ahmad Cell", "089512355443", "Grosir"),
        SavedContact("Rian Pulsa", "081234567890", "Pelanggan"),
        SavedContact("Dewi Store", "085298765432", "Pelanggan Tetap"),
        SavedContact("Kios Barokah", "089566778899", "Langganan")
    )

    fun findProduct(code: String): ProductItem? {
        val clean = code.trim().uppercase()
        return allProducts.firstOrNull { it.code.uppercase() == clean }
    }

    fun filterProductsByPrefix(prefix: String): List<ProductItem> {
        val clean = prefix.trim().uppercase()
        if (clean.isEmpty()) return allProducts.take(8)
        return allProducts.filter { it.code.uppercase().startsWith(clean) }
    }

    fun filterContactsByQuery(query: String): List<SavedContact> {
        val clean = query.trim().replace(" ", "").replace("-", "")
        if (clean.length < 3) return emptyList()
        return savedContacts.filter {
            it.phoneNumber.contains(clean) || it.name.contains(clean, ignoreCase = true)
        }
    }
}
