# Rencana Pengembangan ChatPOS

## Fokus Saat Ini: Fase 1 — UI/UX dan Alur Transaksi

**Platform:** Android  
**Teknologi:** Kotlin, Jetpack Compose, Material 3  
**Package:** `com.example.chatpos`  
**Status:** UI utama dan alur demo transaksi sudah terpasang; integrasi layanan eksternal belum menjadi bagian Fase 1.

Dokumen ini menjadi acuan kerja aktif untuk Fase 1. Isinya hanya mencatat kondisi aplikasi saat ini, perilaku UI yang harus dipertahankan, dan pekerjaan yang masih diperlukan agar prototipe UI/UX siap ditutup. Detail API OtomaX, database permanen, keamanan PIN, printer, dan deployment ditunda ke fase berikutnya.

---

## 1. Tujuan Fase 1

Menyelesaikan pengalaman kasir dari saat membuka ChatPOS sampai melihat hasil transaksi dalam satu alur yang mudah dipahami:

1. Kasir membuka halaman ChatPOS dan melihat identitas toko serta saldo.
2. Kasir memilih kategori/produk atau mengetik perintah transaksi.
3. Sistem membantu mengisi kode produk, nomor tujuan, kontak, dan format nomor.
4. Kasir mengirim input untuk membuat **draft transaksi**, bukan langsung memprosesnya.
5. Kasir dapat memeriksa, mengedit, menyalin, membatalkan, atau memproses draft.
6. Sistem menampilkan warning duplikat dan konfirmasi PIN sebelum proses.
7. Sistem menampilkan satu balasan hasil untuk satu batch transaksi.
8. Kasir dapat membuka pratinjau nota, memilih format pembagian nota WhatsApp, dan melihat riwayat.

**Batasan penting:** transaksi pada Fase 1 masih berjalan dengan data produk dan hasil OtomaX simulasi di memori. Tombol dan modal harus terasa nyata, tetapi belum mengirim transaksi ke server produksi.

---

## 2. Struktur UI yang Sudah Terpasang

### 2.1 Entry point dan shell aplikasi

- `MainActivity.kt` memasang `ChatPOSTheme`, `Surface`, dan satu `ChatPOSViewModel`.
- `AppShell.kt` menyediakan navigasi tab:
  - **Chat** untuk daftar percakapan dan membuka transaksi baru.
  - **Riwayat** untuk daftar transaksi yang sudah berhasil.
- Halaman detail chat menyembunyikan bottom navigation agar area transaksi lebih fokus.

### 2.2 Design system

Token UI berada di `ui/theme/`:

- `Color.kt` — warna primary biru, success hijau, warning amber, error, background, dan divider.
- `Type.kt` — hierarki tipografi untuk heading, body, label, dan angka transaksi.
- `Shape.kt` — radius kartu, bubble, tombol, dan pill.
- `Dimensions.kt` — spacing, ukuran app bar, input, dan touch target.
- `Theme.kt` — Material 3 light theme.
- `ModifierExtensions.kt` dan `CurrencyFormatter.kt` — gaya komponen dan format Rupiah.

Gaya visual utama yang harus dipertahankan:

- Bubble kasir rata kanan berwarna biru.
- Balasan sistem rata kiri dengan kartu putih.
- Kartu transaksi memakai radius lembut, garis pemisah, status, dan angka monospaced/tabular.
- Aksi utama menggunakan biru; warning menggunakan amber; error menggunakan merah.
- Tombol interaktif mempertahankan area sentuh yang nyaman untuk penggunaan kasir.

---

## 3. Fitur Fase 1 yang Sudah Berjalan

### 3.1 Halaman chat dan state awal

- App bar menampilkan `TOKO BERKAH CELL`.
- Status koneksi dan saldo demo ditampilkan pada area transaksi.
- Pesan awal terdiri dari divider **HARI INI** dan welcome card.
- Welcome card otomatis disembunyikan setelah transaksi pertama dibuat.
- Daftar chat menggunakan `LazyColumn` dan otomatis scroll ke pesan terbaru.
- Waktu pesan ditampilkan dalam format jam-menit pada alur chat.

### 3.2 Input perintah transaksi

Implementasi saat ini mendukung:

