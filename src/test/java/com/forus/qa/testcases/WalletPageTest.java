package com.forus.qa.testcases;

import com.aventstack.extentreports.ExtentTest;
import com.forus.qa.base.TestBase;
import com.forus.qa.pages.WalletPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;

public class WalletPageTest extends TestBase {

    WalletPage walletPage;
    ExtentTest test;

    @BeforeClass
    public void setUp() {
        walletPage = new WalletPage();  // Initialize Page Object once
    }

    @BeforeMethod
    public void createExtentTest(Method method) {
        switch (method.getName()) {
            case "verifyWalletWithdrawFlow":
                test = extent.createTest("Wallet Page - Verify Withdraw functionality");
                break;
            case "verifyTransactionLogFilterCombinations":
                test = extent.createTest("Wallet Page - Verify Transaction Log Filters");
                break;
            case "verifyInvoiceDownloadFlow":
                test = extent.createTest("Wallet Page - Verify Invoice Download Flow");
                break;
            case "verifyExportTransactionLogsDownload":
                test = extent.createTest("Wallet Page - Verify Export Transcations Download Flow");
                break;
            case "verifyPaginationTransactionTextPresence":
                test = extent.createTest("Wallet Page - Verify Pagination Work Flow");
                break;
            case "verifySARWalletBalanceDecreaseAfterWithdraw":
                test = extent.createTest("Wallet Page - Verify Withdraw Amount Decresed or Not After Withdrawl");
                break;
            case "withdrawUsingExcelData":
                test = extent.createTest("Wallet Page - Withdraw multiple amounts using Excel data");
                break;
        }
        test.assignCategory("Wallet");
    }

    @Test(priority = 1)
    public void verifyWalletWithdrawFlow() throws InterruptedException {
        try {
            // Reset wallet screen flag before first test
            WalletPage.isWalletScreenOpened = false;

            test.info("Step 1: Clicked on the 'Wallet' section from the sidebar.");
            test.info("Step 2: Clicked on the 'Deposit' button.");
            test.info("Step 3: Clicked on the 'Okay' button to close the deposit confirmation.");
            test.info("Step 4: Clicked on the 'Withdraw' button to open the withdrawal modal.");
            test.info("Step 5: Entered the withdrawal amount (₹10).");
            test.info("Step 6: Clicked on the 'Withdraw' button to proceed.");
            test.info("Step 7: Entered OTP (010101) on the OTP verification screen.");
            test.info("Step 8: Clicked on the 'Withdraw' button again to confirm the withdrawal.");

            walletPage.verifywithdrawfunctionality();
            test.pass("Step 9: Withdrawal submitted successfully.");

            String toastMsg = walletPage.getWithdrawSuccessMessage();
            Assert.assertEquals(toastMsg, "Successful withdraw", "Confirmation message mismatch");
            test.pass("Step 10: Verified success toast message: '" + toastMsg + "'.");

        } catch (Exception e) {
            test.fail("Withdraw flow failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 2)
    public void verifyTransactionLogFilterCombinations() throws InterruptedException {
        try {
            walletPage = new WalletPage();
            test.info("Step 1: Opening Wallet Page and looping through all date + type filter combinations.");
            walletPage.clickAllTransactionFilterCombinations();
            test.pass("Step 2: Successfully clicked all filter combinations and captured screenshots.");
        } catch (Exception e) {
            test.fail("Transaction log filter test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3)
    public void verifyInvoiceDownloadFlow() throws InterruptedException {
        try {
            walletPage = new WalletPage();
            test.info("Step 1: Opened Wallet Page.");
            test.info("Step 2: Clicking on info icon (i) for transaction.");

            String invoiceUrl = walletPage.handleInvoiceDownloadFlow();
            test.info("Step 3: Redirected to invoice URL: " + invoiceUrl);

            Assert.assertFalse(invoiceUrl.contains("/invoice") || invoiceUrl.endsWith(".pdf"),
                    "Invoice URL not correct: " + invoiceUrl);
            test.pass("Invoice URL verified successfully.");

        } catch (Exception e) {
            test.fail("Invoice download flow failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 4)
    public void verifyExportTransactionLogsDownload() throws InterruptedException {
        try {
            walletPage = new WalletPage();

            test.info("Step 1: Delete old transaction files.");
            walletPage.deleteOldTransactionFiles();

            test.info("Step 2: Click Export Transaction Logs.");
            walletPage.clickExportTransactionLogsButton();

            test.info("Step 3: Check latest downloaded file.");
            File downloadedFile = walletPage.getLatestDownloadedTransactionFile();

            Assert.assertNotNull(downloadedFile, "File not downloaded.");
            Assert.assertTrue(downloadedFile.length() > 0, "Downloaded file is empty.");
            test.pass("transactions.xlsx downloaded successfully: " + downloadedFile.getName());

        } catch (Exception e) {
            test.fail("Export Transaction Logs test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 5)
    public void verifyPaginationTransactionTextPresence() throws InterruptedException {
        try {
            walletPage = new WalletPage();
            test.info("Step 1: Starting pagination verification with scrolling.");
            walletPage.verifyPaginationWithScrolling();
            test.pass(" Verified transaction text presence across all paginated pages.");
        } catch (Exception e) {
            test.fail("Pagination transaction test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 6)
    public void verifyWalletBalanceReduced() throws InterruptedException {
        walletPage = new WalletPage();

        double balanceBeforeWithdraw = walletPage.getSARBalance();
        System.out.println("Captured Previous Balance: " + balanceBeforeWithdraw);

        walletPage.checkIfBalanceReducedAfterLastWithdraw(balanceBeforeWithdraw);
    }

    @Test(priority = 7)
    public void withdrawUsingExcelData() throws InterruptedException {
        try {
            walletPage = new WalletPage();
            test.info("Step 1: Starting withdrawal using Excel amounts.");
            walletPage.withdrawMultipleAmountsFromExcel();
            test.pass("All Excel-based withdrawal amounts processed successfully.");
        } catch (Exception e) {
            test.fail("Excel-based withdrawal test failed: " + e.getMessage());
            throw e;
        }
    }
}
