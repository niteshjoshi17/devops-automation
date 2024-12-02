package com.forus.qa.pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class DashboardPage {

    WebDriver driver;

    // Locators for Dashboard elements using @FindBy
    @FindBy(xpath = "//a[contains(@class, 'chakra-link') and .//p[text()='إعدادات']]") // Settings link
    private WebElement settingsLink;

    @FindBy(id = "tabs-35--tab-2") // Arabic tab
    private WebElement arabicTab;

    @FindBy(xpath = "//span[@class='css-ar-6uc9an']") // English language option
    private WebElement englishRadioBtn;

    @FindBy(xpath = "//a[contains(@class, 'chakra-link') and .//p[text()='Wallet']]") // Wallet link
    private WebElement clickWalletTab;

    @FindBy(xpath = "//button[@class='chakra-button css-en-4pbpd1']") // Deposit button
    private WebElement depositButton;

    @FindBy(xpath = "//button[@aria-label='Close']") // Cancel Deposit popup button
    private WebElement cancelPopupButton;

    @FindBy(xpath = "//button[@class='chakra-button css-en-12bjt37']") // Click Withdraw button
    private WebElement clickWithdrawButton;

    @FindBy(xpath = "//div[@class='chakra-modal__body css-en-9diry6']")  // Pop-up model after withdraw
    public WebElement withdrawConfirmationDialog;


    @FindBy(xpath = "//input[@type='text']") // Enter amount to Withdraw
    private WebElement enterAmount;

    @FindBy(xpath = "//button[@class='chakra-button css-en-15vcl3k']") // Click Withdraw after entering amount
    private WebElement clickWithdrawAfterEnterAmount;

    @FindBy(xpath = "//button[@class='chakra-button css-en-15vcl3k']") // Click Withdraw after entering amount
    private WebElement confirmWithdrawBtn;





    // Constructor to initialize the page elements using PageFactory
    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // Initialize the WebElements using PageFactory
    }

    // Method to click on the Settings link
    public void clickOnSettingsLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(settingsLink));
        settingsLink.click();
    }

    // Method to switch language to English
    public void switchToEnglish() {
        arabicTab.click(); // Click on the Arabic tab (if needed)
        englishRadioBtn.click(); // Select the English language radio button
    }

    // Method to click on the Wallet tab
    public void clickWallet() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait until the Wallet link is visible and clickable
        wait.until(ExpectedConditions.visibilityOf(clickWalletTab));
        wait.until(ExpectedConditions.elementToBeClickable(clickWalletTab));

        // Click on the Wallet tab
        clickWalletTab.click();

    }

    // Method to handle and cancel the Deposit popup
    public boolean cancelDepositPopup() {
        // Initialize WebDriverWait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Step 1: Ensure the Deposit button is visible
            System.out.println("Waiting for Deposit button to become visible...");
            wait.until(ExpectedConditions.visibilityOf(depositButton));

            // Adding an explicit wait to slow down execution and ensure button visibility
            Thread.sleep(2000); // Add a short delay (2 second)

//            // Step 2: Scroll to the Deposit button to ensure it's in the viewport
//            System.out.println("Scrolling to the Deposit button...");
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", depositButton);

            // Step 3: Ensure the Deposit button is clickable
            System.out.println("Waiting for Deposit button to become clickable...");
            wait.until(ExpectedConditions.elementToBeClickable(depositButton));

            // Add a delay before clicking the button
            Thread.sleep(5000); // Additional wait (2 second)

            // Step 4: Click the Deposit button
            System.out.println("Clicking the Deposit button...");
            depositButton.click();
            Thread.sleep(2000);

            // Step 5: Wait for the Cancel popup button to appear
            System.out.println("Waiting for Cancel popup to appear...");
            wait.until(ExpectedConditions.visibilityOf(cancelPopupButton));

            // Step 6: Ensure the Cancel button is clickable
            System.out.println("Waiting for Cancel button to become clickable...");
            wait.until(ExpectedConditions.elementToBeClickable(cancelPopupButton));

            // Step 7: Click the Cancel button to close the popup
            System.out.println("Clicking the Cancel button...");
            cancelPopupButton.click();

            // Return true if no exceptions occur
            System.out.println("Deposit popup cancelled successfully.");
            return true;

        } catch (Exception e) {
            // Log the error for debugging
            System.out.println("Error while handling deposit popup: " + e.getMessage());
            return false;
        }
    }

    // Method to click the Withdraw button
    public void clickWithdraw() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            // Wait for the button to be clickable
            WebElement withdrawButton = wait.until(ExpectedConditions.elementToBeClickable(clickWithdrawButton));

            // Log properties of the element
            System.out.println("Withdraw Button - Displayed: " + withdrawButton.isDisplayed());
            System.out.println("Withdraw Button - Enabled: " + withdrawButton.isEnabled());

            // Scroll to the button if not in the viewport
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", withdrawButton);

            // Use JavaScript to click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", withdrawButton);
            System.out.println("Withdraw button clicked using JavaScript.");
        } catch (Exception e) {
            System.out.println("Failed to click Withdraw button: " + e.getMessage());
            // Capture screenshot for debugging
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenshot, new File("withdraw_click_error.png"));
                System.out.println("Screenshot captured: withdraw_click_error.png");
            } catch (IOException ioException) {
                System.out.println("Failed to save screenshot: " + ioException.getMessage());
            }
        }
    }



    // Method to check if the Withdraw popup is displayed
    public boolean isWithdrawPopupDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));  // Increased wait time
        try {
            // Check if the withdraw confirmation modal is displayed
            WebElement modal = wait.until(ExpectedConditions.visibilityOf(withdrawConfirmationDialog));
            return modal.isDisplayed();  // Return true if the modal is visible
        } catch (Exception e) {
            System.out.println("Withdraw popup/modal did not appear.");
            return false;  // Return false if modal did not appear
        }


}

//    // Method to enter the withdrawal amount
//    public void enterAmountToWithdraw(String amount) {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//        // Wait until the 'Enter Amount' field is visible
//        wait.until(ExpectedConditions.visibilityOf(enterAmount));
//
//        // Clear any existing value and enter the new amount
//        enterAmount.clear();
//        enterAmount.sendKeys(amount);
//        System.out.println("Amount entered: " + amount);
//    }


}





