# 🚀 Kotlin + Jetpack Compose - Stitch ChatPOS Starter Templates

Berikut adalah template code yang siap copy-paste untuk memulai implementasi.

---

## 1️⃣ Theme System Setup

### `ui/theme/Color.kt`
```kotlin
package com.example.chatpos.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Colors
val PrimaryDark = Color(0xFF0048C8)
val PrimaryLight = Color(0xFF1E60F2)
val PrimaryContainer = Color(0xFF1E60F2)
val OnPrimary = Color(0xFFFFFFFF)
val OnPrimaryContainer = Color(0xFFECEEFF)

// Secondary Colors (Success)
val SecondaryDark = Color(0xFF006C49)
val SecondaryLight = Color(0xFF6CF8BB)
val OnSecondary = Color(0xFFFFFFFF)

// Tertiary Colors (Warning)
val TertiaryDark = Color(0xFF774A00)
val TertiaryLight = Color(0xFF986000)
val OnTertiary = Color(0xFFFFFFFF)

// Error Colors
val ErrorDark = Color(0xFFBA1A1A)
val ErrorLight = Color(0xFFFFDAD6)
val OnError = Color(0xFFFFFFFF)

// Neutral Colors - Surfaces
val SurfaceLight = Color(0xFFF8F9FF)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFEFF4FF)
val SurfaceContainer = Color(0xFFE5EEFF)
val SurfaceContainerHigh = Color(0xFFDCE9FF)
val SurfaceContainerHighest = Color(0xFFD3E4FE)
val OnSurface = Color(0xFF0B1C30)
val OnSurfaceVariant = Color(0xFF434655)

// Neutral Colors - Text & Borders
val TextPrimary = Color(0xFF0B1C30)
val TextSecondary = Color(0xFF434655)
val TextTertiary = Color(0xFF64748B)
val TextMuted = Color(0xFF94A3B8)

val OutlineLight = Color(0xFF737687)
val OutlineVariant = Color(0xFFC3C5D8)

// Inverse Colors
val InverseSurface = Color(0xFF213145)
val InverseOnSurface = Color(0xFFEAF1FF)
val InversePrimary = Color(0xFFB5C4FF)

// Background
val BackgroundLight = Color(0xFFF8F9FF)

// Semantic Colors
val SuccessGreen = Color(0xFF10B981)
val WarningAmber = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)
val InfoBlue = Color(0xFF6366F1)
```

### `ui/theme/Type.kt`
```kotlin
package com.example.chatpos.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Font Families
val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.W400),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.W500),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.W600),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.W700),
)

val InterFont = FontFamily(
    Font(R.font.inter_regular, FontWeight.W400),
    Font(R.font.inter_medium, FontWeight.W500),
    Font(R.font.inter_semibold, FontWeight.W600),
    Font(R.font.inter_bold, FontWeight.W700),
)

// Typography Styles
val ChatPOSTypography = Typography(
    // Headline Large: 24sp, 700, -2% letter spacing
    headlineLarge = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.02).sp,
    ),
    // Headline Medium: 20sp, 700, -1% letter spacing
    headlineMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.01).sp,
    ),
    // Headline Small: 16sp, 600, 0 letter spacing
    headlineSmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    // Title Large (reuse Headline Small)
    titleLarge = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    // Body Large: 15sp, 400, -1% letter spacing
    bodyLarge = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.01).sp,
    ),
    // Body Medium: 14sp, 400, 0 letter spacing
    bodyMedium = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    // Body Small: 12sp, 400, 0 letter spacing
    bodySmall = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    ),
    // Label Large: 14sp, 600, 0 letter spacing
    labelLarge = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    // Label Medium: 12sp, 600, 1% letter spacing
    labelMedium = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W600,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.01.sp,
    ),
    // Label Small: 11sp, 500, 2% letter spacing
    labelSmall = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.W500,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.02.sp,
    ),
)

// Custom Currency Display Style
val CurrencyDisplayStyle = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = FontWeight.W700,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    letterSpacing = (-0.02).sp,
)
```

