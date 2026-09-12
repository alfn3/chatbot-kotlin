# 🎨 Stitch ChatPOS to Kotlin Android App - UI/UX Implementation Prompt

**Project:** Conversational Retail Point-of-Sale (POS) Mobile Application  
**Framework:** Kotlin + Jetpack Compose  
**Focus Phase:** UI/UX Implementation (Functional Logic & API Integration: Phase 2)  
**Target Device:** Android Mobile (Portrait orientation primary, landscape support)

---

## 📋 PROJECT OVERVIEW

Implementasikan design system Stitch "Conversational Retail POS" menjadi aplikasi Android yang memudahkan kasir retail melakukan transaksi **pulsa, paket data, utility token (PPOB), dan layanan digital** melalui antarmuka chat yang intuitif.

### Core Features (UI-focused Phase 1)
1. **Conversational Chat Interface** - Real-time chat stream dengan message bubbles
2. **Product Selection Grid** - Nominal selection dengan visual hierarchy
3. **Batch Transaction Card** - Multi-item transaction summaries dengan edit capability
4. **Category Quick-Select** - Bottom sheet category pills (Pulsa, Data, PLN, E-Wallet, Game)
5. **Input Command Bar** - Contextual input field dengan keypad toggle & attachment
6. **Transaction Status Indicators** - Draft/Warning/Confirmed visual states
7. **Bottom Sheet Modals** - Order confirmation & batch management

---

## 🎯 DESIGN SYSTEM SPECIFICATIONS

### Color Palette (Material 3 Compatible)
```
Primary: #0048C8 (Electric Blue - actions, user messages, selection)
Primary Container: #1E60F2 (Slightly lighter blue)
Secondary: #006C49 (Success/Approved transactions)
Tertiary: #774A00 (Warning states)
Error: #BA1A1A (Destructive actions, failed states)

Surface: #F8F9FF (Light background)
Surface Container: #E5EEFF (Card backgrounds)
On-Surface: #0B1C30 (Primary text)
On-Surface-Variant: #434655 (Secondary text)
Outline: #737687 (Subtle borders)
Outline-Variant: #C3C5D8 (Very subtle dividers)
```

### Typography System
```
Headlines: Plus Jakarta Sans (700 weight)
  - Lg: 24px, 32px line-height, -2% letter-spacing
  - Md: 20px, 28px line-height, -1% letter-spacing
  - Sm: 16px, 24px line-height, 600 weight

Body: Inter (400 weight)
  - Lg: 15px, 22px line-height, -1% letter-spacing
  - Md: 14px, 20px line-height, normal
  - Sm: 12px, 16px line-height, normal

Labels: Inter (600 weight for Lg/Md, 500 for Sm)
  - Lg: 14px, 20px line-height
  - Md: 12px, 16px line-height
  - Sm: 11px, 14px line-height

Currency Display: Plus Jakarta Sans (700 weight)
  - 18px, 24px line-height, -2% letter-spacing
  - IMPORTANT: Enable tabular numerics (tnum) for alignment
```

### Spacing System
```
xs: 4dp  (gutter-xs = 0.25rem)
sm: 8dp  (gutter-sm = 0.5rem)
md: 12dp (gutter-md = 0.75rem)
lg: 16dp (gutter-lg = 1rem)
xl: 24dp (gutter-xl = 1.5rem)

Chat Gap: 10dp (gutter-sm + adjustment)
Container Padding: 16dp
Bottom Dock Height: 68dp
Chat Cards: 12-16dp border radius
Pill Buttons: Full rounded (9999px equivalent)
```

### Elevation & Shadow
```
Level 0: Flat canvas (#F8FAFC)
Level 1: Cards with 1px solid #C3C5D8 border
         Shadow: elevation(1) = 0 1px 2px rgba(15,23,42,0.04)
Level 2: Top/Bottom bars
         Shadow: elevation(2) = 0 -2px 8px rgba(15,23,42,0.05)
Level 3: Bottom sheets & alerts
         Shadow: elevation(3) = 0 -8px 24px -4px rgba(15,23,42,0.12)
```

---

## 🏗️ APP ARCHITECTURE (Jetpack Compose)

