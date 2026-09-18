package com.example.chatpos.ui.screens

import android.content.Intent
import android.net.Uri
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.ui.components.BalanceStripCard
import com.example.chatpos.ui.components.ChatPOSAppBar
import com.example.chatpos.ui.components.DuplicateTargetWarningCard
import com.example.chatpos.ui.components.ExpenseCard
import com.example.chatpos.ui.components.InputCommandBar
import com.example.chatpos.ui.components.PinConfirmationBottomSheet
import com.example.chatpos.ui.components.ReceiptPrintPreviewModal
import com.example.chatpos.ui.components.SystemMessageCard
import com.example.chatpos.ui.components.TransactionInputAssistant
import com.example.chatpos.ui.components.TransactionSuccessCard
import com.example.chatpos.ui.components.TransactionErrorCard
import com.example.chatpos.ui.components.UserMessageBubble
import com.example.chatpos.ui.components.UserTransactionCard
import com.example.chatpos.ui.components.WelcomeSystemCard
import com.example.chatpos.ui.components.WhatsAppDateDivider
import com.example.chatpos.ui.components.WhatsAppShareModal
import com.example.chatpos.ui.theme.BackgroundCanvas
import com.example.chatpos.viewmodel.ChatPOSViewModel

@Composable
fun ChatScreen(
    viewModel: ChatPOSViewModel,
    onOpenReceiptHistory: () -> Unit = {},
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var isProductPickerVisible by remember { mutableStateOf(true) }

    val counterName by viewModel.counterName.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val balance by viewModel.balance.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val pendingUserCard by viewModel.pendingUserCard.collectAsState()
    val inputTextFieldValue by viewModel.inputTextFieldValue.collectAsState()
    val inputFocusRequest by viewModel.inputFocusRequest.collectAsState()
    val isDestinationNumberInput by viewModel.isDestinationNumberInput.collectAsState()
    val destinationGroupSize by viewModel.destinationGroupSize.collectAsState()

    val hasDotOnCurrentLine by viewModel.hasDotOnCurrentLine.collectAsState()
    val productSuggestions by viewModel.productSuggestions.collectAsState()
    val selectedProductCategory by viewModel.selectedProductCategory.collectAsState()
    val contactSuggestions by viewModel.contactSuggestions.collectAsState()
    val transferAdminHint by viewModel.transferAdminHint.collectAsState()
    val transferPinWarning by viewModel.transferPinWarning.collectAsState()
    val isInputValid by viewModel.isInputValid.collectAsState()
    val isCurrentLineComplete by viewModel.isCurrentLineComplete.collectAsState()
    val inputHintText by viewModel.inputHintText.collectAsState()

    val expenseAttachmentState by viewModel.expenseAttachmentState.collectAsState()
    val expenseAttachmentName by viewModel.expenseAttachmentName.collectAsState()

    val showPinModal by viewModel.showPinModal.collectAsState()
    val selectedReceipt by viewModel.selectedBatchReceipt.collectAsState()
    val selectedWhatsAppTrx by viewModel.selectedWhatsAppTrx.collectAsState()

    // Auto-scroll to bottom whenever a new chat message arrives
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Column {
                ChatPOSAppBar(
                    counterName = counterName,
                    title = if (onBack != null) "ChatPOS" else "Chat",
                    isOnline = isOnline,
                    showOnline = false,
                    onBackClick = onBack,
                    onMenuClick = {
                        Toast.makeText(context, "Menu Pengaturan OtomaX", Toast.LENGTH_SHORT).show()
                    }
                )
                if (onBack != null) {
                    BalanceStripCard(balance = balance)
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                // Interactive Assistant above Input Bar (Product chips / Contact suggestions / Add more button)
                TransactionInputAssistant(
                    hasDotOnCurrentLine = hasDotOnCurrentLine,
                    productSuggestions = productSuggestions,
                    productCategories = viewModel.productCategories,
                    productSubcategories = viewModel.productSubcategories,
                    selectedProductCategory = selectedProductCategory,
                    selectedProductSubcategory = viewModel.selectedProductSubcategory.collectAsState().value,
                    contactSuggestions = contactSuggestions,
                    transferAdminHint = transferAdminHint,
                    transferPinWarning = transferPinWarning,
                    isCurrentLineComplete = isCurrentLineComplete,
                    onProductClick = { product ->
                        viewModel.onProductClicked(product)
                    },
                    onProductCategorySelected = { category ->
                        viewModel.onProductCategorySelected(category)
                        isProductPickerVisible = true
                    },
                    onProductSubcategorySelected = { subcategory ->
                        viewModel.onProductSubcategorySelected(subcategory)
                    },
                    onContactClick = { contact ->
                        viewModel.onContactSelected(contact)
                    },
                    onAddAnotherTransaction = {
                        viewModel.addAnotherTransactionLine()
                    },
                    expenseCategories = viewModel.expenseCategories,
                    expenseAttachmentState = expenseAttachmentState,
                    expenseAttachmentName = expenseAttachmentName,
                    onExpenseCategoryClick = { viewModel.onExpenseCategorySelected(it) },
                    onSimulateAttachmentUpload = { viewModel.simulateExpenseAttachmentUpload() },
                    onClearAttachment = { viewModel.clearExpenseAttachment() },
                    isProductPickerVisible = isProductPickerVisible,
                    onToggleProductPicker = { isProductPickerVisible = !isProductPickerVisible }
                )

                // Bottom Input Command Bar
                InputCommandBar(
                    textFieldValue = inputTextFieldValue,
                    hintText = inputHintText,
                    isSendEnabled = isInputValid && transferPinWarning == null,
                    isDestinationNumberInput = isDestinationNumberInput,
                    destinationGroupSize = destinationGroupSize,
                    focusRequestKey = inputFocusRequest,
                    onTextFieldValueChange = { viewModel.onTextFieldValueChange(it) },
                    onToggleDestinationGroupSize = { viewModel.toggleDestinationGroupSize() },
                    onSendMessage = {
                        viewModel.sendUserTransactions(it)
                        isProductPickerVisible = false
                    }
                )
            }
        },
        containerColor = BackgroundCanvas
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Chat Stream
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                        isProductPickerVisible = false
                    }
            ) {
                items(messages, key = { message -> message.id }) { message ->
                    when (message) {
                        is ChatMessage.DateDivider -> {
                            WhatsAppDateDivider(label = message.label)
                        }
                        is ChatMessage.WelcomeCard -> {
                            AnimatedVisibility(visible = messages.any { it is ChatMessage.WelcomeCard }) {
                                WelcomeSystemCard(
                                    storeName = message.storeName,
                                    message = message.message
                                )
                            }
                        }
                        is ChatMessage.UserTransactionCardMessage -> {
                            UserTransactionCard(
                                message = message,
                                onProcessClicked = { viewModel.onProcessUserCard(it) },
                                onEditClicked = { viewModel.onEditUserCard(it) },
                                onRetryClicked = { viewModel.onProcessUserCard(it) },
                                onCopyTransaction = {
                                    val clipboard = context.getSystemService(ClipboardManager::class.java)
                                    clipboard?.setPrimaryClip(
                                        ClipData.newPlainText("Transaksi", it.rawCommand)
                                    )
                                    Toast.makeText(context, "Transaksi disalin", Toast.LENGTH_SHORT).show()
                                },
                                onDuplicateCancel = { viewModel.cancelDuplicateWarning(it) },
                                onDuplicateProceed = {
                                    val item = it.items.first()
                                    viewModel.proceedDuplicateWarning(item.destinationNumber, item.productCode)
                                }
                            )
                        }
                        is ChatMessage.UserMessage -> {
                            UserMessageBubble(
                                text = message.text,
                                customerTag = message.customerTag,
                                timeString = message.timeString
                            )
                        }
                        is ChatMessage.SystemMessage -> {
                            SystemMessageCard(
                                title = message.title,
                                content = message.content,
                                type = message.type,
                                timeString = message.timeString
                            )
                        }
                        is ChatMessage.DuplicateWarningMessage -> {
                            // Duplicate warnings are rendered inside the originating user bubble.
                        }
                        is ChatMessage.SuccessBatchReceiptMessage -> {
                            TransactionSuccessCard(
                                result = message.batchResult,
                                onPrintReceipt = { viewModel.openReceiptModal(message.batchResult) },
                                onShareWhatsApp = { viewModel.openWhatsAppModal(message.batchResult) }
                            )
                        }
                        is ChatMessage.ErrorBatchReceiptMessage -> {
                            TransactionErrorCard(message = message)
                        }
                        is ChatMessage.ExpenseCardMessage -> {
                            ExpenseCard(
                                message = message,
                                onConfirm = { viewModel.confirmExpense(it) },
                                onEdit = { viewModel.editExpense(it) },
                                onCancel = { viewModel.cancelExpense(it) },
                                onRetryUpload = { viewModel.retryExpenseAttachmentUpload(it) }
                            )
                        }
                        is ChatMessage.CustomerIncomingMessage,
                        is ChatMessage.IncomingTransactionRequest -> {
                            // Customer-specific messages are rendered in the customer chat detail screen.
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    // Modal: 4-Digit PIN Confirmation Sheet
    if (showPinModal && pendingUserCard != null) {
        PinConfirmationBottomSheet(
            totalAmount = pendingUserCard!!.totalAmount,
            itemCount = pendingUserCard!!.items.size,
            onDismiss = { viewModel.dismissPinModal() },
            onPinSuccess = { pin ->
                viewModel.confirmPinAndExecute(pin)
            }
        )
    }

    // Modal: Print Thermal Receipt Preview
    selectedReceipt?.let { receipt ->
        ReceiptPrintPreviewModal(
            result = receipt,
            storeName = counterName,
            onDismiss = { viewModel.dismissReceiptModal() },
            onPrintConfirm = {
                viewModel.dismissReceiptModal()
                Toast.makeText(context, "Mencetak ke Thermal Bluetooth ESC/POS...", Toast.LENGTH_LONG).show()
            }
        )
    }

    // Modal: Send WhatsApp Receipt
    selectedWhatsAppTrx?.let { trx ->
        WhatsAppShareModal(
            result = trx,
            storeName = counterName,
            onDismiss = { viewModel.dismissWhatsAppModal() },
            onSendWhatsApp = { phone, texts ->
                viewModel.dismissWhatsAppModal()
                try {
                    val cleanPhone = if (phone.startsWith("0")) "62" + phone.substring(1) else phone
                    texts.forEach { text ->
                        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(text)}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Aplikasi WhatsApp tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
