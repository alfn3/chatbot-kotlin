package com.example.chatpos.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class OtomaxItemDetail(
    val transactionRefId: String = "TRX" + UUID.randomUUID().toString().substring(0, 8).uppercase(),
    val productCode: String,
    val productName: String,
    val destination: String,
    val price: Long,
    val nominal: Long = price,
    val adminFee: Long = 0L,
    val status: OtomaxStatus = OtomaxStatus.SUCCESS,
    val sn: String = "20260908" + (100000..999999).random().toString()
)

data class OtomaxBatchResult(
    val customerId: String = "#cust001",
    val items: List<OtomaxItemDetail>,
    val totalAmount: Long,
    val sisaSaldo: Long = 1_398_000L,
    val status: OtomaxStatus = OtomaxStatus.SUCCESS,
    val timeStamp: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
)

enum class OtomaxStatus {
    SUCCESS, PENDING, FAILED
}

// Single transaction result for backward compatibility
data class OtomaxTransactionResult(
    val refId: String = "TRX" + UUID.randomUUID().toString().substring(0, 8).uppercase(),
    val productCode: String,
    val productName: String,
    val destination: String,
    val price: Long,
    val sn: String = "20260908" + (100000..999999).random().toString(),
    val sisaSaldo: Long = 1_406_000L,
    val status: OtomaxStatus = OtomaxStatus.SUCCESS,
    val timeStamp: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
)
