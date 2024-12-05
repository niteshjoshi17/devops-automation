package com.forus.qa.testcases;

import com.forus.qa.base.TestBase;
import com.forus.qa.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginPageTest extends TestBase {
    LoginPage loginPage ;

    public LoginPageTest () {
        super();
    }


    @BeforeMethod
    public void setup() {
        initialization();
        loginPage = new LoginPage();
    }

//    @Test(priority = 1)
//    public void loginPageTitleTest() {
//        String title = loginPage.validateLoginPageTitle();
//        Assert.assertEquals(title ,"Forus | Login");
//    }

//    @Test(priority = 1)
//    public void forusLogoImageTest() {
//        boolean flag = loginPage.validateForusLogo();
//        Assert.assertTrue(flag);
//    }

    @Test(priority = 1)
    public void loginTest() {

        System.out.println("Language switched to English, if necessary.");

        // Step 1: Login with email and password
        loginPage.login(prop.getProperty("email"), prop.getProperty("password"));

        // Step 2: Assert the login button is clicked successfully
        Assert.assertTrue(loginPage.loginBtnClick(), "Login button click failed!");

        // Step 3: Wait for the OTP input to appear (after login click)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@aria-label='Please enter your pin code']")));

        // Step 4: Handle OTP (assuming OTP is fixed as "010101")
        String otp = "010101";  // Fixed OTP
        loginPage.handleOtp(otp);

//        // Step 5: Check the URL or another condition to verify login success
//        String currentUrl = driver.getCurrentUrl();
//        Assert.assertTrue(currentUrl.contains("dashboard"), "Login failed! URL does not contain 'dashboard'.");
    }



    //    @Test(priority = 4)
//    public void clickLoginBtn() {
//       boolean btn = loginPage.loginBtnClick();
//        Assert.assertTrue(btn);
//    }
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

}