package com.example.chatpos.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.chatpos.ui.screens.ChatHistoryScreen
import com.example.chatpos.ui.screens.ChatScreen
import com.example.chatpos.ui.screens.CashDrawerScreen
import com.example.chatpos.ui.screens.ExpenseChatScreen
import com.example.chatpos.ui.screens.StockOpnameScreen
import com.example.chatpos.ui.screens.TransactionHistoryScreen
import com.example.chatpos.viewmodel.ChatPOSViewModel

@Composable
fun AppShell(viewModel: ChatPOSViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var isChatDetailOpen by remember { mutableStateOf(false) }
    var isChatPOSOpen by remember { mutableStateOf(false) }
    var isExpenseOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isChatDetailOpen && !isChatPOSOpen) {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0; isChatDetailOpen = false; isChatPOSOpen = false },
                        icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Tab Chat") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2563EB),
                            selectedTextColor = Color(0xFF2563EB),
                            indicatorColor = Color(0xFFDCEBFF)
                        ),
                        label = { Text("Chat") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1; isChatDetailOpen = false; isChatPOSOpen = false },
                        icon = { Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = "Tab Riwayat") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2563EB),
                            selectedTextColor = Color(0xFF2563EB),
                            indicatorColor = Color(0xFFDCEBFF)
                        ),
                        label = { Text("Riwayat") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2; isChatDetailOpen = false; isChatPOSOpen = false },
                        icon = { Icon(Icons.Default.PointOfSale, contentDescription = "Tab Uang Laci") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2563EB),
                            selectedTextColor = Color(0xFF2563EB),
                            indicatorColor = Color(0xFFDCEBFF)
                        ),
                        label = { Text("Uang Laci") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3; isChatDetailOpen = false; isChatPOSOpen = false },
                        icon = { Icon(Icons.Default.Inventory2, contentDescription = "Tab Stok") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2563EB),
                            selectedTextColor = Color(0xFF2563EB),
                            indicatorColor = Color(0xFFDCEBFF)
                        ),
                        label = { Text("Stok") }
                    )
                }
            }
        }
    ) { padding ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> if (isChatPOSOpen) {
                    ChatScreen(
                        viewModel = viewModel,
                        onBack = { isChatPOSOpen = false },
                        onOpenExpenseChat = { isExpenseOpen = true }
                    )
                } else if (isExpenseOpen) {
                    ExpenseChatScreen(viewModel = viewModel, onBack = { isExpenseOpen = false })
                } else ChatHistoryScreen(
                    viewModel = viewModel,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(),
                    onOpenChatPOS = { isChatPOSOpen = true },
                    onDetailVisibilityChanged = { isChatDetailOpen = it },
                    onShareReceipt = {
                        Toast.makeText(context, "Nota siap dibagikan ke WhatsApp", Toast.LENGTH_SHORT).show()
                    }
                )
                1 -> TransactionHistoryScreen(
                    viewModel = viewModel,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(),
                    onGoToChat = { selectedTab = 0; isChatDetailOpen = false; isChatPOSOpen = true }
                )
                2 -> CashDrawerScreen(
                    viewModel = viewModel
                )
                3 -> StockOpnameScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
