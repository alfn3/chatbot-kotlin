package com.example.chatpos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.ui.components.CategoryChips
import com.example.chatpos.ui.components.ExpenseAttachmentCard
import com.example.chatpos.ui.components.ExpenseCard
import com.example.chatpos.ui.theme.BackgroundCanvas
import com.example.chatpos.ui.theme.PrimaryLight
import com.example.chatpos.ui.utils.CurrencyFormatter
import com.example.chatpos.viewmodel.ChatPOSViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseChatScreen(
    viewModel: ChatPOSViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val messages by viewModel.messages.collectAsState()
    val inputTextFieldValue by viewModel.inputTextFieldValue.collectAsState()
    val expenseAttachmentState by viewModel.expenseAttachmentState.collectAsState()
    val expenseAttachmentName by viewModel.expenseAttachmentName.collectAsState()

    val expenseMessages = messages.filterIsInstance<ChatMessage.ExpenseCardMessage>()
    val totalExpense = expenseMessages.filter { it.isConfirmed }.sumOf { it.amount }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Wallet,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pengeluaran",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryLight,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundCanvas
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color(0xFFFEF3C7))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = "Total Hari Ini",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = Color(0xFFB45309)
                    )
                    Text(
                        text = CurrencyFormatter.formatRupiah(totalExpense),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = Color(0xFF92400E)
                    )
                    Text(
                        text = "${expenseMessages.size} transaksi tercatat",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFFB45309)
                    )
                }
                Icon(
                    imageVector = Icons.Default.Wallet,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(32.dp)
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(expenseMessages, key = { it.id }) { message ->
                    ExpenseCard(
                        message = message,
                        onConfirm = { viewModel.confirmExpense(it) },
                        onEdit = { viewModel.editExpense(it) },
                        onCancel = { viewModel.cancelExpense(it) },
                        onRetryUpload = { viewModel.retryExpenseAttachmentUpload(it) }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CategoryChips(
                        selectedCategory = "",
                        onCategorySelected = { category ->
                            val cmd = "keluar.$category."
                            viewModel.onTextFieldValueChange(
                                TextFieldValue(
                                    text = cmd,
                                    selection = TextRange(cmd.length)
                                )
                            )
                        }
                    )
                }

                if (expenseAttachmentState != ChatMessage.ExpenseAttachmentState.NONE || expenseAttachmentName != null) {
                    ExpenseAttachmentCard(
                        attachmentState = expenseAttachmentState,
                        attachmentName = expenseAttachmentName,
                        onRetryUpload = {
                            val lastExpense = expenseMessages.lastOrNull()
                            if (lastExpense != null) {
                                viewModel.retryExpenseAttachmentUpload(lastExpense)
                            }
                        },
                        onSimulateUpload = { viewModel.simulateExpenseAttachmentUpload() },
                        onClearAttachment = { viewModel.clearExpenseAttachment() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    var textFieldValue by remember { mutableStateOf(inputTextFieldValue) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 44.dp, max = 110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFF1F5F9))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        if (textFieldValue.text.isEmpty()) {
                            Text(
                                text = "Format: keluar.[kategori].[nominal].[keterangan]",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 12.sp,
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
                            onValueChange = { newValue ->
                                textFieldValue = newValue
                                viewModel.onTextFieldValueChange(newValue)
                            },
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Transparent,
                                lineHeight = 18.sp
                            ),
                            maxLines = 3,
                            cursorBrush = androidx.compose.ui.graphics.SolidColor(PrimaryLight)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            when (expenseAttachmentState) {
                                ChatMessage.ExpenseAttachmentState.NONE -> {
                                    viewModel.simulateExpenseAttachmentUpload()
                                }
                                ChatMessage.ExpenseAttachmentState.SUCCESS -> {
                                    viewModel.clearExpenseAttachment()
                                }
                                else -> {}
                            }
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = when (expenseAttachmentState) {
                                ChatMessage.ExpenseAttachmentState.SUCCESS -> Icons.Default.CheckCircle
                                ChatMessage.ExpenseAttachmentState.UPLOADING -> Icons.Default.Add
                                ChatMessage.ExpenseAttachmentState.FAILED -> Icons.Default.Warning
                                ChatMessage.ExpenseAttachmentState.NONE -> Icons.Default.Image
                            },
                            contentDescription = "Lampiran",
                            tint = when (expenseAttachmentState) {
                                ChatMessage.ExpenseAttachmentState.SUCCESS -> Color(0xFF15803D)
                                ChatMessage.ExpenseAttachmentState.UPLOADING -> PrimaryLight
                                ChatMessage.ExpenseAttachmentState.FAILED -> Color(0xFFB91C1C)
                                ChatMessage.ExpenseAttachmentState.NONE -> Color(0xFF64748B)
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            if (textFieldValue.text.isNotBlank()) {
                                viewModel.sendUserTransactions(textFieldValue.text)
                            }
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Kirim",
                            tint = if (textFieldValue.text.isNotBlank()) Color.White else Color(0xFFCBD5E1),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
