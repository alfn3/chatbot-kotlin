package com.example.chatpos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatpos.model.OtomaxBatchResult
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.SuccessGreen
import com.example.chatpos.ui.utils.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppShareModal(
    result: OtomaxBatchResult,
    storeName: String = "TOKO BERKAH CELL",
    onDismiss: () -> Unit,
    onSendWhatsApp: (phoneNumber: String, textMessages: List<String>) -> Unit
) {
    var targetPhone by remember { mutableStateOf(result.items.firstOrNull()?.destination ?: "") }
    var separateNotes by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun receiptText(items: List<com.example.chatpos.model.OtomaxItemDetail>): String {
        val itemsText = items.joinToString("\n") { item ->
            "• ${item.productCode} ke ${item.destination}\n  SN: ${item.sn}\n  Harga: ${CurrencyFormatter.formatRupiah(item.price)}"
        }
        val total = items.sumOf { it.price }
        return """
            *STRUK TRANSAKSI - $storeName*
            Tgl: ${result.timeStamp}
            ID Customer: ${result.customerId}
            --------------------------------
            $itemsText
            --------------------------------
            *Total Bayar: ${CurrencyFormatter.formatRupiah(total)}*
            Status: *SUKSES*
            _Terima kasih telah bertransaksi di $storeName!_
        """.trimIndent()
    }

    val receiptMessages = if (separateNotes) {
        result.items.map { receiptText(listOf(it)) }
    } else {
        listOf(receiptText(result.items))
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kirim Nota WhatsApp Pelanggan",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = targetPhone,
                onValueChange = { targetPhone = it },
                label = { Text("Nomor WhatsApp Pelanggan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (result.items.size > 1) {
                Text(
                    text = "Format nota",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF334155),
                    modifier = Modifier.align(Alignment.Start)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = !separateNotes,
                            onClick = { separateNotes = false }
                        )
                        Text("1 nota untuk semua item", color = Color(0xFF334155))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = separateNotes,
                            onClick = { separateNotes = true }
                        )
                        Text("Nota terpisah tiap item", color = Color(0xFF334155))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, DividerColor, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = receiptMessages.joinToString("\n\n--- Nota berikutnya ---\n\n"),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = Color(0xFF334155)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onSendWhatsApp(targetPhone, receiptMessages) },
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Buka WhatsApp & Kirim",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
