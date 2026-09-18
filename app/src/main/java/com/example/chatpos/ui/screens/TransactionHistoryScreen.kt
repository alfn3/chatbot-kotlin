package com.example.chatpos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.model.OtomaxItemDetail
import com.example.chatpos.model.OtomaxStatus
import com.example.chatpos.viewmodel.ChatPOSViewModel
import com.example.chatpos.ui.components.ChatPOSAppBar
import com.example.chatpos.ui.components.SearchFilterBar
import com.example.chatpos.ui.theme.BackgroundCanvas
import com.example.chatpos.ui.theme.ErrorRed
import com.example.chatpos.ui.theme.PrimaryLight
import com.example.chatpos.ui.theme.SuccessGreen
import com.example.chatpos.ui.theme.WarningAmber
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private data class HistoryEntry(
    val item: OtomaxItemDetail,
    val timestamp: String,
    val remainingBalance: Long
)

private fun rupiah(value: Long): String =
    NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value).replace(",00", "")

private fun parseTimestamp(value: String): Date? {
    val formats = listOf("dd/MM/yyyy HH:mm:ss", "dd-MM-yyyy HH:mm:ss")
    return formats.firstNotNullOfOrNull { format ->
        runCatching {
            SimpleDateFormat(format, Locale.getDefault()).parse(value)
        }.getOrNull()
    }
}

private fun displayTimestamp(value: String): String =
    parseTimestamp(value)?.let {
        SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(it)
    } ?: value

@Composable
fun TransactionHistoryScreen(
    viewModel: ChatPOSViewModel,
    contentPadding: PaddingValues,
    onGoToChat: (() -> Unit)? = null
) {
    val messages by viewModel.messages.collectAsState()
    val entries = remember(messages) {
        messages.flatMap { message ->
            when (message) {
                is ChatMessage.SuccessBatchReceiptMessage -> message.batchResult.items.map {
                    HistoryEntry(it, displayTimestamp(message.batchResult.timeStamp), message.batchResult.sisaSaldo)
                }
                is ChatMessage.ErrorBatchReceiptMessage -> message.batchResult.items.map {
                    HistoryEntry(it, displayTimestamp(message.batchResult.timeStamp), message.batchResult.sisaSaldo)
                }
                else -> emptyList()
            }
        }.sortedByDescending { parseTimestamp(it.timestamp)?.time ?: Long.MIN_VALUE }
    }
    var filterQuery by remember { mutableStateOf("") }
    val visibleEntries = entries.filter { entry ->
        val query = filterQuery.trim()
        query.isBlank() ||
            entry.item.destination.contains(query, ignoreCase = true) ||
            entry.item.productName.contains(query, ignoreCase = true) ||
            entry.item.productCode.contains(query, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ChatPOSAppBar(
            counterName = viewModel.counterName.collectAsState().value,
            title = "Riwayat",
            showOnline = false
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .weight(1f),
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                SearchFilterBar(
                    value = filterQuery,
                    onValueChange = { filterQuery = it },
                    onFilterClick = {}
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (visibleEntries.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                modifier = Modifier.size(52.dp),
                                tint = Color(0xFF94A3B8)
                            )
                            Text(
                                text = if (filterQuery.isBlank()) "Belum ada riwayat transaksi"
                                       else "Tidak ada transaksi yang cocok",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = if (filterQuery.isBlank()) "Mulai buat transaksi dari tab Chat untuk melihat riwayat di sini."
                                       else "Coba ubah kata kunci pencarian.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                            if (filterQuery.isBlank()) {
                                Button(
                                    onClick = { onGoToChat?.invoke() },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "Buat Transaksi",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                items(visibleEntries) { entry ->
                    HistoryItemCard(entry)
                }
            }
        }
    }
}

@Composable
private fun HistoryItemCard(entry: HistoryEntry) {
    var isExpanded by remember { mutableStateOf(false) }
    val statusColor = when (entry.item.status) {
        OtomaxStatus.SUCCESS -> SuccessGreen
        OtomaxStatus.PENDING -> WarningAmber
        OtomaxStatus.FAILED -> ErrorRed
    }
    val statusText = when (entry.item.status) {
        OtomaxStatus.SUCCESS -> "Sukses"
        OtomaxStatus.PENDING -> "Pending"
        OtomaxStatus.FAILED -> "Gagal"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${entry.item.productCode}.${entry.item.destination}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF0F172A)
                )
                if (isExpanded) {
                    Text(
                        text = "SN ${entry.item.sn}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "${rupiah(entry.item.price)} - ${rupiah(entry.remainingBalance)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF475569)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = entry.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = statusColor
                )
            }
        }
    }
}
