package com.example.chatpos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.chatpos.model.ExpenseCategories
import com.example.chatpos.model.ProductItem
import com.example.chatpos.model.SavedContact
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.PrimaryLight
import com.example.chatpos.ui.theme.SuccessGreen
import com.example.chatpos.ui.utils.CurrencyFormatter
import androidx.compose.ui.graphics.vector.ImageVector

private fun productCategoryIcon(category: String): ImageVector = when {
    category.equals("Pengeluaran", ignoreCase = true) -> Icons.Default.Payments
    category.equals("Pulsa", ignoreCase = true) -> Icons.Default.PhoneAndroid
    category.equals("Paket Data", ignoreCase = true) -> Icons.Default.Wifi
    category.equals("PLN", ignoreCase = true) || category.contains("Token", ignoreCase = true) ->
        Icons.Default.ElectricBolt
    category.equals("Transfer", ignoreCase = true) -> Icons.Default.SwapHoriz
    category.contains("Wallet", ignoreCase = true) || category.contains("Saldo", ignoreCase = true) ->
        Icons.Default.AccountBalanceWallet
    else -> Icons.Default.ShoppingBag
}

@Composable
fun TransactionInputAssistant(
    hasDotOnCurrentLine: Boolean,
    productSuggestions: List<ProductItem>,
    productCategories: List<String>,
    productSubcategories: List<String>,
    selectedProductCategory: String,
    selectedProductSubcategory: String,
    contactSuggestions: List<SavedContact>,
    transferAdminHint: String?,
    transferPinWarning: String?,
    isCurrentLineComplete: Boolean,
    onProductClick: (ProductItem) -> Unit,
    onProductCategorySelected: (String) -> Unit,
    onProductSubcategorySelected: (String) -> Unit,
    onContactClick: (SavedContact) -> Unit,
    onAddAnotherTransaction: () -> Unit,
    expenseCategories: List<String> = ExpenseCategories.allCategories,
    expenseAttachmentState: ChatMessage.ExpenseAttachmentState = ChatMessage.ExpenseAttachmentState.NONE,
    expenseAttachmentName: String? = null,
    onExpenseCategoryClick: (String) -> Unit = {},
    onSimulateAttachmentUpload: () -> Unit = {},
    onClearAttachment: () -> Unit = {},
    isProductPickerVisible: Boolean = true,
    onToggleProductPicker: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC))
    ) {
        // CASE 1: Line complete -> Prompt "Ada tambahan transaksi lain?"
        if (isCurrentLineComplete) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFF6FF))
                    .border(1.dp, Color(0xFFBFDBFE))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(PrimaryLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Ada tambahan transaksi lain?",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            color = Color(0xFF1E3A8A)
                        )
                        transferPinWarning?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }

                Button(
                    onClick = onAddAnotherTransaction,
                    modifier = Modifier.height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tambah",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = Color.White
                    )
                }
            }
        }
        // CASE 2A: Dot typed, matching contacts found
        else if (contactSuggestions.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, DividerColor)
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = PrimaryLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SARAN KONTAK TUJUAN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = PrimaryLight
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(contactSuggestions) { contact ->
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onContactClick(contact) }
                                .background(Color(0xFFF1F5F9))
                                .border(1.dp, DividerColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = PrimaryLight,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = contact.name,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    ),
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = contact.phoneNumber,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }
        }
        // CASE 2B: Dot typed, waiting for destination number input (Clear visual hint above input!)
        else if (hasDotOnCurrentLine && contactSuggestions.isEmpty()) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9))
                        .border(1.dp, DividerColor)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = PrimaryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = transferPinWarning ?: transferAdminHint
                            ?: "Lanjutkan ketik nomor tujuan (contoh: 089512345678)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        color = Color(0xFF334155)
                    )
                }
            }
        }
        // CASE 3: NO dot typed yet -> Show product selection / suggestions
        else if (!hasDotOnCurrentLine) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0))
            ) {
                LazyRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF1F5F9)),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productCategories) { category ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onProductCategorySelected(category) }
                                .background(
                                    if (category == selectedProductCategory) Color.White
                                    else Color.Transparent
                                )
                                .border(
                                    width = if (category == selectedProductCategory) 1.dp else 0.dp,
                                    color = if (category == selectedProductCategory) Color(0xFFBFDBFE)
                                    else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 9.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = productCategoryIcon(category),
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = if (category == selectedProductCategory) PrimaryLight
                                    else Color(0xFF475569)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = if (category == selectedProductCategory) PrimaryLight
                                    else Color(0xFF475569)
                                )
                            }
                        }
                    }
                }

                // CASE: PENGELUARAN CATEGORY SELECTED
                if (isProductPickerVisible && selectedProductCategory.equals("Pengeluaran", ignoreCase = true)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "PILIH KATEGORI PENGELUARAN",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.3.sp
                                ),
                                color = Color(0xFF334155)
                            )
                        }

                        // Expense category chips
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(expenseCategories) { cat ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onExpenseCategoryClick(cat) }
                                        .background(Color(0xFFEFF6FF))
                                        .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.sp
                                        ),
                                        color = PrimaryLight
                                    )
                                }
                            }
                        }

                        // Attachment simulation button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            when (expenseAttachmentState) {
                                ChatMessage.ExpenseAttachmentState.NONE -> {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onSimulateAttachmentUpload() }
                                            .background(Color(0xFFF1F5F9))
                                            .border(1.dp, DividerColor, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.AttachFile,
                                                contentDescription = null,
                                                tint = Color(0xFF475569),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "📎 Lampirkan Foto Nota (Simulasi)",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 11.sp
                                                ),
                                                color = Color(0xFF334155)
                                            )
                                        }
                                    }
                                }
                                ChatMessage.ExpenseAttachmentState.UPLOADING -> {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFEFF6FF))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(12.dp),
                                                strokeWidth = 2.dp,
                                                color = PrimaryLight
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Mengunggah foto nota...",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                                color = PrimaryLight
                                            )
                                        }
                                    }
                                }
                                ChatMessage.ExpenseAttachmentState.SUCCESS -> {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFECFDF5))
                                            .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = SuccessGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = expenseAttachmentName ?: "Nota terlampir",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 11.sp
                                                ),
                                                color = Color(0xFF15803D)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Hapus",
                                                tint = Color(0xFF64748B),
                                                modifier = Modifier
                                                    .size(14.dp)
                                                    .clickable { onClearAttachment() }
                                            )
                                        }
                                    }
                                }
                                ChatMessage.ExpenseAttachmentState.FAILED -> {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { onSimulateAttachmentUpload() }
                                            .background(Color(0xFFFEE2E2))
                                            .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Gagal unggah. Coba lagi",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                                color = Color(0xFFB91C1C)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (isProductPickerVisible && selectedProductCategory.isNotBlank() && !selectedProductCategory.equals("Pengeluaran", ignoreCase = true) && productSubcategories.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryLight)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "KELOMPOK PRODUK ${(selectedProductCategory.ifBlank { "PULSA" }).uppercase()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.3.sp
                                ),
                                color = Color(0xFF334155)
                            )
                        }
                        if (selectedProductSubcategory.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEFF6FF))
                                    .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 9.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Provider otomatis",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = Color(0xFF2563EB)
                                )
                            }
                        }
                    }
                    LazyRow(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(productSubcategories) { subcategory ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onProductSubcategorySelected(subcategory) }
                                    .background(
                                        if (subcategory == selectedProductSubcategory) PrimaryLight
                                        else Color(0xFFF1F5F9)
                                    )
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = subcategory,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = if (subcategory == selectedProductSubcategory) Color.White else Color(0xFF475569)
                                )
                            }
                        }
                        }
                    }
                }

                if (isProductPickerVisible && productSuggestions.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(productSuggestions) { index, product ->
                            val cardBackgrounds = listOf(
                                Color(0xFFF5F9FF),
                                Color(0xFFF5FBF8),
                                Color(0xFFFFFAF2),
                                Color(0xFFF9F5FF)
                            )
                            val cardTint = cardBackgrounds[index % cardBackgrounds.size]
                            Box(
                                modifier = Modifier
                                        .width(166.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onProductClick(product) }
                                        .background(cardTint)
                                        .border(
                                            1.dp,
                                            if (product == productSuggestions.firstOrNull()) PrimaryLight else Color(0xFFE2E8F0),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 11.dp),
                                    contentAlignment = Alignment.TopStart
                            ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = product.name,
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                ),
                                                color = Color(0xFF0F172A),
                                                maxLines = 2,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(if (product == productSuggestions.firstOrNull()) PrimaryLight else Color(0xFFF1F5F9))
                                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = product.code,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 9.sp,
                                                        fontFamily = FontFamily.Monospace
                                                    ),
                                                    color = if (product == productSuggestions.firstOrNull()) Color.White else Color(0xFF475569)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = CurrencyFormatter.formatRupiah(product.sellPrice),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 14.sp
                                            ),
                                            color = PrimaryLight
                                        )
                                        Text(
                                            text = product.validity,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = Color(0xFF64748B)
                                        )
                                        Spacer(modifier = Modifier.height(7.dp))
                                        Text(
                                            text = if (product.isAvailable) "Stok Ready" else "Tidak tersedia",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 10.sp
                                            ),
                                            color = if (product.isAvailable) Color(0xFF047857) else Color(0xFFB91C1C),
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(5.dp))
                                                .background(
                                                    if (product.isAvailable) Color(0xFFD1FAE5)
                                                    else Color(0xFFFEE2E2)
                                                )
                                                .padding(horizontal = 7.dp, vertical = 4.dp)
                                        )
                                    }
                        }
                    }
                }
            }
        }
    }
}
