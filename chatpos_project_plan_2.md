# Rencana Pengembangan ChatPOS

## Fokus Saat Ini: Fase 1 — UI/UX dan Alur Transaksi

**Platform:** Android  
**Teknologi:** Kotlin, Jetpack Compose, Material 3  
**Package:** `com.example.chatpos`  
**Status:** UI utama dan alur demo transaksi sudah terpasang; integrasi layanan eksternal belum menjadi bagian Fase 1 (kecuali persiapan UI untuk API spesifik).

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
- Input transfer bank & API Cek Rekening:
  - `cek[bank].[norek]` atau format cek rekening terkait.
  - **Persiapan API Cek Rekening:** Sistem memunculkan UI *loading state* ("Mengecek rekening...") setelah perintah cek dieksekusi. Hasil validasi nama pemilik rekening ditampilkan di dalam balasan *bubble* (pada Fase 1 menggunakan data *mock*, disiapkan untuk integrasi Retrofit).
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

### 3.6 Balasan hasil transaksi & Visibilitas Chat Kontak

- Semua item dalam satu pengiriman menghasilkan satu kartu balasan sistem.
- Kartu hasil menampilkan status sukses, detail item, SN, Ref ID, nominal, biaya admin, dan sisa saldo demo.
- Balasan sistem memakai label **BALASAN SERVER** dan rata kiri.
- Aksi nota tersedia dari kartu hasil:
  - Buka pratinjau cetak.
  - Buka dialog berbagi WhatsApp.
- **Kirim WhatsApp & Suggest Contact:** Modal dialog WhatsApp menyediakan baris *Suggest Contact* di atas kartu nota untuk mempercepat pemilihan kontak.
- **Aturan Sinkronisasi Detail Kontak:** 
  - Kartu balasan server **HANYA** akan muncul di layar "Chat Detail Kontak" (obrolan per orang) **JIKA** kasir secara eksplisit mengirimkan nota via WhatsApp ke kontak tersebut. Visibilitas tidak didasarkan pada nomor tujuan transaksi.
  - Saat kasir mengirimkan nota ke kontak tertentu (misal: Kontak A), kartu balasan di layar obrolan utama (Chat POS) dan di layar Chat Detail Kontak akan memperbarui informasi identitasnya menjadi `#custxxx - Kontak A`.

### 3.7 Nota, WhatsApp, dan riwayat

- `ReceiptPrintPreviewModal.kt` menampilkan pratinjau nota thermal 58 mm.
- `WhatsAppShareModal.kt` menyediakan pilihan nota gabungan atau nota terpisah per item.
- Pengiriman WhatsApp memakai intent URL; keberhasilan tetap bergantung pada aplikasi WhatsApp di perangkat.
- `ChatHistoryScreen.kt` menampilkan transaksi berdasarkan kontak/nomor dan detail nota.
- `TransactionHistoryScreen.kt` menyediakan filter berdasarkan nomor, kontak, atau produk.

### 3.8 Detail kartu balasan server dan error state

Aturan visual dan perilaku kartu balasan server yang harus digunakan pada Fase 1:

- Header kartu balasan, termasuk kartu status **GAGAL**, memiliki thin divider sebagai pemisah yang jelas dari detail transaksi.
- Tanggal dan waktu lengkap dengan format `dd-MM-yyyy HH:mm:ss` diletakkan di sebelah header, dengan tata letak yang sama seperti kartu balasan server sukses.
- Informasi `idcustomer : #custxxx` diletakkan di bagian bawah header, mengikuti tata letak balasan server sukses.
- Error transaksi seperti saldo demo tidak cukup atau nomor tujuan salah ditampilkan sebagai kartu balasan server dengan warna error dari `Color.kt`.
- **Pengecualian PIN:** error **PIN Salah** tidak membuat kartu balasan server baru. Pesan error ditampilkan langsung secara inline di dalam layar input PIN pada `PinConfirmationBottomSheet`, sehingga kasir dapat memperbaiki PIN tanpa kehilangan konteks proses.
- Jika balasan server berstatus **GAGAL** karena alasan selain PIN, aksi **Copy Trx** pada bubble chat kasir diganti menjadi dua tombol terpisah: **Edit** (mengembalikan command ke input agar diperbaiki) dan **Ulang Trx** (mengeksekusi ulang transaksi secara langsung). Kedua tombol memiliki fungsi yang berdiri sendiri.

