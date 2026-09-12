# 🎨 Stitch ChatPOS - Kotlin Android Implementation Kit

**Complete UI/UX Implementation Guide untuk Conversational Retail Point-of-Sale Mobile App**

---

## 📚 DOCUMENTATION FILES

Dokumentasi ini terdiri dari 4 file utama:

### 1️⃣ **KOTLIN_STITCH_IMPLEMENTATION_PROMPT.md** (19 KB)
**Comprehensive project specification dan architecture blueprint**

Berisi:
- 📋 Project overview dan feature list
- 🎨 Complete design system specifications
  - Color palette dengan semantic roles
  - Typography system (Plus Jakarta Sans + Inter)
  - Spacing & elevation tokens
  - Shape & border radius definitions
- 🏗️ Recommended project structure (MVVM + Compose)
- 🧩 Detailed component specifications dengan layout breakdown
- 📱 Responsive design breakpoints
- 🎬 Animation & interaction specifications
- ✅ Complete implementation sequence (5 sprints)
- 🧪 Testing checklist (UI, visual regression, accessibility)

**Gunakan:** Sebagai blueprint lengkap sebelum coding dimulai

---

### 2️⃣ **KOTLIN_STITCH_VISUAL_REFERENCE.md** (21 KB)
**Visual layout guide untuk setiap screen & component**

Berisi:
- 📱 Main Chat Screen layout dengan size breakdown
- 💬 Chat bubble specifications (user vs system messages)
- 🛒 Batch transaction card visual structure
- 📦 Product selection grid layouts
- ⌨️ Input command bar detail breakdown
- 🏷️ Category pill row specifications
- 🔝 App bar component breakdown
- 📋 Bottom sheet modals specifications
- 📐 Typography baseline alignment
- ✨ Interaction states summary
- 🎬 Animation timings

**Gunakan:** Saat developing components untuk referensi visual akurat

---

### 3️⃣ **KOTLIN_STITCH_STARTER_TEMPLATES.md** (27 KB)
**Ready-to-copy template code untuk project setup**

Berisi:
- 🎨 Complete Color.kt dengan 30+ color definitions
- 🔤 Type.kt dengan 11 typography styles
- 📏 Shape.kt dan Dimensions.kt tokens
- 🎭 Theme.kt setup dengan Material 3 integration
- 🔧 ModifierExtensions.kt untuk reusable styling
- 💬 ChatBubble.kt component code
- ⌨️ InputCommandBar.kt dengan state management
- 🏷️ CategoryPill.kt component
- 📊 Model classes (ChatMessage, Transaction, TransactionBatch)
- 🧠 ViewModel skeleton code
- 🎬 Screen composition example

**Gunakan:** Copy-paste untuk mempercepat development, customize sesuai kebutuhan

---

### 4️⃣ **KOTLIN_STITCH_QUICK_START.md** (14 KB)
**Step-by-step implementation guide & checklist**

Berisi:
- 🚀 15-menit project setup guide
- 📁 Folder structure template siap pakai
- ✅ 6-phase implementation checklist (7 hari)
- 🎨 Quick color palette copy-paste
- 🏃 First component exercise (30 menit)
- 📱 Device testing instructions
- 🐛 Common issues & solutions
- 📊 Performance checklist
- 📚 Resource links (official docs, tools, samples)
- 🎯 Success criteria checklist
- 📅 Estimated timeline
- 💡 Pro tips

**Gunakan:** Day-by-day execution guide untuk smooth development

---

## 🎯 HOW TO USE THIS KIT

### Scenario 1: Starting Fresh
1. **Read:** KOTLIN_STITCH_QUICK_START.md (Bagian Project Setup)
2. **Follow:** Step-by-step setup instructions
3. **Reference:** KOTLIN_STITCH_VISUAL_REFERENCE.md saat building components
4. **Copy:** Code dari KOTLIN_STITCH_STARTER_TEMPLATES.md

### Scenario 2: Already Have Project
1. **Read:** KOTLIN_STITCH_IMPLEMENTATION_PROMPT.md (Architecture section)
2. **Organize:** Folder structure sesuai recommendations
3. **Copy:** Theme files dari STARTER_TEMPLATES.md
4. **Build:** Components one by one dengan VISUAL_REFERENCE.md

