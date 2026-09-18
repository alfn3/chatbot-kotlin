package com.example.chatpos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import com.example.chatpos.model.OtomaxStatus
import com.example.chatpos.model.formatSavedContactDestination
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.PrimaryLight
import com.example.chatpos.ui.theme.SuccessGreen
import com.example.chatpos.ui.theme.SuccessGreenBg
import com.example.chatpos.ui.utils.CurrencyFormatter

private fun OtomaxStatus.toDisplayText(): String = when (this) {
    OtomaxStatus.SUCCESS -> "Sukses"
    OtomaxStatus.PENDING -> "Menunggu"
    OtomaxStatus.FAILED -> "Gagal"
}

// Bot sistem membalas 1 card tunggal tiap 1x kirim walaupun ada banyak item transaksi produk
@Composable
fun TransactionSuccessCard(
    result: OtomaxBatchResult,
    onPrintReceipt: () -> Unit = {},
    onShareWhatsApp: () -> Unit = {},
    isContactChat: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showActions by remember { mutableStateOf(false) }
    val cardBackground = if (isContactChat) Color(0xFFEFF6FF) else Color.White
    val cardBorder = if (isContactChat) Color(0xFFBFDBFE) else DividerColor
    val actionColor = if (isContactChat) Color(0xFF3B82F6) else SuccessGreen

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 6.dp)
            .clickable { showActions = !showActions },
        horizontalArrangement = if (isContactChat) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.90f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBackground)
                    .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    // Header Success Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreenBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Transaksi berhasil",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BALASAN SERVER",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                ),
                                color = SuccessGreen
                            )
                        }

                        Text(
                            text = formatReceiptTimestamp(result.timeStamp),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = Color(0xFF94A3B8)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = DividerColor, thickness = 1.dp)

                    Text(
                        text = "ID Customer: ${result.customerId}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = Color(0xFF64748B)
                    )

                    // Multi-item details in ONE single card
                    result.items.forEachIndexed { index, item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${item.productCode} • ${maskDestination(item.destination)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = CurrencyFormatter.formatRupiah(item.price),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    color = Color(0xFF0F172A)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.productName,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = Color(0xFF64748B)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    if (item.adminFee > 0L) {
                                        Text(
                                            text = "Nominal ${CurrencyFormatter.formatRupiah(item.nominal)} + Admin ${CurrencyFormatter.formatRupiah(item.adminFee)}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = Color(0xFF64748B)
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                    }
                                    Text(
                                        text = "Ref ID: ${item.transactionRefId}\nSN: ${item.sn}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace
                                        ),
                                        color = SuccessGreen
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Status: ${item.status.toDisplayText()}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = if (item.status == OtomaxStatus.SUCCESS) {
                                            SuccessGreen
                                        } else {
                                            Color(0xFFF59E0B)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = DividerColor, thickness = 1.dp)

                    // Total and Remaining Balance
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total ${result.items.size} Transaksi:",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = CurrencyFormatter.formatRupiah(result.totalAmount),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = Color(0xFF0F172A)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Sisa Saldo:",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = CurrencyFormatter.formatRupiah(result.sisaSaldo),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = PrimaryLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (showActions) "Ketuk untuk menutup aksi" else "Ketuk untuk melihat aksi",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFF667781)
                    )
                }
            }

            if (showActions) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    OutlinedButton(
                        onClick = onPrintReceipt,
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0F172A))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "Cetak struk",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Cetak Struk",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }

                    Button(
                        onClick = onShareWhatsApp,
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = actionColor)
                    ) {
                        Icon(
                            imageVector = if (isContactChat) Icons.Default.Replay else Icons.Default.Share,
                            contentDescription = if (isContactChat) "Ulangi transaksi" else "Kirim nota ke WhatsApp",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isContactChat) "Ulangi Trx" else "Kirim WA",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = Color.White
                        )
                    }

                }
            }
        }
    }
}

private fun formatReceiptTimestamp(timestamp: String): String {
    val parts = timestamp.split(" ")
    val date = parts.firstOrNull()?.split("/")?.takeIf { it.size == 3 }?.let {
        "${it[0]}-${it[1]}-${it[2]}"
    } ?: parts.firstOrNull().orEmpty()
    val time = parts.getOrNull(1)?.split(":")?.take(2)?.joinToString(":").orEmpty()
    return listOf(date, time).filter { it.isNotBlank() }.joinToString(" ")
}

private fun maskDestination(destination: String): String {
    val digits = destination.filter(Char::isDigit)
    if (digits.length <= 7) return destination
    val masked = "${digits.take(4)}...${digits.takeLast(3)}"
    val contact = formatSavedContactDestination(destination)
        .substringAfter(" (", missingDelimiterValue = "")
        .removeSuffix(")")
    return if (contact.isBlank()) masked else "$masked ($contact)"
}