package com.forus.qa.testcases;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.forus.qa.base.TestBase;
import com.forus.qa.pages.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginPageTest extends TestBase {
    LoginPage loginPage;
    public static ExtentReports extent;
    public static ExtentTest test;

    @BeforeClass
    public void setupExtent() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
    }

    @BeforeMethod
    public void setup() {
        initialization();
        loginPage = new LoginPage();
    }

    @Test(priority = 1)
    public void loginTest() throws InterruptedException {
        test = extent.createTest("Login Page - Valid Login Test");

        test.info("Step 1: Browser launched and application opened");

        // Step 1: Login with email and password
        test.info("Step 2: Entering Email and Password");
        boolean loginSuccessful = loginPage.login(prop.getProperty("email"), prop.getProperty("password"));

        if (loginSuccessful) {
            test.pass("Step 3: Login button clicked successfully");
        } else {
            test.fail("Step 3: Login button click failed");
            Assert.fail("Login failed");
        }

        // Step 4: Wait for OTP
        test.info("Step 4: Waiting for OTP fields to become visible");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElements(loginPage.getOtpInputFields()));

        // Step 5: Handle OTP
        test.info("Step 5: Entering fixed OTP - 010101");
        loginPage.handleOtp("010101");

        test.pass("Step 6: OTP submitted successfully");
    }

    @AfterMethod
    public void tearDown() {
       // driver.quit();
        test.info("Browser closed after test");
        extent.flush(); // flush after each test
    }
}
