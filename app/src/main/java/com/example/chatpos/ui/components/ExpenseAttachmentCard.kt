package com.example.chatpos.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.ErrorRed
import com.example.chatpos.ui.theme.PrimaryLight
import com.example.chatpos.ui.theme.SuccessGreen

@Composable
fun ExpenseAttachmentCard(
    attachmentState: ChatMessage.ExpenseAttachmentState = ChatMessage.ExpenseAttachmentState.NONE,
    attachmentName: String? = null,
    onRetryUpload: () -> Unit = {},
    onSimulateUpload: () -> Unit = {},
    onClearAttachment: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
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
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = when (attachmentState) {
                        ChatMessage.ExpenseAttachmentState.SUCCESS -> Icons.AutoMirrored.Filled.ReceiptLong
                        ChatMessage.ExpenseAttachmentState.UPLOADING -> Icons.Default.AttachFile
                        ChatMessage.ExpenseAttachmentState.FAILED -> Icons.Default.Warning
                        ChatMessage.ExpenseAttachmentState.NONE -> Icons.Default.Image
                    },
                    contentDescription = null,
                    tint = when (attachmentState) {
                        ChatMessage.ExpenseAttachmentState.SUCCESS -> SuccessGreen
                        ChatMessage.ExpenseAttachmentState.UPLOADING -> PrimaryLight
                        ChatMessage.ExpenseAttachmentState.FAILED -> ErrorRed
                        ChatMessage.ExpenseAttachmentState.NONE -> Color(0xFF94A3B8)
                    },
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = when (attachmentState) {
                        ChatMessage.ExpenseAttachmentState.SUCCESS ->
                            attachmentName ?: "Foto Nota Terlampir"
                        ChatMessage.ExpenseAttachmentState.UPLOADING ->
                            "Mengunggah foto nota..."
                        ChatMessage.ExpenseAttachmentState.FAILED ->
                            "Gagal mengunggah foto nota"
                        ChatMessage.ExpenseAttachmentState.NONE ->
                            "Belum dipilih"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = when (attachmentState) {
                        ChatMessage.ExpenseAttachmentState.SUCCESS -> Color(0xFF15803D)
                        ChatMessage.ExpenseAttachmentState.UPLOADING -> PrimaryLight
                        ChatMessage.ExpenseAttachmentState.FAILED -> ErrorRed
                        ChatMessage.ExpenseAttachmentState.NONE -> Color(0xFF64748B)
                    },
                    maxLines = 1
                )
            }

            when (attachmentState) {
                ChatMessage.ExpenseAttachmentState.UPLOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 2.dp,
                        color = PrimaryLight
                    )
                }
                ChatMessage.ExpenseAttachmentState.FAILED -> {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Coba lagi",
                        tint = ErrorRed,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { onRetryUpload() }
                    )
                }
                ChatMessage.ExpenseAttachmentState.SUCCESS -> {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Hapus",
                        tint = Color(0xFF64748B),
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { onClearAttachment() }
                    )
                }
                ChatMessage.ExpenseAttachmentState.NONE -> {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Unggah",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { onSimulateUpload() }
                    )
                }
            }
        }
    }
}
