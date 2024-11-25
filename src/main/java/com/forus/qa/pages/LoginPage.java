package com.forus.qa.pages;

import com.forus.qa.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage extends TestBase {

    // Page Factory -- Object Repository:
    @FindBy(name = "email")
    WebElement email;

    @FindBy(xpath = "//input[@type='password']")
    WebElement password;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement loginBtn;

    @FindBy(xpath = "//img[@src='/logo.svg']")
    WebElement forusLogo;

    @FindBy(xpath = "//button[@type='button'][1]")
    WebElement changeLanguageToEnglish;

    // No need for @FindBy here for OTP because we will dynamically get all input fields
    // @FindBy(xpath = "//input[@aria-label='Please enter your pin code']")
    // WebElement enterOTP;

    // Initializing Page Objects:
    public LoginPage() {


        PageFactory.initElements(driver, this);
        ensureLanguageIsEnglish();
    }

    // Ensure language is set to English
    private void ensureLanguageIsEnglish() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // Check if the language change button is displayed and clickable
            if (changeLanguageToEnglish.isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(changeLanguageToEnglish));
                changeLanguageToEnglish.click();
                System.out.println("Language changed to English successfully.");
            }
        } catch (Exception e) {
            System.out.println("Language is already set to English or button not present: " + e.getMessage());
        }
    }

    // Actions:
    public String validateLoginPageTitle() {
        return driver.getTitle();
    }

    public boolean validateForusLogo() {
        return forusLogo.isDisplayed();
    }

    public boolean login(String em, String pwd) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Change language from Arabic to English:
       if (changeLanguageToEnglish.isDisplayed()) {
           wait.until(ExpectedConditions.elementToBeClickable(changeLanguageToEnglish)) ;
           changeLanguageToEnglish.click();
       }


        // Wait for the email field to be visible and enter email
        wait.until(ExpectedConditions.visibilityOf(email));
        email.sendKeys(em);

        // Scroll to the password field and enter password
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", password);
        password.sendKeys(pwd);

        // Click the login button
        return loginBtnClick();
    }

    public boolean loginBtnClick() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Wait until button is clickable
            wait.until(ExpectedConditions.elementToBeClickable(loginBtn));

            // Ensure the button is visible and click it
            loginBtn.click();
            return true;
        } catch (Exception e) {
            System.out.println("Click failed. Using JavaScript: " + e.getMessage());

            // Use JavaScript:
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", loginBtn);
                return true;
            } catch (Exception jsException) {
                System.out.println("JavaScript click failed: " + jsException.getMessage());
                return false;
            }
        }
    }

    // Handle OTP input after login
    public void handleOtp(String otp) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for OTP input fields to be visible (find all input fields with the same aria-label)
        wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("//input[@aria-label='Please enter your pin code']"))));

        // Find all OTP input fields (this returns a List of WebElements)
        List<WebElement> otpInputFields = driver.findElements(By.xpath("//input[@aria-label='Please enter your pin code']"));

        // We have exactly 6 OTP input fields
        if (otpInputFields.size() == 6) {
            // Loop through each digit of the OTP (fixed 6 digits)
            for (int i = 0; i < otp.length(); i++) {
                otpInputFields.get(i).sendKeys(String.valueOf(otp.charAt(i)));
            }
            System.out.println("OTP entered successfully!");
        } else {
            System.out.println("Error: The number of OTP input fields is not correct.");
        }
    }
}