### 3.9 Aturan UI Chat dan Riwayat

#### Chat screen

- Ikon Search di header/top app bar dihapus.
- Search bar di bawah header menggunakan hint inline dengan ikon: `{icon search} Search...`.
- Kartu kontak **Chat POS** memiliki padding atas dan bawah yang cukup agar tidak menempel dengan header maupun daftar di bawahnya.
- Teks label **Chat** di bawah kartu kontak Chat POS dihapus dan diganti dengan thin divider.
- Ikon profil pada item kartu Chat POS diubah menggunakan ikon sistem untuk transaksi (misalnya ikon POS, toko, atau keranjang belanja), menggantikan ikon profil default.
- Kartu kontak **Chat POS** mengikuti tata letak natural kartu kontak biasa: posisi teks, padding, radius, dan hierarki tipografi dibuat identik. Perbedaannya hanya pada warna background kartu dan penggunaan ikon sistem transaksi tersebut.

#### Riwayat screen

- Ikon Search di header/top app bar dihapus.
- Search bar menggunakan hint inline dengan ikon: `{icon search} Search...`.
- Judul teks **Riwayat** di dalam halaman dihapus karena konteks sudah ditunjukkan oleh tab dan header.
- Search bar diberi margin bottom yang cukup sebelum daftar riwayat.
- Riwayat ditampilkan per item transaksi/produk, bukan hanya dikelompokkan per kontak, dan diurutkan kronologis berdasarkan waktu transaksi (item terbaru berada di paling atas).
- Kartu item riwayat menggunakan layout dua kolom:
  - **Kolom kiri, rata kiri**
    - Baris 1: `{produk}.{notujuan}`
    - Baris 2: `{harga} - {sisa saldo}`
  - **Kolom kanan, rata kanan**
    - Baris 1: `{dd-mm-yyyy HH:mm:ss}`
    - Baris 2: `{status}`
- Warna status mengikuti semantic color:
  - **Sukses:** hijau.
  - **Pending:** amber/oranye.
  - **Gagal:** merah.

---

## 4. Batasan Implementasi Saat Ini

Bagian berikut **belum merupakan integrasi produksi** dan harus tetap dianggap simulasi selama Fase 1:

- Tidak ada request HTTP/API ke OtomaX (kecuali mock simulasi API Cek Rekening).
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
- Tambahkan thin divider pada header kartu balasan server, posisikan timestamp `dd-MM-yyyy HH:mm:ss` di sebelah header, dan letakkan identitas `idcustomer : #custxxx` di bawah header sesuai layout kartu sukses.
- Terapkan pengecualian PIN: pesan **PIN Salah** harus muncul inline di `PinConfirmationBottomSheet`, tanpa membuat kartu balasan server baru.
- Untuk status **GAGAL** selain PIN, tampilkan tombol aksi terpisah yaitu **Edit** dan **Ulang Trx** pada bubble kasir yang memiliki fungsinya masing-masing.
- Pastikan simulasi state error untuk saldo demo tidak cukup atau nomor salah menggunakan kartu balasan server dan warna error dari `Color.kt`.
- Terapkan visualisasi *loading state* untuk simulasi **API Cek Rekening** di dalam kolom obrolan sebelum menampilkan nama pemilik rekening.
- Pastikan seluruh ikon dan tombol aksi penting memiliki `contentDescription` atau label aksesibilitas yang jelas.
- Tambahkan feedback visual saat draft disimulasikan sedang dikirim, seperti shimmer atau loading spinner, sebelum kartu hasil ditampilkan.
- Hapus ikon Search dari top app bar pada Chat screen dan Riwayat screen.
- Ubah hint search bar pada kedua screen menjadi inline dengan ikon: `{icon search} Search...`.
- Rapikan Chat screen: tambahkan padding vertikal pada kartu Chat POS, ganti label **Chat** di bawahnya dengan thin divider, ubah ikon profilnya dengan ikon sistem transaksi, dan samakan layout kartu Chat POS dengan kartu kontak biasa kecuali warna background.
- Terapkan fitur *Suggest Contact* pada modal WhatsApp dan logika pembaruan identitas (`#custxxx - Nama Kontak`) beserta penambahan kartu secara eksklusif ke layar Chat Detail Kontak terkait.
- Rapikan Riwayat screen: hapus judul **Riwayat**, tambahkan margin bottom setelah search bar, tampilkan item per produk secara kronologis (terbaru di atas), dan gunakan layout kartu dua kolom sesuai format transaksi, harga/saldo, timestamp, dan status.
- Uji alur utama pada kondisi:
  - input satu transaksi;
  - input batch beberapa baris;
  - edit draft;
  - nomor duplikat;
  - PIN benar dan PIN tidak lengkap;
  - input batch kosong;
  - hasil tanpa item kosong;
  - keyboard terbuka pada layar kecil.
