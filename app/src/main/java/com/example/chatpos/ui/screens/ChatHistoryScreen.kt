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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import java.text.NumberFormat
import java.util.Locale

private fun rupiah(value: Long): String =
    NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value).replace(",00", "")

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

    Column(modifier = Modifier.fillMaxSize()) {
        ChatPOSAppBar(
            counterName = viewModel.counterName.collectAsState().value,
            title = "Chat",
            showOnline = false
        )
        if (selectedNumber != null) {
            val selected = selectedNumber
            androidx.compose.runtime.LaunchedEffect(selected) {
                onDetailVisibilityChanged(true)
            }
            val selectedItems = contacts[selected].orEmpty()
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(contentPadding).weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            selectedNumber = null
                            onDetailVisibilityChanged(false)
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                        }
                        Text(
                            text = contactLabel(selected!!),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
                items(selectedItems) { itemWithTime ->
                    ReceiptItemCard(
                        item = itemWithTime.second.first,
                        timestamp = itemWithTime.second.second,
                        onShareReceipt = onShareReceipt
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(contentPadding).weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Cari kontak atau nomor") },
                        shape = RoundedCornerShape(14.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenChatPOS),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PointOfSale, contentDescription = null, tint = Color(0xFF1E60F2))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("ChatPOS", fontWeight = FontWeight.Bold)
                                Text(
                                    "Buka transaksi baru",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Chat",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(
                    contacts.entries.filter { entry ->
                        val query = searchQuery.trim()
                        query.isBlank() ||
                            entry.key.contains(query, ignoreCase = true) ||
                            contactLabel(entry.key).contains(query, ignoreCase = true)
                    }.toList()
                ) { entry ->
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
                                contentDescription = null,
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
private fun ReceiptItemCard(
    item: OtomaxItemDetail,
    timestamp: String,
    onShareReceipt: () -> Unit
) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Color(0xFF1E60F2))
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
