package com.forus.qa.testcases;

import com.aventstack.extentreports.ExtentTest;
import com.forus.qa.base.TestBase;
import com.forus.qa.pages.PortfolioPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.forus.qa.base.TestBase.driver;

public class PortfolioPageTest extends TestBase {

    PortfolioPage portfolioPage;
    ExtentTest test;

    @BeforeClass
    public void setUp() {
        portfolioPage = new PortfolioPage();  // Page Object initialization
    }

    @BeforeMethod
    public void createExtentTest(Method method) {
        // Create Extent test with custom readable names
        switch (method.getName()) {
            case "verifyArchivedLoanDetailsFlow":
                test = extent.createTest("Portfolio Page - Validate Archived Loan Details");
                break;
            case "verifyCancelledStatusForArchivedLoan":
                test = extent.createTest("Portfolio Page - Verify CANCELLED Status for Archived Loan");
                break;
                case "verifyRecordCountIsNine":
                test = extent.createTest("Portfolio Page - Verify Record Count is 9 in Active Campaigns Page");
                break;
            case "verifyUserLoanCancellationStatus":
                test = extent.createTest("Portfolio Page - Verify Details Of The Particular User In Active Camapigns");
                break;
            default:
                test = extent.createTest(method.getName());
        }
    }

    @Test(priority = 1)
    public void verifyArchivedLoanDetailsFlow() throws InterruptedException {
        try {
            test.assignCategory(Portfolio);

            test.info("Step 1: Navigate to the 'Portfolio' section.");
            portfolioPage.switchTabsToCheckDetails();
            test.pass("Switched between Active and Archived Campaigns successfully.");

            test.info("Step 2: Click on a particular loan entry under Archived Campaigns.");
            portfolioPage.openArchivedLoanDetails();
            test.pass("Clicked on loan, opened details, clicked info icon, and confirmed with OK.");

        } catch (Exception e) {
            test.fail("Test failed due to exception: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 2)
    public void verifyCancelledStatusForArchivedLoan() throws InterruptedException {
        try {
            test.assignCategory(Portfolio);

            test.info("Step 1: Verifying that loan status is CANCELLED.");
            String actualStatus = portfolioPage.getCancelledStatusText();
            test.info("Actual status found: " + actualStatus);

            Assert.assertEquals(actualStatus, "CANCELLED", "Loan status does not match expected value.");
            test.pass("Loan status verified as CANCELLED.");

        } catch (Exception e) {
            test.fail("Test failed while verifying CANCELLED status: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3)
    public void verifyRecordCountIsNine() {
        try {
            test.assignCategory(Portfolio);
            test.info("Step 1: Validating that displayed record count is 9.");

            int actualCount = portfolioPage.extractRecordCountFromText();  // ✅ Method call from PortfolioPage
            test.info("Actual count extracted: " + actualCount);

            Assert.assertEquals(actualCount, 9, "Displayed record count mismatch.");
            test.pass("Record count verified successfully as 9.");

        } catch (Exception e) {
            test.fail("Test failed while verifying record count: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 4)
    public void verifyUserLoanCancellationStatus() {
        try {
            test.assignCategory(Portfolio);
            test.info("Step 1: Navigating to active loan and initiating cancellation process.");

            // Call the method to handle UI actions and return either status or error
            String statusText = portfolioPage.activeloandetailsofuser();
            test.info("Captured status or error: " + statusText);

            // Handle negative case: 48-hour cancellation window exceeded
            if (statusText.equalsIgnoreCase("Investment exceeded 48 hours cancellation period")) {
                test.warning("Cancellation not allowed due to 48-hour restriction.");
                Assert.assertEquals(statusText, "Investment exceeded 48 hours cancellation period",
                        "Expected cancellation restriction message not found.");
                test.pass("Proper error message shown for exceeding 48-hour cancellation period.");
            } else {
                // Positive case: loan still shows as APPROVED
                Assert.assertEquals(statusText, "APPROVED", "Loan status is not 'APPROVED' as expected.");
                test.pass("Loan status verified successfully as 'APPROVED' before cancellation flow.");
            }

        } catch (Exception e) {
            test.fail("Test failed while verifying loan cancellation flow: " + e.getMessage());
            throw e;
        }
    }





}
