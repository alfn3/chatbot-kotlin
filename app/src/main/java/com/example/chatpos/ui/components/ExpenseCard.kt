package com.example.chatpos.ui.components

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.ErrorRed
import com.example.chatpos.ui.theme.PrimaryLight
import com.example.chatpos.ui.theme.SuccessGreen
import com.example.chatpos.ui.utils.CurrencyFormatter

@Composable
fun ExpenseCard(
    message: ChatMessage.ExpenseCardMessage,
    onConfirm: (ChatMessage.ExpenseCardMessage) -> Unit = {},
    onEdit: (ChatMessage.ExpenseCardMessage) -> Unit = {},
    onCancel: (ChatMessage.ExpenseCardMessage) -> Unit = {},
    onRetryUpload: (ChatMessage.ExpenseCardMessage) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.92f),
            horizontalAlignment = Alignment.End
        ) {
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
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = when {
                            message.isCancelled -> DividerColor
                            message.isConfirmed -> SuccessGreen.copy(alpha = 0.5f)
                            else -> Color(0xFFFDE68A)
                        },
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 4.dp
                        )
                    )
                    .padding(14.dp)
            ) {
                Column {
                    // Header: Pengeluaran Operasional & Category Chip
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            message.isCancelled -> Color(0xFFF1F5F9)
                                            message.isConfirmed -> Color(0xFFECFDF5)
                                            else -> Color(0xFFFFFBEB)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = when {
                                        message.isCancelled -> Color(0xFF94A3B8)
                                        message.isConfirmed -> SuccessGreen
                                        else -> Color(0xFFD97706)
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PENGELUARAN",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    fontSize = 11.sp
                                ),
                                color = Color(0xFF0F172A)
                            )
                        }

                        // Category Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEFF6FF))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = message.category,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp
                                ),
                                color = PrimaryLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Nominal
                    Text(
                        text = CurrencyFormatter.formatRupiah(message.amount),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 20.sp
                        ),
                        color = if (message.isCancelled) Color(0xFF94A3B8) else Color(0xFF0F172A)
                    )

                    // Note / Keterangan
                    if (message.note.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = message.note,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = Color(0xFF475569)
                        )
                    }

                    // Attachment status preview
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, DividerColor, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = when (message.attachmentState) {
                                        ChatMessage.ExpenseAttachmentState.SUCCESS -> Icons.AutoMirrored.Filled.ReceiptLong
                                        ChatMessage.ExpenseAttachmentState.UPLOADING -> Icons.Default.AttachFile
                                        ChatMessage.ExpenseAttachmentState.FAILED -> Icons.Default.Warning
                                        ChatMessage.ExpenseAttachmentState.NONE -> Icons.Default.Image
                                    },
                                    contentDescription = null,
                                    tint = when (message.attachmentState) {
                                        ChatMessage.ExpenseAttachmentState.SUCCESS -> SuccessGreen
                                        ChatMessage.ExpenseAttachmentState.UPLOADING -> PrimaryLight
                                        ChatMessage.ExpenseAttachmentState.FAILED -> ErrorRed
                                        ChatMessage.ExpenseAttachmentState.NONE -> Color(0xFF94A3B8)
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = when (message.attachmentState) {
                                        ChatMessage.ExpenseAttachmentState.SUCCESS ->
                                            message.attachmentName ?: "Foto Nota Terlampir"
                                        ChatMessage.ExpenseAttachmentState.UPLOADING ->
                                            "Mengunggah foto nota (simulasi)..."
                                        ChatMessage.ExpenseAttachmentState.FAILED ->
                                            "Gagal mengunggah foto nota"
                                        ChatMessage.ExpenseAttachmentState.NONE ->
                                            "Tanpa lampiran foto nota"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = when (message.attachmentState) {
                                        ChatMessage.ExpenseAttachmentState.SUCCESS -> Color(0xFF15803D)
                                        ChatMessage.ExpenseAttachmentState.UPLOADING -> PrimaryLight
                                        ChatMessage.ExpenseAttachmentState.FAILED -> ErrorRed
                                        ChatMessage.ExpenseAttachmentState.NONE -> Color(0xFF64748B)
                                    },
                                    maxLines = 1
                                )
                            }

                            if (message.attachmentState == ChatMessage.ExpenseAttachmentState.UPLOADING) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp,
                                    color = PrimaryLight
                                )
                            } else if (message.attachmentState == ChatMessage.ExpenseAttachmentState.FAILED && !message.isConfirmed) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Coba lagi",
                                    tint = ErrorRed,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { onRetryUpload(message) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = DividerColor)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Status and timestamp
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Status badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when {
                                        message.isCancelled -> Color(0xFFF1F5F9)
                                        message.isConfirmed -> Color(0xFFDCFCE7)
                                        else -> Color(0xFFFEF3C7)
                                    }
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (message.isConfirmed) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = when {
                                        message.isCancelled -> "Dibatalkan"
                                        message.isConfirmed -> "Tercatat di Kas Laci"
                                        else -> "Draft Pengeluaran"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = when {
                                        message.isCancelled -> Color(0xFF64748B)
                                        message.isConfirmed -> Color(0xFF15803D)
                                        else -> Color(0xFFB45309)
                                    }
                                )
                            }
                        }

                        Text(
                            text = message.timeString,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Color(0xFF94A3B8)
                        )
                    }

                    // Action buttons (only when draft and not cancelled)
                    if (!message.isConfirmed && !message.isCancelled) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onCancel(message) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF64748B)
                                )
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Batal", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { onEdit(message) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = PrimaryLight
                                )
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }

                            Button(
                                onClick = { onConfirm(message) },
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF16A34A)
                                )
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Konfirmasi", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
