package com.example.chatpos.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.chatpos.model.CashDenomination
import com.example.chatpos.ui.theme.BackgroundCanvas
import com.example.chatpos.ui.theme.DividerColor
import com.example.chatpos.ui.theme.ErrorRed
import com.example.chatpos.ui.theme.PrimaryLight
import com.example.chatpos.ui.theme.SuccessGreen
import com.example.chatpos.ui.utils.CurrencyFormatter
import com.example.chatpos.viewmodel.ChatPOSViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashDrawerScreen(
    viewModel: ChatPOSViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isShiftClosed by viewModel.isShiftClosed.collectAsState()
    val closedShiftSummary by viewModel.closedShiftSummary.collectAsState()
    val initialCash by viewModel.drawerInitialCash.collectAsState()
    val totalSales by viewModel.drawerTotalSales.collectAsState()
    val totalExpenses by viewModel.drawerTotalExpenses.collectAsState()
    val expectedCash by viewModel.drawerExpectedCash.collectAsState()
    val actualCountedCash by viewModel.drawerActualCountedCash.collectAsState()
    val cashDifference by viewModel.drawerCashDifference.collectAsState()
    val denominations by viewModel.cashDenominations.collectAsState()
    val isManualCashMode by viewModel.isManualCashMode.collectAsState()
    val manualCashInput by viewModel.manualCashInput.collectAsState()

    var showCloseShiftConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Rekonsiliasi Uang Laci",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (isShiftClosed) "Shift Ditutup • Rekonsiliasi Selesai" else "Shift 1 Aktif • Buka 08:00 WIB",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = if (isShiftClosed) Color(0xFF64748B) else Color(0xFF15803D)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    if (isShiftClosed) {
                        TextButton(
                            onClick = { viewModel.resetShift() }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Shift Baru", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            )
        },
        containerColor = BackgroundCanvas
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(2.dp))
            }

            // SECTION 1: Ringkasan Kas Sistem (Expected Cash Card)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEFF6FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PointOfSale,
                                        contentDescription = null,
                                        tint = PrimaryLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "SALDO KAS SISTEM",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = Color(0xFF0F172A)
                                )
                            }

                            // Simulation status pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "Simulasi Kas",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Row: Kas Modal Awal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Kas Modal Awal",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = CurrencyFormatter.formatRupiah(initialCash),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = Color(0xFF0F172A)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Row: Penjualan Tunai
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Penjualan Tunai (Masuk)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                            }
                            Text(
                                text = "+${CurrencyFormatter.formatRupiah(totalSales)}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = SuccessGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Row: Pengeluaran Operasional
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = ErrorRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Pengeluaran Operasional (Keluar)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                            }
                            Text(
                                text = "-${CurrencyFormatter.formatRupiah(totalExpenses)}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = if (totalExpenses > 0) ErrorRed else Color(0xFF64748B)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = DividerColor)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Total Kas Sistem Diharapkan
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Kas Diharapkan:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = CurrencyFormatter.formatRupiah(expectedCash),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 18.sp
                                ),
                                color = PrimaryLight
                            )
                        }
                    }
                }
            }

            // SECTION 2: Status Selisih Kas (Difference Indicator)
            item {
                val isBalanced = cashDifference == 0L
                val isSurplus = cashDifference > 0L

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isBalanced -> Color(0xFFDCFCE7)
                            isSurplus -> Color(0xFFEFF6FF)
                            else -> Color(0xFFFEE2E2)
                        }
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = when {
                            isBalanced -> Color(0xFF86EFAC)
                            isSurplus -> Color(0xFFBFDBFE)
                            else -> Color(0xFFFCA5A5)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isBalanced -> Color(0xFF16A34A)
                                            isSurplus -> PrimaryLight
                                            else -> ErrorRed
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when {
                                        isBalanced -> Icons.Default.CheckCircle
                                        isSurplus -> Icons.AutoMirrored.Filled.TrendingUp
                                        else -> Icons.Default.Warning
                                    },
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = when {
                                        isBalanced -> "Uang Laci Sesuai (Pas)"
                                        isSurplus -> "Uang Laci Lebih (Surplus)"
                                        else -> "Uang Laci Kurang (Defisit)"
                                    },
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = when {
                                        isBalanced -> Color(0xFF15803D)
                                        isSurplus -> Color(0xFF1E40AF)
                                        else -> Color(0xFF991B1B)
                                    }
                                )
                                Text(
                                    text = when {
                                        isBalanced -> "Fisik laci sama persis dengan sistem"
                                        isSurplus -> "Fisik laci melebihi catatan sistem"
                                        else -> "Fisik laci kurang dari catatan sistem!"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = when {
                                        isBalanced -> Color(0xFF166534)
                                        isSurplus -> Color(0xFF1D4ED8)
                                        else -> Color(0xFFB91C1C)
                                    }
                                )
                            }
                        }

                        // Difference Amount
                        Text(
                            text = when {
                                isBalanced -> "Rp 0"
                                isSurplus -> "+${CurrencyFormatter.formatRupiah(cashDifference)}"
                                else -> CurrencyFormatter.formatRupiah(cashDifference)
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 16.sp
                            ),
                            color = when {
                                isBalanced -> Color(0xFF15803D)
                                isSurplus -> Color(0xFF1E40AF)
                                else -> Color(0xFF991B1B)
                            }
                        )
                    }
                }
            }

            // SECTION 3: Perhitungan Uang Fisik Aktual
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "HITUNG UANG FISIK DI LACI",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = CurrencyFormatter.formatRupiah(actualCountedCash),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 16.sp
                                ),
                                color = Color(0xFF0F172A)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tab selector: Hitung Pecahan vs Input Manual
                        TabRow(
                            selectedTabIndex = if (isManualCashMode) 1 else 0,
                            containerColor = Color(0xFFF1F5F9),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .padding(2.dp)
                        ) {
                            Tab(
                                selected = !isManualCashMode,
                                onClick = { viewModel.setManualCashMode(false) },
                                text = { Text("Hitung Pecahan", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                            )
                            Tab(
                                selected = isManualCashMode,
                                onClick = { viewModel.setManualCashMode(true) },
                                text = { Text("Total Manual", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isManualCashMode) {
                            // Manual Input Mode
                            Column {
                                Text(
                                    text = "Masukkan total uang tunai yang ada di laci:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = manualCashInput,
                                    onValueChange = { viewModel.onManualCashInputChange(it) },
                                    label = { Text("Total Fisik Uang Laci (Rp)") },
                                    placeholder = { Text("cth: 1500000") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        } else {
                            // Pecahan Mode: List of Denominations
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Uang Kertas",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF64748B)
                                    )
                                )

                                denominations.filter { !it.isCoin }.forEach { denom ->
                                    DenominationRow(
                                        denom = denom,
                                        enabled = !isShiftClosed,
                                        onCountChange = { newCount ->
                                            viewModel.updateDenominationCount(denom.value, denom.isCoin, newCount)
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Uang Logam / Koin",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF64748B)
                                    )
                                )

                                denominations.filter { it.isCoin }.forEach { denom ->
                                    DenominationRow(
                                        denom = denom,
                                        enabled = !isShiftClosed,
                                        onCountChange = { newCount ->
                                            viewModel.updateDenominationCount(denom.value, denom.isCoin, newCount)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 4: Tutup Shift Button / Ringkasan Penutupan
            item {
                if (!isShiftClosed) {
                    Button(
                        onClick = { showCloseShiftConfirmDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E40AF)
                        )
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tutup Shift Sekarang",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                } else {
                    // Closed shift banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Ringkasan Penutupan Shift Selesai",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color(0xFF0F172A)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Waktu Tutup: ${closedShiftSummary?.shiftClosedAt ?: "Sekarang"} • Kasir: ${closedShiftSummary?.cashierName ?: "Kasir Utama"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "Hasil rekonsiliasi telah divalidasi lokal (simulasi) dan siap diserahkan ke shift berikutnya.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF475569)
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        Toast.makeText(context, "Mencetak struk rekonsiliasi shift...", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Cetak", fontSize = 12.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        Toast.makeText(context, "Laporan shift siap dibagikan", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Bagikan", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Modal: Confirmation to Close Shift
    if (showCloseShiftConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showCloseShiftConfirmDialog = false },
            title = {
                Text(
                    text = "Konfirmasi Tutup Shift?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Pastikan seluruh uang fisik di laci sudah dihitung dan rekonsiliasi sudah sesuai.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• Saldo Sistem: ${CurrencyFormatter.formatRupiah(expectedCash)}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "• Fisik Aktual: ${CurrencyFormatter.formatRupiah(actualCountedCash)}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "• Selisih: ${if (cashDifference >= 0) "+" else ""}${CurrencyFormatter.formatRupiah(cashDifference)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (cashDifference == 0L) SuccessGreen else if (cashDifference > 0) PrimaryLight else ErrorRed
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCloseShiftConfirmDialog = false
                        viewModel.closeShift()
                        Toast.makeText(context, "Shift 1 berhasil ditutup", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E40AF))
                ) {
                    Text("Ya, Tutup Shift")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCloseShiftConfirmDialog = false }) {
                    Text("Kembali Periksa")
                }
            }
        )
    }
}

@Composable
private fun DenominationRow(
    denom: CashDenomination,
    enabled: Boolean,
    onCountChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, DividerColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = CurrencyFormatter.formatRupiah(denom.value),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Total: ${CurrencyFormatter.formatRupiah(denom.total)}",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                ),
                color = Color(0xFF64748B)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { if (denom.count > 0) onCountChange(denom.count - 1) },
                enabled = enabled && denom.count > 0,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (denom.count > 0 && enabled) Color(0xFFE2E8F0) else Color(0xFFF1F5F9))
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Kurang", modifier = Modifier.size(14.dp))
            }

            Box(
                modifier = Modifier
                    .width(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${denom.count}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = Color(0xFF0F172A)
                )
            }

            IconButton(
                onClick = { onCountChange(denom.count + 1) },
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
