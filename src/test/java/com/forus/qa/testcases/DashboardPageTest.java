package com.forus.qa.testcases;

import com.forus.qa.base.TestBase;
import com.forus.qa.pages.DashboardPage;
import com.forus.qa.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class DashboardPageTest extends TestBase {

    LoginPage loginPage;
    DashboardPage dashboardPage;

    // WebDriverWait instance to use throughout the test
    WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        initialization(); // Initialize WebDriver and open the browser
        loginPage = new LoginPage();           // Initialize LoginPage
        dashboardPage = new DashboardPage(driver); // Initialize DashboardPage
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Initialize WebDriverWait
    }

    @Test(priority = 1)
    public void completeFlowTest() {
        System.out.println("Starting complete flow test.");

        // Step 1: Login with email and password
        loginPage.login(prop.getProperty("email"), prop.getProperty("password"));
        Assert.assertTrue(loginPage.loginBtnClick(), "Login button click failed!");

        // Step 2: Wait for the OTP input to appear (after login click)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='Please enter your pin code']")));

        // Step 3: Handle OTP (reusing the working method logic)
        String otp = "010101";  // Fixed OTP (or you can make this dynamic)
        loginPage.handleOtp(otp);  // Calling the handleOtp method

        // Step 4: Wait for the Settings page to load
        wait.until(ExpectedConditions.urlContains("https://app-test.forusinvest.com/investor"));

        // Step 5: Assert that we are on the expected page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("https://app-test.forusinvest.com/investor"),
                "Login failed! URL does not contain 'https://app-test.forusinvest.com/investor'.");

        // Step 6: Click on Settings and switch the language to English
        dashboardPage.clickOnSettingsLink();   // Click on Settings link
        dashboardPage.switchToEnglish();       // Switch language to English

        // Step 7: (Optional) Verify language switch to English
        // wait.until(ExpectedConditions.visibilityOf(dashboardPage.getEnglishIndicator()));
        // Assert.assertTrue(dashboardPage.getEnglishIndicator().isDisplayed(), "Dashboard language switch to English failed.");

        // Step 8: Verify wallet link is clickable and click it
        System.out.println("Step 8: Clicking on the Wallet link...");
        dashboardPage.clickWallet();
        System.out.println("Wallet link clicked successfully.");

        // Step 9: Click Deposit and cancel the popup
        System.out.println("Step 9: Click Deposit and handle the popup...");
        boolean isPopupCancelled = dashboardPage.cancelDepositPopup();
        Assert.assertTrue(isPopupCancelled, "Failed to cancel the Deposit popup!");
        System.out.println("Test Passed: Deposit popup was cancelled successfully.");

        System.out.println("Complete flow executed successfully.");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit(); // Quit browser after test
    }
}