- Format produk umum: `[kode].[nomor]`, misalnya `10.089512345678`.
- Input beberapa baris untuk membuat satu batch.
- Tombol **Tambah Transaksi Lain** untuk menambah baris berikutnya.
- Cursor otomatis dipindahkan ke posisi akhir setelah perubahan dari assistant.
- Nomor tujuan yang diketik sebagai angka dapat diformat per kelompok digit.
- Toggle kelompok digit tujuan 3 atau 4 digit.
- Validasi dasar kode, nomor tujuan, rekening, nominal, dan PIN yang salah tempat.
- Hint input berubah sesuai konteks Pulsa, PLN, E-Wallet, cek rekening, dan transfer bank.
- Input transfer bank:
  - `cek[bank].[norek]` atau format cek rekening terkait.
  - `tbank.[bank5/10].[norek].[nominal]`.
- PIN tidak boleh diketik di akhir perintah; PIN diminta melalui modal terpisah.

### 3.3 Assistant produk dan kontak

- Kategori produk tersedia melalui assistant input.
- Kategori aktif mencakup Pulsa, Transfer, dan kategori yang berasal dari katalog produk demo.
- Subkategori produk dapat ditampilkan berdasarkan kelompok masa berlaku.
- Saran produk berubah berdasarkan awalan kode, nomor tujuan, operator, kategori, dan subkategori.
- Smart contact suggestion mencari kontak tersimpan berdasarkan nama atau nomor.
- Klik produk mengisi kode dan titik secara otomatis.
- Klik kontak melengkapi nomor tujuan pada baris aktif.
- Untuk nomor seluler yang sudah lengkap, produk Pulsa yang relevan dapat disarankan.
- Ikon lampiran dan keypad tidak digunakan pada input bar saat ini.

### 3.4 Draft transaksi dan batch

Saat kasir menekan kirim:

- Input tidak langsung diproses.
- Sistem membuat `UserTransactionCardMessage` sebagai kartu draft di chat.
- Kartu menampilkan `#cust001`, waktu, daftar item, produk, nomor tujuan, biaya admin bila ada, dan total.
- Satu kiriman beberapa baris diringkas menjadi satu batch.
- Tombol **Edit** mengembalikan perintah ke input agar dapat diperbaiki.
- Tombol **Proses Sekarang** melanjutkan draft ke tahap warning/PIN.
- Tombol salin tersedia pada kartu yang sudah diproses.

Komponen terkait:

- `ChatBubble.kt`
- `BatchTransactionCard.kt`
- `TransactionItem` dan `TransactionBatch`
- `ChatMessage.UserTransactionCardMessage`

### 3.5 Warning duplikat dan PIN

- Nomor tujuan yang sama dalam rentang 15 menit ditandai sebagai transaksi berulang.
- Kartu draft menampilkan informasi berapa menit sejak transaksi sebelumnya.
- Kasir dapat memilih **Batalkan** atau **Tetap Proses**.
- Konfirmasi PIN menggunakan bottom sheet dengan keypad 4 digit.
- Draft ditandai sedang dikirim saat proses simulasi berlangsung.

### 3.6 Balasan hasil transaksi

- Semua item dalam satu pengiriman menghasilkan satu kartu balasan sistem.
- Kartu hasil menampilkan status sukses, detail item, SN, Ref ID, nominal, biaya admin, dan sisa saldo demo.
- Balasan sistem memakai label **BALASAN SERVER** dan rata kiri.
- Aksi nota tersedia dari kartu hasil:
  - Buka pratinjau cetak.
  - Buka dialog berbagi WhatsApp.

### 3.7 Nota, WhatsApp, dan riwayat

- `ReceiptPrintPreviewModal.kt` menampilkan pratinjau nota thermal 58 mm.
- `WhatsAppShareModal.kt` menyediakan pilihan nota gabungan atau nota terpisah per item.
- Pengiriman WhatsApp memakai intent URL; keberhasilan tetap bergantung pada aplikasi WhatsApp di perangkat.
- `ChatHistoryScreen.kt` menampilkan transaksi berdasarkan kontak/nomor dan detail nota.
- `TransactionHistoryScreen.kt` menyediakan filter berdasarkan nomor, kontak, atau produk.

---

## 4. Batasan Implementasi Saat Ini

Bagian berikut **belum merupakan integrasi produksi** dan harus tetap dianggap simulasi selama Fase 1:

