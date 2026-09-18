package com.example.chatpos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.ui.theme.ErrorContainer
import com.example.chatpos.ui.theme.ErrorDark
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TransactionErrorCard(
    message: ChatMessage.ErrorBatchReceiptMessage,
    modifier: Modifier = Modifier
) {
    val displayTimestamp = runCatching {
        SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            .parse(message.batchResult.timeStamp)
            ?.let { SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(it) }
    }.getOrNull() ?: message.batchResult.timeStamp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, ErrorContainer, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Transaksi gagal",
                        tint = ErrorDark,
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(ErrorContainer)
                            .padding(5.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BALASAN SERVER",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = ErrorDark
                    )
                }
                Text(
                    text = displayTimestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF94A3B8)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 10.dp),
                color = ErrorContainer
            )
            Text(
                text = "ID Customer: ${message.customerTag}",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF64748B)
            )
            Text(
                text = message.errorMessage,
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF334155)
            )
        }
    }
}