- Uji responsivitas saat keyboard terbuka agar input bar, assistant grid, dan kartu draft tidak saling overlap atau tertutup.
- Lakukan stress test UI dengan mengetik 10–15 baris draft sekaligus untuk memeriksa performa `LazyColumn`, penggunaan memori, dan kestabilan scrolling.
- Pastikan state welcome card, draft, hasil, dan riwayat tidak menampilkan data yang saling bertentangan.
- Beri feedback yang konsisten untuk loading, invalid input, batal, salin, dan kembali.

### Prioritas menengah

- Periksa ulang spacing, typography, warna status, dan ukuran tombol terhadap referensi di folder `stitch/`.
- Pastikan thin divider, padding kartu, margin search bar, dan alignment dua kolom tetap konsisten pada layar kecil.
- Pastikan status Sukses, Pending, dan Gagal memiliki warna yang konsisten di kartu balasan dan kartu riwayat.
- Terapkan `AnimatedVisibility` saat welcome card disembunyikan setelah transaksi pertama dibuat.
- Terapkan `animateContentSize` saat kasir menambah baris transaksi baru agar perubahan tinggi kartu batch berlangsung halus.
- Buat `@Preview` Compose untuk seluruh state komponen utama, termasuk state kosong, draft, loading, warning, sukses, dan error, untuk mempermudah audit desain.
- Pastikan filter riwayat menampilkan empty state interaktif dengan tombol CTA, misalnya **Buat Transaksi**, yang mengarahkan pengguna kembali ke tab Chat.
- Pastikan dialog WhatsApp menangani daftar item tunggal dan batch tanpa teks terpotong.
- Pastikan pratinjau nota tetap terbaca pada perangkat dengan ukuran layar berbeda.

### Tidak dikerjakan pada Fase 1

