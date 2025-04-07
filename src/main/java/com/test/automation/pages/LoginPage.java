package com.test.automation.pages;

import com.test.automation.pages.base.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for the login page
 */
@Slf4j
public class LoginPage extends BasePage {
    
    @FindBy(id = "user-name")
    private WebElement usernameInput;
    
    @FindBy(id = "password")
    private WebElement passwordInput;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;
    
    /**
     * Constructs a new LoginPage instance
     */
    public LoginPage() {
        super();
        log.info("LoginPage initialized");
    }
    
    /**
     * Enters username in the username field
     * 
     * @param username Username to enter
     * @return LoginPage instance for method chaining
     */
    @Step("Enter username: {0}")
    public LoginPage enterUsername(String username) {
        log.debug("Entering username: {}", username);
        type(usernameInput, username);
        return this;
    }
    
    /**
     * Enters password in the password field
     * 
     * @param password Password to enter
     * @return LoginPage instance for method chaining
     */
    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        log.debug("Entering password");
        type(passwordInput, password);
        return this;
    }
    
    /**
     * Clicks the login button
     * 
     * @return InventoryPage instance if login is successful, LoginPage otherwise
     */
    @Step("Click login button")
    public Object clickLoginButton() {
        log.debug("Clicking login button");
        click(loginButton);
        
        // Check if login was successful
        if (isErrorMessageDisplayed()) {
            return this; // Stay on the LoginPage if there's an error
        }
        
        // Return the inventory page which is the landing page after login
        log.info("Login successful, navigating to Inventory Page");
        return new InventoryPage();
    }
    
    /**
     * Performs login with the given credentials
     * 
     * @param username Username
     * @param password Password
     * @return InventoryPage instance if login is successful, LoginPage otherwise
     */
    @Step("Login with username: {0}")
    public Object login(String username, String password) {
        log.info("Logging in with username: {}", username);
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }
    
    /**
     * Checks if the error message is displayed
     * 
     * @return true if the error message is displayed, false otherwise
     */
    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
    
    /**
     * Gets the error message text
     * 
     * @return Error message text
     */
    @Step("Get error message text")
    public String getErrorMessageText() {
        return isErrorMessageDisplayed() ? getText(errorMessage) : "";
    }
    
    /**
     * Logs in as a standard user
     * 
     * @return InventoryPage instance
     */
    @Step("Login as standard user")
    public InventoryPage loginAsStandardUser() {
        log.info("Logging in as standard user");
        return (InventoryPage) login("standard_user", "secret_sauce");
    }
} 