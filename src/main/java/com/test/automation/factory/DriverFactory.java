package com.test.automation.factory;

import com.test.automation.config.FrameworkConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Factory class for managing WebDriver instances
 */
@Slf4j
public class DriverFactory {
    
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();
    private static final FrameworkConfig config = FrameworkConfig.getInstance();
    
    static {
        // Add shutdown hook to clean up any remaining driver instances
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Running shutdown hook to clean up WebDriver instances");
            quitAllDrivers();
        }));
    }
    
    /**
     * Gets the current WebDriver instance
     * Creates a new instance if none exists
     * 
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            initializeDriver();
        }
        return driver.get();
    }
    
    /**
     * Gets the current WebDriverWait instance
     * Creates a new instance if none exists
     * 
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWait() {
        if (wait.get() == null) {
            wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(10)));
        }
        return wait.get();
    }
    
    /**
     * Initializes a new WebDriver instance based on configuration
     */
    private static void initializeDriver() {
        try {
            WebDriver webDriver;
            
            if (config.isUseGrid()) {
                webDriver = createRemoteDriver();
            } else {
                webDriver = createLocalDriver();
            }
            
            // Set implicit wait
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            // Set page load timeout
            webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            
            // Set script timeout
            webDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
            
            // Maximize window
            webDriver.manage().window().maximize();
            
            driver.set(webDriver);
            log.info("WebDriver initialized successfully");
            
        } catch (Exception e) {
            log.error("Failed to initialize WebDriver", e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }
    
    /**
     * Creates a remote WebDriver instance
     * 
     * @return RemoteWebDriver instance
     */
    private static WebDriver createRemoteDriver() throws Exception {
        String browser = config.getBrowser().toLowerCase();
        String gridUrl = config.getGridUrl();
        
        if (gridUrl == null || gridUrl.isEmpty()) {
            throw new IllegalStateException("Grid URL is not configured for remote execution");
        }
        
        if (browser.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            return new RemoteWebDriver(new URL(gridUrl), options);
        } else if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            return new RemoteWebDriver(new URL(gridUrl), options);
        } else {
            throw new IllegalArgumentException("Unsupported browser for remote execution: " + browser);
        }
    }
    
    /**
     * Creates a local WebDriver instance
     * 
     * @return WebDriver instance
     */
    private static WebDriver createLocalDriver() {
        String browser = config.getBrowser().toLowerCase();
        // Read the runMode system property (defaulting to headless if not 'headed')
        String runMode = System.getProperty("runMode", "headless"); 
        boolean headless = !"headed".equalsIgnoreCase(runMode);
        
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");

                // Add options to disable password manager
                java.util.Map<String, Object> prefs = new java.util.HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                chromeOptions.setExperimentalOption("prefs", prefs);
                
                // Disable infobars 
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);

                // Conditionally add headless argument
                if (headless) {
                    log.info("Configuring Chrome to run in headless mode.");
                    chromeOptions.addArguments("--headless=new");
                } else {
                    log.info("Configuring Chrome to run in headed mode (runMode=headed detected).");
                }

                return new ChromeDriver(chromeOptions);
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                // Conditionally add headless argument for Firefox
                if (headless) {
                    log.info("Configuring Firefox to run in headless mode.");
                    firefoxOptions.addArguments("--headless");
                } else {
                    log.info("Configuring Firefox to run in headed mode (runMode=headed detected).");
                }
                return new FirefoxDriver(firefoxOptions);
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                // Conditionally add headless argument for Edge (removing previous hardcoding)
                 if (headless) {
                    log.info("Configuring Edge to run in headless mode.");
                    edgeOptions.addArguments("--headless=new");
                } else {
                    log.info("Configuring Edge to run in headed mode (runMode=headed detected).");
                }
                return new EdgeDriver(edgeOptions);
                
            case "safari":
                // Safari doesn't need WebDriverManager
                return new SafariDriver();
                
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
    
    /**
     * Quits all WebDriver instances and cleans up ThreadLocal storage
     */
    private static void quitAllDrivers() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                currentDriver.quit();
            } catch (Exception e) {
                log.error("Error while quitting WebDriver during shutdown", e);
            } finally {
                driver.remove();
                wait.remove();
            }
        }
    }
    
    /**
     * Quits the current WebDriver instance and removes it from ThreadLocal
     */
    public static void quitDriver() {
        try {
            WebDriver currentDriver = driver.get();
            if (currentDriver != null) {
                // Force close all windows first
                try {
                    for (String handle : currentDriver.getWindowHandles()) {
                        currentDriver.switchTo().window(handle).close();
                    }
                } catch (Exception e) {
                    log.warn("Error closing browser windows", e);
                }
                
                // Then quit the driver
                currentDriver.quit();
                log.info("WebDriver quit successfully");
            }
        } catch (Exception e) {
            log.error("Error while quitting WebDriver", e);
        } finally {
            // Always clean up ThreadLocal storage
            driver.remove();
            wait.remove();
        }
    }
} 