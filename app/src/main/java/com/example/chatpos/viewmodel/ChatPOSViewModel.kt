package com.example.chatpos.viewmodel

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.model.OtomaxBatchResult
import com.example.chatpos.model.OtomaxItemDetail
import com.example.chatpos.model.OtomaxStatus
import com.example.chatpos.model.ProductItem
import com.example.chatpos.model.SampleProducts
import com.example.chatpos.model.SavedContact
import com.example.chatpos.model.TransactionItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatPOSViewModel : ViewModel() {

    private val _counterName = MutableStateFlow("TOKO BERKAH CELL")
    val counterName = _counterName.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline = _isOnline.asStateFlow()

    private val _balance = MutableStateFlow(1_417_000L)
    val balance = _balance.asStateFlow()

    // 1. Initial State: Date divider & Welcome card (Welcome card is hidden once a transaction exists)
    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage.DateDivider(label = "HARI INI"),
            ChatMessage.WelcomeCard(
                storeName = "TOKO BERKAH CELL",
                message = "Selamat datang di ChatPOS! Silakan pilih produk di atas atau ketik perintah transaksi (contoh: 5.089512345678)."
            )
        )
    )
    val messages = _messages.asStateFlow()

    // Controls text and automatic cursor positioning at the end
    private val _inputTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val inputTextFieldValue = _inputTextFieldValue.asStateFlow()

    private val _inputFocusRequest = MutableStateFlow(0)
    val inputFocusRequest = _inputFocusRequest.asStateFlow()

    private val _showPinModal = MutableStateFlow(false)
    val showPinModal = _showPinModal.asStateFlow()

    private val _pendingUserCard = MutableStateFlow<ChatMessage.UserTransactionCardMessage?>(null)
    val pendingUserCard = _pendingUserCard.asStateFlow()

    // Modal dialogs
    private val _selectedBatchReceipt = MutableStateFlow<OtomaxBatchResult?>(null)
    val selectedBatchReceipt = _selectedBatchReceipt.asStateFlow()

    private val _selectedWhatsAppTrx = MutableStateFlow<OtomaxBatchResult?>(null)
    val selectedWhatsAppTrx = _selectedWhatsAppTrx.asStateFlow()

    private val recentTrxNumbers = mutableMapOf<String, Long>()
    private val _selectedProductCategory = MutableStateFlow("")
    val selectedProductCategory = _selectedProductCategory.asStateFlow()
    private val _selectedProductSubcategory = MutableStateFlow("")
    val selectedProductSubcategory = _selectedProductSubcategory.asStateFlow()
    private val _destinationGroupSize = MutableStateFlow(4)
    val destinationGroupSize = _destinationGroupSize.asStateFlow()
    val productCategories = (listOf("Pulsa", "Transfer") + SampleProducts.allProducts
        .map { it.category }
        .distinct()).distinct()
    private fun productGroup(product: ProductItem): String {
        val validity = product.validity.lowercase()
        return when {
            validity.contains("unlimited") -> "Unlimited"
            validity.contains("30") || validity.contains("bulanan") -> "Bulanan"
            validity.contains("7") || validity.contains("harian") -> "Harian"
            else -> "Lainnya"
        }
    }

    val productSubcategories: List<String>
        get() = SampleProducts.allProducts
            .filter { it.category == _selectedProductCategory.value }
            .map(::productGroup)
            .distinct()

    // Check if there has been any user transaction submitted
    val hasUserTransactions = _messages.map { list ->
        list.any { it is ChatMessage.UserTransactionCardMessage }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private fun getCurrentLine(text: String): String {
        return text.substringAfterLast("\n")
    }

    private fun isTransferCommand(line: String): Boolean {
        return line.startsWith("cek.", ignoreCase = true) ||
            line.matches(Regex("""^cek[^.]+\..*""", RegexOption.IGNORE_CASE)) ||
            line.startsWith("tbank.", ignoreCase = true)
    }

    val hasDotOnCurrentLine = _inputTextFieldValue.map { tfv ->
        getCurrentLine(tfv.text).contains(".")
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val productSuggestions = combine(
        _inputTextFieldValue,
        combine(_selectedProductCategory, _selectedProductSubcategory) { category, subcategory ->
            category to subcategory
        }
    ) { tfv, selection ->
        val (category, subcategory) = selection
        val line = getCurrentLine(tfv.text)
        if (line.contains(".")) {
            emptyList<ProductItem>()
        } else {
            val prefix = line.trim()
            val digits = prefix.filter(Char::isDigit)
            val completeDestination = prefix.startsWith("0") && digits.length >= 10
            if (completeDestination) {
                val detectedOperator = detectMobileOperator(digits)
                SampleProducts.allProducts.filter { product ->
                    product.category == "Pulsa" &&
                        (detectedOperator == null || product.operator == detectedOperator) &&
                        (subcategory.isBlank() || productGroup(product) == subcategory)
                }
            } else if (category.isBlank() || category == "Transfer") {
                emptyList()
            } else if (prefix.startsWith("0") && digits.length >= 3) {
                emptyList()
            } else {
                SampleProducts.filterProductsByPrefix(prefix).filter { product ->
                    product.category == category &&
                        (subcategory.isBlank() || productGroup(product) == subcategory)
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private fun detectMobileOperator(number: String): String? = when {
        number.startsWith("0812") || number.startsWith("0813") -> "Three"
        number.startsWith("0811") || number.startsWith("0814") ||
            number.startsWith("0815") || number.startsWith("0816") ||
            number.startsWith("0855") || number.startsWith("0856") -> "Telkomsel"
        number.startsWith("089") -> "Telkomsel"
        else -> null
    }

    val contactSuggestions = _inputTextFieldValue.map { tfv ->
        val line = getCurrentLine(tfv.text)
        if (line.contains(".") && isTransferCommand(line)) {
            val parts = line.split(".")
            val isCompactCheck = parts.firstOrNull()?.startsWith("cek", ignoreCase = true) == true &&
                !parts.first().equals("cek", ignoreCase = true)
            val query = if (isCompactCheck) parts.getOrNull(1).orEmpty() else parts.getOrNull(2).orEmpty()
            if (query.isBlank()) {
                SampleProducts.savedContacts
            } else {
                SampleProducts.filterContactsByQuery(query)
            }
        } else if (line.contains(".")) {
            val query = line.substringAfter(".")
            SampleProducts.filterContactsByQuery(query)
        } else if (line.trim().length >= 2) {
            val digits = line.filter(Char::isDigit)
            if (line.trim().startsWith("0") && digits.length >= 10) {
                emptyList()
            } else {
                SampleProducts.filterContactsByQuery(line.trim())
            }
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val isDestinationNumberInput = _inputTextFieldValue.map { tfv ->
        val line = getCurrentLine(tfv.text).trim()
        val destination = if (line.contains(".")) line.substringAfterLast(".") else line
        destination.filter(Char::isDigit).length >= 1 &&
            destination.trimStart().startsWith("0")
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isInputValid = _inputTextFieldValue.map { tfv ->
        val lines = tfv.text.lines().map(String::trim).filter(String::isNotEmpty)
        lines.isNotEmpty() && lines.all { isTransactionLineValid(it) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private fun isTransactionLineValid(line: String): Boolean {
        if (hasTrailingPin(line)) return false
        if (isTransferCommand(line)) {
            return if (line.startsWith("tbank.", ignoreCase = true)) {
                val parts = line.split(".")
                parts.size >= 4 &&
                    parts[1].isNotBlank() &&
                    parts[2].filter(Char::isDigit).length >= 6 &&
                    parts[3].toLongOrNull()?.let { it > 0 } == true
            } else {
                val parts = line.split(".")
                parts.size >= 2 && parts.last().filter(Char::isDigit).length >= 6
            }
        }
        if (!line.contains(".")) return false
        val parts = line.split(".", limit = 2)
        return parts[0].isNotBlank() &&
            parts[1].filter(Char::isDigit).length >= 10
    }

    val isCurrentLineComplete = _inputTextFieldValue.map { tfv ->
        val line = getCurrentLine(tfv.text).trim()
        if (hasTrailingPin(line)) {
            false
        } else if (isTransferCommand(line) && line.startsWith("tbank.", ignoreCase = true)) {
            val parts = line.split(".")
            val bankCode = parts.getOrNull(1).orEmpty()
            val account = parts.getOrNull(2).orEmpty()
            val nominal = parts.getOrNull(3)?.toLongOrNull() ?: 0L
            bankCode.length >= 2 && account.length >= 6 && nominal > 0
        } else if (isTransferCommand(line)) {
            val parts = line.split(".")
            val isCompactCheck = parts.firstOrNull()?.startsWith("cek", ignoreCase = true) == true &&
                !parts.first().equals("cek", ignoreCase = true)
            val bank = if (isCompactCheck) parts.first().drop(3) else parts.getOrNull(1).orEmpty()
            val accountIndex = if (isCompactCheck) 1 else 2
            bank.isNotBlank() && parts.getOrNull(accountIndex).orEmpty().length >= 6
        } else if (line.contains(".")) {
            val parts = line.split(".", limit = 2)
            val code = parts.getOrNull(0) ?: ""
            val destination = parts.getOrNull(1) ?: ""
            code.isNotBlank() && destination.length >= 10
        } else {
            false
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val inputHintText = combine(_inputTextFieldValue, _selectedProductCategory) { tfv, category ->
        val line = getCurrentLine(tfv.text)
        if (line.startsWith("cek", ignoreCase = true)) {
            "Format cek: cek[bank].[norek] (cth: cekbri.123456789)"
        } else if (line.startsWith("tbank.", ignoreCase = true)) {
            "Format transfer: tbank.[bank][5/10].[norek].[nominal]"
        } else if (category == "Transfer") {
            "Contoh: cekbri.123456789 atau tbank.bri5.123456789.400000"
        } else if (category == "Pulsa") {
            "Format Pulsa: [kode].[nomor] (cth: 10.089512345678)"
        } else if (category == "PLN") {
            "Format PLN: [kode].[id pelanggan] (cth: PLN20.1234567890)"
        } else if (category == "E-Wallet") {
            "Format E-Wallet: [kode].[nomor] (cth: DANA10.089512345678)"
        } else if (line.contains(".")) {
            "Ketik nomor tujuan (cth: 0895...)"
        } else {
            "Pilih produk di atas / ketik kode (cth: 5)"
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "Pilih produk di atas / ketik kode (cth: 5)")

    val transferAdminHint = _inputTextFieldValue.map { tfv ->
        val line = getCurrentLine(tfv.text)
        when {
            line.startsWith("tbank.", ignoreCase = true) && Regex("""^tbank\.[^.]+5\.""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Transfer bank terdeteksi • Admin Rp5.000 (kode 5)"
            line.startsWith("tbank.", ignoreCase = true) && Regex("""^tbank\.[^.]+10\.""", RegexOption.IGNORE_CASE).containsMatchIn(line) ->
                "Transfer bank terdeteksi • Admin Rp10.000 (kode 10)"
            line.startsWith("cek", ignoreCase = true) ->
                "Cek nama rekening • Tidak ada biaya transfer"
            else -> null
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val transferPinWarning = _inputTextFieldValue.map { tfv ->
        val line = getCurrentLine(tfv.text).trim()
        if (hasTrailingPin(line)) {
            "PIN di akhir tidak perlu diketik; sistem akan meminta PIN saat proses."
        } else {
            null
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private fun hasTrailingPin(line: String): Boolean {
        val parts = line.split(".")
        if (parts.size < 3 || !parts.last().matches(Regex("""\d{4,6}"""))) {
            return false
        }
        return when {
            line.startsWith("tbank.", ignoreCase = true) -> parts.size >= 5
            line.startsWith("cek", ignoreCase = true) -> parts.size >= 3
            else -> parts.size == 3 && parts[0].isNotBlank() && parts[1].length >= 6
        }
    }

    fun onTextFieldValueChange(newValue: TextFieldValue) {
        val currentLine = getCurrentLine(newValue.text)
        val isNumberOnlyLine = currentLine.startsWith("0") &&
            !currentLine.contains(".") &&
            currentLine.all { it.isDigit() || it == '-' }
        val formattedLine = if (isNumberOnlyLine) {
            formatDestinationNumber(currentLine)
        } else {
            currentLine
        }
        val prefix = newValue.text.substringBeforeLast("\n", missingDelimiterValue = "")
        val text = if (newValue.text.contains("\n")) "$prefix\n$formattedLine" else formattedLine
        _inputTextFieldValue.value = newValue.copy(
            text = text,
            selection = TextRange(text.length)
        )
    }

    fun toggleDestinationGroupSize() {
        _destinationGroupSize.value = if (_destinationGroupSize.value == 4) 3 else 4
        val current = _inputTextFieldValue.value.text
        val line = getCurrentLine(current)
        val destination = line.substringAfterLast(".").filter(Char::isDigit)
        if (destination.startsWith("0")) {
            val prefix = current.substringBeforeLast("\n", missingDelimiterValue = "")
            val code = line.substringBeforeLast(".").takeIf { line.contains(".") }
            val formatted = formatDestinationNumber(destination)
            val updatedLine = if (code != null) "$code.$formatted" else formatted
            val updatedText = if (current.contains("\n")) "$prefix\n$updatedLine" else updatedLine
            _inputTextFieldValue.value = TextFieldValue(updatedText, TextRange(updatedText.length))
        }
    }

    private fun formatDestinationNumber(value: String): String {
        val digits = value.filter(Char::isDigit).take(13)
        if (digits.length <= 4) return digits
        val firstGroupLength = _destinationGroupSize.value
        val groups = mutableListOf(digits.take(firstGroupLength))
        var remaining = digits.drop(firstGroupLength)
        while (remaining.isNotEmpty()) {
            groups += remaining.take(firstGroupLength)
            remaining = remaining.drop(firstGroupLength)
        }
        return groups.joinToString("-")
    }

    fun onProductCategorySelected(category: String) {
        _selectedProductCategory.value = category
        _selectedProductSubcategory.value = ""
    }

    fun onProductSubcategorySelected(subcategory: String) {
        _selectedProductSubcategory.value = subcategory
    }

    // When user clicks a product chip: auto-insert code + dot, and move cursor to end
    fun onProductClicked(product: ProductItem) {
        val currentText = _inputTextFieldValue.value.text
        val lastNewlineIdx = currentText.lastIndexOf("\n")
        val currentLine = getCurrentLine(currentText)
        val destination = currentLine.filter(Char::isDigit).takeIf { it.startsWith("0") }.orEmpty()
        val newCode = "${product.code}.${destination}"

        val newText = if (lastNewlineIdx == -1) {
            newCode
        } else {
            val previousLines = currentText.substring(0, lastNewlineIdx + 1)
            previousLines + newCode
        }

        _inputTextFieldValue.value = TextFieldValue(
            text = newText,
            selection = TextRange(newText.length)
        )
        _inputFocusRequest.value += 1
    }

    // When user clicks a contact: auto-complete number and move cursor to end
    fun onContactSelected(contact: SavedContact) {
        val currentText = _inputTextFieldValue.value.text
        val lastNewlineIdx = currentText.lastIndexOf("\n")
        val currentLine = getCurrentLine(currentText)
        if (!currentLine.contains(".")) {
            val formatted = formatDestinationNumber(contact.phoneNumber)
            _inputTextFieldValue.value = TextFieldValue(
                text = if (lastNewlineIdx == -1) formatted else currentText.substring(0, lastNewlineIdx + 1) + formatted,
                selection = TextRange((if (lastNewlineIdx == -1) formatted else currentText.substring(0, lastNewlineIdx + 1) + formatted).length)
            )
            return
        }
        val codePart = currentLine.substringBefore(".")

        val completedLine = "$codePart.${contact.phoneNumber}"
        val newText = if (lastNewlineIdx == -1) {
            completedLine
        } else {
            val previousLines = currentText.substring(0, lastNewlineIdx + 1)
            previousLines + completedLine
        }

        _inputTextFieldValue.value = TextFieldValue(
            text = newText,
            selection = TextRange(newText.length)
        )
    }

    // When user clicks "+ Tambah Transaksi Lain": creates newline and moves cursor to end
    fun addAnotherTransactionLine() {
        val current = _inputTextFieldValue.value.text.trimEnd()
        if (current.isNotBlank()) {
            val newText = "$current\n"
            _inputTextFieldValue.value = TextFieldValue(
                text = newText,
                selection = TextRange(newText.length)
            )
        }

    }

    // When user clicks Send:
    // "setelah kirim pada chat belum langsung proses. tampilkan dulu hasil output, ketika user klik proses baru kirim ke sistem."
    fun sendUserTransactions(rawInput: String) {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) return
        if (trimmed.split("\n").any { hasTrailingPin(it.trim()) }) return

        val lines = trimmed.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        val items = mutableListOf<TransactionItem>()
        val currentTime = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())

        for (line in lines) {
            val parts = line.split(".")
            if (isTransferCommand(line) && line.startsWith("cek", ignoreCase = true)) {
                val compact = parts.firstOrNull()?.startsWith("cek", ignoreCase = true) == true &&
                    !parts.first().equals("cek", ignoreCase = true)
                val bank = if (compact) parts.first().drop(3) else parts.getOrNull(1).orEmpty()
                val account = parts.getOrNull(if (compact) 1 else 2).orEmpty()
                if (bank.isNotBlank() && account.length >= 6) {
                    items.add(
                        TransactionItem(
                            productCode = "CEK.$bank",
                            productName = "Cek Nama Rekening $bank",
                            destinationNumber = account,
                            price = 0L,
                            nominal = 0L,
                            adminFee = 0L
                        )
                    )
                }
            } else if (line.startsWith("tbank.", ignoreCase = true) && parts.size >= 4) {
                val bankCode = parts[1].lowercase()
                val account = parts[2]
                val nominal = parts[3].toLongOrNull() ?: 0L
                val adminFee = when {
                    bankCode.endsWith("5") && nominal in 10_000L..499_999L -> 5_000L
                    bankCode.endsWith("10") && nominal in 500_000L..2_000_000L -> 10_000L
                    else -> 0L
                }
                if (bankCode.length > 2 && account.length >= 6 && nominal > 0 && adminFee > 0) {
                    val bank = bankCode.removeSuffix("10").removeSuffix("5").uppercase()
                    items.add(
                        TransactionItem(
                            productCode = "TBANK.${bankCode.uppercase()}",
                            productName = "Transfer Bank $bank • Admin Rp${adminFee / 1_000}K",
                            destinationNumber = account,
                            price = nominal + adminFee,
                            nominal = nominal,
                            adminFee = adminFee
                        )
                    )
                }
            } else if (line.contains(".")) {
                val parts = line.split(".", limit = 2)
                val code = parts[0].trim()
                val dest = parts[1].trim().filter(Char::isDigit)
                val product = SampleProducts.findProduct(code)

                val name = product?.name ?: "Pulsa $code"
                val price = product?.sellPrice ?: 10_000L

                items.add(
                    TransactionItem(
                        productCode = code,
                        productName = name,
                        destinationNumber = dest,
                        price = price
                    )
                )
            }
        }

        if (items.isNotEmpty()) {
            val total = items.sumOf { it.price }
            val userCard = ChatMessage.UserTransactionCardMessage(
                rawCommand = trimmed,
                items = items,
                totalAmount = total,
                isProcessed = false,
                timeString = currentTime
            )

            // Post right-aligned card to chat history for cashier review
            // Hide welcome card once a transaction is added
            val currentList = _messages.value.filterNot { it is ChatMessage.WelcomeCard }
            _messages.value = currentList + userCard

            // Clear input box
            _inputTextFieldValue.value = TextFieldValue("")
            _selectedProductCategory.value = ""
            _selectedProductSubcategory.value = ""
        } else {
            // Fallback plain message
            _messages.value = _messages.value + ChatMessage.UserMessage(text = trimmed, timeString = currentTime)
            _inputTextFieldValue.value = TextFieldValue("")
            _selectedProductCategory.value = ""
            _selectedProductSubcategory.value = ""
        }
    }

    // When cashier reviews the card in chat and clicks "Proses Sekarang"
    fun onProcessUserCard(userCard: ChatMessage.UserTransactionCardMessage) {
        _pendingUserCard.value = userCard

        // Check duplicate warning on any destination
        val now = System.currentTimeMillis()
        var hasDuplicate = false
        for (item in userCard.items) {
            val lastTime = recentTrxNumbers[item.destinationNumber]
            if (lastTime != null && (now - lastTime) < 15 * 60 * 1000) {
                val minutesAgo = ((now - lastTime) / (60 * 1000)).toInt()
                _messages.value = _messages.value + ChatMessage.DuplicateWarningMessage(
                    targetNumber = item.destinationNumber,
                    productCode = item.productCode,
                    lastTransactedMinutesAgo = if (minutesAgo < 1) 1 else minutesAgo
                )
                _messages.value = _messages.value.map { message ->
                    if (message.id == userCard.id && message is ChatMessage.UserTransactionCardMessage) {
                        message.copy(duplicateWarningMinutes = if (minutesAgo < 1) 1 else minutesAgo)
                    } else message
                }.filterNot { it is ChatMessage.DuplicateWarningMessage }
                hasDuplicate = true
                break
            }
        }

        if (!hasDuplicate) {
            _showPinModal.value = true
        }
    }

    fun onCancelUserCard(userCard: ChatMessage.UserTransactionCardMessage) {
        // Remove the draft card from messages
        val remaining = _messages.value.filterNot { it.id == userCard.id }
        val hasTransaction = remaining.any {
            it is ChatMessage.UserTransactionCardMessage ||
                it is ChatMessage.SuccessBatchReceiptMessage
        }
        _messages.value = if (hasTransaction) {
            remaining
        } else {
            remaining + ChatMessage.WelcomeCard()
        }
    }

    fun onEditUserCard(userCard: ChatMessage.UserTransactionCardMessage) {
        val remaining = _messages.value.filterNot { it.id == userCard.id }
        val hasTransaction = remaining.any {
            it is ChatMessage.UserTransactionCardMessage ||
                it is ChatMessage.SuccessBatchReceiptMessage
        }
        _messages.value = if (hasTransaction) {
            remaining
        } else {
            remaining + ChatMessage.WelcomeCard()
        }
        _inputTextFieldValue.value = TextFieldValue(
            text = userCard.rawCommand,
            selection = TextRange(userCard.rawCommand.length)
        )
    }

    fun proceedDuplicateWarning(targetNumber: String, productCode: String) {
        val card = _messages.value.filterIsInstance<ChatMessage.UserTransactionCardMessage>()
            .firstOrNull { it.items.any { item -> item.destinationNumber == targetNumber && item.productCode == productCode } }
        if (card != null) {
            _messages.value = _messages.value.map {
                if (it.id == card.id) card.copy(duplicateWarningMinutes = null) else it
            }
        }
        _showPinModal.value = true
    }

    fun cancelDuplicateWarning(userCard: ChatMessage.UserTransactionCardMessage) {
        _inputTextFieldValue.value = TextFieldValue(
            text = userCard.rawCommand,
            selection = TextRange(userCard.rawCommand.length)
        )
        onCancelUserCard(userCard)
    }

    fun dismissPinModal() {
        _showPinModal.value = false
    }

    // When PIN is confirmed: send to OtomaX and return ONE single card response!
    fun confirmPinAndExecute(pin: String) {
        _showPinModal.value = false
        val userCard = _pendingUserCard.value ?: return

        // Mark the user card as processed in chat history
        _messages.value = _messages.value.map { msg ->
            if (msg.id == userCard.id && msg is ChatMessage.UserTransactionCardMessage) {
            msg.copy(isProcessed = true, isSending = true)
            } else {
                msg
            }
        }

        viewModelScope.launch {
            delay(1200)

            var newBalance = _balance.value
            val resultItems = mutableListOf<OtomaxItemDetail>()

            userCard.items.forEach { item ->
                newBalance -= item.price
                recentTrxNumbers[item.destinationNumber] = System.currentTimeMillis()

                resultItems.add(
                    OtomaxItemDetail(
                        transactionRefId = "TRX" + java.util.UUID.randomUUID().toString().substring(0, 8).uppercase(),
                        productCode = item.productCode,
                        productName = item.productName,
                        destination = item.destinationNumber,
                        price = item.price,
                        nominal = item.nominal,
                        adminFee = item.adminFee
                    )
                )
            }

            _balance.value = newBalance

            // Build ONE single response card for all items
            val batchResult = OtomaxBatchResult(
                customerId = userCard.customerTag,
                items = resultItems,
                totalAmount = userCard.totalAmount,
                sisaSaldo = newBalance,
                status = OtomaxStatus.SUCCESS
            )

            _messages.value = _messages.value.map { msg ->
                if (msg.id == userCard.id && msg is ChatMessage.UserTransactionCardMessage) {
                    msg.copy(isSending = false)
                } else {
                    msg
                }
            }
            _messages.value = _messages.value + ChatMessage.SuccessBatchReceiptMessage(
                batchResult = batchResult,
                replyText = userCard.rawCommand,
                customerTag = userCard.customerTag,
                replyTimeString = userCard.timeString
            )

            _pendingUserCard.value = null
        }
    }

    fun openReceiptModal(result: OtomaxBatchResult) {
        _selectedBatchReceipt.value = result
    }

    fun dismissReceiptModal() {
        _selectedBatchReceipt.value = null
    }

    fun openWhatsAppModal(result: OtomaxBatchResult) {
        _selectedWhatsAppTrx.value = result
    }

    fun dismissWhatsAppModal() {
        _selectedWhatsAppTrx.value = null
    }
}
