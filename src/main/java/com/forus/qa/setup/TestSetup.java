package com.forus.qa.setup;

import com.forus.qa.base.TestBase;
import com.forus.qa.util.TestUtil;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class TestSetup extends TestBase {

    @BeforeSuite
    public void globalSetup() throws InterruptedException {
        extent = TestBase.initExtentReport();   // Initialize Extent Report once
        initialization();                       // Launch browser once
        TestUtil.loginToApplication();          // Login once
    }

    @AfterSuite
    public void globalTeardown() {
        if (extent != null) {
            extent.flush();                     // Finalize Extent Report
            System.out.println("Extent report flushed.");
        }

        if (driver != null) {
        //    driver.quit();                      // Quit browser after all tests
            System.out.println("Browser closed.");
        }
    }
}
