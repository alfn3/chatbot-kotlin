package com.example.chatpos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatpos.ui.screens.AppShell
import com.example.chatpos.ui.theme.BackgroundCanvas
import com.example.chatpos.ui.theme.ChatPOSTheme
import com.example.chatpos.viewmodel.ChatPOSViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatPOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundCanvas
                ) {
                    val viewModel: ChatPOSViewModel = viewModel()
                    AppShell(viewModel = viewModel)
                }
            }
        }
    }
}
