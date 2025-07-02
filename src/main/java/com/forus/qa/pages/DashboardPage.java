package com.forus.qa.pages;

import com.aventstack.extentreports.reporter.configuration.Theme;
import com.forus.qa.base.TestBase;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.forus.qa.base.TestBase.driver;

public class DashboardPage extends TestBase {

    // Use xpath instead of name
    @FindBy(xpath = "//div[@class='css-en-1sjopvp']")
    WebElement portfolioValueText;

    @FindBy(xpath = "//p[@class='chakra-text css-en-10wia40']")
    WebElement portfolioValue;


    @FindBy(xpath = "(//p[text()='Show all'])[1]")
    WebElement showAll;

    // Constructor to initialize elements
    public DashboardPage() {
        PageFactory.initElements(driver, this);
    }

    // Method to click Show All
    public void clickShowAll() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(showAll));
        showAll.click();
    }
        public String getPortfolioValue() {
            return portfolioValue.getText().trim();
        }
    @FindBy(xpath = "(//button[text()='Copy'])[1]")
    WebElement RiyadBank;

    @FindBy(xpath = "(//button[text()='Copy'])[2]")
    WebElement IBAN;

    @FindBy(xpath = "(//button[text()='Copy'])[3]")
    WebElement BeneficiaryName;

     public void validateCopyFunctionality() throws InterruptedException {
         // Check and click RiyadBank
       if (RiyadBank.isDisplayed()) {
           RiyadBank.click();
           Thread.sleep(2000);
           System.out.println("Clicked: Riyad bank Copy Button");
       } else {
           throw new AssertionError("RiyadBank Copy button not clickable");
       }
         // Check and click IBAN
         if (IBAN.isDisplayed()) {
             IBAN.click();
             Thread.sleep(2000);
             System.out.println("Clicked: IBAN Copy Button");
         } else {
             throw new AssertionError("IBAN Copy button not clickable");
         }

         // Check and click BeneficiaryName
         if (BeneficiaryName.isDisplayed()) {
             BeneficiaryName.click();
             Thread.sleep(2000);
             System.out.println("Clicked: BeneficiaryName Copy Button");
         } else {
             throw new AssertionError("BeneficiaryName Copy button not clickable");
         }

     }

    @FindBy(xpath = "(//p[text()='Show all'])[2]")
    WebElement LatesttransactionsShowAll;

    @FindBy(xpath = "//div[@class='css-en-i19jp7']")
    WebElement wallettext;

     public void checkLatesttranscatinsbyclickingonShowAll() throws InterruptedException {
         driver.navigate().back();
         LatesttransactionsShowAll.click();
         Thread.sleep(2000);
         if (wallettext.isDisplayed()) {
             ;
             System.out.println("Clicking on Show All button navigates to wallet");
         }
         else {
             System.out.println("Navigation is not working correctly");
         }

     }

     @FindBy(xpath = "(//button[@class='chakra-button css-en-ias91b'])[1]")
         WebElement clickonibtn;

    @FindBy(xpath = "//button[@class='chakra-button css-en-1w3p79a']")
    WebElement okayBtn;

    public void verifytranscationdetailbyclickingonPaidbtn() throws InterruptedException {
        driver.navigate().back();
        clickonibtn.click();
        Thread.sleep(1000);
        okayBtn.click();
        Thread.sleep(1000);

    }




    }


