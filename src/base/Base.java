package base;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;

public class Base {
    private static AndroidDriver<AndroidElement> driver;

    public static AndroidDriver<AndroidElement> setup() {
        try {
            System.out.println("Setting up AndroidDriver...");

            // Validate APK file
            File appDir = new File("src");
            File app = new File(appDir, "expense-tracker.apk");
            System.out.println("Path APK: " + app.getAbsolutePath());
            System.out.println("APK exists: " + app.exists());
            if (!app.exists()) {
                System.err.println("APK file not found at: " + app.getAbsolutePath());
                throw new RuntimeException("APK file not found: " + app.getAbsolutePath());
            }

            // Define capabilities
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
            cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
            cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
            cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, "16");
            cap.setCapability(MobileCapabilityType.NO_RESET, false); // Ubah ke false untuk instal ulang
            cap.setCapability(MobileCapabilityType.FULL_RESET, false);
            cap.setCapability("appium:uiautomator2ServerInstallTimeout", 120000);
            // Opsional: tambahkan appPackage dan appActivity
            // cap.setCapability("appium:appPackage", "com.example.acerental");
            // cap.setCapability("appium:appActivity", "com.example.acerental.MainActivity");

            // Initialize AndroidDriver
            URL appiumServerURL = new URL("http://127.0.0.1:4723/");
            System.out.println("Connecting to Appium server at: " + appiumServerURL);
            driver = new AndroidDriver<>(appiumServerURL, cap);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            System.out.println("AndroidDriver initialized successfully");

            return driver;
        } catch (Exception e) {
            System.err.println("Failed to initialize AndroidDriver: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize AndroidDriver", e);
        }
    }

    public static void tearDown() {
        if (driver != null) {
            System.out.println("Tearing down AndroidDriver...");
            driver.quit();
            System.out.println("AndroidDriver closed");
        }
    }

    public static WebDriverWait getWait() {
        return new WebDriverWait(driver, 15);
    }
}