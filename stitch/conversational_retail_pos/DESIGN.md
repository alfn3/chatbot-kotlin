---
name: Conversational Retail POS
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#434655'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#737687'
  outline-variant: '#c3c5d8'
  surface-tint: '#0051df'
  primary: '#0048c8'
  on-primary: '#ffffff'
  primary-container: '#1e60f2'
  on-primary-container: '#eceeff'
  inverse-primary: '#b5c4ff'
  secondary: '#006c49'
  on-secondary: '#ffffff'
  secondary-container: '#6cf8bb'
  on-secondary-container: '#00714d'
  tertiary: '#774a00'
  on-tertiary: '#ffffff'
  tertiary-container: '#986000'
  on-tertiary-container: '#ffecd9'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b5c4ff'
  on-primary-fixed: '#00174c'
  on-primary-fixed-variant: '#003dab'
  secondary-fixed: '#6ffbbe'
  secondary-fixed-dim: '#4edea3'
  on-secondary-fixed: '#002113'
  on-secondary-fixed-variant: '#005236'
  tertiary-fixed: '#ffddb8'
  tertiary-fixed-dim: '#ffb95f'
  on-tertiary-fixed: '#2a1700'
  on-tertiary-fixed-variant: '#653e00'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
typography:
  headline-lg:
    fontFamily: plusJakartaSans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: plusJakartaSans
    fontSize: 20px
    fontWeight: '700'
    lineHeight: 28px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: plusJakartaSans
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: '0'
  body-lg:
    fontFamily: inter
    fontSize: 15px
    fontWeight: '400'
    lineHeight: 22px
    letterSpacing: -0.01em
  body-md:
    fontFamily: inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: '0'
  body-sm:
    fontFamily: inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
    letterSpacing: '0'
  label-lg:
    fontFamily: inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: '0'
  label-md:
    fontFamily: inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.01em
  label-sm:
    fontFamily: inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.02em
  currency-display:
    fontFamily: plusJakartaSans
    fontSize: 18px
    fontWeight: '700'
    lineHeight: 24px
    letterSpacing: -0.02em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter-xs: 0.25rem
  gutter-sm: 0.5rem
  gutter-md: 0.75rem
  gutter-lg: 1rem
  gutter-xl: 1.5rem
  chat-gap: 0.625rem
  container-padding: 1rem
  bottom-dock-height: 4.25rem
---

## Brand & Style

This design system powers a conversational mobile point-of-sale interface tailored for high-volume prepaid mobile reload, data package, utility token (PPOB), and digital service counters. The aesthetic merges utility-focused chat interfaces (like modern messaging tools) with structured fintech efficiency.

### Personality & Tone
- **High-Velocity & Decisive:** Built for split-second cashier inputs, automated validation, and clear feedback without visual clutter.
- **Accurate & Dependable:** High-contrast financial typography, transparent calculation readouts, and immediate color-coded alerts to eliminate operator error during peak store traffic.
- **Approachable & Ergonomic:** Balanced rounded cards, soft-edged buttons, and bottom-anchored touch targets designed for rapid, single-handed thumb interaction on mobile devices.

### Design Movement
**Modern Utility & Structured Functionalism:** Clear structural hierarchy driven by neat borders (`1px`), clean neutral slates, sharp semantic badges, and elevated conversational message bubbles. Surfaces avoid skeuomorphism, relying instead on clean white message cards floating over subtle slate backdrops with vibrant electric blue focal points.

## Colors

The palette leverages an electric blue foundation paired with an unambiguous semantic color taxonomy tailored to financial status feedback.

### Key Roles & Semantics
- **Primary (`#1E60F2` / `#2563EB`):** Applied to primary actions, selected operator states, user message bubbles, active input strokes, and transactional summary highlights.
- **Secondary / Success (`#10B981`):** Indicates successful API submissions, completed transactions, valid phone numbers, online status, and positive balances.
- **Tertiary / Warning (`#F59E0B` / `#D97706`):** Highlights duplicate destination warnings, pending batch states, queue notices, and high-frequency repeated numbers.
- **Destructive / Error (`#EF4444`):** Used for failed transaction notifications, batch item removal triggers, invalid meter IDs, and cancellation flows.
- **Processing / System (`#6366F1`):** Represents background queue processing and live API syncs.
- **Neutrals & Surfaces:**
  - Background Canvas: `#F8FAFC` (Slate 50)
  - Surface Containers & Cards: `#FFFFFF`
  - Subtle Borders & Dividers: `#E2E8F0` (Slate 200)
  - Text Primary: `#0F172A` (Slate 900)
  - Text Secondary & Placeholders: `#64748B` (Slate 500)
  - Text Muted: `#94A3B8` (Slate 400)

## Typography

Typography prioritizes tabular legibility and rapid skimming under varying store lighting conditions. 