### Scenario 3: Need Detailed Reference
1. **Component specs?** → KOTLIN_STITCH_IMPLEMENTATION_PROMPT.md (Component Specifications)
2. **Layout details?** → KOTLIN_STITCH_VISUAL_REFERENCE.md (Corresponding section)
3. **Code template?** → KOTLIN_STITCH_STARTER_TEMPLATES.md
4. **Timeline?** → KOTLIN_STITCH_QUICK_START.md (Timeline section)

---

## 📋 IMPLEMENTATION CHECKLIST (7 Days)

### Day 1: Setup & Foundation (3-4 hours)
- [ ] Create Android project dengan Jetpack Compose template
- [ ] Update build.gradle.kts dengan dependencies
- [ ] Download Plus Jakarta Sans + Inter fonts
- [ ] Implement Color.kt, Type.kt, Shape.kt
- [ ] Create Theme.kt dengan Material 3
- [ ] Test theme dengan preview

### Day 2: Utilities & Core Components (4-5 hours)
- [ ] Create Dimensions.kt dan ModifierExtensions.kt
- [ ] Build UserMessageBubble component
- [ ] Build SystemMessageCard component
- [ ] Build AppBar component
- [ ] Create preview untuk setiap component

### Day 3: More Components (4-5 hours)
- [ ] Build CategoryPill dan CategoryPillRow
- [ ] Build InputCommandBar dengan state management
- [ ] Build ProductNominalCard
- [ ] Test pada berbagai ukuran teks

### Day 4: Complex Components (4-5 hours)
- [ ] Build BatchTransactionCard (view mode)
- [ ] Build BatchTransactionCard (edit mode dengan drag)
- [ ] Build ProductNominalGrid (2-column layout)
- [ ] Build BottomSheet components
- [ ] Test all interactive states

### Day 5: Screen Assembly (3-4 hours)
- [ ] Create data models (ChatMessage, Transaction)
- [ ] Create ChatPOSViewModel
- [ ] Assemble ChatScreen dengan LazyColumn
- [ ] Implement message animation
- [ ] Test scroll performance

### Day 6: Polish (3-4 hours)
- [ ] Add accessibility labels (contentDescription)
- [ ] Test landscape orientation
- [ ] Test tablet layout (responsive)
- [ ] Profile performance dengan Layout Inspector
- [ ] Fix jank atau performance issues

### Day 7: Final Testing (2-3 hours)
- [ ] Test pada real device (kalau possible)
- [ ] Verify all colors match spec
- [ ] Test dark mode preparation (optional)
- [ ] Screenshot tests untuk regression
- [ ] Create demo/walkthrough

---

## 🎨 DESIGN SYSTEM QUICK REFERENCE

### Colors (Main)
```
Primary:        #0048C8 (Electric Blue)
Secondary:      #006C49 (Success Green)
Tertiary:       #774A00 (Warning Orange)
Error:          #BA1A1A (Red)
Background:     #F8F9FF (Light Purple-ish)
Surface:        #FFFFFF (White)
```

### Typography
```
Headlines:      Plus Jakarta Sans (700 weight)
Body Text:      Inter (400 weight)
Labels:         Inter (600 weight)
Currency:       Plus Jakarta Sans (18sp, 700, tabular numerics)
```

### Spacing Grid (Base 4dp)
```
xs: 4dp    sm: 8dp    md: 12dp   lg: 16dp   xl: 24dp
Chat Gap:   10dp      Container: 16dp       Bottom Dock: 68dp
```

### Key Dimensions
```
App Bar:        56dp
Button Height:  40dp  (or 44dp for input)
Input Height:   44dp
Avatar:         36dp
Touch Target:   48dp minimum
Pill:           36dp height, 9999dp radius (full)
Card Radius:    16dp
```

---

## 🚀 QUICK START COMMANDS

### Create Project
```bash
# Using Android Studio or:
# File > New > Project > Phone and Tablet > Empty Compose Activity
```

### Download Dependencies
```bash
./gradlew build
```

