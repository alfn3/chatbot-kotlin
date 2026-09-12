# 🎨 Stitch ChatPOS - Visual Reference & Layout Guide

Dokumentasi lengkap tentang setiap screen dan component dengan spesifikasi visual detail.

---

## 📱 MAIN CHAT SCREEN

### Screen Overview
```
┌─────────────────────────────────────┐
│ T │ TOKO BERKAH CELL    │ ● ONLINE │ ⋯  ← App Bar (56dp)
├─────────────────────────────────────┤
│                                     │
│        Saldo: Rp1.450.000          │  ← System info card
│                                     │
│  ┌───────────────────────────────┐  │
│  │ Mulai transaksi baru          │  │ ← Empty state message
│  │ Ketik nomor atau pilih produk │  │
│  └───────────────────────────────┘  │
│                                     │
│              💬                     │ ← Chat icon (centered)
│                                     │
│  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │ ← Scrollable area
│                                     │
├─────────────────────────────────────┤
│ [Pulsa] [📡 Data] [⚡ PLN] [💎 BPJ│ ← Category pills (scrollable)
├─────────────────────────────────────┤
│ 📎 │ Ketik nomor HP...  │ ⌨️│ ➤    │  ← Input Bar (68dp total)
└─────────────────────────────────────┘
```

### Detailed Component Sizes
- **App Bar Height:** 56dp
- **Category Pill Area Height:** 52dp (44dp pills + 8dp padding)
- **Input Bar Height:** 68dp (includes safe area)
- **Scrollable Chat Area:** Full screen - 56dp - 52dp - 68dp

### Color Scheme
```
Background: #F8F9FF (surface)
App Bar: #FFFFFF (surface-container-lowest)
Category Pills: White with border (#E5EEFF)
Input Bar: #FFFFFF (surface-container-lowest)
```

---

## 💬 CHAT MESSAGE BUBBLES

### User Message Bubble (Right-aligned)
```
                    ┌─────────────────────────┐
                    │ Pulsa 5K untuk 0895...  │ ← Bubble content
                    └─────────────────────────┘
                      ↑ Tight corner radius    ↑ Loose radius
```

