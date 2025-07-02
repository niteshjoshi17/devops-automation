package com.forus.qa.util;

import com.forus.qa.pages.LoginPage;
import static com.forus.qa.base.TestBase.driver;
import static com.forus.qa.base.TestBase.prop;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.ArrayList;


import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestUtil {

    public static long PAGE_LOAD_TIMEOUT = 20;
    public static long IMPLICIT_WAIT = 10;

    // ✅ Use login page to log in with email/password + OTP
    public static void loginToApplication() throws InterruptedException {
        LoginPage loginPage = new LoginPage();

        boolean loginSuccessful = loginPage.login(prop.getProperty("email"), prop.getProperty("password"));
        if (!loginSuccessful) {
            throw new AssertionError("Login failed");
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElements(loginPage.getOtpInputFields()));

        loginPage.handleOtp("010101");
    }

    // ✅ Screenshot method for failure cases
    public static String takeScreenshot(String screenshotName) {
        String dateName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String destPath = System.getProperty("user.dir") + "/screenshots/" + screenshotName + "_" + dateName + ".png";

        try {
            FileUtils.copyFile(src, new File(destPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destPath;
    }

    // ✅ Read withdraw amounts from Excel
    public static List<String> readWithdrawAmounts(String filePath, String sheetName) {
        List<String> amounts = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {  // skip header row
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                amounts.add(cell.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return amounts;
    }
}
