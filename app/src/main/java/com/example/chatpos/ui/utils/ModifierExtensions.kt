package com.example.chatpos.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatpos.ui.theme.ChatPOSDimensions
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.WarningAmber
import com.example.chatpos.ui.theme.WarningAmberBg

fun Modifier.chatCard(
    backgroundColor: Color = Color.White,
    borderColor: Color = DividerColor,
) = this
    .background(
        color = backgroundColor,
        shape = RoundedCornerShape(ChatPOSDimensions.BorderRadiusLarge)
    )
    .border(
        width = 1.dp,
        color = borderColor,
        shape = RoundedCornerShape(ChatPOSDimensions.BorderRadiusLarge)
    )

fun Modifier.userMessageBubble() = this
    .background(
        color = Color(0xFF1E60F2),
        shape = RoundedCornerShape(
            topStart = ChatPOSDimensions.BorderRadiusLarge,
            topEnd = ChatPOSDimensions.BorderRadiusLarge,
            bottomStart = ChatPOSDimensions.BorderRadiusLarge,
            bottomEnd = ChatPOSDimensions.BorderRadiusSmall,
        )
    )

fun Modifier.systemCard() = this
    .chatCard(
        backgroundColor = Color.White,
        borderColor = DividerColor,
    )

fun Modifier.warningCard(
    backgroundColor: Color = WarningAmberBg,
    borderColor: Color = WarningAmber,
) = this
    .background(
        color = backgroundColor,
        shape = RoundedCornerShape(ChatPOSDimensions.BorderRadiusLarge)
    )
    .border(
        width = 1.dp,
        color = borderColor,
        shape = RoundedCornerShape(ChatPOSDimensions.BorderRadiusLarge)
    )