### Recommended Project Structure
```
app/
├── ui/
│   ├── theme/
│   │   ├── Color.kt (Material 3 color scheme)
│   │   ├── Type.kt (Typography definitions)
│   │   ├── Shape.kt (Corner radius definitions)
│   │   └── Theme.kt (CompositionLocal providers)
│   ├── screens/
│   │   ├── ChatScreen.kt (Main chat interface)
│   │   ├── ProductSelectionScreen.kt (Grid modal)
│   │   ├── BatchTransactionScreen.kt (Batch card editor)
│   │   └── ConfirmationScreen.kt (Bottom sheet)
│   ├── components/
│   │   ├── ChatBubble.kt (User & System messages)
│   │   ├── BatchTransactionCard.kt (Card container)
│   │   ├── ProductNominalGrid.kt (Nominal selection)
│   │   ├── CategoryPill.kt (Quick select)
│   │   ├── InputCommandBar.kt (Bottom input)
│   │   ├── StatusIndicator.kt (Draft/Warning/Confirmed)
│   │   ├── BottomSheetModals.kt (Order confirmation)
│   │   └── AppBar.kt (Top navigation)
│   └── utils/
│       ├── CurrencyFormatter.kt (Rp formatting + tabular nums)
│       └── ModifierExtensions.kt (Reusable modifiers)
├── viewmodel/
│   └── ChatPOSViewModel.kt (UI State management)
├── model/
│   ├── ChatMessage.kt (Data models)
│   ├── Product.kt
│   ├── Transaction.kt
│   └── TransactionState.kt
└── MainActivity.kt
```

---

## 🎨 COMPONENT SPECIFICATIONS

### 1. Chat Screen Layout
**Container:** Full screen LazyColumn with vertical scrolling

**Structure (Top to Bottom):**
```
[AppBar - Fixed]
  ├─ Counter name (Plus Jakarta Sans, headline-sm)
  ├─ Online status indicator (green dot + text)
  ├─ Search icon + Menu icon
  
[Chat Stream - Scrollable]
  ├─ System message cards (left-aligned, white bg, 1px border)
  ├─ User message bubbles (right-aligned, primary blue bg, white text)
  ├─ Batch transaction card (spans up to 88% width)
  ├─ Status messages with appropriate colors
  └─ Empty state (centered chat icon + instruction text)

[Quick Category Bar - Above Input]
  └─ Horizontal scrolling pill buttons (Pulsa, Data, PLN, E-Wallet, Game)

[Input Action Bar - Fixed Bottom]
  ├─ Attachment/Camera icon
  ├─ Text input field (hint: "Ketik nomor HP / ID Pelanggan...")
  ├─ Keypad toggle
  └─ Send button (Circular FAB, 40dp, primary blue)
```

### 2. Chat Message Bubbles

