package com.example.chatpos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.model.SampleProducts
import com.example.chatpos.viewmodel.ChatPOSViewModel
import com.example.chatpos.ui.components.BalanceStripCard
import com.example.chatpos.ui.components.ChatPOSAppBar

@Composable
fun TransactionHistoryScreen(viewModel: ChatPOSViewModel, contentPadding: PaddingValues) {
    val messages by viewModel.messages.collectAsState()
    val transactions = messages
        .filterIsInstance<ChatMessage.SuccessBatchReceiptMessage>()
        .flatMap { receipt ->
            receipt.batchResult.items.map { it to receipt.batchResult.timeStamp }
        }
        .groupBy { it.first.destination }
    var filterQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        ChatPOSAppBar(
            counterName = viewModel.counterName.collectAsState().value,
            title = "Riwayat",
            showOnline = false
        )
        LazyColumn(
        modifier = Modifier.fillMaxSize().padding(contentPadding).weight(1f),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            OutlinedTextField(
                value = filterQuery,
                onValueChange = { filterQuery = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Filter nomor, kontak, atau produk") },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Riwayat",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
        items(
            transactions.entries.filter { entry ->
                val query = filterQuery.trim()
                query.isBlank() ||
                    entry.key.contains(query, ignoreCase = true) ||
                    contactLabel(entry.key).contains(query, ignoreCase = true) ||
                    entry.value.any { (item, _) ->
                        item.productName.contains(query, ignoreCase = true) ||
                            item.productCode.contains(query, ignoreCase = true)
                    }
            }.toList()
        ) { (number, items) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = contactLabel(number),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    items.forEach { (item, timestamp) ->
                        Text(
                            text = "${item.productCode}.${item.destination} • ${item.productName} • ${item.price}\n$timestamp",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF475569),
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        }
                    }
                }
            }
        }
    }
}

private fun contactLabel(number: String): String {
    val contact = SampleProducts.savedContacts.firstOrNull { it.phoneNumber == number }
    return contact?.let { "${it.name} ($number)" } ?: number
}
