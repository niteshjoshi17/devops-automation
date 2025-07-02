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

    @FindBy(xpath = "//div[@class='css-en-1m8iww1']")
    WebElement eyeBtn;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement loginBtn;

    @FindBy(xpath = "//img[@src='/logo.svg']")
    WebElement forusLogo;

    @FindBy(xpath = "//button[@type='button'][1]")
    WebElement changeLanguageToEnglish;

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

    public boolean login(String em, String pwd) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Step 1: Wait and enter email
            wait.until(ExpectedConditions.visibilityOf(email));
            email.sendKeys(em);

            // Step 2: Scroll to password and enter it
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", password);
            password.sendKeys(pwd);

            // Optional: Click eye button to show password
            eyeBtn.click();
            Thread.sleep(1000);

            // Step 3: Click login button (try normal click first)
            wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
            loginBtn.click();
            return true;

        } catch (Exception e) {
            System.out.println("Login click failed, trying JavaScript: " + e.getMessage());

            // Step 4: Retry with JavaScript click
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", loginBtn);
                return true;
            } catch (Exception jsEx) {
                System.out.println("JavaScript click also failed: " + jsEx.getMessage());
                return false;
            }
        }


}

    @FindBy(xpath = "//input[@aria-label='Please enter your pin code']")
    private List<WebElement> otpInputFields;

    public List<WebElement> getOtpInputFields() {
        return otpInputFields;
    }



    // Handle OTP input after login
    public void handleOtp(String otp) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until OTP input fields are visible
        wait.until(ExpectedConditions.visibilityOfAllElements(otpInputFields));

        // Check for exactly 6 OTP fields
        if (otpInputFields.size() == 6) {
            for (int i = 0; i < otp.length(); i++) {
                otpInputFields.get(i).sendKeys(String.valueOf(otp.charAt(i)));
            }
            System.out.println("OTP entered successfully!");
        } else {
            System.out.println("Error: The number of OTP input fields is not correct.");
        }
    }

}