### Test on Emulator
```bash
./gradlew installDebug
./gradlew runDebug
```

### Build APK
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

---

## 📂 FILE ORGANIZATION

```
stitch-chatpos-kotlin/
├── README.md (this file)
├── KOTLIN_STITCH_IMPLEMENTATION_PROMPT.md
├── KOTLIN_STITCH_VISUAL_REFERENCE.md
├── KOTLIN_STITCH_STARTER_TEMPLATES.md
├── KOTLIN_STITCH_QUICK_START.md
└── app/src/main/
    ├── java/com/example/chatpos/
    │   ├── ui/theme/
    │   ├── ui/components/
    │   ├── ui/screens/
    │   ├── viewmodel/
    │   ├── model/
    │   └── MainActivity.kt
    └── res/
        ├── font/ (Put TTF files here)
        ├── values/
        └── drawable/
```

---

## 🧪 TESTING STRATEGY

### Unit Tests
```kotlin
@Test
fun testCurrencyFormatter_1500_returnsRp1500() {
    assertEquals("Rp1.500", formatCurrency(1500))
}
```

### UI Component Tests
```kotlin
@Rule
val composeTestRule = createComposeRule()

@Test
fun testUserMessageBubble_displaysText() {
    composeTestRule.setContent {
        UserMessageBubble("Test message")
    }
    composeTestRule.onNodeWithText("Test message").assertIsDisplayed()
}
```

### Visual Regression Tests
- Use screenshot testing library (e.g., Paparazzi)
- Screenshot each component in all states
- Baseline pada commit pertama
- Compare setiap PR

---

## 🎬 ANIMATION TIMINGS

| Animation | Duration | Easing |
|-----------|----------|--------|
| Chat bubble entrance | 300ms | easeOut |
| Batch card entrance | 200ms | easeOut |
| Bottom sheet slide | 350ms | easeOut |
| Button press | 100ms | linear |
| Category selection | 150ms | easeInOut |

---

## ♿ ACCESSIBILITY REQUIREMENTS

- [ ] All interactive elements ≥ 48dp touch target
- [ ] Color contrast ≥ 4.5:1 (WCAG AA)
- [ ] Content descriptions on all images/icons
- [ ] Keyboard navigation support (Tab key)
- [ ] Minimum text size 12sp (14sp preferred)
- [ ] Semantic structure for screen readers

---

## 📊 PERFORMANCE TARGETS

- **Launch Time:** < 2 seconds
- **Frame Rate:** 60 fps (or match device refresh rate)
- **Memory:** < 100MB for app + UI state
- **Scroll Performance:** Smooth scrolling dengan 100+ messages
- **Component Render Time:** < 16ms per frame

**Profile dengan:**
- Layout Inspector (Android Studio)
- Compose State Hoisting (remember, mutableStateOf)
- LazyColumn instead of Column for long lists

---

## 🔄 PHASE 2 PREVIEW (API & Logic - Later)

Setelah UI Phase selesai, akan ada Phase 2 yang mencakup:
- API integration (Retrofit)
- Real-time messaging
- Transaction processing
- Database caching (Room)
- Error handling & retry logic
- Authentication & security

Dokumentasi Phase 2 akan disediakan setelah Phase 1 selesai.

---

## 💡 PRO TIPS

1. **Preview Often** - Gunakan Compose Preview untuk rapid iteration
2. **Test Early** - Test components pada real device sebelum terlalu jauh
3. **Design Tokens** - Centralize semua spacing, colors, fonts
4. **Avoid Recomposition** - Use `remember`, `mutableStateOf` wisely
5. **Performance First** - Profile seiring development, bukan di akhir
6. **Document Assumptions** - Comment tentang kenapa design choice tertentu dipilih
7. **Version Control** - Commit frequently dengan meaningful messages

---

## 🤝 CONTRIBUTION & CUSTOMIZATION

### Customize Theme
Edit `ui/theme/Color.kt`:
```kotlin
val PrimaryDark = Color(0xFF0048C8) // Ubah ke warna pilihan Anda
```

### Add New Component
1. Create file di `ui/components/NewComponent.kt`
2. Implement composable function
3. Add @Preview annotation
4. Test dengan preview
5. Use dari screen

