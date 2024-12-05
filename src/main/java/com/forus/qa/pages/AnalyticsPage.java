package com.forus.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AnalyticsPage {

    WebDriver driver;
    WebDriverWait wait;

    // Locators for Analytics elements
    @FindBy(xpath = "//a[contains(@class, 'chakra-link') and .//p[text()='Analytics']]")
    private WebElement analyticsLink;

    @FindBy(xpath = "(//button[text()='Invest'])[1]")  // Ensure this XPath is specific enough
    private WebElement investButton;

    // Constructor to initialize the page elements using PageFactory
    public AnalyticsPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));  // Create a reusable WebDriverWait
        PageFactory.initElements(driver, this);
    }

    // Method to click on the Analytics link
    public void clickOnAnalyticsLink() {
        wait.until(ExpectedConditions.elementToBeClickable(analyticsLink));
        analyticsLink.click();
    }

    // Method to click on the Invest button
    public void clickOnInvest() {
        wait.until(ExpectedConditions.elementToBeClickable(investButton));
        investButton.click();
    }
}
