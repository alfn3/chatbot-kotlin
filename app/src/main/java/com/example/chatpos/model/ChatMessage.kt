package com.example.chatpos.model

import java.util.UUID

sealed class ChatMessage(
    open val id: String = UUID.randomUUID().toString(),
    open val timeString: String = "09:41"
) {
    data class DateDivider(
        override val id: String = UUID.randomUUID().toString(),
        val label: String = "HARI INI",
        override val timeString: String = ""
    ) : ChatMessage(id, timeString)

    data class WelcomeCard(
        override val id: String = UUID.randomUUID().toString(),
        val storeName: String = "TOKO BERKAH CELL",
        val message: String = "Selamat datang di ChatPOS! Silakan pilih produk di atas atau ketik perintah transaksi (contoh: 5.089512345678).",
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    data class ExpenseContactCard(
        override val id: String = UUID.randomUUID().toString(),
        val label: String = "Pengeluaran",
        val subtitle: String = "Catat pengeluaran operasional konter",
        val totalExpense: Long = 0,
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    data class UserTransactionCardMessage(
        override val id: String = UUID.randomUUID().toString(),
        val rawCommand: String,
        val items: List<TransactionItem>,
        val totalAmount: Long,
        val customerTag: String = "#cust001",
        val isProcessed: Boolean = false,
        val isSending: Boolean = false,
        val hasProcessingError: Boolean = false,
        val duplicateWarningMinutes: Int? = null,
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    data class UserMessage(
        override val id: String = UUID.randomUUID().toString(),
        val text: String,
        val customerTag: String? = null,
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    data class SystemMessage(
        override val id: String = UUID.randomUUID().toString(),
        val title: String,
        val content: String,
        val type: MessageType = MessageType.INFO,
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    data class DuplicateWarningMessage(
        override val id: String = UUID.randomUUID().toString(),
        val title: String = "Peringatan Transaksi Berulang",
        val targetNumber: String,
        val productCode: String,
        val lastTransactedMinutesAgo: Int = 8,
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    // Consolidated single card response from system bot for 1 batch
    data class SuccessBatchReceiptMessage(
        override val id: String = UUID.randomUUID().toString(),
        val batchResult: OtomaxBatchResult,
        val replyText: String,
        val customerTag: String = "#cust001",
        val replyTimeString: String = "09:41",
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    data class ErrorBatchReceiptMessage(
        override val id: String = UUID.randomUUID().toString(),
        val batchResult: OtomaxBatchResult,
        val errorMessage: String,
        val customerTag: String = "#cust001",
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    data class CustomerIncomingMessage(
        override val id: String = UUID.randomUUID().toString(),
        val customerNumber: String,
        val text: String,
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    data class IncomingTransactionRequest(
        override val id: String = UUID.randomUUID().toString(),
        val customerNumber: String,
        val command: String,
        val productLabel: String,
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    enum class ExpenseAttachmentState {
        NONE, UPLOADING, SUCCESS, FAILED
    }

    data class ExpenseCardMessage(
        override val id: String = UUID.randomUUID().toString(),
        val rawCommand: String = "",
        val category: String,
        val amount: Long,
        val note: String,
        val attachmentState: ExpenseAttachmentState = ExpenseAttachmentState.NONE,
        val attachmentName: String? = null,
        val isConfirmed: Boolean = false,
        val isCancelled: Boolean = false,
        override val timeString: String = "09:41"
    ) : ChatMessage(id, timeString)

    enum class MessageType {
        INFO, WARNING, ERROR, SUCCESS
    }
}