**User Message Bubble:**
- Alignment: Right-aligned
- Background: Primary (#0048C8)
- Text Color: White (#FFFFFF)
- Border Radius: 16dp all corners, except bottom-right = 4dp
- Padding: 12dp horizontal, 8dp vertical
- Typography: body-md
- Max Width: ~85% of screen

**System/Bot Message Card:**
- Alignment: Left-aligned
- Background: #FFFFFF
- Border: 1px solid #C3C5D8
- Border Radius: 16dp all corners
- Padding: 16dp
- Max Width: ~88% of screen
- Shadow: elevation 1
- Contains optional badge (e.g., "Saran Sistem")

**Batch Warning Card:**
- Background: #FFFBEB (warm amber tint)
- Border: 1px solid #F59E0B
- Border Radius: 16dp
- Padding: 16dp
- Icon: Warning/Alert icon (24dp)
- Typography: body-md for content
- Contains dual action buttons

### 3. Batch Transaction Card

**Layout Structure:**
```
┌─ Card Header ──────────────────┐
│ 👤 Pelanggan ▼ | #cust001      │
├─ Transaction Items ────────────┤
│ S10.08951234 | Rp11.000        │
│ Pulsa Telkomsel 10K | ●draft   │
│                                │
│ D20.08951234 | Rp22.000        │
│ Paket Data XL 20K | ●draft     │
├─ Total Row ────────────────────┤
│ Total (2 Item) | Rp33.000      │
├─ Action Buttons ───────────────┤
│ [EDIT] [PROSES]               │
├─ Status Footer ─────────────────┤
│ ● Draft | 09:41 ✎            │
└────────────────────────────────┘
```

**Styling:**
- Background: Primary Blue (#1E60F2) - full gradient or solid
- Text: White on primary
- Border Radius: 16dp
- Padding: 16dp (internal spacing)
- Status dot: 8dp, color-coded (slate=draft, amber=warning, green=confirmed)
- Edit button: Secondary (white bg, 1px border, blue text)
- Process button: Solid white text on primary
- Button height: 40dp

**Key Interaction States:**
- **Draft:** Amber dot, both EDIT/PROSES enabled
- **Warning:** Red/amber dot, warning icon, EDIT/BATALKAN buttons
- **Confirmed:** Green dot, PROSES disabled, view-only state

### 4. Product Selection Grid

**Layout Structure:**
```
┌─ Category Tabs ────────┐
│ Semua | 5K-25K | 50K+  │
├─ Nominal Cards Grid ──┐
│ ┌──────────┐ ┌──────┐ │
│ │ Pulsa 5K │ │Pulsa │ │
│ │ Rp5.500  │ │Rp10  │ │
│ │+7 hari   │ │+15   │ │
│ │Stok Ready│ │Terlr │ │
│ └──────────┘ └──────┘ │
│ ┌──────────────────────┐
│ │ Pulsa 15K            │ (Selected)
│ │ Rp15.000             │
│ │ Stok Ready           │
│ └──────────────────────┘
└────────────────────────┘
```

**Card Specifications:**
- Grid: 2 columns for mobile, 3 columns for tablet
- Card height: Auto, min 120dp
- Border radius: 12dp
- Padding: 12dp
- Border: 1px solid #E5EEFF (unselected), 2px solid #0048C8 (selected)
- Background: #FFFFFF (unselected), #EFF6FF (selected)
- Selected card: Apply elevation 2

**Typography Inside Cards:**
```
Denomination: Plus Jakarta Sans, 18px, 700, -2% letter-spacing
Price: Plus Jakarta Sans, 18px, 700, -2% letter-spacing
Validity: Inter, 12px, 400, gray text
Status: Inter, 12px, 600, green/red semantic
```

### 5. Input Command Bar (Bottom Dock)

**Layout Structure:**
```
┌─ Input Container ──────────────────────┐
│ 📎 │ Ketik nomor HP / ID...│ ⌨️│▶️   │
└────────────────────────────────────────┘
```

**Specifications:**
- Height: 68dp (including safe area)
- Background: #FFFFFF
- Shadow: elevation 2 (upward)
- Horizontal padding: 12dp
- Gap between elements: 8dp

**Elements:**
1. **Attachment Icon** (24dp, tappable)
   - Color: #434655
   - Leading padding: 12dp

2. **Input Field**
   - Height: 44dp
   - Border radius: 24dp
   - Background: #F8F9FF
   - Border: 1px solid #E5EEFF (focus: 2px solid #0048C8)
   - Padding: 12dp horizontal
   - Placeholder text: "Ketik nomor HP / ID Pelanggan..."
   - Typography: body-md
   - Cursor color: #0048C8

3. **Keypad Toggle** (24dp, tappable)
   - Color: #434655
   - Tooltip on long-press

4. **Send Button (FAB)**
   - Size: 40dp circular
   - Background: Primary (#0048C8)
   - Icon: Arrow Right (20dp, white)
   - Elevation: 3
   - Pressed: Darker blue (#003dab)

### 6. Quick Category Bar

**Layout:**
```
[ Pulsa ][ 📡 Data ][ ⚡ PLN ][ 💎 BPJS ][ 💳 E-Wallet ][ 🎮 Game ]
```

**Specifications:**
- Height: 44dp
- Horizontal scrolling
- Padding: 12dp horizontal (screen edges), 8dp between pills
- Each pill:
  - Height: 36dp
  - Border radius: 9999px (full pill)
  - Padding: 12dp horizontal, 8dp vertical
  - Border: 1px solid #C3C5D8 (unselected), 2px solid #0048C8 (selected)
  - Background: #FFFFFF (unselected), #EFF6FF (selected)
  - Icon: 16dp, 4dp gap from text
  - Typography: label-md
  - Min width: 80dp

### 7. App Bar (Top Fixed)

**Layout:**
```
┌─ App Bar ──────────────────────────────┐
│ T │ TOKO BERKAH CELL │ ● ONLINE │ ⋯   │
│   │                 │         │ │ 🔍 │
└────────────────────────────────────────┘
```

**Specifications:**
- Height: 56dp
- Background: #FFFFFF (or light surface)
- Shadow: elevation 2
- Padding: 12dp horizontal, 8dp vertical
- Elements:
  1. **Avatar** (36dp, circular)
     - Background: Primary (#0048C8)
     - Text: White, label-lg
  2. **Store Name & Status** (flex)
     - Name: headline-sm, bold
     - Status: body-sm with dot indicator
     - Dot: 8dp, green for online, gray for offline
  3. **Search Icon** (24dp, tappable)
  4. **Overflow Menu** (3-dot, 24dp)

### 8. Bottom Sheet Modals

**Order Confirmation Sheet:**
```
┌─ Drag Handle ──────────────────┐ ← 4dp height, 36dp width
├─ Header ───────────────────────┤
│ Konfirmasi Pesanan             │
├─ Content ──────────────────────┤
│ Nomor: 0895612341234          │
│ Produk: Pulsa Telkomsel 10K    │
│ Total: Rp11.000               │
├─ Action Buttons ────────────────┤
│ [BATALKAN] [KONFIRMASI]        │
└────────────────────────────────┘
```

**Specifications:**
- Top border radius: 24dp
- Padding: 16dp
- Background: #FFFFFF
- Drag handle: 4dp height, 36dp width, #C3C5D8, centered top
- Close on swipe down
- Scrim: Semi-transparent black overlay

---

## 🎭 ANIMATION & INTERACTION SPECIFICATIONS

### Transitions
- **Chat bubbles:** Fade-in + slide-up (300ms, easeOut)
- **Batch card:** Scale + fade (200ms, easeOut)
- **Bottom sheet:** Slide-up from bottom (350ms, easeOut)
- **Button pressed:** Scale 0.95, shadow reduce (100ms)

### Haptic Feedback
- Button tap: Light impact
- Send action: Medium impact
- Warning state: Strong haptic
- Bottom sheet dismiss: Light impact

### Loading States
- Chat message: Shimmer skeleton (3 lines, 100ms loop)
- Transaction processing: Circular progress indicator (16dp)
- Button loading: Spinner inside button, text hidden

---

## 📱 RESPONSIVE DESIGN BREAKPOINTS

### Portrait (Primary)
- Min Width: 320dp (Pixel 3a)
- Chat width: Full width - 32dp padding (16dp each side)
- Message max width: 85%
- 2-column product grid

### Landscape
- Chat width: 60% (left), Preview panel: 40% (right)
- Input bar: Horizontal layout
- 3-column product grid

### Tablet
- Chat width: 65%
- Sidebar: 35% (batch summary preview)
- 3-column product grid
- Larger touch targets (48dp+ buttons)

---

## 🔧 IMPLEMENTATION SEQUENCE (Phase 1)

### Sprint 1: Foundation
1. [ ] Setup theme system (Color.kt, Type.kt, Shape.kt)
2. [ ] Create Theme.kt with CompositionLocal providers
3. [ ] Build reusable modifiers (elevation, shadow, shape)
4. [ ] Create Typography composables (HeadlineLg, BodyMd, etc.)

### Sprint 2: Core Components
1. [ ] ChatBubble (user + system variants)
2. [ ] AppBar component
3. [ ] InputCommandBar
4. [ ] CategoryPill & Quick bar
5. [ ] Product nominal card

### Sprint 3: Complex Components
1. [ ] BatchTransactionCard
2. [ ] Bottom sheet modals
3. [ ] Status indicators
4. [ ] Message state animations

### Sprint 4: Screen Integration
1. [ ] ChatScreen assembly
2. [ ] Navigation structure
3. [ ] State management (ViewModel)
4. [ ] Responsive layout handling

### Sprint 5: Polish & Testing
1. [ ] Accessibility (contentDescription, semantic)
2. [ ] Dark mode support (optional)
3. [ ] Performance optimization (LazyColumn, recomposition)
4. [ ] UI screenshot tests

---

## 🧪 TESTING CHECKLIST

### UI Component Tests
- [ ] Verify chat bubble alignments & text wrapping
- [ ] Product grid responsive layout
- [ ] Batch card edit mode interactions
- [ ] Bottom sheet drag & dismiss
- [ ] Input field focus states

### Visual Regression Tests
- [ ] Screenshot tests for all screens
- [ ] Dark mode screenshots
- [ ] Various text lengths
- [ ] Numeric alignment (tabular)

### Accessibility Tests
- [ ] Touch target sizes ≥ 48dp
- [ ] Color contrast ratio ≥ 4.5:1
- [ ] Content descriptions on all interactive elements
- [ ] Keyboard navigation support

---

## 📝 DESIGN SYSTEM TOKENS (For Copy-Paste)

### Color Definitions (Kotlin)
```kotlin
val PrimaryDark = Color(0xFF0048C8)
val PrimaryLight = Color(0xFF1E60F2)
val SecondaryDark = Color(0xFF006C49)
val TertiaryDark = Color(0xFF774A00)
val ErrorDark = Color(0xFFBA1A1A)

val SurfaceLight = Color(0xFFF8F9FF)
val SurfaceContainerLight = Color(0xFFE5EEFF)
val OutlineLight = Color(0xFF737687)
val TextPrimary = Color(0xFF0B1C30)
val TextSecondary = Color(0xFF434655)
```

### Spacing Tokens (Kotlin)
```kotlin
val SpacingXs = 4.dp
val SpacingSm = 8.dp
val SpacingMd = 12.dp
val SpacingLg = 16.dp
val SpacingXl = 24.dp
val ChatGap = 10.dp
val ContainerPadding = 16.dp
```

### Shape Tokens (Kotlin)
```kotlin
val ShapesSmall = RoundedCornerShape(4.dp)
val ShapesDefault = RoundedCornerShape(8.dp)
val ShapesMedium = RoundedCornerShape(12.dp)
val ShapesLarge = RoundedCornerShape(16.dp)
val ShapesXlarge = RoundedCornerShape(24.dp)
val ShapesFull = RoundedCornerShape(9999.dp)
```

### Elevation/Shadow (Kotlin)
```kotlin
val ElevationLevel0 = 0.dp
val ElevationLevel1 = 1.dp
val ElevationLevel2 = 2.dp
val ElevationLevel3 = 3.dp
```

---

## 🚀 PHASE 2 PREVIEW (API & Logic - Deferred)

This document focuses **ONLY on UI/UX implementation**. Phase 2 will include:

- [ ] API integration layer (Retrofit/OkHttp)
- [ ] ViewModel with coroutines
- [ ] Room database for offline caching
- [ ] Real-time message handling
- [ ] Transaction processing logic
- [ ] Error handling & retry mechanisms
- [ ] WebSocket for live chat (optional)
- [ ] Authentication & session management

---

## 📚 SUPPORTING RESOURCES

### Fonts to Download
- **Plus Jakarta Sans** - Headline/display typography
  - Download: fonts.google.com (variable font)
  - Add to: `app/src/main/res/font/`
  
- **Inter** - Body/data typography
  - Download: fonts.google.com (variable font)
  - Add to: `app/src/main/res/font/`

### Jetpack Compose Docs
- Material 3: https://m3.material.io/
- Compose Layout: https://developer.android.com/jetpack/compose/layouts
- LazyColumn: https://developer.android.com/jetpack/compose/lists

### Sample Icon Set
- Use Material Icons (24dp) or download from design file
- Colors: Follow semantic color roles

---

## ✅ ACCEPTANCE CRITERIA

**Definition of Done (UI Phase):**
1. All components render pixel-perfect per Stitch specs
2. Responsive layouts work on 4.5"-6" phones & tablets
3. All states (draft/warning/confirmed) display correctly
4. Touch targets meet 48dp minimum
5. Color contrast meets WCAG AA standards
6. Typography renders with correct font sizes & weights
7. Animations are smooth (60fps) & match 300-350ms timings
8. Empty states show helpful guidance
9. No visual jank on 60Hz+ displays
10. Accessibility features implemented (contentDescription, semantics)

---

## 📞 DESIGN HANDOFF NOTES

**Color Fallbacks:** If Plus Jakarta Sans unavailable, fall back to Poppins (similar geometry).

**Tabular Numerics:** Critical for currency display alignment. Enable via:
```kotlin
PlatformTextStyle(
    includeFontPadding = false
)
// Or in TextField:
textStyle = LocalTextStyle.current.copy(
    platformStyle = PlatformTextStyle(
        includeFontPadding = false
    )
)
```

**Performance Optimization:** Use `remember { MutableInteractionSource() }` for buttons to prevent recomposition on state changes.

**State Management:** Use `mutableStateOf` for local UI state, ViewModel for screen-level state.

---

**Document Version:** 1.0  
**Last Updated:** 2024  
**Status:** Ready for Implementation  
**Next Review:** After Sprint 1 completion