- **Display & Headings:** Plus Jakarta Sans delivers friendly geometry with robust presence in modal headings, store branding banners, and total transaction amounts.
- **Body & Data:** Inter provides neutral clarity for customer names, product SKUs, phone numbers, and operational notes.
- **Tabular Numerics:** Phone numbers (`0895...`), product codes (`S10`, `D20`), and currency figures (`Rp33.000`) must strictly render with `font-feature-settings: 'tnum'` to preserve vertical column alignment inside batch summaries.

## Layout & Spacing

The layout is optimized for vertical mobile chat streams with docked thumb zones.

### Screen Composition
- **App Bar (Top):** Fixed `56px` height containing the counter identity, connection status dot, search, and overflow options.
- **Conversational Stream:** Vertically scrolling viewport with `16px` horizontal padding and a tight `10px` (`0.625rem`) vertical spacing cadence between sequential chat turns.
- **Interactive Quick-Bar:** Floating pill row directly above the persistent bottom input for fast category switching (`Pulsa`, `Paket Data`, `PLN`, `E-Wallet`).
- **Input Action Bar (Bottom Dock):** Anchored `68px` container holding contextual utility shortcuts (attachment/camera icon, keypad toggle) alongside the primary command line and circular dispatch button.
- **Bottom Sheets:** Swipeable modal sheets for order confirmation and batch reordering with `16px` margins and bottom safe-area compliance.

## Elevation & Depth

This system avoids heavy shadows, favoring clean structural delineation through micro-borders and soft ambient diffusion:

- **Level 0 (Base Canvas):** Background canvas rendered in `#F8FAFC` flat.
- **Level 1 (Chat Cards & Interactive Grids):** Pure white `#FFFFFF` surface bounded by a subtle `1px solid #E2E8F0` outline. Shadow: `0 1px 2px 0 rgba(15, 23, 42, 0.04)`.
- **Level 2 (Persistent Bars & Popovers):** Top navigation and bottom input dock. Shadow: `0 -2px 8px 0 rgba(15, 23, 42, 0.05)` with `#FFFFFF` background.
- **Level 3 (Confirmation Sheets & Alerts):** Elevated bottom sheets and batch warnings. Shadow: `0 -8px 24px -4px rgba(15, 23, 42, 0.12)`, utilizing an orange-tinted soft glow (`rgba(245, 158, 11, 0.08)`) when applied to duplicate transaction warning cards.

## Shapes

The interface balances friendly customer-facing chat bubbles with professional data containers:

- **Interactive Chips & Category Pills:** Fully rounded pill shapes (`9999px`) for operators and service types.
- **Message Cards & Grids:** Standardized at `12px` to `16px` border radius (`rounded-lg` to `rounded-xl`).
- **Nominal Price Selectors:** Crisp `8px` rounded rectangles for quick touch-targets displaying denominations (`5K`, `10K`, `20K`).
- **Bottom Sheets & Floating Drawers:** `24px` top border radius (`rounded-t-2xl`) with an upper drag indicator (`4px` height by `36px` width, `9999px` radius).

## Components

### Chat Message Bubbles & Cards
- **Cashier/User Message:** Right-aligned electric blue bubble (`#1E60F2`), white text, rounded corners (`16px`) with a reduced bottom-right radius (`4px`).
- **System Bot / Form Card:** Left-aligned white cards with `1px solid #E2E8F0` border, `16px` corner radius, spanning up to 88% of screen width. Contains header badge, itemized transaction list, and action buttons.
- **Batch Warning Card:** Light amber background tint (`#FFFBEB`), warm amber border (`#F59E0B`), displaying warning icon, notification copy, and dual action buttons ("Batalkan" ghost button and "Tetap Proses" solid amber button).

### Action Buttons & Controls
- **Primary Transaction Action:** Solid electric blue button (`#1E60F2`), `10px` radius, `40px` to `48px` touch height, medium weight white text. Hover/active state deepens to `#1D4ED8`.
- **Secondary / Ghost Action:** White or transparent surface with `1px solid #CBD5E1` border and `#0F172A` label text.
- **Quick Selection Grids:** 2x3 or 3x3 nominal cards (e.g., `5K`, `10K`, `15K`) displaying large bold denomination labels on top and small purchase cost labels beneath. Selected item gains a high-contrast `#1E60F2` border and light blue background tint (`#EFF6FF`).

### Transaction Batch Lists
- Line items feature a clean two-row structure: top row shows product SKU + destination phone number in bold text, bottom row indicates product descriptor and customer tag. 
- Right side displays high-contrast bold price readouts with a status indicator dot (slate for draft, amber for warning, emerald for confirmed).
- In edit mode, items display a left-hand grab handle for reordering and a red removal cross (`#EF4444`) on the right.

### Input Command Field
- Single-line pill or rounded input container (`44px` height, `24px` radius) with search/paperclip actions on the left and a quick keypad trigger on the right.
- Send trigger is a circular `40px` electric blue FAB holding a rightward directional arrow icon.