- Retrofit/HTTP client dan protokol OtomaX (kecuali kerangka UI untuk merespons API Cek Rekening).
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
- UI menampilkan *loading state* dan balasan *mock* nama rekening saat format Cek Rekening dijalankan.
- Edit, batal, proses, warning duplikat, dan PIN memiliki perilaku yang konsisten.
- Kartu balasan server memiliki thin divider pada header, timestamp `dd-MM-yyyy HH:mm:ss` di sebelah header, dan identitas `idcustomer : #custxxx` di bawah header.
- Error saldo demo tidak cukup atau nomor salah menghasilkan kartu balasan server berwarna error menggunakan token dari `Color.kt`.
- Error **PIN Salah** tampil inline di layar input PIN dan tidak menghasilkan kartu balasan server baru.
- Balasan berstatus **GAGAL** selain PIN memunculkan tombol terpisah **Edit** dan **Ulang Trx** pada bubble kasir dengan fungsinya masing-masing.
- Saat draft sedang disimulasikan dikirim, pengguna melihat shimmer atau loading spinner yang jelas sebelum hasil akhir muncul.
- Satu batch menghasilkan satu kartu balasan sistem untuk status sukses maupun gagal.
- Aksi Kirim WA mengubah identitas pada kartu balasan menjadi `#custxxx - Nama Kontak` dan menyalin kartu tersebut secara eksklusif ke layar Chat Detail Kontak milik penerima WA.
- Welcome card menghilang dengan `AnimatedVisibility`, dan penambahan baris transaksi menggunakan `animateContentSize` tanpa lompatan layout yang mengganggu.
- Pratinjau nota dan pilihan berbagi WhatsApp dapat dibuka dari hasil transaksi.
- Tab Chat dan Riwayat dapat digunakan tanpa kehilangan alur navigasi.
- Chat screen tidak menampilkan ikon Search di header, search bar memakai hint `{icon search} Search...`, kartu Chat POS memiliki padding yang cukup, dan thin divider menggantikan label Chat.
- Layout kartu Chat POS identik dengan kartu kontak biasa dalam posisi teks, padding, radius, dan tipografi (perbedaannya hanya pada warna background dan penggunaan ikon sistem khusus transaksi menggantikan profil).
- Riwayat screen tidak menampilkan ikon Search di header maupun judul **Riwayat** di dalam halaman.
- Daftar riwayat tampil per item produk dan terurut kronologis berdasarkan timestamp (item terbaru berada di paling atas).
- Setiap item riwayat memakai dua kolom: produk/tujuan serta harga/sisa saldo di kiri, timestamp/status di kanan.
- Status riwayat menggunakan warna semantic yang berbeda untuk Sukses, Pending, dan Gagal.
- Empty state filter riwayat memiliki CTA **Buat Transaksi** yang kembali ke tab Chat.
- Ikon dan tombol aksi utama memiliki content description atau label aksesibilitas yang dapat dipahami.
- Input batch kosong, PIN tidak lengkap, dan batch 10–15 baris telah diuji tanpa crash atau perilaku UI yang tidak terkendali.
- Saat keyboard terbuka pada layar kecil, input bar, assistant grid, dan kartu draft tidak overlap atau tertutup.
- `LazyColumn` tetap responsif saat menangani stress test 10–15 baris draft.
- Seluruh state komponen utama memiliki `@Preview` untuk audit desain.
- Tampilan utama konsisten dengan referensi Stitch dan nyaman digunakan pada perangkat Android target.
- Semua data eksternal dan hasil transaksi simulasi diberi batas yang jelas sehingga tidak disalahartikan sebagai transaksi produksi.

---

## 8. Roadmap Selanjutnya: Fase 1B (UI/UX Operasional Konter)

Fase 1B melanjutkan fondasi UI/UX Fase 1 ke kebutuhan operasional konter sehari-hari. Seluruh fitur di bawah ini tetap berupa simulasi lokal di memori: tidak ada sinkronisasi kas nyata, upload file ke server, database permanen, atau integrasi perangkat produksi.

### 8.1 Manajemen Visibilitas Fitur (Enable/Disable)
Menambahkan sistem *toggle* atau opsi visibilitas sederhana untuk mengaktifkan atau menonaktifkan seluruh modul Fase 1B (Pengeluaran, Uang Laci, Stock Opname). Hal ini memastikan UI kasir dapat disederhanakan murni untuk transaksi PPOB jika fitur operasional konter tidak dibutuhkan oleh agen.

### 8.2 Simulasi UI Kontak Chat Pengeluaran

Menambahkan kartu kontak **Chat Pengeluaran** yang ditempatkan tepat di bawah kartu kontak Chat POS pada daftar pesan layar Chat. Kartu ini menjadi *entry point* untuk mencatat pengeluaran operasional konter.

