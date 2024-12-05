package com.forus.qa.testcases;

import com.forus.qa.base.TestBase;
import com.forus.qa.pages.AnalyticsPage;
import com.forus.qa.pages.LoginPage;
import com.forus.qa.pages.WalletPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class AnalyticsTest extends TestBase {
    public LoginPage loginPage;
    public AnalyticsPage analyticsPage;
    public WalletPage walletPage;
    public WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        // Initialize WebDriver and open the browser
        initialization();
        loginPage = new LoginPage(); // Initialize LoginPage
        analyticsPage = new AnalyticsPage(driver); // Initialize AnalyticsPage
        walletPage = new WalletPage(driver); // Initialize WalletPage (added this initialization)
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Initialize WebDriverWait
    }

    @Test
    public void completeFlowTest() {
        try {
            System.out.println("Starting the complete flow test...");

            // Step 1: Login with email and password
            System.out.println("Step 1: Logging in...");
            loginPage.login(prop.getProperty("email"), prop.getProperty("password"));
            Assert.assertTrue(loginPage.loginBtnClick(), "Login button click failed!");
            System.out.println("Login successful.");

            // Step 2: Wait for the OTP input to appear for login
            System.out.println("Step 2: Waiting for OTP input field...");
            wait.until(ExpectedConditions.visibilityOf(loginPage.enterOTP));

            // Step 3: Handle OTP for login
            System.out.println("Step 3: Handling OTP...");
            String otp = "010101"; // Replace with dynamic OTP retrieval logic if available
            loginPage.handleOtp(otp);
            System.out.println("OTP handled successfully.");

            // Step 4: Wait for the Dashboard page to load
            System.out.println("Step 4: Waiting for the Dashboard to load...");
            wait.until(ExpectedConditions.urlContains("https://app-test.forusinvest.com/investor"));
            Assert.assertTrue(driver.getCurrentUrl().contains("https://app-test.forusinvest.com/investor"),
                    "Login failed! URL does not contain the expected path.");
            System.out.println("Dashboard loaded successfully.");

            // Step 5: Click on Settings and switch language to English
            System.out.println("Step 5: Clicking on Settings link...");
            walletPage.clickOnSettingsLink();
            System.out.println("Switching language to English...");
            walletPage.switchToEnglish();
            System.out.println("Language switched to English successfully.");

            // Step 6: Click on Analytics Link
            System.out.println("Step 6: Clicking on Analytics link...");
            Thread.sleep(2000);
            analyticsPage.clickOnAnalyticsLink();
            Thread.sleep(2000);
            System.out.println("Analytics Link is clicked successfully...");

            // Step 7: Click on Invest Button and Navigate to Marketplace
            System.out.println("Step 7: Clicking on Invest Button...");
            Thread.sleep(2000);
            analyticsPage.clickOnInvest();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.urlContains("https://app-test.forusinvest.com/investor/marketplace"));
            Assert.assertTrue(driver.getCurrentUrl().contains("https://app-test.forusinvest.com/investor/marketplace"),
                    "Failed to navigate to the Marketplace page.");
            System.out.println("Successfully navigated to the Marketplace page.");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser after each test
        driver.quit();
    }
}
