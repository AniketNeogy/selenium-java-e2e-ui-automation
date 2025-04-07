package com.test.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.test.automation.factory.DriverFactory;
import com.test.automation.utils.ExtentManager;
import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TestNG Listener for test events and reporting
 */
@Slf4j
public class TestListener implements ITestListener {
    
    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ConcurrentHashMap<String, ExtentTest> testMap = new ConcurrentHashMap<>();
    
    @Override
    public void onStart(ITestContext context) {
        log.info("Test Suite Started: {}", context.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        log.info("Test Started: {}", result.getName());
        
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();
        String description = result.getMethod().getDescription();
        
        // For parallel execution - using thread safe map to store test instances
        ExtentTest test = extent.createTest(className + " - " + testName, description);
        testMap.put(getTestMapKey(result), test);
        
        // Adding test attributes for better categorization
        test.assignCategory(className);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test Passed: {}", result.getName());
        getTest(result).log(Status.PASS, MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test Failed: {}", result.getName(), result.getThrowable());
        
        ExtentTest test = getTest(result);
        test.log(Status.FAIL, MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
        test.log(Status.FAIL, result.getThrowable());
        
        // Capture screenshot on failure
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                String base64Screenshot = Base64.getEncoder().encodeToString(screenshot);
                
                // Add screenshot to Extent Report using MediaEntityBuilder
                test.fail("Screenshot on failure:", 
                          MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                
                // Add screenshot to Allure Report
                saveScreenshot(screenshot);
            } catch (Exception e) {
                log.error("Failed to capture or attach screenshot", e);
                test.log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
            }
        } else {
             log.warn("WebDriver was null, could not capture screenshot for failed test: {}", result.getName());
             test.log(Status.WARNING, "WebDriver instance was null, screenshot not captured.");
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("Test Skipped: {}", result.getName());
        getTest(result).log(Status.SKIP, MarkupHelper.createLabel("Test Skipped", ExtentColor.YELLOW));
    }
    
    @Override
    public void onFinish(ITestContext context) {
        log.info("Test Suite Finished: {}", context.getName());
        
        // Write the Extent Report
        extent.flush();
        
        // Ensure all drivers are cleaned up - REMOVED from here, handled by @AfterMethod
        // DriverFactory.quitDriver(); 
    }
    
    /**
     * Gets the ExtentTest instance for the current test
     * 
     * @param result ITestResult of the current test
     * @return The ExtentTest instance
     */
    private ExtentTest getTest(ITestResult result) {
        return testMap.get(getTestMapKey(result));
    }
    
    /**
     * Creates a unique key for the test map based on the test method and thread ID
     * 
     * @param result ITestResult of the test
     * @return Unique key for the test
     */
    private String getTestMapKey(ITestResult result) {
        return result.getMethod().getMethodName() + "_" + Thread.currentThread().getId();
    }
    
    /**
     * Saves screenshot for Allure reporting
     * 
     * @param screenshot Screenshot as byte array
     * @return The screenshot byte array
     */
    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] saveScreenshot(byte[] screenshot) {
        return screenshot;
    }
} 