### `ui/theme/Shape.kt`
```kotlin
package com.example.chatpos.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val ChatPOSShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
)

// Additional custom shapes
object ChatPOSShapeTokens {
    val ExtraSmall = RoundedCornerShape(4.dp)
    val Small = RoundedCornerShape(8.dp)
    val Medium = RoundedCornerShape(12.dp)
    val Large = RoundedCornerShape(16.dp)
    val ExtraLarge = RoundedCornerShape(24.dp)
    val Full = RoundedCornerShape(9999.dp)
}
```

### `ui/theme/Theme.kt`
```kotlin
package com.example.chatpos.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = ColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = SecondaryLight,
    onSecondary = OnSecondary,
    secondaryContainer = Color(0xFF6CF8BB),
    onSecondaryContainer = Color(0xFF00714D),
    tertiary = TertiaryLight,
    onTertiary = OnTertiary,
    tertiaryContainer = Color(0xFFFFDDB8),
    onTertiaryContainer = Color(0xFF2A1700),
    error = ErrorDark,
    onError = OnError,
    errorContainer = ErrorLight,
    onErrorContainer = Color(0xFF93000A),
    background = BackgroundLight,
    onBackground = OnSurface,
    surface = SurfaceContainerLowest,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = OnSurfaceVariant,
    outline = OutlineLight,
    outlineVariant = OutlineVariant,
    scrim = Color.Black,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    inversePrimary = InversePrimary,
    surfaceBright = SurfaceLight,
    surfaceDim = Color(0xFFCBDBF5),
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainerLowest = SurfaceContainerLowest,
)

@Composable
fun ChatPOSTheme(
    useDarkMode: Boolean = isSystemInDarkMode(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColorScheme, // TODO: Add dark mode support
        typography = ChatPOSTypography,
        shapes = ChatPOSShapes,
        content = content,
    )
}
```

---

## 2️⃣ Spacing & Modifier Utilities

### `ui/theme/Dimensions.kt`
```kotlin
package com.example.chatpos.ui.theme

import androidx.compose.ui.unit.dp

object ChatPOSDimensions {
    // Spacing
    val SpacingXs = 4.dp
    val SpacingSm = 8.dp
    val SpacingMd = 12.dp
    val SpacingLg = 16.dp
    val SpacingXl = 24.dp
    val SpacingXxl = 32.dp
    
    val ChatGap = 10.dp
    val ContainerPadding = 16.dp
    val BottomDockHeight = 68.dp
    
    // Component sizes
    val AvatarSize = 36.dp
    val LargeAvatarSize = 48.dp
    val IconSize = 24.dp
    val SmallIconSize = 16.dp
    
    val TouchTargetMin = 48.dp
    val ButtonHeight = 40.dp
    val SmallButtonHeight = 36.dp
    
    val AppBarHeight = 56.dp
    
    // Border radius
    val BorderRadiusSmall = 4.dp
    val BorderRadiusDefault = 8.dp
    val BorderRadiusMedium = 12.dp
    val BorderRadiusLarge = 16.dp
    val BorderRadiusXlarge = 24.dp
    
    // Elevation
    val ElevationSmall = 1.dp
    val ElevationMedium = 2.dp
    val ElevationLarge = 3.dp
}
```

### `ui/utils/ModifierExtensions.kt`
```kotlin
package com.example.chatpos.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatpos.ui.theme.ChatPOSDimensions

fun Modifier.chatCard(
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline,
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

fun Modifier.batchCard() = this
    .background(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(ChatPOSDimensions.BorderRadiusLarge)
    )

fun Modifier.userMessageBubble() = this
    .background(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(
            topStart = ChatPOSDimensions.BorderRadiusLarge,
            topEnd = ChatPOSDimensions.BorderRadiusLarge,
            bottomStart = ChatPOSDimensions.BorderRadiusLarge,
            bottomEnd = ChatPOSDimensions.BorderRadiusSmall,
        )
    )

fun Modifier.systemCard() = this
    .chatCard(
        backgroundColor = MaterialTheme.colorScheme.surface,
        borderColor = MaterialTheme.colorScheme.outline,
    )

fun Modifier.warningCard(
    backgroundColor: Color = Color(0xFFFFFBEB),
    borderColor: Color = Color(0xFFF59E0B),
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

fun Modifier.elevatedSurface() = this
    .chatCard()
```

