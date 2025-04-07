package com.test.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.test.automation.config.FrameworkConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manager class for ExtentReports initialization
 */
@Slf4j
public class ExtentManager {
    
    private static final String REPORT_DIR = "test-output/extent-reports";
    private static ExtentReports extent;
    
    private ExtentManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Gets the ExtentReports instance
     * 
     * @return ExtentReports instance
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }
    
    /**
     * Creates a new ExtentReports instance
     * 
     * @return ExtentReports instance
     */
    private static ExtentReports createInstance() {
        // Create report directory if it doesn't exist
        File reportDir = new File(REPORT_DIR);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        
        // Create unique report file name with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportFilePath = REPORT_DIR + "/TestReport_" + timestamp + ".html";
        
        // Configure the report
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("UI Automation Test Results");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setEncoding("UTF-8");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        
        // Create and configure ExtentReports
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Add system info
        FrameworkConfig config = FrameworkConfig.getInstance();
        extent.setSystemInfo("Browser", config.getBrowser());
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Environment", config.getBaseUrl());
        
        log.info("Extent Report initialized at: {}", reportFilePath);
        
        return extent;
    }
} 