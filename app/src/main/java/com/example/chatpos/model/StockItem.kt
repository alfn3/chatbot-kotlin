package com.example.chatpos.model

import java.util.UUID

data class StockItem(
    val id: String = UUID.randomUUID().toString(),
    val barcode: String,
    val itemCode: String,
    val itemName: String,
    val category: String,
    val systemStock: Int,
    val physicalStock: Int = systemStock,
    val unitPrice: Long = 0L,
    val lastAuditedTime: String = ""
) {
    val difference: Int get() = physicalStock - systemStock
}

object SampleStockData {
    val initialItems: List<StockItem> = listOf(
        StockItem(
            barcode = "8991001",
            itemCode = "SP-TSEL-10G",
            itemName = "Perdana Telkomsel 10GB 30 Hari",
            category = "Perdana & Voucher",
            systemStock = 25,
            physicalStock = 25,
            unitPrice = 35_000L
        ),
        StockItem(
            barcode = "8991002",
            itemCode = "SP-TRI-5G",
            itemName = "Perdana Tri Happy 5GB",
            category = "Perdana & Voucher",
            systemStock = 14,
            physicalStock = 14,
            unitPrice = 20_000L
        ),
        StockItem(
            barcode = "8991003",
            itemCode = "VC-ISAT-3G",
            itemName = "Voucher Fisik Indosat 3GB 7 Hari",
            category = "Perdana & Voucher",
            systemStock = 30,
            physicalStock = 30,
            unitPrice = 16_000L
        ),
        StockItem(
            barcode = "8991004",
            itemCode = "VC-SMART-6G",
            itemName = "Voucher Smartfren 6GB Unlimited",
            category = "Perdana & Voucher",
            systemStock = 18,
            physicalStock = 18,
            unitPrice = 25_000L
        ),
        StockItem(
            barcode = "8991005",
            itemCode = "ACC-CAB-TYPC",
            itemName = "Kabel Data Fast Charging Type-C",
            category = "Kabel & Charger",
            systemStock = 12,
            physicalStock = 12,
            unitPrice = 25_000L
        ),
        StockItem(
            barcode = "8991006",
            itemCode = "ACC-ADAPT-20W",
            itemName = "Kepala Charger 20W Quick Charge",
            category = "Kabel & Charger",
            systemStock = 8,
            physicalStock = 8,
            unitPrice = 45_000L
        ),
        StockItem(
            barcode = "8991007",
            itemCode = "ACC-TG-UNIV",
            itemName = "Tempered Glass Universal 2.5D",
            category = "Aksesoris",
            systemStock = 40,
            physicalStock = 40,
            unitPrice = 15_000L
        ),
        StockItem(
            barcode = "8991008",
            itemCode = "ACC-EAR-35",
            itemName = "Earphone Extra Bass Jack 3.5mm",
            category = "Aksesoris",
            systemStock = 15,
            physicalStock = 15,
            unitPrice = 30_000L
        )
    )

    val stockCategories = listOf("Semua", "Perdana & Voucher", "Kabel & Charger", "Aksesoris", "Ada Selisih")
}
