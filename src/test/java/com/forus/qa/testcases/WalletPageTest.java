package com.forus.qa.testcases;

import com.forus.qa.base.TestBase;
import com.forus.qa.pages.DashboardPage;
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

public class WalletPageTest extends TestBase {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private WalletPage walletPage;
    private WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        // Initialize WebDriver and open the browser
        initialization();
        loginPage = new LoginPage(); // Initialize LoginPage
        dashboardPage = new DashboardPage(driver); // Initialize DashboardPage
        walletPage = new WalletPage(driver); // Initialize WalletPage
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Initialize WebDriverWait
    }

    @Test(priority = 1)
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

            // Step 6: Click on Wallet tab
            System.out.println("Step 6: Clicking on the Wallet link...");
            walletPage.clickWallet();
            System.out.println("Wallet link clicked and Wallet page loaded successfully.");

            // Step 7: Click Deposit and cancel the popup
            Thread.sleep(1000);
            System.out.println("Step 7: Handling Deposit popup...");
            boolean isPopupCancelled = walletPage.cancelDepositPopup();
            Assert.assertTrue(isPopupCancelled, "Failed to cancel the Deposit popup!");
            System.out.println("Deposit popup was cancelled successfully.");

            // Step 8: Click Withdraw and verify the modal appears
            Thread.sleep(2000);
            System.out.println("Step 8: Clicking Withdraw...");
            walletPage.clickWithdraw();
            Thread.sleep(10000);

            // Wait for the Withdraw modal to appear
            System.out.println("Waiting for the Withdraw confirmation modal...");
            wait.until(ExpectedConditions.visibilityOf(walletPage.withdrawConfirmationDialog));

            // Verify the Withdraw modal is displayed
            boolean isPopupVisible = walletPage.isWithdrawPopupDisplayed();
            Assert.assertTrue(isPopupVisible, "Withdraw popup/modal did not appear.");
            System.out.println("Withdraw popup/modal appeared successfully.");

            // Step 9: Enter the withdrawal amount
            System.out.println("Step 9: Entering the withdrawal amount...");
            String amountToWithdraw = "10"; // Set the amount you want to test
            walletPage.enterAmountToWithdraw(amountToWithdraw); // Call the method to enter the amount
            System.out.println("Withdrawal amount entered successfully.");

            // Step 10: Assert or verify the entered amount
            System.out.println("Step 10: Verifying the entered withdrawal amount...");
            String enteredAmount = walletPage.getEnteredAmount();
            Assert.assertEquals(enteredAmount, amountToWithdraw, "The entered amount does not match the expected value!");
            System.out.println("Entered amount verified successfully.");

            // Step 11: Confirm withdrawal
            System.out.println("Step 11: Confirming withdrawal...");
            Thread.sleep(2000);
            walletPage.confirmWithdraw();
            Thread.sleep(2000);
            System.out.println("Withdrawal confirmed successfully.");

            // Step 12: Handle OTP for withdrawal confirmation
            System.out.println("Step 12: Handling OTP for withdrawal confirmation...");
            wait.until(ExpectedConditions.visibilityOf(loginPage.enterOTP));

            String withdrawalOtp = "010101"; // Enter OTP

            loginPage.handleOtp(withdrawalOtp); // Reusing the same method for OTP input
            Thread.sleep(2000);

            System.out.println("OTP for withdrawal handled successfully.");

        } catch (Exception e) {
            System.err.println("Test execution failed due to an error: " + e.getMessage());
            Assert.fail("Test case failed due to an exception: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Tearing down the test and closing the browser...");
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed successfully.");
        }
    }
}
