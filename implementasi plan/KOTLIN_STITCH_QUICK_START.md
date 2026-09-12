# 🚀 Stitch ChatPOS Kotlin Implementation - Quick Start Guide

**Ringkasan eksekusi untuk implementasi design system Stitch ke aplikasi Kotlin Android.**

---

## 📦 PROJECT SETUP (15 menit)

### 1. Create New Project
```bash
# Android Studio > New Project > Phone and Tablet > Empty Compose Activity
# Target: API 26 (Android 8.0)
# Kotlin DSL enabled
```

### 2. Update `build.gradle.kts` (Project)
```kotlin
plugins {
    id("com.android.application") version "8.1.1"
    kotlin("android") version "1.9.0"
}

kotlin {
    jvmToolchain(17)
}
```

### 3. Update `build.gradle.kts` (App)
```kotlin
dependencies {
    val composeVersion = "1.6.0"
    val materialVersion = "1.2.0-alpha06"
    
    // Jetpack Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material3:material3:$materialVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    
    // Navigation (if needed later)
    implementation("androidx.navigation:navigation-compose:2.7.0")
}
```

### 4. Add Fonts to Project
```
app/src/main/res/font/
├── plus_jakarta_sans_regular.ttf
├── plus_jakarta_sans_medium.ttf
├── plus_jakarta_sans_semibold.ttf
├── plus_jakarta_sans_bold.ttf
├── inter_regular.ttf
├── inter_medium.ttf
├── inter_semibold.ttf
└── inter_bold.ttf
```

**Download dari:**
- Plus Jakarta Sans: https://fonts.google.com/specimen/Plus+Jakarta+Sans
- Inter: https://fonts.google.com/specimen/Inter

---

## 📁 PROJECT STRUCTURE (Create these directories)

```
app/src/main/
├── java/com/example/chatpos/
│   ├── ui/
│   │   ├── theme/
│   │   │   ├── Color.kt
│   │   │   ├── Type.kt
│   │   │   ├── Shape.kt
│   │   │   ├── Theme.kt
│   │   │   └── Dimensions.kt
│   │   ├── components/
│   │   │   ├── ChatBubble.kt
│   │   │   ├── InputCommandBar.kt
│   │   │   ├── CategoryPill.kt
│   │   │   ├── BatchTransactionCard.kt
│   │   │   ├── AppBar.kt
│   │   │   └── ...other components
│   │   ├── screens/
│   │   │   ├── ChatScreen.kt
│   │   │   ├── ProductSelectionScreen.kt
│   │   │   └── ...other screens
│   │   └── utils/
│   │       ├── ModifierExtensions.kt
│   │       └── CurrencyFormatter.kt
│   ├── viewmodel/
│   │   └── ChatPOSViewModel.kt
│   ├── model/
│   │   ├── ChatMessage.kt
│   │   ├── Product.kt
│   │   ├── Transaction.kt
│   │   └── TransactionState.kt
│   └── MainActivity.kt
└── res/
    ├── font/ (TTF files)
    ├── values/
    │   ├── strings.xml
    │   └── colors.xml (optional)
    └── drawable/ (icons if using custom)
```

---

## ✅ IMPLEMENTATION CHECKLIST

### Phase 1: Foundation Setup (Day 1)
- [ ] Create new Android project with Compose
- [ ] Add dependencies to `build.gradle.kts`
- [ ] Download and add font files
- [ ] Create Color.kt with all color definitions
- [ ] Create Type.kt with typography styles
- [ ] Create Shape.kt with shape definitions
- [ ] Create Dimensions.kt with spacing tokens
- [ ] Create Theme.kt and test with preview

### Phase 2: Utilities & Extensions (Day 1-2)
- [ ] Create ModifierExtensions.kt
- [ ] Create CurrencyFormatter.kt (for Rp formatting)
- [ ] Create test previews for modifiers
- [ ] Test color contrasts (WCAG AA)

### Phase 3: Core Components (Day 2-4)
- [ ] UserMessageBubble component
- [ ] SystemMessageCard component
- [ ] AppBar component
- [ ] CategoryPill + CategoryPillRow
- [ ] InputCommandBar component
- [ ] Create preview for each component
- [ ] Test various text lengths

### Phase 4: Complex Components (Day 4-5)
- [ ] BatchTransactionCard (view mode)
- [ ] BatchTransactionCard (edit mode)
- [ ] ProductNominalGrid
- [ ] Bottom sheet modals
- [ ] Status indicators
- [ ] Test all state variations

