package com.test.automation.tests;

import com.test.automation.config.FrameworkConfig;
import com.test.automation.factory.DriverFactory;
import com.test.automation.listeners.TestListener;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base Test class that all test classes should extend
 * Handles WebDriver initialization and cleanup
 */
@Listeners(TestListener.class)
@Slf4j
public class BaseTest {
    
    protected final FrameworkConfig config = FrameworkConfig.getInstance();
    
    /**
     * Gets the WebDriver instance for the current thread
     * 
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
    
    /**
     * Initializes WebDriver before each test method
     * 
     * @param browser Browser to use (optional, can be provided through testng.xml)
     */
    @BeforeMethod
    @Parameters(value = {"browser"})
    public void setup(@Optional String browser) {
        if (browser != null && !browser.isEmpty()) {
            System.setProperty("browser", browser);
        }
        
        log.info("Initializing WebDriver for test execution");
        // This will initialize the driver if it doesn't exist
        getDriver();
        navigateToBaseUrl();
    }
    
    /**
     * Navigates to the base URL specified in the configuration
     */
    @Step("Navigating to base URL")
    private void navigateToBaseUrl() {
        String baseUrl = config.getBaseUrl();
        log.info("Navigating to base URL: {}", baseUrl);
        getDriver().get(baseUrl);
    }
    
    /**
     * Quits WebDriver after each test method
     */
    @AfterMethod
    public void tearDown() {
        log.info("Tearing down WebDriver after test execution");
        DriverFactory.quitDriver();
    }
} 