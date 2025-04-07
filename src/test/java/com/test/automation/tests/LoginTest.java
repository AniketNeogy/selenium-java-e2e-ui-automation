package com.test.automation.tests;

import com.test.automation.constants.SauceConstants;
import com.test.automation.pages.InventoryPage;
import com.test.automation.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class for login functionality
 */
@Slf4j
@Feature("Authentication")
public class LoginTest extends BaseTest {
    
    /**
     * Test data provider for valid login credentials
     * 
     * @return Object array of username and password
     */
    @DataProvider(name = "validLoginData")
    public Object[][] getValidLoginData() {
        return new Object[][] {
                {SauceConstants.STANDARD_USER, SauceConstants.STANDARD_PASSWORD},
                {SauceConstants.PROBLEM_USER, SauceConstants.STANDARD_PASSWORD},
                {SauceConstants.PERFORMANCE_GLITCH_USER, SauceConstants.STANDARD_PASSWORD},
                {SauceConstants.ERROR_USER, SauceConstants.STANDARD_PASSWORD},
                {SauceConstants.VISUAL_USER, SauceConstants.STANDARD_PASSWORD}
        };
    }
    
    /**
     * Test data provider for invalid login credentials
     * 
     * @return Object array of username, password, and expected error message
     */
    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        return new Object[][] {
                {"invalid_user", SauceConstants.STANDARD_PASSWORD, SauceConstants.ERROR_INVALID_CREDENTIALS},
                {SauceConstants.STANDARD_USER, "invalid_password", SauceConstants.ERROR_INVALID_CREDENTIALS},
                {"", SauceConstants.STANDARD_PASSWORD, SauceConstants.ERROR_USERNAME_REQUIRED},
                {SauceConstants.STANDARD_USER, "", SauceConstants.ERROR_PASSWORD_REQUIRED}
        };
    }
    
    /**
     * Test data provider for locked out user
     * 
     * @return Object array of username, password, and expected error message
     */
    @DataProvider(name = "lockedOutUserData")
    public Object[][] getLockedOutUserData() {
        return new Object[][] {
                {SauceConstants.LOCKED_OUT_USER, SauceConstants.STANDARD_PASSWORD, SauceConstants.ERROR_LOCKED_OUT}
        };
    }
    
    /**
     * Positive test for login with valid credentials
     * 
     * @param username Username
     * @param password Password
     */
    @Test(dataProvider = "validLoginData")
    @Description("Verify that user can login with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Valid Login")
    public void testValidLogin(String username, String password) {
        log.info("Testing valid login with username: {}", username);
        
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
        
        // Verify we've navigated to the inventory page
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(SauceConstants.INVENTORY_PAGE), 
                "Expected to navigate to inventory page after login, but URL was: " + currentUrl);
        
        // Verify the inventory page is loaded
        InventoryPage inventoryPage = new InventoryPage();
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page is not loaded after successful login");
    }
    
    /**
     * Negative test for login with invalid credentials
     * 
     * @param username Username
     * @param password Password
     * @param expectedError Expected error message
     */
    @Test(dataProvider = "invalidLoginData")
    @Description("Verify that user cannot login with invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login")
    public void testInvalidLogin(String username, String password, String expectedError) {
        log.info("Testing invalid login with username: {}, expected error: {}", username, expectedError);
        
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
        
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
                "Error message is not displayed for invalid credentials");
        Assert.assertEquals(loginPage.getErrorMessageText(), expectedError,
                "Incorrect error message displayed");
    }
    
    /**
     * Negative test for login with locked out user
     * 
     * @param username Username
     * @param password Password
     * @param expectedError Expected error message
     */
    @Test(dataProvider = "lockedOutUserData")
    @Description("Verify that locked out user cannot login")
    @Severity(SeverityLevel.NORMAL)
    @Story("Locked Out User")
    public void testLockedOutUser(String username, String password, String expectedError) {
        log.info("Testing login with locked out user: {}", username);
        
        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);
        
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
                "Error message is not displayed for locked out user");
        Assert.assertEquals(loginPage.getErrorMessageText(), expectedError,
                "Incorrect error message displayed for locked out user");
    }
} 