### Extend Typography
Edit `ui/theme/Type.kt`:
```kotlin
val bodyExtraLarge = TextStyle(
    fontFamily = InterFont,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp,
    // ...
)
```

---

## 📖 LEARNING RESOURCES

### Jetpack Compose
- Official Docs: https://developer.android.com/jetpack/compose
- Codelabs: https://developer.android.com/codelabs
- YouTube Channel: "Android Developers"

### Material Design 3
- Design System: https://m3.material.io/
- Color Tool: https://material-io.github.io/material-color-utilities/
- Components: https://m3.material.io/components

### Kotlin
- Official Docs: https://kotlinlang.org/docs/
- Coroutines: https://kotlinlang.org/docs/coroutines-overview.html

### Android Architecture
- MVVM Pattern: https://developer.android.com/jetpack/guide
- Repository Pattern: https://developer.android.com/topic/architecture
- State Management: https://developer.android.com/jetpack/compose/state

---

## 🐛 TROUBLESHOOTING

### Fonts Not Loading
**Solution:** Verify fonts in `app/src/main/res/font/` and use correct resource names

### Text Not Visible
**Solution:** Check color contrast, ensure text color contrasts with background

### Components Jittering
**Solution:** Move state out of recomposable scope using `remember` and `mutableStateOf`

### Preview Not Showing
**Solution:** Add `@Preview` annotation, rebuild project (Build > Rebuild)

### Performance Issues
**Solution:** Use LazyColumn for lists, profile dengan Layout Inspector

---

## 📞 SUPPORT

**Questions atau issues?**
1. Check KOTLIN_STITCH_QUICK_START.md (Troubleshooting section)
2. Review KOTLIN_STITCH_IMPLEMENTATION_PROMPT.md (relevant section)
3. Check official Jetpack Compose documentation
4. Profile dengan Android Studio Layout Inspector

---

## ✨ SUCCESS CHECKLIST

**Project adalah success ketika:**

- ✅ All screens render per design spec
- ✅ App runs smoothly (60+ fps)
- ✅ Touch targets all ≥ 48dp
- ✅ Text contrast meets WCAG AA
- ✅ Works portrait & landscape
- ✅ Responsive pada phones & tablets
- ✅ No layout jank
- ✅ Fonts render correctly
- ✅ Accessibility features implemented
- ✅ Performance baseline established

---

## 📅 TIMELINE

| Phase | Duration | Deliverable |
|-------|----------|------------|
| Setup | 1 day | Project structure, theme |
| Components | 3 days | 15+ UI components |
| Screens | 1 day | Main chat screen |
| Polish | 1 day | Accessibility, performance |
| Testing | 1 day | QA, screenshot tests |
| **Total** | **7 days** | Complete UI Phase |

---

## 🎉 YOU'RE READY!

This kit contains everything you need untuk successfully implement the Stitch ChatPOS design system ke aplikasi Kotlin Android. 

**Start dengan:**
1. Read KOTLIN_STITCH_QUICK_START.md (5 menit)
2. Follow Day 1 setup instructions (3-4 jam)
3. Build first component (1 jam)
4. Continue dengan checklist harian

**Happy coding! 🚀**

---

## 📄 FILE VERSIONS

| File | Size | Last Updated |
|------|------|--------------|
| KOTLIN_STITCH_IMPLEMENTATION_PROMPT.md | 19 KB | 2024 |
| KOTLIN_STITCH_VISUAL_REFERENCE.md | 21 KB | 2024 |
| KOTLIN_STITCH_STARTER_TEMPLATES.md | 27 KB | 2024 |
| KOTLIN_STITCH_QUICK_START.md | 14 KB | 2024 |
| README.md (this file) | - | 2024 |

**Total Package:** ~81 KB of documentation + code templates

---

**Generated untuk:** Conversational Retail Point-of-Sale (ChatPOS) Mobile App  
**Target Platform:** Android (Kotlin + Jetpack Compose)  
**UI Design System:** Stitch by Design Team  
**Status:** Ready for Implementation  

**Next Steps:** Choose your starting scenario above and begin! 💪