- Kartu kontak **Chat Pengeluaran** menampilkan ikon dompet/keuangan, label **Pengeluaran**, dan subtitle tentang pencatatan pengeluaran operasional.
- Kartu kontak menampilkan total pengeluaran terakhir (jika ada) sebagai info tambahan, identik dengan layout kartu kontak biasa.
- Pengguna dapat mengetuk kartu kontak untuk membuka layar detail (Chat Pengeluaran).
- **Perubahan Pola Input:** Pada layar detail Chat Pengeluaran, input *tidak* menggunakan bar pengetikan chat/format teks (seperti `keluar.nominal...`).
- Sebagai gantinya, antarmuka menyediakan tombol aksi besar bergambar **`+` (Tambah Pengeluaran)** di bagian bawah layar.
- Menekan tombol `+` akan membuka sebuah **Modal/Bottom Sheet form input pengeluaran** yang lebih terstruktur.
- Di dalam modal form, UI menyediakan isian untuk nominal, keterangan, dan *chips* kategori pengeluaran: **Belanja Stok**, **Transportasi**, **Listrik/Internet**, **Biaya Operasional**, dan **Lainnya**.
- Modal form menyediakan opsi unggah foto nota sebagai lampiran simulasi untuk pengeluaran harian.
- Foto nota hanya ditampilkan sebagai preview lokal dengan state unggah berhasil, gagal, atau belum dipilih; belum dikirim ke backend.
- Setelah disubmit dari modal, data dirender ke dalam *stream chat* sebagai kartu riwayat pengeluaran. Kartu ini menampilkan kategori, nominal, catatan, waktu, status pencatatan, dan *thumbnail* nota (jika ada).
- Kartu riwayat pengeluaran perlu menyediakan aksi edit (membuka ulang modal), hapus/batalkan, dan konfirmasi.

### 8.3 Screen Uang Laci

Menyediakan layar simulasi untuk rekonsiliasi uang tunai di laci pada akhir shift.

- Menampilkan ringkasan saldo kas yang diharapkan berdasarkan transaksi simulasi.
- Kasir dapat memasukkan uang fisik yang dihitung per pecahan atau melalui total manual.
- UI menghitung dan menampilkan selisih antara saldo sistem simulasi dan uang aktual di laci.
- Selisih positif, seimbang, dan negatif harus memiliki indikator visual yang berbeda dan mudah dipahami.
- Tombol **Tutup Shift** membuka konfirmasi sebelum shift ditandai selesai.
- Setelah konfirmasi, UI menampilkan ringkasan penutupan shift yang dapat ditinjau kembali.
- Status tutup shift tetap lokal dan tidak mengunci akun, mengubah saldo produksi, atau menyimpan data permanen.

### 8.4 Screen Stock Opname

Menyediakan layar tabel untuk simulasi pemeriksaan stok fisik barang konter.

- Tabel menampilkan kode barang, nama produk, stok sistem, stok fisik, dan selisih.
- Kasir dapat menambahkan item melalui simulasi pemindai barcode.
- Jika pemindai tidak digunakan, kode barang dapat dimasukkan secara manual melalui input pencarian.
- Hasil scan atau input manual harus memuat data produk demo yang sesuai atau menampilkan state item tidak ditemukan.
- Perubahan stok fisik langsung memperbarui perhitungan selisih pada baris terkait.
- Selisih negatif harus divalidasi dan diberi peringatan yang jelas sebelum opname dikonfirmasi.
- UI menyediakan state kosong, loading simulasi, item duplikat, barcode tidak dikenal, dan tabel dengan banyak baris.
- Konfirmasi stock opname hanya mengubah state simulasi lokal; tidak mengurangi atau menambah stok permanen.

### Batasan dan prioritas Fase 1B

Fase 1B dikerjakan setelah Definition of Done Fase 1 tercapai. Fokusnya adalah konsistensi pengalaman operasional, validasi input (terutama melalui form modal untuk menghindari salah ketik format), responsivitas tabel/kartu, serta *state feedback* yang mudah dipahami. Integrasi kamera aktual, barcode scanner fisik, akuntansi, penyimpanan permanen, dan sinkronisasi server ditunda ke fase setelahnya.

---

## 9. Referensi Fase 1

- `app/src/main/java/com/example/chatpos/`
- `app/src/main/java/com/example/chatpos/ui/`
- `app/src/main/java/com/example/chatpos/viewmodel/ChatPOSViewModel.kt`
- `stitch/conversational_retail_pos/DESIGN.md`
- Folder layar pada `stitch/pos_konter_*/`
- Template dan referensi UI pada `implementasi plan/`

Dokumen ini menggantikan roadmap multi-fase lama sebagai acuan kerja aktif. Implementasi Fase 1B dimulai setelah Definition of Done Fase 1 tercapai.