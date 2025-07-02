package com.forus.qa.base;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.MediaEntityBuilder;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

public class TestBase {

    public static WebDriver driver;
    public static Properties prop;

    public static ExtentReports extent;      // Extent report instance
    public static String category = "";      // Will hold category for ExtentTest

    public static final String Dashboard = "Dashboard Page";
    public static final String Portfolio = "Portfolio Page";
    public static final String Wallet = "Wallet Page";

    public static com.aventstack.extentreports.ExtentTest test; // ExtentTest instance for logging

    // Load config.properties file
    public TestBase() {
        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream("./src/main/java/com/forus/qa/config/config.properties");
            prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Browser launch logic
    public static void initialization() {
        String browserName = prop.getProperty("browser");

        if (browserName.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "/home/unthinkable-lap/Documents/chromedriver");

            // ✅ Disable browser notification popups
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.default_content_setting_values.notifications", 2); // 1 = allow, 2 = block

            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);

            driver = new ChromeDriver(options);

        } else if (browserName.equalsIgnoreCase("firefox") || browserName.equalsIgnoreCase("ff")) {
            System.setProperty("webdriver.gecko.driver", "/home/unthinkable-lap/Documents/geckodriver");
            driver = new FirefoxDriver(); // Firefox notification handling is more advanced
        } else {
            throw new RuntimeException("Browser not supported: " + browserName);
        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(prop.getProperty("url"));
    }

    // Extent Report initialization
    public static ExtentReports initExtentReport() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("./test-output/ExtentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Host Name", "Forus Invest");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("User", "Nitesh");
        }
        return extent;
    }

    // Take screenshot utility
    public static String takeScreenshot(String screenshotName) {
        String dateName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String destPath = System.getProperty("user.dir") + "/screenshots/" + screenshotName + "_" + dateName + ".png";

        try {
            FileUtils.copyFile(src, new File(destPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destPath;
    }

    // Screenshot on test failure (after every method)
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = takeScreenshot(result.getName());

            if (test != null) {
                test.fail("Test Failed: " + result.getThrowable());
                test.fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            }
        }
    }

}
