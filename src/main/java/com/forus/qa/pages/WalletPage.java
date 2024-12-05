package com.forus.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WalletPage {

    WebDriver driver;

    // Locators for Wallet elements using @FindBy
    @FindBy(xpath = "//a[contains(@class, 'chakra-link') and .//p[text()='إعدادات']]")
    private WebElement settingsLink;

    @FindBy(id = "tabs-35--tab-2")
    private WebElement arabicTab;

    @FindBy(xpath = "//span[@class='css-ar-6uc9an']")
    private WebElement englishRadioBtn;

    @FindBy(xpath = "//a[contains(@class, 'chakra-link') and .//p[text()='Wallet']]")
    private WebElement clickWalletTab;

    @FindBy(xpath = "//button[@class='chakra-button css-en-4pbpd1']")
    private WebElement depositButton;

    @FindBy(xpath = "//button[@aria-label='Close']")
    private WebElement cancelPopupButton;

    @FindBy(xpath = "//button[@class='chakra-button css-en-12bjt37']")
    private WebElement clickWithdrawButton;

    @FindBy(xpath = "//div[@class='chakra-modal__body css-en-9diry6']")
    public WebElement withdrawConfirmationDialog;

    @FindBy(xpath = "//input[@type='text']")
    private WebElement enterAmount;

    @FindBy(xpath = "//button[@class='chakra-button css-en-15vcl3k']")
    private WebElement confirmWithdrawBtn;

    // Constructor to initialize the page elements using PageFactory
    public WalletPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Method to click on the Settings link
    public void clickOnSettingsLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(settingsLink));
        settingsLink.click();
    }

    // Method to switch language to English
    public void switchToEnglish() {
        arabicTab.click();
        englishRadioBtn.click();
    }

    // Method to click on the Wallet tab
    public void clickWallet() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(clickWalletTab));
        wait.until(ExpectedConditions.elementToBeClickable(clickWalletTab));
        clickWalletTab.click();
    }

    // Method to handle and cancel the Deposit popup
    public boolean cancelDepositPopup() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            wait.until(ExpectedConditions.visibilityOf(depositButton));
            Thread.sleep(2000);
            depositButton.click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.visibilityOf(cancelPopupButton));
            cancelPopupButton.click();
            return true;
        } catch (Exception e) {
            System.out.println("Error while handling deposit popup: " + e.getMessage());
            return false;
        }
    }

    // Method to click the Withdraw button
    public void clickWithdraw() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(clickWithdrawButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", clickWithdrawButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickWithdrawButton);
        } catch (Exception e) {
            System.out.println("Failed to click Withdraw button: " + e.getMessage());
        }
    }

    // Method to check if the Withdraw popup is displayed
    public boolean isWithdrawPopupDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        try {
            wait.until(ExpectedConditions.visibilityOf(withdrawConfirmationDialog));
            return withdrawConfirmationDialog.isDisplayed();
        } catch (Exception e) {
            System.out.println("Withdraw popup/modal did not appear.");
            return false;
        }
    }

    // Method to enter the withdrawal amount
    public void enterAmountToWithdraw(String amount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(enterAmount));
        enterAmount.clear();
        enterAmount.sendKeys(amount);
    }

    // Method to get the entered withdrawal amount
    public String getEnteredAmount() {
        return enterAmount.getAttribute("value");
    }

    // Method to confirm the withdrawal after entering the amount
    public void confirmWithdraw() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(confirmWithdrawBtn));
        confirmWithdrawBtn.click();
    }
}
