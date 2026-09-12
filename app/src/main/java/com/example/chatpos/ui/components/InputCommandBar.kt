package com.example.chatpos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.PrimaryLight

@Composable
fun InputCommandBar(
    textFieldValue: TextFieldValue,
    hintText: String,
    isSendEnabled: Boolean,
    isDestinationNumberInput: Boolean = false,
    destinationGroupSize: Int = 4,
    focusRequestKey: Int = 0,
    onTextFieldValueChange: (TextFieldValue) -> Unit,
    onSendMessage: (String) -> Unit,
    onToggleDestinationGroupSize: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val focusRequester = androidx.compose.runtime.remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(focusRequestKey) {
        if (focusRequestKey > 0) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        HorizontalDivider(color = DividerColor, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Expandable Input Box for multi-line support with TextFieldValue (automatic cursor control)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 44.dp, max = 110.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF1F5F9))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (textFieldValue.text.isEmpty()) {
                    Text(
                        text = hintText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    )
                }
                if (textFieldValue.text.isNotEmpty()) {
                    Text(
                        text = buildAnnotatedString {
                            val lines = textFieldValue.text.split("\n")
                            lines.forEachIndexed { index, line ->
                                withStyle(
                                    SpanStyle(
                                        background = listOf(
                                            Color(0x223B82F6),
                                            Color(0x22F59E0B),
                                            Color(0x2210B981),
                                            Color(0x228B5CF6)
                                        )[index % 4]
                                    )
                                ) {
                                    append(line)
                                }
                                if (index < lines.lastIndex) append("\n")
                            }
                        },
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF0F172A),
                            lineHeight = 18.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = onTextFieldValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Transparent,
                        lineHeight = 18.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (isDestinationNumberInput) KeyboardType.Phone else KeyboardType.Text
                    ),
                    maxLines = 4,
                    cursorBrush = SolidColor(PrimaryLight)
                )
                if (isDestinationNumberInput) {
                    Text(
                        text = "${destinationGroupSize} digit",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                        color = PrimaryLight,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEFF6FF))
                            .clickable(onClick = onToggleDestinationGroupSize)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Circular Send FAB
            IconButton(
                onClick = {
                    if (isSendEnabled && textFieldValue.text.isNotBlank()) {
                        onSendMessage(textFieldValue.text)
                    }
                },
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSendEnabled) PrimaryLight else Color(0xFFCBD5E1))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Kirim",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