- Tidak ada request HTTP/API ke OtomaX.
- Hasil sukses, SN, Ref ID, timestamp, dan pengurangan saldo dibuat lokal oleh `ChatPOSViewModel`.
- Katalog produk, kontak, saldo, status online, dan transaksi hanya berada di memory.
- Data hilang saat aplikasi dibuat ulang atau proses aplikasi dihentikan.
- PIN belum disimpan atau dilindungi dengan Android Keystore.
- Tombol cetak masih menampilkan notifikasi; belum berkomunikasi dengan printer Bluetooth.
- Warning duplikat menggunakan map transaksi lokal, bukan histori permanen.
- Belum ada state error/pending dari server nyata.

Batasan ini tidak perlu diselesaikan untuk menutup Fase 1, tetapi harus diberi label jelas sebagai **demo/simulasi** pada dokumentasi dan pengujian UI.

---

## 5. Pekerjaan Tersisa untuk Menutup Fase 1

Prioritas pekerjaan berikut berdasarkan dampaknya terhadap pengalaman UI, bukan integrasi backend:

### Prioritas tinggi

- Rapikan konsistensi tampilan draft, hasil sukses, warning, dan modal agar seluruhnya mengikuti token pada `ui/theme/`.
- Pastikan seluruh aksi penting memiliki label dan content description yang jelas.
- Uji alur utama pada kondisi:
  - input satu transaksi;
  - input batch beberapa baris;
  - edit draft;
  - nomor duplikat;
  - PIN benar dan PIN tidak lengkap;
  - hasil tanpa item kosong;
  - keyboard terbuka dan layar kecil.
- Pastikan state welcome card, draft, hasil, dan riwayat tidak menampilkan data yang saling bertentangan.
- Beri feedback yang konsisten untuk loading, invalid input, batal, salin, dan kembali.

### Prioritas menengah

- Periksa ulang spacing, typography, warna status, dan ukuran tombol terhadap referensi di folder `stitch/`.
- Tambahkan preview Compose untuk komponen utama dan state penting.
- Pastikan filter riwayat menampilkan empty state yang informatif.
- Pastikan dialog WhatsApp menangani daftar item tunggal dan batch tanpa teks terpotong.
- Pastikan pratinjau nota tetap terbaca pada perangkat dengan ukuran layar berbeda.

### Tidak dikerjakan pada Fase 1

- Retrofit/HTTP client dan protokol OtomaX.
- Room, DataStore, atau sinkronisasi data permanen.
- Keystore, enkripsi PIN, root detection, dan hardening perangkat.
- Driver printer ESC/POS Bluetooth.
- Retry, polling pending, idempotency, dan rekonsiliasi transaksi.
- Release hardening, ProGuard/R8, serta deployment produksi.

---

## 6. Definition of Done Fase 1

Fase 1 dapat dianggap selesai apabila:

- Aplikasi dapat dibuka dan menampilkan shell ChatPOS tanpa error.
- Kasir dapat membuat draft satu transaksi maupun batch dari input chat.
- Assistant produk/kontak memberikan saran yang relevan dan dapat mengisi input.
- Draft tidak langsung memproses transaksi ketika tombol kirim ditekan.
- Edit, batal, proses, warning duplikat, dan PIN memiliki perilaku yang konsisten.
- Satu batch menghasilkan satu kartu balasan sistem.
- Pratinjau nota dan pilihan berbagi WhatsApp dapat dibuka dari hasil transaksi.
- Tab Chat dan Riwayat dapat digunakan tanpa kehilangan alur navigasi.
- Tampilan utama konsisten dengan referensi Stitch dan nyaman digunakan pada perangkat Android target.
- Semua data eksternal dan hasil transaksi simulasi diberi batas yang jelas sehingga tidak disalahartikan sebagai transaksi produksi.

---

## 7. Referensi Fase 1

- `app/src/main/java/com/example/chatpos/`
- `app/src/main/java/com/example/chatpos/ui/`
- `app/src/main/java/com/example/chatpos/viewmodel/ChatPOSViewModel.kt`
- `stitch/conversational_retail_pos/DESIGN.md`
- Folder layar pada `stitch/pos_konter_*/`
- Template dan referensi UI pada `implementasi plan/`

Dokumen ini menggantikan roadmap multi-fase lama sebagai acuan kerja aktif. Fase berikutnya baru perlu ditambahkan setelah Definition of Done Fase 1 tercapai.
