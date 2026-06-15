package test;

import base.Base;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import pages.MainPage;

public class FullFlowTest {

    static AndroidDriver<AndroidElement> driver;
    static MainPage page;

    public static void main(String[] args) {
        try {
            driver = Base.setup();
            page = new MainPage(driver);
            Thread.sleep(3000);

            tc01_tambahPengeluaranMakanan();
            tc02_tambahPemasukanGaji();
            tc03_tambahPengeluaranTransport();
            tc04_tambahPemasukanFreelance();
            tc05_tambahPengeluaranTagihan();
            tc06_tambahPengeluaranHiburan();
            tc07_negativeTanpaDeskripsi();
            tc08_negativeTanpaNominal();
            tc09_negativeTanpaKategori();
            tc10_filterSemua();
            tc11_filterPemasukan();
            tc12_filterPengeluaran();

            separator("SEMUA TEST CASE SELESAI");

        } catch (Exception e) {
            System.err.println("Test gagal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Base.tearDown();
        }
    }

    // =====================================================================
    // TC-01: Tambah Pengeluaran - Makan Siang
    // Flow: lihat saldo awal → isi form → submit → lihat riwayat
    // =====================================================================
    static void tc01_tambahPengeluaranMakanan() throws InterruptedException {
        separator("TC-01: Tambah Pengeluaran - Makan Siang");
        page.lihatSaldo();           // saldo terlihat di atas (Rp 0 awal)
        Thread.sleep(3000);

        page.enterDescription("Makan Siang");
        page.enterAmount("50000");
        page.selectChipMakanan();
        page.selectPengeluaran();
        Thread.sleep(1000);
        page.tapTambahTransaksi();
        Thread.sleep(2000);

        page.scrollToRiwayat();
        Thread.sleep(4000);
    }

    // =====================================================================
    // TC-02: Tambah Pemasukan - Gaji Bulanan
    // Flow: scroll ke atas lihat saldo update → isi form → submit → lihat riwayat
    // =====================================================================
    static void tc02_tambahPemasukanGaji() throws InterruptedException {
        separator("TC-02: Tambah Pemasukan - Gaji Bulanan");
        page.scrollToTop();
        page.lihatSaldo();           // saldo setelah TC-01
        Thread.sleep(3000);

        page.enterDescription("Gaji Bulanan");
        page.enterAmount("2000000");
        page.selectChipGaji();
        page.selectPemasukan();
        Thread.sleep(1000);
        page.tapTambahTransaksi();
        Thread.sleep(2000);

        page.scrollToRiwayat();
        Thread.sleep(4000);
    }

    // =====================================================================
    // TC-03: Tambah Pengeluaran - Ojek Online
    // =====================================================================
    static void tc03_tambahPengeluaranTransport() throws InterruptedException {
        separator("TC-03: Tambah Pengeluaran - Ojek Online");
        page.scrollToTop();
        page.lihatSaldo();
        Thread.sleep(3000);

        page.enterDescription("Ojek Online");
        page.enterAmount("25000");
        page.selectChipTransport();
        page.selectPengeluaran();
        Thread.sleep(1000);
        page.tapTambahTransaksi();
        Thread.sleep(2000);

        page.scrollToRiwayat();
        Thread.sleep(4000);
    }

    // =====================================================================
    // TC-04: Tambah Pemasukan - Freelance (kategori kustom)
    // =====================================================================
    static void tc04_tambahPemasukanFreelance() throws InterruptedException {
        separator("TC-04: Tambah Pemasukan - Freelance (Kategori Kustom)");
        page.scrollToTop();
        page.lihatSaldo();
        Thread.sleep(3000);

        page.enterDescription("Project Freelance");
        page.enterAmount("750000");
        page.selectChipLainnya();
        page.enterCustomCategory("Freelance");
        page.selectPemasukan();
        Thread.sleep(1000);
        page.tapTambahTransaksi();
        Thread.sleep(2000);

        page.scrollToRiwayat();
        Thread.sleep(4000);
    }

    // =====================================================================
    // TC-05: Tambah Pengeluaran - Tagihan Listrik
    // =====================================================================
    static void tc05_tambahPengeluaranTagihan() throws InterruptedException {
        separator("TC-05: Tambah Pengeluaran - Tagihan Listrik");
        page.scrollToTop();
        page.lihatSaldo();
        Thread.sleep(3000);

        page.enterDescription("Tagihan Listrik");
        page.enterAmount("150000");
        page.selectChipTagihan();
        page.selectPengeluaran();
        Thread.sleep(1000);
        page.tapTambahTransaksi();
        Thread.sleep(2000);

        page.scrollToRiwayat();
        Thread.sleep(4000);
    }

    // =====================================================================
    // TC-06: Tambah Pengeluaran - Nonton Film
    // =====================================================================
    static void tc06_tambahPengeluaranHiburan() throws InterruptedException {
        separator("TC-06: Tambah Pengeluaran - Nonton Film");
        page.scrollToTop();
        page.lihatSaldo();
        Thread.sleep(3000);

        page.enterDescription("Nonton Film");
        page.enterAmount("75000");
        page.selectChipHiburan();
        page.selectPengeluaran();
        Thread.sleep(1000);
        page.tapTambahTransaksi();
        Thread.sleep(2000);

        page.scrollToRiwayat();
        Thread.sleep(4000);
    }

    // =====================================================================
    // TC-07: Negative - Tambah Tanpa Deskripsi
    // Expected: transaksi TIDAK bertambah, saldo TIDAK berubah
    // =====================================================================
    static void tc07_negativeTanpaDeskripsi() throws InterruptedException {
        separator("TC-07: Negative - Tambah Tanpa Deskripsi");
        page.scrollToTop();
        page.lihatSaldo();           // catat saldo sebelum
        Thread.sleep(3000);

        page.enterDescription("");
        page.enterAmount("100000");
        page.selectChipMakanan();
        page.selectPengeluaran();
        Thread.sleep(1000);
        page.tapTambahTransaksi();
        Thread.sleep(2000);

        page.scrollToTop();
        System.out.println("[CEK] Saldo seharusnya TIDAK berubah:");
        page.lihatSaldo();
        Thread.sleep(3000);

        page.scrollToRiwayat();
        Thread.sleep(4000);
    }

    // =====================================================================
    // TC-08: Negative - Tambah Tanpa Nominal
    // Expected: transaksi TIDAK bertambah, saldo TIDAK berubah
    // =====================================================================
    static void tc08_negativeTanpaNominal() throws InterruptedException {
        separator("TC-08: Negative - Tambah Tanpa Nominal");
        page.scrollToTop();
        page.lihatSaldo();
        Thread.sleep(3000);

        page.enterDescription("Test Tanpa Nominal");
        page.enterAmount("");
        page.selectChipMakanan();
        page.selectPengeluaran();
        Thread.sleep(1000);
        page.tapTambahTransaksi();
        Thread.sleep(2000);

        page.scrollToTop();
        System.out.println("[CEK] Saldo seharusnya TIDAK berubah:");
        page.lihatSaldo();
        Thread.sleep(3000);

        page.scrollToRiwayat();
        Thread.sleep(4000);
    }

    // =====================================================================
    // TC-09: Negative - Tambah Tanpa Kategori
    // Expected: transaksi TIDAK bertambah, saldo TIDAK berubah
    // =====================================================================
    static void tc09_negativeTanpaKategori() throws InterruptedException {
        separator("TC-09: Negative - Tambah Tanpa Kategori");
        page.scrollToTop();
        page.lihatSaldo();
        Thread.sleep(3000);

        page.clearCategory();    // pastikan chip dari TC sebelumnya sudah tidak aktif
        page.enterDescription("Test Tanpa Kategori");
        page.enterAmount("50000");
        // tidak pilih chip, tidak isi kategori kustom
        page.selectPengeluaran();
        Thread.sleep(1000);
        page.tapTambahTransaksi();
        Thread.sleep(2000);

        page.scrollToTop();
        System.out.println("[CEK] Saldo seharusnya TIDAK berubah:");
        page.lihatSaldo();
        Thread.sleep(3000);

        page.scrollToRiwayat();
        Thread.sleep(4000);
    }

    // =====================================================================
    // TC-10: Filter Riwayat - Semua
    // =====================================================================
    static void tc10_filterSemua() throws InterruptedException {
        separator("TC-10: Filter Riwayat - Semua");
        page.selectFilter("Semua");
        Thread.sleep(5000);
    }

    // =====================================================================
    // TC-11: Filter Riwayat - Pemasukan
    // =====================================================================
    static void tc11_filterPemasukan() throws InterruptedException {
        separator("TC-11: Filter Riwayat - Pemasukan");
        page.selectFilter("Pemasukan");
        Thread.sleep(5000);
    }

    // =====================================================================
    // TC-12: Filter Riwayat - Pengeluaran
    // =====================================================================
    static void tc12_filterPengeluaran() throws InterruptedException {
        separator("TC-12: Filter Riwayat - Pengeluaran");
        page.selectFilter("Pengeluaran");
        Thread.sleep(5000);
    }

    // ─────────────────────────────────────────────────────────────────────
    static void separator(String title) {
        System.out.println("\n========================================");
        System.out.println("  " + title);
        System.out.println("========================================");
    }
}