---

## 3️⃣ Core Components

### `ui/components/ChatBubble.kt`
```kotlin
package com.example.chatpos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatpos.ui.theme.ChatPOSDimensions

@Composable
fun UserMessageBubble(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(end = ChatPOSDimensions.SpacingMd)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(
                    topStart = ChatPOSDimensions.BorderRadiusLarge,
                    topEnd = ChatPOSDimensions.BorderRadiusLarge,
                    bottomStart = ChatPOSDimensions.BorderRadiusLarge,
                    bottomEnd = ChatPOSDimensions.BorderRadiusSmall,
                )
            )
            .padding(ChatPOSDimensions.SpacingMd),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
fun SystemMessageCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(start = ChatPOSDimensions.SpacingMd)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(ChatPOSDimensions.BorderRadiusLarge)
            )
            .padding(ChatPOSDimensions.SpacingLg),
    ) {
        androidx.compose.foundation.layout.Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
```

### `ui/components/InputCommandBar.kt`
```kotlin
package com.example.chatpos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatpos.ui.theme.ChatPOSDimensions

@Composable
fun InputCommandBar(
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var inputText by remember { mutableStateOf("") }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ChatPOSDimensions.BottomDockHeight)
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(ChatPOSDimensions.SpacingMd),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Attachment Button
        IconButton(
            onClick = { /* TODO: Handle attachment */ },
            modifier = Modifier.size(ChatPOSDimensions.IconSize),
        ) {
            Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = "Attach file",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(ChatPOSDimensions.IconSize),
            )
        }
        
        // Input Field
        Box(
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(24.dp),
                )
                .padding(horizontal = ChatPOSDimensions.SpacingMd),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (inputText.isEmpty()) {
                Text(
                    text = "Ketik nomor HP / ID Pelanggan...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                cursorBrush = androidx.compose.ui.graphics.SolidColor(
                    MaterialTheme.colorScheme.primary
                ),
            )
        }
        
        // Send Button (FAB)
        IconButton(
            onClick = {
                if (inputText.isNotBlank()) {
                    onSendMessage(inputText)
                    inputText = ""
                }
            },
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                ),
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send message",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
```

### `ui/components/CategoryPill.kt`
```kotlin
package com.example.chatpos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.chatpos.ui.theme.ChatPOSDimensions

@Composable
fun CategoryPill(
    label: String,
    icon: ImageVector,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(9999.dp),
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(9999.dp),
            )
            .padding(
                horizontal = ChatPOSDimensions.SpacingMd,
                vertical = ChatPOSDimensions.SpacingSm,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(ChatPOSDimensions.SpacingSm),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = ChatPOSDimensions.SpacingSm),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
```

---

## 4️⃣ View Models & State Management

### `model/ChatMessage.kt`
```kotlin
package com.example.chatpos.model

import java.time.LocalDateTime

sealed class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {
    data class UserMessage(
        val text: String,
        val sendTime: LocalDateTime = LocalDateTime.now(),
    ) : ChatMessage()
    
    data class SystemMessage(
        val title: String,
        val content: String,
        val type: MessageType = MessageType.INFO,
        val receiveTime: LocalDateTime = LocalDateTime.now(),
    ) : ChatMessage()
    
    enum class MessageType {
        INFO, WARNING, ERROR, SUCCESS
    }
}

data class TransactionBatch(
    val id: String = java.util.UUID.randomUUID().toString(),
    val items: List<TransactionItem> = emptyList(),
    val customerTag: String? = null,
    val status: TransactionStatus = TransactionStatus.DRAFT,
    val totalAmount: Long = 0L,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

data class TransactionItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val productCode: String,
    val productName: String,
    val destinationNumber: String,
    val amount: Long,
    val validity: String? = null,
    val status: ItemStatus = ItemStatus.DRAFT,
)

enum class TransactionStatus {
    DRAFT, PROCESSING, WARNING, CONFIRMED, FAILED
}

enum class ItemStatus {
    DRAFT, PENDING, WARNING, CONFIRMED, FAILED
}
```

