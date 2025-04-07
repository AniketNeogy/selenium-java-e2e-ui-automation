package com.test.automation.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer for failed tests
 * This class will retry a failed test a specified number of times
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger log = LoggerFactory.getLogger(RetryAnalyzer.class);
    
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1; // Retry failed tests once
    
    /**
     * This method is called when a test fails
     * 
     * @param result Result of test method execution
     * @return true if test should be retried, false otherwise
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            log.info("Retrying test: {} for {} time", result.getName(), retryCount+1);
            retryCount++;
            return true;
        }
        return false;
    }
} 