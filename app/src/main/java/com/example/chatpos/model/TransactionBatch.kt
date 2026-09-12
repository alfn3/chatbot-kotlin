package com.example.chatpos.model

import java.util.UUID

data class TransactionItem(
    val id: String = UUID.randomUUID().toString(),
    val productCode: String,
    val productName: String,
    val destinationNumber: String,
    val price: Long,
    val customerTag: String? = null,
    val status: ItemStatus = ItemStatus.DRAFT,
    val nominal: Long = price,
    val adminFee: Long = 0L
)

enum class ItemStatus {
    DRAFT, PENDING, CONFIRMED, FAILED
}

data class TransactionBatch(
    val id: String = UUID.randomUUID().toString(),
    val customerTag: String = "#cust001",
    val items: List<TransactionItem> = emptyList(),
    val status: BatchStatus = BatchStatus.DRAFT,
    val isEditMode: Boolean = false
) {
    val totalAmount: Long
        get() = items.sumOf { it.price }

    val itemCount: Int
        get() = items.size
}

enum class BatchStatus {
    DRAFT, CONFIRMED, PROCESSING, SUCCESS, FAILED
}
