package com.forus.qa.testcases;

import com.aventstack.extentreports.ExtentTest;
import com.forus.qa.base.TestBase;
import com.forus.qa.pages.DashboardPage;
import org.testng.Assert;
import org.testng.annotations.*;

public class DashboardPageTest extends TestBase {

    DashboardPage dashboardPage;
    ExtentTest test;

    @BeforeClass
    public void setUp() {
        dashboardPage = new DashboardPage(); // ✅ Only page object init
    }

    @Test(priority = 1)
    public void validatePortfolioValue() {
        test = extent.createTest("Dashboard Page - Validate Portfolio Value").assignCategory(Dashboard);

        test.info("Step 1: Browser launched and user is logged into the application");
        test.info("Step 2: Navigated to Dashboard screen");

        String actualValue = dashboardPage.getPortfolioValue();
        String expectedValue = "SAR";

        test.info("Step 3: Expected portfolio value: " + expectedValue);
        test.info("Step 4: Actual portfolio value from UI: " + actualValue);

        if (actualValue.equals(expectedValue)) {
            test.pass("Step 5: Portfolio value matched successfully: " + actualValue);
        } else {
            test.fail("Step 5: Portfolio value mismatch. Expected: " + expectedValue + ", but got: " + actualValue);
        }

        Assert.assertEquals(actualValue, expectedValue, "Portfolio value mismatch!");
    }

    @Test(priority = 2)
    public void copyfunctionality() throws InterruptedException {
        test = extent.createTest("Dashboard Page - Verify 'Copy Functionality is Working Fine").assignCategory(Dashboard);

        test.info("Step 1: Attempting to go to Wallet Recharge Section on Dashboard");
        dashboardPage.validateCopyFunctionality();
        test.pass("Step 2: Click on Copy button to check functionality");
    }

    @Test(priority = 3)
    public void verifyShowAllClick() throws InterruptedException {
        test = extent.createTest("Dashboard Page - Verify 'Show All' Button").assignCategory(Dashboard);

        test.info("Step 1: Attempting to click on 'Show All' button on Dashboard");
        dashboardPage.clickShowAll();
        test.pass("Step 2: 'Show All' button clicked successfully");

        Thread.sleep(2000);
    }

    @Test(priority = 4)
    public void verifyShowAllforLatestTrancations() throws InterruptedException {
        test = extent.createTest("Dashboard Page - Verify Show All is Working Fine").assignCategory(Dashboard);

        test.info("Step 1: Attempting to go to Latest Transcation Section on Dashboard");
        dashboardPage.checkLatesttranscatinsbyclickingonShowAll();
        test.pass("Step 2: Click on Show All button to check functionality");
    }

    @Test(priority = 5)
    public void verifydetailtranscationsforLatestTrancationsbyclickingonPaid() throws InterruptedException {
        test = extent.createTest("Dashboard Page - Verify Detailed Transcations On Clicking On Paid In Latest Transccation Section").assignCategory(Dashboard);

        test.info("Step 1: Attempting to go to Latest Transcation Section on Dashboard");
        dashboardPage.verifytranscationdetailbyclickingonPaidbtn();
        test.pass("Step 2: Click on i button to check details transcations");
    }

    @AfterClass
    public void tearDown() {
        if (extent != null) {
            extent.flush();
            System.out.println("Extent report flushed successfully.");
        }

        // driver.quit(); // ❌ Remove if using global quit from TestSetup
    }
}
