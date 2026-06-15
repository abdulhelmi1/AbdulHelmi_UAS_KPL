package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.Base;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class MainPage {
    private AndroidDriver<AndroidElement> driver;
    private WebDriverWait wait;

    private static final String ET_DESCRIPTION     = "com.example.smartexpensetracker:id/etDescription";
    private static final String ET_AMOUNT          = "com.example.smartexpensetracker:id/etAmount";
    private static final String ET_CATEGORY        = "com.example.smartexpensetracker:id/etCategory";
    private static final String CHIP_MAKANAN       = "com.example.smartexpensetracker:id/chipMakanan";
    private static final String CHIP_TRANSPORT     = "com.example.smartexpensetracker:id/chipTransport";
    private static final String CHIP_TAGIHAN       = "com.example.smartexpensetracker:id/chipTagihan";
    private static final String CHIP_HIBURAN       = "com.example.smartexpensetracker:id/chipHiburan";
    private static final String CHIP_GAJI          = "com.example.smartexpensetracker:id/chipGaji";
    private static final String CHIP_LAINNYA       = "com.example.smartexpensetracker:id/chipLainnya";
    private static final String BTN_TOGGLE_INCOME  = "com.example.smartexpensetracker:id/btnToggleIncome";
    private static final String BTN_TOGGLE_EXPENSE = "com.example.smartexpensetracker:id/btnToggleExpense";
    private static final String BTN_ADD            = "com.example.smartexpensetracker:id/btnAdd";
    private static final String TV_BALANCE         = "com.example.smartexpensetracker:id/tvBalance";
    private static final String TV_TOTAL_INCOME    = "com.example.smartexpensetracker:id/tvTotalIncome";
    private static final String TV_TOTAL_EXPENSE   = "com.example.smartexpensetracker:id/tvTotalExpense";
    private static final String SP_FILTER          = "com.example.smartexpensetracker:id/spFilter";

    public MainPage(AndroidDriver<AndroidElement> driver) {
        this.driver = driver;
        this.wait = Base.getWait();
    }

    // ── Input ─────────────────────────────────────────────────────────────

    public void enterDescription(String description) {
        retryAction(() -> {
            AndroidElement el = (AndroidElement) wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id(ET_DESCRIPTION)));
            el.clear();
            el.sendKeys(description);
        });
        System.out.println("Entered description: " + description);
    }

    public void enterAmount(String amount) {
        retryAction(() -> {
            AndroidElement el = (AndroidElement) wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id(ET_AMOUNT)));
            el.clear();
            el.sendKeys(amount);
        });
        System.out.println("Entered amount: " + amount);
    }

    public void enterCustomCategory(String category) {
        retryAction(() -> {
            AndroidElement el = (AndroidElement) wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id(ET_CATEGORY)));
            el.clear();
            el.sendKeys(category);
        });
        System.out.println("Entered custom category: " + category);
    }

    // ── Chip Kategori Cepat ───────────────────────────────────────────────

    public void selectChipMakanan()   { clickChip(CHIP_MAKANAN,   "Makanan"); }
    public void selectChipTransport() { clickChip(CHIP_TRANSPORT, "Transport"); }
    public void selectChipTagihan()   { clickChip(CHIP_TAGIHAN,   "Tagihan"); }
    public void selectChipHiburan()   { clickChip(CHIP_HIBURAN,   "Hiburan"); }
    public void selectChipGaji()      { clickChip(CHIP_GAJI,      "Gaji"); }
    public void selectChipLainnya()   { clickChip(CHIP_LAINNYA,   "Lainnya"); }

    private void clickChip(String id, String name) {
        retryAction(() -> wait.until(ExpectedConditions.elementToBeClickable(By.id(id))).click());
        System.out.println("Selected chip: " + name);
    }

    public void clearCategory() {
        // Deselect chip yang masih aktif dengan klik ulang
        String[] allChips = {
            CHIP_MAKANAN, CHIP_TRANSPORT, CHIP_TAGIHAN,
            CHIP_HIBURAN, CHIP_GAJI, CHIP_LAINNYA
        };
        for (String chipId : allChips) {
            try {
                AndroidElement chip = (AndroidElement) driver.findElement(By.id(chipId));
                if (Boolean.parseBoolean(chip.getAttribute("checked"))) {
                    chip.click();
                    System.out.println("Deselected chip: " + chipId);
                    break;
                }
            } catch (Exception ignored) {}
        }
        // Kosongin juga field kategori kustom
        try {
            AndroidElement etCat = (AndroidElement) driver.findElement(By.id(ET_CATEGORY));
            etCat.clear();
        } catch (Exception ignored) {}
        System.out.println("Category cleared");
    }

    // ── Toggle Pemasukan / Pengeluaran ────────────────────────────────────

    public void selectPemasukan() {
        retryAction(() -> wait.until(ExpectedConditions.elementToBeClickable(
                By.id(BTN_TOGGLE_INCOME))).click());
        System.out.println("Selected: PEMASUKAN");
    }

    public void selectPengeluaran() {
        retryAction(() -> wait.until(ExpectedConditions.elementToBeClickable(
                By.id(BTN_TOGGLE_EXPENSE))).click());
        System.out.println("Selected: PENGELUARAN");
    }

    // ── Tambah Transaksi ──────────────────────────────────────────────────

    public void tapTambahTransaksi() {
        scrollToElement(BTN_ADD);
        retryAction(() -> wait.until(ExpectedConditions.elementToBeClickable(By.id(BTN_ADD))).click());
        System.out.println("Tapped TAMBAH TRANSAKSI");
    }

    // ── Scroll ────────────────────────────────────────────────────────────

    public void scrollToTop() {
        try {
            driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollToBeginning(5)"));
            System.out.println("Scrolled to top");
        } catch (Exception ignored) {
            swipeDown(); // fallback
        }
    }

    public void scrollToRiwayat() {
        // Method 1: scrollToEnd via ScrollView class (langsung ke bawah, tanpa reset ke atas)
        try {
            driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().className(\"android.widget.ScrollView\")).scrollToEnd(5)"));
            System.out.println("Scrolled to Riwayat (method 1)");
            return;
        } catch (Exception ignored) {}

        // Method 2: scrollToEnd via scrollable(true)
        try {
            driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(5)"));
            System.out.println("Scrolled to Riwayat (method 2)");
            return;
        } catch (Exception ignored) {}

        // Method 3: swipe fallback
        System.out.println("UiScrollable gagal, pakai swipe");
        for (int i = 0; i < 5; i++) {
            swipeUp();
            try { Thread.sleep(300); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
        }
    }

    private void scrollToElement(String resourceId) {
        try {
            driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"))"));
        } catch (Exception ignored) {
            swipeUp();
        }
    }

    // Swipe ke atas → layar scroll ke bawah (menuju riwayat)
    @SuppressWarnings("rawtypes")
    private void swipeUp() {
        try {
            int width  = driver.manage().window().getSize().width;
            int height = driver.manage().window().getSize().height;
            new TouchAction(driver)
                .press(PointOption.point(width / 2, (int)(height * 0.75)))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(400)))
                .moveTo(PointOption.point(width / 2, (int)(height * 0.25)))
                .release()
                .perform();
        } catch (Exception ignored) {}
    }

    // Swipe ke bawah → layar scroll ke atas (menuju saldo)
    @SuppressWarnings("rawtypes")
    private void swipeDown() {
        try {
            int width  = driver.manage().window().getSize().width;
            int height = driver.manage().window().getSize().height;
            new TouchAction(driver)
                .press(PointOption.point(width / 2, (int)(height * 0.25)))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(400)))
                .moveTo(PointOption.point(width / 2, (int)(height * 0.75)))
                .release()
                .perform();
        } catch (Exception ignored) {}
    }

    // ── Baca Saldo ────────────────────────────────────────────────────────

    public void lihatSaldo() {
        try {
            String balance = driver.findElement(By.id(TV_BALANCE)).getText();
            String income  = driver.findElement(By.id(TV_TOTAL_INCOME)).getText();
            String expense = driver.findElement(By.id(TV_TOTAL_EXPENSE)).getText();
            System.out.println("[SALDO BERSIH] " + balance);
            System.out.println("[PEMASUKAN]    " + income);
            System.out.println("[PENGELUARAN]  " + expense);
        } catch (Exception e) {
            System.err.println("Gagal baca saldo: " + e.getMessage());
        }
    }

    // ── Filter Riwayat ────────────────────────────────────────────────────

    public void selectFilter(String filterOption) {
        scrollToRiwayat();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        retryAction(() -> wait.until(ExpectedConditions.elementToBeClickable(By.id(SP_FILTER))).click());
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        // Android Spinner dropdown pakai CheckedTextView, fallback ke xpath //*
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.CheckedTextView[@text='" + filterOption + "']"))).click();
        } catch (Exception e) {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@text='" + filterOption + "']"))).click();
        }
        System.out.println("Selected filter: " + filterOption);
    }

    // ── Helper ────────────────────────────────────────────────────────────

    private void retryAction(Runnable action) {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                action.run();
                return;
            } catch (StaleElementReferenceException e) {
                retryCount++;
                if (retryCount >= maxRetries) throw e;
                System.out.println("Stale element, retrying (" + retryCount + "/" + maxRetries + ")");
                try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
    }
}
