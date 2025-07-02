package com.forus.qa.pages;

import com.forus.qa.base.TestBase;
import com.forus.qa.util.TestUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class WalletPage extends TestBase {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    JavascriptExecutor js = (JavascriptExecutor) driver;
    LoginPage loginPage = new LoginPage();

    // static flag to avoid re-clicking Wallet link
    public static boolean isWalletScreenOpened = false;

    public WalletPage() {
        PageFactory.initElements(driver, this);
    }

    //  WIDGETS  //
    @FindBy(xpath = "(//div[@class='chakra-stack css-en-owjkmg'])[4]")
    WebElement walletLink;

    @FindBy(xpath = "//button[text()='Deposit']")
    WebElement depositbtn;

    @FindBy(xpath = "(//button[text()='Withdraw'])[2]")
    WebElement walletbtn;

    @FindBy(xpath = "//button[text()='okay']")
    WebElement okaybtn;

    @FindBy(xpath = "//input[@class='chakra-input css-en-1ws9z5s']")
    WebElement amountfield;

    @FindBy(xpath = "(//button[normalize-space()='Withdraw'])[4]")
    WebElement withdrawbtn;

    @FindBy(xpath = "(//div[contains(@class, 'chakra-alert__desc') and text()='Successful withdraw'])[1]")
    WebElement successfulwithdrwmsg;

    @FindBy(xpath = "//button[@id='menu-button-31']")
    WebElement date;

    @FindBy(xpath = "//p[text()='Today']")
    WebElement today;

    @FindBy(xpath = "//p[text()='Yesterday']")
    WebElement yesterday;

    @FindBy(xpath = "//p[text()='Last week']")
    WebElement lastWeek;

    @FindBy(xpath = "//p[text()='Last month']")
    WebElement lastMonth;

    @FindBy(xpath = "//button[@id='menu-button-39']")
    WebElement withdrawfilter;

    @FindBy(xpath = "//p[text()='All']")
    WebElement Allbtn;

    @FindBy(xpath = "(//p[text()='Deposit'])[2]")
    WebElement depositfilter;

    @FindBy(xpath = "(//p[text()='Withdraw'])[2]")
    WebElement withdrawfilterintrasncationlog;

    @FindBy(xpath = "(//p[text()='Payout'])[2]")
    WebElement Payout;

    @FindBy(xpath = "(//p[text()='Investment'])[2]")
    WebElement investment;

    @FindBy(xpath = "//p[text()='Fee']")
    WebElement fee;

    //  ACTIONS //

    public void verifywithdrawfunctionality() throws InterruptedException {
        if (!isWalletScreenOpened) {
            wait.until(ExpectedConditions.elementToBeClickable(walletLink)).click();
            System.out.println("Clicked on Wallet link");
            Thread.sleep(1500);
            isWalletScreenOpened = true;
        } else {
            System.out.println("Already on Wallet screen. Skipping wallet click.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(depositbtn)).click();
        System.out.println("Clicked on Deposit button");

        wait.until(ExpectedConditions.elementToBeClickable(okaybtn)).click();
        System.out.println("Clicked on Okay button");

        wait.until(ExpectedConditions.elementToBeClickable(walletbtn)).click();
        System.out.println("Clicked on Wallet Withdraw button");

        wait.until(ExpectedConditions.elementToBeClickable(amountfield)).sendKeys("10");
        System.out.println("Entered withdrawal amount");

        wait.until(ExpectedConditions.elementToBeClickable(withdrawbtn)).click();
        System.out.println("Clicked Withdraw button after amount entry");

        loginPage.handleOtp("010101");
        System.out.println("Entered OTP");

        PageFactory.initElements(driver, this);  // Refresh web elements

        wait.until(ExpectedConditions.elementToBeClickable(withdrawbtn)).click();
        System.out.println("Clicked Withdraw button after OTP");

        wait.until(ExpectedConditions.visibilityOf(successfulwithdrwmsg));
        System.out.println("Withdrawal success message verified.");

        Thread.sleep(1500);  // Let UI settle
        PageFactory.initElements(driver, this);
        System.out.println("PageFactory reinitialized after withdraw.");
    }

    public void withdrawMultipleAmountsFromExcel() throws InterruptedException {
        List<String> amounts = TestUtil.readWithdrawAmounts("./WithdrawData.xlsx", "Sheet1");

        if (!isWalletScreenOpened) {
            wait.until(ExpectedConditions.elementToBeClickable(walletLink)).click();
            Thread.sleep(1500);
            isWalletScreenOpened = true;
        }

        for (String amount : amounts) {
            System.out.println("\uD83D\uDE80 Withdrawing Amount: " + amount + " SAR");

            wait.until(ExpectedConditions.elementToBeClickable(depositbtn)).click();
            wait.until(ExpectedConditions.elementToBeClickable(okaybtn)).click();
            wait.until(ExpectedConditions.elementToBeClickable(walletbtn)).click();
            wait.until(ExpectedConditions.elementToBeClickable(amountfield)).clear();
            wait.until(ExpectedConditions.elementToBeClickable(amountfield)).sendKeys(amount);
            wait.until(ExpectedConditions.elementToBeClickable(withdrawbtn)).click();

            loginPage.handleOtp("010101");
            wait.until(ExpectedConditions.elementToBeClickable(withdrawbtn)).click();

            wait.until(ExpectedConditions.visibilityOf(successfulwithdrwmsg));
            System.out.println("✅ Withdraw successful for " + amount + " SAR");

            Thread.sleep(2000);
        }
    }

    public String getWithdrawSuccessMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(successfulwithdrwmsg)).getText().trim();
        } catch (TimeoutException e) {
            return "Toast not visible";
        }
    }

    public void clickAllTransactionFilterCombinations() throws InterruptedException {
        // Open Wallet screen first (always)
        wait.until(ExpectedConditions.elementToBeClickable(walletLink)).click();
        Thread.sleep(1500);
        System.out.println("Opened Wallet screen.");

        // Click date filters one by one
        WebElement[] dateFilters = {today, yesterday, lastWeek, lastMonth};
        for (WebElement dateOption : dateFilters) {
            wait.until(ExpectedConditions.elementToBeClickable(date)).click();
            wait.until(ExpectedConditions.elementToBeClickable(dateOption)).click();
            System.out.println("Clicked Date Filter: " + dateOption.getText());
            Thread.sleep(1000);
        }

        // Click type filters one by one
        WebElement[] typeFilters = {Allbtn, depositfilter, withdrawfilterintrasncationlog, Payout, investment, fee};
        for (WebElement typeOption : typeFilters) {
            wait.until(ExpectedConditions.elementToBeClickable(withdrawfilter)).click();
            wait.until(ExpectedConditions.elementToBeClickable(typeOption)).click();
            System.out.println("Clicked Type Filter: " + typeOption.getText());
            Thread.sleep(1000);
        }

        System.out.println("Simple click-through of all filters completed.");

    }

    @FindBy(xpath = "(//button[@class='chakra-button css-en-ias91b'])[1]")
    WebElement iIcon;

    @FindBy(xpath = "//button[@class='chakra-button css-en-1chxgxc']")
    WebElement downloadInvoice;

    public String handleInvoiceDownloadFlow() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(iIcon)).click();
        String mainWindow = driver.getWindowHandle();

        wait.until(ExpectedConditions.elementToBeClickable(downloadInvoice)).click();
        Thread.sleep(2000);  // wait for new tab to open

        Set<String> allWindows = driver.getWindowHandles();
        String invoiceUrl = "";

        for (String window : allWindows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                invoiceUrl = driver.getCurrentUrl();
                System.out.println("Switched to invoice tab: " + invoiceUrl);

                Thread.sleep(2000);
                driver.close();  // ❗ Close new tab (but stay in that closed tab context for now)
                break;
            }
        }

        // Now switch back to original tab after closing
        driver.switchTo().window(mainWindow);  // ✅ Back to Wallet page
        System.out.println("✅ Switched back to main window/tab: " + driver.getTitle());

        return invoiceUrl;
    }


    @FindBy(xpath = "//button[@class='chakra-button css-en-sfac2t']")
    WebElement exporttranscationlogs;

    // ✅ Step 1: Click the export button
    public void clickExportTransactionLogsButton() {
        driver.navigate().back();
        wait.until(ExpectedConditions.elementToBeClickable(exporttranscationlogs)).click();
        System.out.println(" Clicked on Export Transaction Logs button");
    }

    // ✅ Step 2: Optional - Delete old transaction files
    public void deleteOldTransactionFiles() {
        String downloadDir = System.getProperty("user.home") + "/Downloads";
        File dir = new File(downloadDir);

        File[] matchingFiles = dir.listFiles((d, name) -> name.matches("transactions( \\(\\d+\\))?\\.xlsx"));

        if (matchingFiles != null) {
            for (File file : matchingFiles) {
                boolean deleted = file.delete();
                System.out.println(deleted
                        ? "Deleted old file: " + file.getName()
                        : "Failed to delete: " + file.getName());
            }
        } else {
            System.out.println("No old transaction files found to delete.");
        }
    }

    // ✅ Step 3: Detect latest downloaded file
    public File getLatestDownloadedTransactionFile() throws InterruptedException {
        Thread.sleep(3000); // Wait for download to finish

        String downloadDir = System.getProperty("user.home") + "/Downloads";
        File dir = new File(downloadDir);

        File[] matchingFiles = dir.listFiles((d, name) -> name.matches("transactions( \\(\\d+\\))?\\.xlsx"));

        if (matchingFiles == null || matchingFiles.length == 0) {
            System.out.println("No matching transactions file found.");
            return null;
        }

        File latestFile = Arrays.stream(matchingFiles)
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);

        System.out.println(" Latest transaction file: " + latestFile.getAbsolutePath());
        return latestFile;
    }

    @FindBy(xpath = "//div[@class='css-en-qkulqy']//button[not(.//svg) and normalize-space(text()) != '...']")
    List<WebElement> paginationButtons;

    @FindBy(xpath = "//p[contains(@class,'chakra-text') and contains(@class,'css-en-j602pp')]")
    List<WebElement> transactionTexts;

    public void refreshPageFactoryElements() {
        PageFactory.initElements(driver, this);
    }

    public void verifyPaginationWithScrolling() throws InterruptedException {
        int totalPages = paginationButtons.size();

        for (int i = 1; i <= totalPages; i++) {
            refreshPageFactoryElements();

            for (WebElement btn : paginationButtons) {
                String btnText = btn.getText().trim();
                if (btnText.equals(String.valueOf(i))) {

                    //Scroll into view before clicking
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
                    Thread.sleep(500);

                    wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
                    System.out.println("Clicked Page: " + btnText);
                    Thread.sleep(1500);

                    refreshPageFactoryElements();  // refresh transactionTexts

                    if (transactionTexts.size() > 0) {
                        System.out.println("Page " + btnText + ": Found " + transactionTexts.size() + " transactions.");
                    } else {
                        System.out.println("Page " + btnText + ": No transactions found.");
                    }

                    break;
                }
            }

        }

        System.out.println("Pagination with scrolling complete.");
    }


    // 1. This WebElement should already be there:
    @FindBy(xpath = "//p[contains(@class,'chakra-text') and contains(@class,'css-en-1whx6sj')]")
    WebElement walletAmountSAR;

    // 2. Method to extract balance:
    public double getSARBalance() {
        String balanceText = walletAmountSAR.getText().trim(); // e.g., "100,066,165.9SAR"
        String numericPart = balanceText.replaceAll("[^\\d.]", ""); // removes commas, "SAR", etc.
        return Double.parseDouble(numericPart);
    }

    // 3. Method to check if reduced:
    public void checkIfBalanceReducedAfterLastWithdraw(double previousBalance) {
        double currentBalance = getSARBalance();
        System.out.println("Previous Balance: " + previousBalance);
        System.out.println("Current Balance: " + currentBalance);

        if (currentBalance < previousBalance) {
            System.out.println("✅ Balance reduced — withdrawal likely succeeded.");
        } else {
            System.out.println("❌ Balance not reduced — withdrawal may have failed.");
            throw new AssertionError("Balance has not reduced after withdrawal attempt.");
        }
    }




}

//int[] marks;
//marks = new int[5];
//marks[0] = 85;
//marks[1] = 90;
//int[] marks = {85, 90, 75, 95, 80};


















