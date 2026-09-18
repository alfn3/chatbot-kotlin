package com.example.chatpos.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatpos.model.SampleStockData
import com.example.chatpos.model.StockItem
import com.example.chatpos.ui.theme.BackgroundCanvas
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.ErrorRed
import com.example.chatpos.ui.theme.PrimaryLight
import com.example.chatpos.ui.theme.SuccessGreen
import com.example.chatpos.ui.utils.CurrencyFormatter
import com.example.chatpos.viewmodel.ChatPOSViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockOpnameScreen(
    viewModel: ChatPOSViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val stockItems by viewModel.stockItems.collectAsState()
    val searchQuery by viewModel.stockSearchQuery.collectAsState()
    val selectedCategory by viewModel.stockSelectedCategory.collectAsState()
    val isFinalized by viewModel.isOpnameFinalized.collectAsState()

    var showScannerSheet by remember { mutableStateOf(false) }
    var showConfirmFinalizeDialog by remember { mutableStateOf(false) }

    val filteredItems = stockItems.filter { item ->
        val matchesCategory = when (selectedCategory) {
            "Semua" -> true
            "Ada Selisih" -> item.difference != 0
            else -> item.category.equals(selectedCategory, ignoreCase = true)
        }
        val matchesQuery = searchQuery.isBlank() ||
            item.itemName.contains(searchQuery, ignoreCase = true) ||
            item.itemCode.contains(searchQuery, ignoreCase = true) ||
            item.barcode.contains(searchQuery, ignoreCase = true)

        matchesCategory && matchesQuery
    }

    val totalItemsCount = stockItems.size
    val matchedCount = stockItems.count { it.difference == 0 }
    val negativeDiffCount = stockItems.count { it.difference < 0 }
    val positiveDiffCount = stockItems.count { it.difference > 0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Stock Opname Konter",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (isFinalized) "Opname Selesai (Simulasi)" else "Pemeriksaan Fisik Barang Aktif",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = if (isFinalized) Color(0xFF15803D) else Color(0xFF64748B)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { viewModel.resetStockOpname() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset Opname", tint = Color(0xFF64748B))
                    }
                }
            )
        },
        containerColor = BackgroundCanvas
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Summary Chips Statistics
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatisticPill(
                    label = "Total",
                    value = "$totalItemsCount",
                    color = Color(0xFF0F172A),
                    bgColor = Color(0xFFF1F5F9),
                    modifier = Modifier.weight(1f)
                )
                StatisticPill(
                    label = "Sesuai",
                    value = "$matchedCount",
                    color = Color(0xFF15803D),
                    bgColor = Color(0xFFDCFCE7),
                    modifier = Modifier.weight(1f)
                )
                StatisticPill(
                    label = "Minus",
                    value = "$negativeDiffCount",
                    color = Color(0xFFB91C1C),
                    bgColor = Color(0xFFFEE2E2),
                    modifier = Modifier.weight(1f)
                )
                StatisticPill(
                    label = "Lebih",
                    value = "$positiveDiffCount",
                    color = Color(0xFF1D4ED8),
                    bgColor = Color(0xFFDBEAFE),
                    modifier = Modifier.weight(1f)
                )
            }

            // Search Bar & Scan Barcode Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onStockSearchQueryChange(it) },
                    placeholder = { Text("Cari barang / kode / barcode...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF64748B))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onStockSearchQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = { showScannerSheet = true },
                    modifier = Modifier.height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Scan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Category Filter Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(SampleStockData.stockCategories) { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9999.dp))
                            .clickable { viewModel.onStockCategorySelected(cat) }
                            .background(if (isSelected) Color(0xFFEFF6FF) else Color.White)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) PrimaryLight else DividerColor,
                                shape = RoundedCornerShape(9999.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            ),
                            color = if (isSelected) PrimaryLight else Color(0xFF475569)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main List / Table of Items
            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tidak ada barang yang cocok",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Coba ubah kata kunci pencarian atau scan barcode barang baru.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showScannerSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight)
                        ) {
                            Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Scan Barcode")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        StockItemCard(
                            item = item,
                            enabled = !isFinalized,
                            onPhysicalStockChange = { newStock ->
                                viewModel.updatePhysicalStock(item.id, newStock)
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            // Bottom Confirmation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, DividerColor)
                    .padding(16.dp)
            ) {
                if (!isFinalized) {
                    Button(
                        onClick = {
                            if (negativeDiffCount > 0) {
                                showConfirmFinalizeDialog = true
                            } else {
                                viewModel.finalizeStockOpname()
                                Toast.makeText(context, "Stock opname berhasil disimpan", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (negativeDiffCount > 0) Color(0xFFD97706) else Color(0xFF16A34A)
                        )
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (negativeDiffCount > 0) "Konfirmasi Opname (Ada $negativeDiffCount Selisih Minus)" else "Simpan & Konfirmasi Stock Opname",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Opname Fisik Selesai & Divalidasi",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF15803D)
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.resetStockOpname() },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Audit Baru", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Modal: Barcode Scanner Simulation
    if (showScannerSheet) {
        BarcodeScannerModalSheet(
            onDismiss = { showScannerSheet = false },
            onScan = { barcode ->
                val result = viewModel.scanBarcode(barcode)
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Modal: Confirm Finalize Dialog with Negative Difference Warning
    if (showConfirmFinalizeDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmFinalizeDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD97706))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Peringatan Selisih Negatif", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Terdapat $negativeDiffCount barang dengan jumlah stok fisik LEBIH KECIL dari catatan sistem.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Barang dengan selisih minus:",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF0F172A)
                    )
                    stockItems.filter { it.difference < 0 }.forEach { item ->
                        Text(
                            text = "• ${item.itemName}: fisik ${item.physicalStock} / sistem ${item.systemStock} (${item.difference})",
                            style = MaterialTheme.typography.bodySmall,
                            color = ErrorRed
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tetap konfirmasi hasil pemeriksaan fisik ini?",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmFinalizeDialog = false
                        viewModel.finalizeStockOpname()
                        Toast.makeText(context, "Stock opname disimpan dengan catatan selisih", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                ) {
                    Text("Tetap Konfirmasi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmFinalizeDialog = false }) {
                    Text("Cek Ulang Fisik")
                }
            }
        )
    }
}

@Composable
private fun StatisticPill(
    label: String,
    value: String,
    color: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                ),
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = color.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun StockItemCard(
    item: StockItem,
    enabled: Boolean,
    onPhysicalStockChange: (Int) -> Unit
) {
    val isBalanced = item.difference == 0
    val isNegative = item.difference < 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isNegative) Color(0xFFFCA5A5) else DividerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header row: Code & Category Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.itemCode,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        ),
                        color = PrimaryLight
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• Barcode: ${item.barcode}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = Color(0xFF64748B)
                    )
                }

                // Difference Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            when {
                                isBalanced -> Color(0xFFDCFCE7)
                                isNegative -> Color(0xFFFEE2E2)
                                else -> Color(0xFFDBEAFE)
                            }
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = when {
                            isBalanced -> "Pas (0)"
                            isNegative -> "Kurang (${item.difference})"
                            else -> "Lebih (+${item.difference})"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = when {
                            isBalanced -> Color(0xFF15803D)
                            isNegative -> Color(0xFFB91C1C)
                            else -> Color(0xFF1D4ED8)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Item Name
            Text(
                text = item.itemName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                ),
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(8.dp))

            // Stock Comparison & Stepper Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stock System & Price
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sistem: ",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "${item.systemStock} pcs",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            ),
                            color = Color(0xFF0F172A)
                        )
                    }
                    Text(
                        text = CurrencyFormatter.formatRupiah(item.unitPrice),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFF94A3B8)
                    )
                }

                // Physical Stock Stepper
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Fisik: ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = { if (item.physicalStock > 0) onPhysicalStockChange(item.physicalStock - 1) },
                        enabled = enabled && item.physicalStock > 0,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (item.physicalStock > 0 && enabled) Color(0xFFE2E8F0) else Color(0xFFF1F5F9))
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Kurang", modifier = Modifier.size(14.dp))
                    }

                    Box(
                        modifier = Modifier.width(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${item.physicalStock}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp
                            ),
                            color = if (isNegative) ErrorRed else Color(0xFF0F172A)
                        )
                    }

                    IconButton(
                        onClick = { onPhysicalStockChange(item.physicalStock + 1) },
                        enabled = enabled,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (enabled) Color(0xFFDBEAFE) else Color(0xFFF1F5F9))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Tambah", tint = PrimaryLight, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BarcodeScannerModalSheet(
    onDismiss: () -> Unit,
    onScan: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var manualBarcode by remember { mutableStateOf("") }
    var isSimulatingScan by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Simulasi Pemindai Barcode",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Arahkan kamera ke barcode barang atau pilih barcode demo di bawah",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated Camera Viewfinder Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .border(2.dp, PrimaryLight, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isSimulatingScan) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Membaca Barcode...", color = Color.White, fontSize = 12.sp)
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Simulated Red laser scan line
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(2.dp)
                                .background(Color.Red.copy(alpha = 0.8f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "[ Kamera Barcode Aktif (Simulasi) ]",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Scan Cepat Barcode Demo:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0F172A),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Quick Demo Barcodes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onScan("8991001")
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Perdana Tsel", fontSize = 11.sp, maxLines = 1)
                }
                OutlinedButton(
                    onClick = {
                        onScan("8991005")
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Kabel Type-C", fontSize = 11.sp, maxLines = 1)
                }
                OutlinedButton(
                    onClick = {
                        onScan("8991008")
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Earphone", fontSize = 11.sp, maxLines = 1)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Test Unknown Barcode button to satisfy DoD "barcode tidak dikenal"
            OutlinedButton(
                onClick = {
                    onScan("9990001")
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB45309))
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Test Barcode Tidak Dikenal (9990001)", fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(12.dp))

            // Manual Barcode Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = manualBarcode,
                    onValueChange = { manualBarcode = it },
                    placeholder = { Text("Ketik kode barcode manual...", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(10.dp)
                )

                Button(
                    onClick = {
                        if (manualBarcode.isNotBlank()) {
                            onScan(manualBarcode.trim())
                            onDismiss()
                        }
                    },
                    enabled = manualBarcode.isNotBlank(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight)
                ) {
                    Text("Cari")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