**Specifications:**
- Alignment: Right (85% max width)
- Background: Primary Blue (#0048C8)
- Text: White, body-md
- Border Radius: 16dp (all), except bottom-right = 4dp
- Padding: 12dp horizontal, 8dp vertical
- Margin Bottom: 10dp (chat-gap)

**Border Radius Details:**
```
topStart:    16dp ┐
topEnd:      16dp ├─ All corners
bottomStart: 16dp │
bottomEnd:    4dp ┘
```

### System Message Card (Left-aligned)
```
┌─────────────────────────────────────────────┐
│ 🔷 Saran Sistem  ← Badge (label-md, blue)   │
├─────────────────────────────────────────────┤
│ Nomor ini sudah pernah digunakan hari ini   │
│ Duplikat transaksi? Hubungi supervisior     │
│                                             │
│ [BATALKAN] [TETAP PROSES]                  │
└─────────────────────────────────────────────┘
  ↑ Left margin (8dp from edge)
```

**Specifications:**
- Alignment: Left (88% max width)
- Background: White (#FFFFFF)
- Border: 1px solid #C3C5D8
- Border Radius: 16dp all
- Padding: 16dp
- Shadow: elevation 1
- Margin Bottom: 10dp

### Warning/Alert Card
```
┌─────────────────────────────────────────────┐  ← #FFFBEB bg, #F59E0B border
│ ⚠️  Duplikat Nomor Terdeteksi                │
│ Nomor 0895... sudah diproses 2x hari ini    │
│                                             │
│ [BATALKAN] [TETAP PROSES]                  │
└─────────────────────────────────────────────┘
```

**Specifications:**
- Background: #FFFBEB (warm amber tint)
- Border: 2px solid #F59E0B
- Border Radius: 16dp
- Padding: 16dp
- Icon size: 24dp (left side)
- Spacing: 12dp between icon and text

---

## 🛒 BATCH TRANSACTION CARD

### Full Card Layout
```
╔═══════════════════════════════════════════════════════════════╗
║                    BATCH TRANSACTION CARD                     ║ ← Primary blue bg
╠═══════════════════════════════════════════════════════════════╣
║ 👤 Pelanggan  ▼  │  #cust001                                  ║
╠═══════════════════════════════════════════════════════════════╣
║ S10.08951234 | Pulsa Telkomsel 10K               │   Rp11.000 ║
║ (2 row item layout)                              │   ● draft  ║
├─────────────────────────────────────────────────────────────┤
║ D20.08951001 | Paket Data XL 20K                 │   Rp22.000 ║
║ (2 row item layout)                              │   ● draft  ║
╠═══════════════════════════════════════════════════════════════╣
║                Total (2 Item)         │         Rp33.000      ║
╠═══════════════════════════════════════════════════════════════╣
║  [EDIT]                          [PROSES]                     ║
╠═══════════════════════════════════════════════════════════════╣
║ ● Draft              │         09:41  ✎                       ║
╚═══════════════════════════════════════════════════════════════╝
```

### Item Row Structure
```
LEFT COLUMN (70%)          RIGHT COLUMN (30%)
┌──────────────────┐      ┌──────────────┐
│ SKU + Destination│      │    AMOUNT    │
│ S10.08951234     │      │  Rp11.000    │
│ (product name)   │      │  ● status    │
│ Pulsa Telkomsel  │      │              │
└──────────────────┘      └──────────────┘
```

### Card Colors & Styling
- **Background:** Primary blue (#1E60F2) gradient or solid
- **Text Color:** White on all content
- **Border Radius:** 16dp all corners
- **Padding:** 16dp internal spacing
- **Elevation:** 2
- **Margin:** 8dp bottom (chat-gap variation)

### Typography Inside Card
```
Header "Pelanggan": label-md (600), white
SKU (S10...):       label-lg (600), white, tabular numerics CRITICAL
Product Name:       body-sm (400), white, 70% opacity
Amount:             currency-display (18px, 700), white, tabular numerics
Status Badge:       label-sm (500), white
Total Row:          body-md + currency-display mix
Button Text:        label-lg (600), white or blue
```

### Status Indicator Dots
```
Draft:      ● Neutral gray (#737687) at 8dp
Warning:    ● Amber (#F59E0B) with warning icon
Confirmed:  ● Green (#10B981) with checkmark
Processing: ● Blue (#6366F1) with spinner
```

### Edit Mode Styling
When card is in edit mode:
```
DRAG HANDLE                          DELETE BUTTON
    ↓                                    ↓
┌─ ═ ─┬─────────────────────────────┬─ ✕ ─┐
│ S10.08951234 | Pulsa 10K │ Rp11.000│
└─────────────────────────────────────────┘
  ↑ Reorderable item with visual feedback
```

- **Drag Handle:** Left side, 24dp × 16dp rectangle, rounded, semi-transparent white
- **Delete Button:** Right side, 24dp × 24dp circle, red/error color
- **Item height:** 56dp (increased from view-only)

---

## 📦 PRODUCT SELECTION GRID

### Category Tabs
```
┌──────────────────────────────────────────┐
│ Semua │ 5K - 25K │ 50K - 100K │ 100K+   │
└──────────────────────────────────────────┘
  Active tab: Blue background & underline
```

### 2-Column Grid (Mobile)
```
┌─────────────────┐  ┌─────────────────┐
│ Pulsa 5K        │  │ Pulsa 10K       │
│ Rp5.500         │  │ Rp10.000        │
│ +7 hari aktif   │  │ +15 hari aktif  │
│ Stok Ready      │  │ Stok Ready      │
└─────────────────┘  └─────────────────┘ ← Unselected: white bg, gray border

┌─────────────────┐
│ Pulsa 15K       │ ← Selected: light blue bg, 2px blue border
│ Rp15.000        │
│ Stok Ready      │
└─────────────────┘
```

### Card Specifications
- **Grid Columns:** 2 (mobile), 3 (tablet)
- **Card Min Height:** 120dp
- **Card Width:** Equal division minus gutter
- **Border Radius:** 12dp
- **Padding:** 12dp internal
- **Border:** 1px solid #E5EEFF (unselected), 2px solid #0048C8 (selected)
- **Gap between cards:** 12dp
- **Container padding:** 16dp (horizontal)

### Typography in Cards
```
Denomination:  Plus Jakarta Sans 18px 700 -2%
Price:         Plus Jakarta Sans 18px 700 -2%
Validity:      Inter 12px 400, gray (#434655)
Status:        Inter 12px 600, green (#10B981)
```

### Selected State Visual
- Light blue background: #EFF6FF
- Thick blue border: 2px solid #0048C8
- Shadow elevation increased to 2
- Slight scale animation (scale 1.02) on selection

---

## ⌨️ INPUT COMMAND BAR

### Layout Breakdown
```
WIDTH: 100%
HEIGHT: 68dp (56dp content + 12dp safe area)

┌─────────────────────────────────────────────────────┐
│ 12dp │ 📎 │ 8dp │ [Input Field] │ 8dp │ ⌨️ │ 8dp │ ➤ │ 12dp │
└─────────────────────────────────────────────────────┘
  pad   icon  gap       flex(1)       gap  icon  gap  fab   pad
```

### Component Dimensions
```
Attachment Icon:  24dp × 24dp, centered
Input Field:      height 44dp, border-radius 24dp, flex(1)
Keypad Toggle:    24dp × 24dp, centered
Send FAB:         40dp × 40dp circular, centered vertically
Padding:          12dp left & right (container level)
Gap between:      8dp each
```

### Input Field Detail
```
┌──────────────────────────────────────┐
│ Ketik nomor HP / ID Pelanggan...     │ ← Placeholder text
└──────────────────────────────────────┘

Background: #F8F9FF (surface-container-low)
Border: 1px solid #E5EEFF (default)
        2px solid #0048C8 (focused)
Border Radius: 24dp
Padding: 12dp horizontal
Cursor: Primary blue (#0048C8)
Placeholder Color: #434655 (on-surface-variant)
Text Color: #0B1C30 (on-surface)
```

### Send FAB
```
     ┌─────┐
     │  ➤  │ ← White arrow icon (20dp)
     └─────┘ ← Blue circle (40dp)

Background: Primary (#0048C8)
Pressed: Darker blue (#003dab)
Icon: White, 20dp
Shadow: elevation 3
Ripple: Light ripple on press
```

### Focus States
```
Normal State:
┌──────────────────────────────────────┐
│ Placeholder text                     │
└──────────────────────────────────────┘

Focused State:
┌══════════════════════════════════════┐ ← Thicker border
│ Placeholder text                     │
└══════════════════════════════════════┘

With Text:
┌──────────────────────────────────────┐
│ 0895612341234                        │
└──────────────────────────────────────┘
```

---

## 🏷️ CATEGORY PILL ROW

### Layout
```
horizontal scrollable container
├─ [Pulsa icon text] ← Selected (blue bg)
├─ [📡 Data text]
├─ [⚡ PLN text]
├─ [💎 BPJS text]
├─ [💳 E-Wallet text]
└─ [🎮 Game text]

scroll →
```

### Pill Specifications
- **Height:** 36dp content + 8dp padding (vertical)
- **Min Width:** 80dp
- **Padding:** 12dp horizontal, 8dp vertical
- **Border Radius:** 9999px (full pill)
- **Icon Size:** 16dp
- **Icon-Text Gap:** 4dp
- **Pills Gap:** 8dp between each pill

### Color States
```
Unselected Pill:
├─ Background: White (#FFFFFF)
├─ Border: 1px solid #C3C5D8
└─ Text: Gray (#434655)

Selected Pill:
├─ Background: Light blue (#EFF6FF)
├─ Border: 2px solid #0048C8
└─ Text: Primary blue (#0048C8)
```

### Container Padding
- Horizontal: 12dp (left and right screen edge)
- Vertical: 8dp (top and bottom)
- Container Height: 52dp total

---

## 🔝 APP BAR

### Layout
```
┌─────────────────────────────────────────────┐
│ 12dp │ 36dp │ 8dp │ Title/Status │ 8dp │ 🔍 │ 8dp │ ⋯ │ 12dp │
└─────────────────────────────────────────────┘
       avatar        flex(1)              icons
```

### Component Details
```
Avatar:
├─ Size: 36dp circular
├─ Background: Primary (#0048C8)
├─ Icon/Text: White, centered

Title & Status:
├─ Store Name: headline-sm, 16px 600, bold
├─ Status Line: body-sm, 12px 400
├─ Status Dot: 8dp circle (green online, gray offline)

Search Icon:
├─ Size: 24dp
├─ Color: #434655 (on-surface-variant)

Overflow Menu:
├─ Size: 24dp (3 dots)
├─ Color: #434655 (on-surface-variant)
```

### Container
- **Height:** 56dp
- **Padding:** 12dp horizontal, 8dp vertical
- **Background:** White (#FFFFFF)
- **Shadow:** elevation 2 (downward)
- **Position:** Fixed top, sticky scroll

---

## 📋 BOTTOM SHEET MODALS

### Order Confirmation Sheet
```
╔═══════════════════════════════════════════════════╗
║           ─────────  Drag Handle  ─────────      ║ ← 4dp × 36dp, rounded
╠═══════════════════════════════════════════════════╣
║ Konfirmasi Pesanan                              ║ ← headline-md
╠═══════════════════════════════════════════════════╣
║ Nomor Tujuan:                                   ║
║ 0895612341234                                    ║
║                                                  ║
║ Produk:                                         ║
║ Pulsa Telkomsel 10K                             ║
║                                                  ║
║ Total:                                          ║
║ Rp11.000                                        ║
╠═══════════════════════════════════════════════════╣
║ [BATALKAN] [KONFIRMASI]                         ║
╚═══════════════════════════════════════════════════╝
```

### Sheet Specifications
- **Top Border Radius:** 24dp (rounded-t-2xl)
- **Padding:** 16dp
- **Max Height:** 80% screen
- **Drag Handle:** 4dp height, 36dp width, #C3C5D8
- **Background:** White (#FFFFFF)
- **Scrim:** Semi-transparent black overlay (α = 0.32)

### Button Layout Inside Sheet
```
┌──────────────┐        ┌──────────────┐
│  BATALKAN    │ (8dp)  │ KONFIRMASI   │
└──────────────┘        └──────────────┘

├─ Ghost button (white bg, border)  ├─ Solid button (blue bg)
├─ Each takes ~45% width            └─ Each takes ~45% width
└─ 8dp gap between
```

---

## 🎯 RESPONSIVE BREAKPOINTS

### Mobile (Portrait) - 320dp to 600dp
```
Chat Message: 85% width
System Card: 88% width
Product Grid: 2 columns
Input Bar: Single line
Tab: Scrollable if many
```

### Mobile (Landscape) - 600dp to 900dp
```
Chat: 60% width (left)
Preview: 40% width (right)
Product Grid: 3 columns
Input Bar: Compact layout
```

### Tablet (Portrait) - 900dp+
```
Chat: 65% width (left sidebar + main)
Sidebar: 35% width (transaction list)
Product Grid: 3 columns
Larger touch targets (48dp+)
Input Bar: Enhanced layout
```

---

## 🔧 SPACING & ALIGNMENT GRID

All designs follow 4dp baseline grid:

```
4dp   = 1 unit  (xs spacing)
8dp   = 2 units (sm spacing)
12dp  = 3 units (md spacing)
16dp  = 4 units (lg spacing)
24dp  = 6 units (xl spacing)
32dp  = 8 units (xxl spacing)
36dp  = 9 units (avatar/icon)
40dp  = 10 units (button height)
44dp  = 11 units (input height)
48dp  = 12 units (touch target)
56dp  = 14 units (app bar)
68dp  = 17 units (input dock)
```

### Grid Application
- Padding: Always multiple of 4dp
- Margin: Always multiple of 4dp
- Gap: Always multiple of 4dp
- Height: Always multiple of 4dp
- Width: Prefer multiples of 4dp

---

## 📐 TEXT BASELINE ALIGNMENT

Critical for chat message alignment:

```
User Bubble Line Height: 22px
├─ Text size: 14px
├─ Ascender: ~4px above
└─ Descender: ~4px below

Padding vertical: 8dp
├─ Top: padding (8dp) + ascender (4dp) = 12dp
└─ Bottom: padding (8dp) + descender (4px) ≈ 8dp

Net result: Vertically centered
```

---

## ✨ INTERACTION STATES SUMMARY

| Component | Normal | Hover | Pressed | Disabled | Selected |
|-----------|--------|-------|---------|----------|----------|
| Button | solid | lighter | darker | gray | N/A |
| Pill | white border | underline | scale down | gray | blue bg |
| Card | shadow 1 | shadow 2 | no shadow | gray | shadow 2 |
| Input | gray border | blue border | blue border | gray bg | N/A |

---

## 🎬 ANIMATION TIMINGS

```
Chat Bubble Entrance:  300ms (fade-in + slide-up)
Batch Card Entrance:   200ms (scale + fade)
Bottom Sheet Slide:    350ms (from bottom)
Button Press:          100ms (scale 0.95)
Category Selection:    150ms (border & bg change)
Input Focus:           200ms (border change)
```

---

**This visual reference should be used alongside component development.**  
**All dimensions and colors are finalized and ready for implementation.**
