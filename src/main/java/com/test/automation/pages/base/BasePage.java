package com.test.automation.pages.base;

import com.test.automation.factory.DriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base Page class that all page objects should extend
 * Contains common methods used across all pages
 */
@Slf4j
public class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor js;
    
    /**
     * Base constructor for all page objects
     * Initializes WebDriver and common utilities
     */
    public BasePage() {
        this.driver = DriverFactory.getDriver();
        if (this.driver == null) {
            throw new IllegalStateException("WebDriver is null. Make sure it's properly initialized before creating page objects.");
        }
        
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
        
        log.debug("BasePage initialized with driver: {}", driver);
    }
    
    /**
     * Waits for an element to be visible
     * 
     * @param element WebElement to wait for
     * @return The WebElement once visible
     */
    protected WebElement waitForElementVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            log.error("Element not visible after timeout: {}", element);
            takeScreenshot();
            throw e;
        }
    }
    
    /**
     * Waits for an element to be clickable
     * 
     * @param element WebElement to wait for
     * @return The WebElement once clickable
     */
    protected WebElement waitForElementClickable(WebElement element) {
        try {
            // Scroll to element before checking if it's clickable
            scrollToElement(element);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            log.error("Element not clickable after timeout: {}", element);
            takeScreenshot();
            throw e;
        } catch (StaleElementReferenceException e) {
            log.warn("Stale element reference when waiting for element to be clickable, retrying...");
            // Removed Thread.sleep before retry
            return wait.until(ExpectedConditions.elementToBeClickable(element));
            // No InterruptedException handling needed here anymore
        }
    }
    
    /**
     * Safely clicks on an element.
     * Tries standard click first, falls back to JavaScript click if intercepted.
     * 
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        WebElement clickableElement = null;
        try {
            clickableElement = waitForElementClickable(element);
            clickableElement.click(); // Try standard click first
        } catch (ElementClickInterceptedException e) {
            log.warn("Standard click intercepted for element: {}. Retrying with JavaScript click.", element, e);
            try {
                // Ensure element reference is still valid before JS click
                if (clickableElement == null) {
                    // If the wait failed initially (though unlikely as exception was interception)
                    clickableElement = waitForElementClickable(element); 
                }
                js.executeScript("arguments[0].click();", clickableElement);
            } catch (Exception jsEx) {
                log.error("JavaScript click also failed for element: {}", element, jsEx);
                takeScreenshot(); // Screenshot on final failure
                throw jsEx; // Re-throw the exception from the JS click attempt
            }
        } catch (StaleElementReferenceException e) {
            log.warn("Stale element reference during click for element: {}. Re-locating and retrying click once.", element, e);
            try {
                // Re-wait and try standard click again
                clickableElement = waitForElementClickable(element); 
                clickableElement.click(); 
            } catch (Exception retryEx) {
                 log.error("Click failed even after retry for stale element: {}", element, retryEx);
                 takeScreenshot();
                 // Decide whether to attempt JS click after stale retry failure
                 if (retryEx instanceof ElementClickInterceptedException) {
                     log.warn("Retry click intercepted, trying JS click as last resort.");
                     try {
                        js.executeScript("arguments[0].click();", clickableElement); // Use potentially refreshed clickableElement
                     } catch (Exception jsRetryEx) {
                         log.error("JS click also failed after stale element retry for element: {}", element, jsRetryEx);
                         throw jsRetryEx;
                     }
                 } else {
                    throw retryEx; // Re-throw the exception from the retry attempt
                 }
            }
        } catch (Exception e) {
            log.error("Failed to click element: {}", element, e);
            takeScreenshot();
            throw e; // Re-throw other unexpected exceptions
        }
    }
    
    /**
     * Safely enters text into an input field
     * 
     * @param element WebElement to enter text into
     * @param text Text to enter
     */
    protected void type(WebElement element, String text) {
        try {
            WebElement visibleElement = waitForElementVisible(element);
            visibleElement.clear();
            visibleElement.sendKeys(text);
        } catch (StaleElementReferenceException e) {
            log.warn("StaleElementReferenceException occurred, retrying...", e);
            // Wait and retry
            try {
                Thread.sleep(500);
                WebElement visibleElement = waitForElementVisible(element);
                visibleElement.clear();
                visibleElement.sendKeys(text);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.error("Thread was interrupted", ex);
            } catch (Exception ex) {
                log.error("Failed to type text after retry", ex);
                throw ex;
            }
        }
    }
    
    /**
     * Gets text from an element
     * 
     * @param element WebElement to get text from
     * @return The text of the element
     */
    protected String getText(WebElement element) {
        return waitForElementVisible(element).getText();
    }
    
    /**
     * Checks if an element is displayed
     * 
     * @param element WebElement to check
     * @return true if the element is displayed, false otherwise
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
    
    /**
     * Selects an option from a dropdown by visible text
     * 
     * @param element Select WebElement
     * @param optionText Text of the option to select
     */
    protected void selectByVisibleText(WebElement element, String optionText) {
        new Select(waitForElementVisible(element)).selectByVisibleText(optionText);
    }
    
    /**
     * Selects an option from a dropdown by value
     * 
     * @param element Select WebElement
     * @param value Value of the option to select
     */
    protected void selectByValue(WebElement element, String value) {
        new Select(waitForElementVisible(element)).selectByValue(value);
    }
    
    /**
     * Scrolls to an element using JavaScript
     * 
     * @param element WebElement to scroll to
     */
    protected void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        try {
            Thread.sleep(300); // Small pause after scrolling
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Thread was interrupted during scroll pause", e);
        }
    }
    
    /**
     * Highlights an element (for debugging purposes)
     * 
     * @param element WebElement to highlight
     */
    protected void highlightElement(WebElement element) {
        String originalStyle = element.getAttribute("style");
        js.executeScript(
                "arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
                element
        );
        
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Thread was interrupted during highlight pause", e);
        }
        
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, originalStyle);
    }
    
    /**
     * Takes a screenshot of the current page
     * 
     * @return Screenshot as byte array
     */
    public byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    
    /**
     * Finds child elements within a parent element
     * 
     * @param parent Parent WebElement
     * @param childLocator By locator for child elements
     * @return List of child WebElements
     */
    protected List<WebElement> findChildElements(WebElement parent, By childLocator) {
        return parent.findElements(childLocator);
    }
} 