### `viewmodel/ChatPOSViewModel.kt`
```kotlin
package com.example.chatpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatpos.model.ChatMessage
import com.example.chatpos.model.TransactionBatch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatPOSViewModel : ViewModel() {
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()
    
    private val _currentBatch = MutableStateFlow<TransactionBatch?>(null)
    val currentBatch = _currentBatch.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow("Pulsa")
    val selectedCategory = _selectedCategory.asStateFlow()
    
    private val _counterName = MutableStateFlow("TOKO BERKAH CELL")
    val counterName = _counterName.asStateFlow()
    
    private val _isOnline = MutableStateFlow(true)
    val isOnline = _isOnline.asStateFlow()
    
    fun addMessage(message: ChatMessage) {
        viewModelScope.launch {
            val currentMessages = _messages.value.toMutableList()
            currentMessages.add(message)
            _messages.emit(currentMessages)
        }
    }
    
    fun selectCategory(categoryName: String) {
        viewModelScope.launch {
            _selectedCategory.emit(categoryName)
        }
    }
    
    fun updateBatch(batch: TransactionBatch) {
        viewModelScope.launch {
            _currentBatch.emit(batch)
        }
    }
    
    fun clearBatch() {
        viewModelScope.launch {
            _currentBatch.emit(null)
        }
    }
}
```

---

## 5️⃣ Screen Composition Example

### `screens/ChatScreen.kt`
```kotlin
package com.example.chatpos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.chatpos.ui.components.AppBar
import com.example.chatpos.ui.components.CategoryPillRow
import com.example.chatpos.ui.components.ChatBubble
import com.example.chatpos.ui.components.InputCommandBar
import com.example.chatpos.viewmodel.ChatPOSViewModel

@Composable
fun ChatScreen(
    viewModel: ChatPOSViewModel,
) {
    val messages = viewModel.messages.collectAsState().value
    val counterName = viewModel.counterName.collectAsState().value
    val isOnline = viewModel.isOnline.collectAsState().value
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        // Top App Bar
        AppBar(
            counterName = counterName,
            isOnline = isOnline,
            onSearchClick = { /* TODO */ },
            onMenuClick = { /* TODO */ },
        )
        
        // Chat Stream
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { message ->
                when (message) {
                    is ChatMessage.UserMessage -> {
                        UserMessageBubble(text = message.text)
                    }
                    is ChatMessage.SystemMessage -> {
                        SystemMessageCard(
                            title = message.title,
                            content = message.content,
                        )
                    }
                }
            }
        }
        
        // Quick Category Bar
        CategoryPillRow(
            selectedCategory = viewModel.selectedCategory.collectAsState().value,
            onCategorySelected = { viewModel.selectCategory(it) },
        )
        
        // Input Bar
        InputCommandBar(
            onSendMessage = { text ->
                viewModel.addMessage(ChatMessage.UserMessage(text))
            },
        )
    }
}
```

---

## 🔄 Implementation Tips

### Running the Project
1. Create new Android Project with Jetpack Compose template
2. Copy theme files into `ui/theme/`
3. Copy components into `ui/components/`
4. Add font files to `res/font/` (Plus Jakarta Sans + Inter)
5. Update `build.gradle` with latest Compose version

### Testing Components
```kotlin
@Preview(showBackground = true)
@Composable
fun PreviewChatBubble() {
    ChatPOSTheme {
        UserMessageBubble(text = "Pulsa 5K untuk nomor 0895...")
    }
}
```

### Performance Tips
- Use `remember` for state that shouldn't recompose
- Use `LazyColumn` for long message lists
- Avoid re-creating lambdas in composables (use `remember { }`)
- Use `key()` in LazyColumn for stable scroll positions

---

**Next Steps:**
1. Set up project structure
2. Implement theme system
3. Build components one by one with preview
4. Assemble screen compositions
5. Test on actual devices (phones & tablets)

Ready to start Phase 2 (API & Logic) once UI is complete!
