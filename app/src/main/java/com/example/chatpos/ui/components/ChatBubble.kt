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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.model.formatSavedContactDestination
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.PrimaryLight
import com.example.chatpos.ui.theme.SuccessGreen
import com.example.chatpos.ui.theme.SuccessGreenBg
import com.example.chatpos.ui.utils.CurrencyFormatter

// WhatsApp Style Date Divider ("HARI INI")
@Composable
fun WhatsAppDateDivider(
    label: String = "HARI INI",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(9999.dp))
                .background(Color(0xFFE2E8F0).copy(alpha = 0.8f))
                .padding(horizontal = 14.dp, vertical = 4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp
                ),
                color = Color(0xFF475569)
            )
        }
    }
}

// Initial Welcome Greeting Card (Only shown when there are no transactions yet)
@Composable
fun WelcomeSystemCard(
    storeName: String = "TOKO BERKAH CELL",
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, DividerColor, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF4FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = null,
                        tint = PrimaryLight,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = storeName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        ),
                        color = Color(0xFF475569)
                    )
                }
            }
        }
    }
}

// User Transaction Card - Menjorok ke kanan (Right-aligned)
// Menampilkan output hasil input lebih dahulu, menunggu klik "Proses" baru diteruskan ke sistem OtomaX!
@Composable
fun UserTransactionCard(
    message: ChatMessage.UserTransactionCardMessage,
    onProcessClicked: (ChatMessage.UserTransactionCardMessage) -> Unit = {},
    onEditClicked: (ChatMessage.UserTransactionCardMessage) -> Unit = {},
    onRetryClicked: (ChatMessage.UserTransactionCardMessage) -> Unit = {},
    onCopyTransaction: (ChatMessage.UserTransactionCardMessage) -> Unit = {},
    onDuplicateCancel: (ChatMessage.UserTransactionCardMessage) -> Unit = {},
    onDuplicateProceed: (ChatMessage.UserTransactionCardMessage) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showActions by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            modifier = modifier.fillMaxWidth(0.90f),
            horizontalAlignment = Alignment.End
        ) {
            message.duplicateWarningMinutes?.let { minutes ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFE58A00), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Peringatan transaksi berulang • ${minutes} menit lalu",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFB45309)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 4.dp
                        )
                    )
                    .background(
                        if (message.duplicateWarningMinutes != null) Color(0xFFFFB74D)
                        else PrimaryLight
                    )
                    .clickable { if (message.isProcessed) showActions = !showActions }
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = message.customerTag,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = if (message.duplicateWarningMinutes != null) Color(0xFF1F2937) else Color.White
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = message.timeString,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.55f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(6.dp))
                    message.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.productCode} • ${formatSavedContactDestination(item.destinationNumber)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    color = if (message.duplicateWarningMinutes != null) Color(0xFF1F2937) else Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.productName,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = if (message.duplicateWarningMinutes != null) Color(0xFF374151) else Color.White.copy(alpha = 0.82f)
                                )
                                if (item.adminFee > 0L) {
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "Nominal ${CurrencyFormatter.formatRupiah(item.nominal)} + Admin ${CurrencyFormatter.formatRupiah(item.adminFee)}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = if (message.duplicateWarningMinutes != null) Color(0xFF374151) else Color.White.copy(alpha = 0.82f)
                                    )
                                }
                            }
                            Text(
                                text = CurrencyFormatter.formatRupiah(item.price),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = if (message.duplicateWarningMinutes != null) Color(0xFF1F2937) else Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.55f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total ${message.items.size} transaksi",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = if (message.duplicateWarningMinutes != null) Color(0xFF374151) else Color.White.copy(alpha = 0.82f)
                        )
                        Text(
                            text = CurrencyFormatter.formatRupiah(message.totalAmount),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Monospace
                            ),
                            color = if (message.duplicateWarningMinutes != null) Color(0xFF1F2937) else Color.White
                        )
                    }

                }
            }

            if (message.isSending) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(12.dp),
                        strokeWidth = 2.dp,
                        color = PrimaryLight
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Mengirim...",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = PrimaryLight
                    )
                }
            }

            if (message.isProcessed && !message.isSending && showActions) {
                if (message.hasProcessingError) {
                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onEditClicked(message) },
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryLight)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit transaksi",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onRetryClicked(message) },
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = "Ulangi transaksi",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ulang Trx", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { onCopyTransaction(message) },
                        modifier = Modifier.padding(top = 6.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryLight)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Salin transaksi",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy Trx", fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (message.duplicateWarningMinutes != null && !message.isProcessed && !message.isSending) {
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDuplicateCancel(message) },
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text("Batalkan", fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = { onDuplicateProceed(message) },
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B))
                    ) {
                        Text("Tetap Proses", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            } else if (!message.isProcessed && !message.isSending) {
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onEditClicked(message) },
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Edit",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                    Button(
                        onClick = { onProcessClicked(message) },
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF128C7E))
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Proses Sekarang",
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

// Simple text bubble (fallback)
@Composable
fun UserMessageBubble(
    text: String,
    customerTag: String? = null,
    timeString: String = "09:41",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 4.dp
                    )
                )
                .background(PrimaryLight)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Column {
                if (!customerTag.isNullOrBlank()) {
                    Text(
                        text = customerTag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = Color(0xFFDCE9FF)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun SystemMessageCard(
    title: String,
    content: String,
    type: ChatMessage.MessageType = ChatMessage.MessageType.INFO,
    timeString: String = "09:41",
    modifier: Modifier = Modifier
) {
    val titleColor = when (type) {
        ChatMessage.MessageType.ERROR -> MaterialTheme.colorScheme.error
        ChatMessage.MessageType.WARNING -> Color(0xFFF59E0B)
        ChatMessage.MessageType.SUCCESS -> Color(0xFF10B981)
        ChatMessage.MessageType.INFO -> PrimaryLight
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, DividerColor, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = titleColor
                    )
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFF94A3B8)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
