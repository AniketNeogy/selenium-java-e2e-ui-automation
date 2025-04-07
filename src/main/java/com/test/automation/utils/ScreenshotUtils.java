package com.test.automation.utils;

import com.test.automation.config.FrameworkConfig;
import com.test.automation.factory.DriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for taking and saving screenshots
 */
@Slf4j
public class ScreenshotUtils {
    
    private static final FrameworkConfig config = FrameworkConfig.getInstance();
    
    private ScreenshotUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Takes a screenshot and returns it as a byte array
     * 
     * @return Screenshot as byte array
     */
    public static byte[] takeScreenshot() {
        log.debug("Taking screenshot");
        WebDriver driver = DriverFactory.getDriver();
        return driver != null ? ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES) : null;
    }
    
    /**
     * Takes a screenshot and saves it to a file
     * 
     * @param fileName Base name for the screenshot file (without extension)
     * @return Path to the saved screenshot file
     */
    public static String saveScreenshot(String fileName) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            log.warn("Cannot take screenshot, WebDriver is null");
            return null;
        }
        
        try {
            // Create screenshots directory if it doesn't exist
            Path screenshotDir = Paths.get(config.getScreenshotDir());
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }
            
            // Generate unique filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotFileName = fileName + "_" + timestamp + ".png";
            Path screenshotPath = screenshotDir.resolve(screenshotFileName);
            
            // Take and save screenshot
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(screenshotPath, screenshotBytes);
            
            log.info("Screenshot saved to: {}", screenshotPath);
            return screenshotPath.toString();
            
        } catch (IOException e) {
            log.error("Failed to save screenshot", e);
            return null;
        }
    }
} 