### Phase 5: Screen Assembly (Day 5-6)
- [ ] Create data models (ChatMessage, Transaction, etc.)
- [ ] Create ChatPOSViewModel
- [ ] Assemble ChatScreen with LazyColumn
- [ ] Test scroll performance
- [ ] Test message animations

### Phase 6: Polish & Testing (Day 6-7)
- [ ] Add accessibility labels (contentDescription)
- [ ] Test on various devices (emulator + real phone)
- [ ] Test landscape orientation
- [ ] Test tablet layout
- [ ] Screenshot tests
- [ ] Performance profiling

---

## 🎨 QUICK COLOR PALETTE (Copy-Paste)

```kotlin
// Paste into Color.kt
val PrimaryDark = Color(0xFF0048C8)
val PrimaryLight = Color(0xFF1E60F2)
val SecondaryGreen = Color(0xFF10B981)
val TertiaryWarning = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)
val BackgroundLight = Color(0xFFF8F9FF)
val SurfaceWhite = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF0B1C30)
val TextSecondary = Color(0xFF434655)
val BorderLight = Color(0xFFE5EEFF)
```

---

## 🏃 FIRST COMPONENT EXERCISE (30 menit)

### Build Your First Component: UserMessageBubble

**File: `ui/components/ChatBubble.kt`**
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun UserMessageBubble(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(end = 16.dp)
            .background(
                color = Color(0xFF1E60F2),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 4.dp,
                )
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
        )
    }
}

// Preview
@androidx.compose.ui.tooling.preview.Preview
@Composable
fun PreviewUserMessageBubble() {
    UserMessageBubble(text = "Pulsa 5K untuk nomor 0895612341234")
}
```

**Test It:**
1. Paste code above
2. Click "Preview" button (top right)
3. Verify:
   - Text appears in blue bubble
   - Corners match spec (16dp except bottom-right = 4dp)
   - Text is white
   - Padding is correct

---

## 📱 TESTING ON REAL DEVICE (Optional but Recommended)

### Connect Device
1. Enable Developer Mode on Android phone
2. Enable USB Debugging
3. Connect via USB
4. Android Studio will detect device

### Deploy App
```bash
# In Android Studio
# Device dropdown (top toolbar) > Select your device
# Run > Run 'app'
```

### Test Checklist
- [ ] All components render correctly
- [ ] Text scales properly on different phone sizes
- [ ] Touch targets are easy to tap (48dp minimum)
- [ ] Colors look correct (not washed out)
- [ ] Fonts are readable

---

## 🐛 COMMON ISSUES & SOLUTIONS

### Issue 1: Fonts Not Found
```
Error: Cannot find font file "plus_jakarta_sans_regular.ttf"
```
**Solution:**
- Verify fonts are in `app/src/main/res/font/`
- Filename should be lowercase with hyphens
- Use `R.font.plus_jakarta_sans_regular` (not `.ttf`)

### Issue 2: Color Not Showing
```
Text not visible because color is same as background
```
**Solution:**
- Check color contrast using: https://webaim.org/resources/contrastchecker/
- Aim for 4.5:1 ratio for text

### Issue 3: Component Not Previewing
```
Preview not showing/crashing
```
**Solution:**
- Add `@Preview` annotation above composable
- Check for compilation errors
- Rebuild project (Build > Rebuild Project)

### Issue 4: Spacing Doesn't Look Right
```
Gaps between components seem off
```
**Solution:**
- All spacing should be multiples of 4dp
- Use DimensionTokens from Dimensions.kt
- Double-check padding vs margin usage

---

## 🚀 BUILD & RELEASE PREP (Phase 2 Later)

Once UI is complete, add:
- [ ] API integration layer
- [ ] Error handling
- [ ] Loading states with actual data
- [ ] Network error handling
- [ ] Session management
- [ ] Offline support (Room DB)
- [ ] Real-time updates (WebSocket)

---

## 📊 PERFORMANCE CHECKLIST

- [ ] LazyColumn for long lists (not Column)
- [ ] `remember` for state that shouldn't recompose
- [ ] `rememberUpdatedState` for callbacks
- [ ] Avoid creating objects in composable bodies
- [ ] Use `key()` in LazyColumn for stable positions
- [ ] Profile with Compose Layout Inspector

---

## 🧪 TESTING COMMANDS

```bash
# Run tests
./gradlew test

