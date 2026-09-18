package com.example.chatpos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.model.OtomaxItemDetail
import com.example.chatpos.model.SampleProducts
import com.example.chatpos.viewmodel.ChatPOSViewModel
import com.example.chatpos.ui.components.BalanceStripCard
import com.example.chatpos.ui.components.ChatPOSAppBar
import com.example.chatpos.ui.components.SearchFilterBar
import com.example.chatpos.ui.components.TransactionSuccessCard
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun rupiah(value: Long): String =
    NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value).replace(",00", "")

private fun formatChatTimestamp(value: String): String {
    val source = value.trim()
    if (source.matches(Regex("""\d{2}-\d{2}-\d{4} \d{2}:\d{2}"""))) return source
    val time = source.takeIf { it.matches(Regex("""\d{2}:\d{2}""")) } ?: "00:00"
    val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    return "$date $time"
}

@Composable
fun ChatHistoryScreen(
    viewModel: ChatPOSViewModel,
    contentPadding: PaddingValues,
    onOpenChatPOS: () -> Unit,
    onDetailVisibilityChanged: (Boolean) -> Unit,
    onShareReceipt: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    val receipts = messages.filterIsInstance<ChatMessage.SuccessBatchReceiptMessage>()
    val contacts = remember(receipts) {
        receipts.flatMap { receipt ->
            receipt.batchResult.items.map { item -> receipt.customerTag to (item to receipt.batchResult.timeStamp) }
        }.groupBy { it.second.first.destination }
    }
    var selectedNumber by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var incomingText by remember { mutableStateOf("") }
    val visibleContacts = contacts.entries.filter { entry ->
        val query = searchQuery.trim()
        query.isBlank() ||
            entry.key.contains(query, ignoreCase = true) ||
            contactLabel(entry.key).contains(query, ignoreCase = true)
    }.toList()

    Column(modifier = Modifier.fillMaxSize()) {
        if (selectedNumber != null) {
            val selected = selectedNumber
            androidx.compose.runtime.LaunchedEffect(selected) {
                onDetailVisibilityChanged(true)
            }
            ChatPOSAppBar(
                title = contactLabel(selected!!),
                showOnline = false,
                onBackClick = {
                    selectedNumber = null
                    onDetailVisibilityChanged(false)
                }
            )
            val customerMessages = messages.filter { message ->
                when (message) {
                    is ChatMessage.CustomerIncomingMessage -> message.customerNumber == selected
                    is ChatMessage.IncomingTransactionRequest -> message.customerNumber == selected
                    is ChatMessage.SuccessBatchReceiptMessage ->
                        message.batchResult.items.any { it.destination == selected }
                    else -> false
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(contentPadding).weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = incomingText,
                            onValueChange = { incomingText = it },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            placeholder = { Text("Pesan masuk simulasi...") },
                            shape = RoundedCornerShape(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.simulateIncomingCustomerMessage(selected!!, incomingText)
                                incomingText = ""
                            },
                            enabled = incomingText.isNotBlank(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Kirim")
                        }
                    }
                }
                items(customerMessages, key = { it.id }) { message ->
                    when (message) {
                        is ChatMessage.CustomerIncomingMessage -> IncomingCustomerBubble(message)
                        is ChatMessage.IncomingTransactionRequest -> IncomingTransactionActionCard(
                            message = message,
                            onProcess = {
                                viewModel.prepareIncomingTransaction(message)
                                onOpenChatPOS()
                            }
                        )
                        is ChatMessage.SuccessBatchReceiptMessage -> {
                            val customerResult = message.batchResult.copy(
                                items = message.batchResult.items.filter { it.destination == selected }
                            )
                            TransactionSuccessCard(
                                result = customerResult,
                                onPrintReceipt = onShareReceipt,
                                onShareWhatsApp = onShareReceipt,
                                isContactChat = true
                            )
                        }
                        else -> Unit
                    }
                }
            }
        } else {
            ChatPOSAppBar(
                counterName = viewModel.counterName.collectAsState().value,
                title = "Chat",
                showOnline = false
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(contentPadding).weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    SearchFilterBar(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        onFilterClick = {}
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable(onClick = onOpenChatPOS),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.PointOfSale,
                                contentDescription = "Buka ChatPOS",
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(Color(0xFFDCEBFF), CircleShape)
                                    .padding(9.dp),
                                tint = Color(0xFF1E60F2)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ChatPOS", fontWeight = FontWeight.Bold)
                                Text(
                                    "Buka transaksi baru",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFE2E8F0)
                    )
                }
                if (visibleContacts.isEmpty()) {
                    item {
                        EmptyHistoryState(
                            hasSearchQuery = searchQuery.isNotBlank(),
                            onCreateTransaction = onOpenChatPOS
                        )
                    }
                }
                items(visibleContacts) { entry ->
                    val number = entry.key
                    val contactItems = entry.value
                    val latest = contactItems.last().second.first
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { selectedNumber = number },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Kontak ${contactLabel(number)}",
                                modifier = Modifier.size(42.dp).background(Color(0xFFEFF6FF), CircleShape).padding(9.dp),
                                tint = Color(0xFF1E60F2)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(contactLabel(number), fontWeight = FontWeight.Bold)
                                Text(
                                    "${latest.productCode} • ${latest.productName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                            }

                            Text("${contactItems.size} nota", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IncomingCustomerBubble(message: ChatMessage.CustomerIncomingMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.86f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PESAN MASUK",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF128C7E)
                    )
                    Text(
                        text = formatChatTimestamp(message.timeString),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(top = 7.dp),
                    color = Color(0xFFE2E8F0),
                    thickness = 1.dp
                )
                Text(
                    text = message.text,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun IncomingTransactionActionCard(
    message: ChatMessage.IncomingTransactionRequest,
    onProcess: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.86f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PERMINTAAN TRANSAKSI TERDETEKSI",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E60F2)
                    )
                    Text(
                        text = formatChatTimestamp(message.timeString),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B)
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(top = 7.dp),
                    color = Color(0xFFE2E8F0),
                    thickness = 1.dp
                )
                Text(
                    text = "${message.productLabel}\n${message.command}",
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Button(
                    onClick = onProcess,
                    modifier = Modifier.padding(top = 10.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Process Transaction")
                }
            }
        }
    }
}

@Composable
private fun EmptyHistoryState(
    hasSearchQuery: Boolean,
    onCreateTransaction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = "Tidak ada riwayat transaksi",
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = if (hasSearchQuery) "Transaksi tidak ditemukan" else "Belum ada riwayat transaksi",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = if (hasSearchQuery) {
                    "Coba ubah kata kunci atau buat transaksi baru."
                } else {
                    "Mulai transaksi baru dari halaman ChatPOS."
                },
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B),
                modifier = Modifier.padding(top = 4.dp)
            )
            Button(
                onClick = onCreateTransaction,
                modifier = Modifier.padding(top = 14.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Buat Transaksi")
            }
        }
    }
}

@Composable
private fun ReceiptItemCard(
    item: OtomaxItemDetail,
    timestamp: String,
    onShareReceipt: () -> Unit
) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ReceiptLong, contentDescription = "Detail nota", tint = Color(0xFF1E60F2))
                Spacer(modifier = Modifier.width(8.dp))
                Text(item.productName, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = onShareReceipt) {
                    Icon(Icons.Default.Share, contentDescription = "Bagikan ke WhatsApp")
                }
            }
            Text("${item.productCode}.${item.destination}", style = MaterialTheme.typography.bodySmall)
            Text(timestamp, style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
            Text("SN ${item.sn}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
            Text(rupiah(item.price), fontWeight = FontWeight.Bold, color = Color(0xFF1E60F2))
        }
    }
}

private fun contactLabel(number: String): String {
    val contact = SampleProducts.savedContacts.firstOrNull { it.phoneNumber == number }
    return contact?.let { "${it.name} ($number)" } ?: number
}
