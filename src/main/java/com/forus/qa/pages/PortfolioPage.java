package com.forus.qa.pages;

import com.forus.qa.base.TestBase;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PortfolioPage extends TestBase {

    // Create a wait object
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    JavascriptExecutor js = (JavascriptExecutor) driver;

    public PortfolioPage() {
        PageFactory.initElements(driver, this);
    }


    @FindBy(xpath = "(//div[@class='chakra-stack css-en-owjkmg'])[3]")
    WebElement portfolioLink;

    @FindBy(xpath = "//button[contains(text(),'Active Campaigns')]")
    WebElement activeCampaignsTab;

    @FindBy(xpath = "//button[contains(text(),'Archived Campaigns')]")
    WebElement archivedCampaignsTab;

    @FindBy(xpath = "(//p[contains(text(),'OEN-201')])[3]")
    WebElement archivedLoanEntry;

    @FindBy(xpath = "//button[@aria-label='Search database']")
    WebElement searchIconButton;

    @FindBy(xpath = "//button[@class='chakra-button css-en-g1qvx0']")
    WebElement okButton;

    @FindBy(xpath = "//span[contains(text(),'CANCELLED')]")
    WebElement cancelled;

    @FindBy(xpath = "(//p[contains(text(),'Status')])[1]")
    WebElement status;


    // Switch tabs between Active and Archived campaigns
    public void switchTabsToCheckDetails() {

        wait.until(ExpectedConditions.elementToBeClickable(portfolioLink)).click();

        // Click on Active Campaigns tab
        wait.until(ExpectedConditions.elementToBeClickable(activeCampaignsTab)).click();

        // Click on Archived Campaigns tab
        wait.until(ExpectedConditions.elementToBeClickable(archivedCampaignsTab)).click();
    }

    // Open an archived loan, click the info icon, confirm details, and scroll
    public void openArchivedLoanDetails() throws InterruptedException {

        // Click on specific loan entry in Archived Campaigns
        wait.until(ExpectedConditions.elementToBeClickable(archivedLoanEntry)).click();

        // Scroll to and click the info (i) icon
        js.executeScript("arguments[0].scrollIntoView(true);", searchIconButton);
        wait.until(ExpectedConditions.elementToBeClickable(searchIconButton)).click();

        // Click OK button in the pop-up/modal
        wait.until(ExpectedConditions.elementToBeClickable(okButton)).click();

        // Optional: Scroll slightly up after action
        js.executeScript("window.scrollTo(679, 148);");
        Thread.sleep(2000); // Only if needed to wait for animation/UI reset
    }

    public String getCancelledStatusText() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(status));
        wait.until(ExpectedConditions.visibilityOf(cancelled));

        return cancelled.getText().trim();
    }

    @FindBy(xpath = "(//p[@class='chakra-text css-en-108semt'])[1]")
    WebElement showing9from9data;

    public int extractRecordCountFromText() {
        driver.navigate().back();

        // Click on Active Campaigns tab
        wait.until(ExpectedConditions.elementToBeClickable(activeCampaignsTab)).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(showing9from9data));

        String text = showing9from9data.getText().trim();  // e.g., "Showing 9 from 9 data"
        System.out.println("Text: " + text);

        Pattern pattern = Pattern.compile("Showing\\s+(\\d+)\\s+from");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new AssertionError("Couldn't extract number from text: " + text);
        }
    }

    @FindBy(xpath = "//p[text()='OEN-202-581']")
    WebElement activeloan202;

    @FindBy(xpath = "//span[text()='APPROVED']")
    WebElement approvedstatus;

    @FindBy(xpath = "//button[text()='Cancel Investment']")
    WebElement cancelinvestmentbtn;

    @FindBy(xpath = "//button[text()='Cancel']")
    WebElement cancelpopup;

    @FindBy(xpath = "//button[text()='Confirm']")
    WebElement confirmpopup;

    @FindBy(xpath = "(//div[contains(@class, 'chakra-alert__desc') and contains(text(), 'Investment exceeded 48 hours')])[1]")
    WebElement errormsgforcancel;


    public String activeloandetailsofuser() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 1: Click on a particular loan in Active Campaigns
        wait.until(ExpectedConditions.elementToBeClickable(activeloan202)).click();
        System.out.println("Clicked on loan with ID: OEN-202-581");

        // Step 2: Click on Cancel Investment button
        wait.until(ExpectedConditions.elementToBeClickable(cancelinvestmentbtn)).click();
        System.out.println("Clicked on 'Cancel Investment' button");


        // Step 3: Verify Cancel and Confirm popups are visible
        if (cancelpopup.isDisplayed()) {
            System.out.println("Cancel popup is displayed");
        } else {
            System.out.println("Cancel popup is NOT displayed");
        }

        if (confirmpopup.isDisplayed()) {
            System.out.println("Confirm popup is displayed");
        } else {
            System.out.println("Confirm popup is NOT displayed");

        }
        // Step 4: Check if the 48-hour cancellation error toast appears
        try {
            confirmpopup.click();
            wait.until(ExpectedConditions.visibilityOf(errormsgforcancel));
            String errorText = errormsgforcancel.getText().trim();
            System.out.println("Error message captured: " + errorText);
            return errorText;  // Exit early if error is shown
        } catch (Exception e) {
            System.out.println("No 48-hour error toast found. Proceeding to verify popups.");
        }


        // Step 5: Verify status is Approved
        wait.until(ExpectedConditions.visibilityOf(status));
        System.out.println("Status element is visible");

        wait.until(ExpectedConditions.visibilityOf(approvedstatus));
        System.out.println("Approved status is visible");

        String approvedText = approvedstatus.getText().trim();
        System.out.println("Final status text captured: " + approvedText);

        return approvedText;
    }















}