# Run UI tests
./gradlew connectedAndroidTest

# Build APK for manual testing
./gradlew assembleDebug

# Check code lint
./gradlew lint

# Profile performance
./gradlew profileDebugBuildTask
```

---

## 📚 USEFUL RESOURCES

### Official Documentation
- Jetpack Compose: https://developer.android.com/jetpack/compose
- Material Design 3: https://m3.material.io/
- Kotlin Documentation: https://kotlinlang.org/docs/

### Design Reference
- Material Color Tool: https://material-io.github.io/material-color-utilities/
- Compose Samples: https://github.com/android/compose-samples
- Material 3 Components: https://m3.material.io/components

### Tools
- Android Studio: Latest version from https://developer.android.com/studio
- Figma (for design collaboration): https://www.figma.com
- Postman (for API testing later): https://www.postman.com

---

## 🎯 SUCCESS CRITERIA

**Your UI implementation is successful when:**

1. ✅ All components render pixel-perfect per Stitch specs
2. ✅ App runs smoothly at 60fps (or higher)
3. ✅ Touch targets are all 48dp or larger
4. ✅ Text contrast passes WCAG AA (4.5:1+)
5. ✅ All components have preview
6. ✅ Works on 4.5" phone to 10" tablet
7. ✅ Both portrait and landscape orientations work
8. ✅ No layout jank or jitter when scrolling
9. ✅ Fonts render correctly (Plus Jakarta Sans + Inter)
10. ✅ Colors match design spec (±2% tolerance)

---

## 📅 ESTIMATED TIMELINE

| Phase | Task | Days | Notes |
|-------|------|------|-------|
| 1 | Setup & Foundation | 1 | Project creation, theme setup |
| 2 | Basic Components | 2 | Chat bubbles, pills, buttons |
| 3 | Complex Components | 2 | Batch card, grid, modals |
| 4 | Screen Assembly | 1 | Main screen composition |
| 5 | Polish & Testing | 1 | Accessibility, performance |
| **Total** | **UI Phase** | **7 days** | Ready for Phase 2 (API) |

---

## 🎓 LEARNING PATH

If you're new to Jetpack Compose:

1. **Day 1:** Learn Compose basics
   - Read: https://developer.android.com/jetpack/compose/documentation
   - Video: "Jetpack Compose Tutorial" on Android Developers YouTube

2. **Day 2:** Study Material Design 3
   - Read: https://m3.material.io/
   - Focus: Color system, Typography, Components

3. **Day 3-7:** Hands-on implementation
   - Build components one by one
   - Test each with preview
   - Reference Stitch design docs

4. **Optional:** Advanced topics
   - State management with ViewModel
   - Navigation with Compose
   - Custom layouts and shapes

---

## 💡 PRO TIPS

1. **Use Compose Preview liberally** - Preview is your friend, don't skip it
2. **Start with layout, then styling** - Get structure right first, colors second
3. **Test on real device early** - Emulator doesn't always match real behavior
4. **Use Design Tokens** - All spacing/colors in one place = easy maintenance
5. **Keyboard support** - Test tabbing through interactive elements
6. **Dark mode preparation** - Design system supports it, implement later
7. **Font fallbacks** - Have backup font if download fails
8. **Performance first** - Profile early, optimize later if needed

---

## 📞 QUICK REFERENCE

### Key File Locations
```
Theme System:     app/src/main/java/.../ui/theme/
Components:       app/src/main/java/.../ui/components/
Screens:          app/src/main/java/.../ui/screens/
ViewModels:       app/src/main/java/.../viewmodel/
Models:           app/src/main/java/.../model/
Resources:        app/src/main/res/
Fonts:            app/src/main/res/font/
```

### Common Imports
```kotlin
import androidx.compose.material3.*
import androidx.compose.foundation.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import androidx.compose.ui.graphics.Color
```

---

## 🎉 NEXT STEPS

1. **This Week:** Complete UI implementation
2. **Next Week:** Start Phase 2 (API & Logic)
   - Set up Retrofit for API calls
   - Implement ViewModel state management
   - Add real data handling
3. **Week 3:** Testing & Optimization
4. **Week 4:** Deployment & Launch

---

**Good luck! 🚀 You've got this! Start with the foundations, build components incrementally, test often.**

**Questions? Check the visual reference or implementation prompt documents!**
