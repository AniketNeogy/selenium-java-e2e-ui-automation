package com.test.automation.pages.components;

import com.test.automation.pages.CartPage;
import com.test.automation.pages.base.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Header component that appears on most pages after login
 */
@Slf4j
public class HeaderComponent extends BasePage {

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;
    
    @FindBy(id = "inventory_sidebar_link")
    private WebElement allItemsLink;
    
    @FindBy(id = "about_sidebar_link")
    private WebElement aboutLink;
    
    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;
    
    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppStateLink;
    
    @FindBy(id = "react-burger-cross-btn")
    private WebElement closeMenuButton;
    
    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;
    
    @FindBy(className = "shopping_cart_badge")
    private WebElement shoppingCartBadge;
    
    /**
     * Constructor for the HeaderComponent
     * 
     * @param driver WebDriver instance
     */
    public HeaderComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Opens the side menu
     */
    @Step("Open side menu")
    public void openMenu() {
        log.info("Opening side menu");
        click(menuButton);
        waitForElementVisible(logoutLink);
    }
    
    /**
     * Clicks the All Items link in the side menu
     */
    @Step("Click All Items link")
    public void clickAllItems() {
        log.info("Clicking All Items link");
        if (!isDisplayed(allItemsLink)) {
            openMenu();
        }
        click(allItemsLink);
    }
    
    /**
     * Clicks the About link in the side menu
     */
    @Step("Click About link")
    public void clickAbout() {
        log.info("Clicking About link");
        if (!isDisplayed(aboutLink)) {
            openMenu();
        }
        click(aboutLink);
    }
    
    /**
     * Clicks the Logout link in the side menu
     */
    @Step("Click Logout link")
    public void logout() {
        log.info("Clicking Logout link");
        if (!isDisplayed(logoutLink)) {
            openMenu();
        }
        click(logoutLink);
    }
    
    /**
     * Clicks the Reset App State link in the side menu
     */
    @Step("Click Reset App State link")
    public void resetAppState() {
        log.info("Clicking Reset App State link");
        if (!isDisplayed(resetAppStateLink)) {
            openMenu();
        }
        click(resetAppStateLink);
    }
    
    /**
     * Closes the side menu
     */
    @Step("Close side menu")
    public void closeMenu() {
        log.info("Closing side menu");
        if (isDisplayed(closeMenuButton)) {
            click(closeMenuButton);
        }
    }
    
    /**
     * Navigates to the shopping cart
     * 
     * @return CartPage
     */
    @Step("Click shopping cart")
    public CartPage goToCart() {
        log.info("Going to shopping cart");
        click(shoppingCartLink);
        return new CartPage();
    }
    
    /**
     * Gets the number of items in the cart
     * 
     * @return Number of items in the cart, or 0 if cart is empty
     */
    @Step("Get cart item count")
    public int getCartItemCount() {
        if (isDisplayed(shoppingCartBadge)) {
            return Integer.parseInt(shoppingCartBadge.getText());
        }
        return 0;
    }
} 