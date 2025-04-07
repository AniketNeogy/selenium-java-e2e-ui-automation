package com.test.automation.config;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for framework settings
 */
@Data
public class FrameworkConfig {
    private static final String CONFIG_FILE = "config.properties";
    
    private String baseUrl;
    private String browser;
    private int implicitWaitSeconds;
    private int pageLoadTimeoutSeconds;
    private int scriptTimeoutSeconds;
    private String downloadDirectory;
    private boolean headless;
    private String gridUrl;
    private boolean useGrid;
    private String screenshotDir;
    
    private static FrameworkConfig instance;
    
    private FrameworkConfig() {
        loadConfig();
    }
    
    public static synchronized FrameworkConfig getInstance() {
        if (instance == null) {
            instance = new FrameworkConfig();
        }
        return instance;
    }
    
    private void loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
            
            baseUrl = properties.getProperty("base.url", "https://www.example.com");
            browser = properties.getProperty("browser", "chrome");
            implicitWaitSeconds = Integer.parseInt(properties.getProperty("implicit.wait.seconds", "10"));
            pageLoadTimeoutSeconds = Integer.parseInt(properties.getProperty("page.load.timeout.seconds", "30"));
            scriptTimeoutSeconds = Integer.parseInt(properties.getProperty("script.timeout.seconds", "30"));
            downloadDirectory = properties.getProperty("download.dir", System.getProperty("user.dir") + "/downloads");
            headless = Boolean.parseBoolean(properties.getProperty("headless", "false"));
            gridUrl = properties.getProperty("grid.url", "http://localhost:4444/wd/hub");
            useGrid = Boolean.parseBoolean(properties.getProperty("use.grid", "false"));
            screenshotDir = properties.getProperty("screenshot.dir", System.getProperty("user.dir") + "/screenshots");
            
        } catch (IOException e) {
            System.err.println("Failed to load config file: " + e.getMessage());
            // Use defaults
        }
    }
} 