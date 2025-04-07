package com.test.automation.pages;

import com.test.automation.pages.base.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for the checkout complete page
 */
@Slf4j
public class CheckoutCompletePage extends BasePage {
    
    @FindBy(className = "checkout_complete_container")
    private WebElement completeContainer;
    
    @FindBy(className = "complete-header")
    private WebElement completeHeader;
    
    @FindBy(className = "complete-text")
    private WebElement completeText;
    
    @FindBy(id = "back-to-products")
    private WebElement backHomeButton;
    
    /**
     * Constructor for the CheckoutCompletePage
     */
    public CheckoutCompletePage() {
        super();
        log.info("CheckoutCompletePage initialized");
    }
    
    /**
     * Gets the confirmation header text
     * 
     * @return Confirmation header text
     */
    @Step("Get confirmation header")
    public String getConfirmationHeader() {
        return getText(completeHeader);
    }
    
    /**
     * Gets the confirmation text
     * 
     * @return Confirmation text
     */
    @Step("Get confirmation text")
    public String getConfirmationText() {
        return getText(completeText);
    }
    
    /**
     * Returns to the products page
     * 
     * @return InventoryPage
     */
    @Step("Back to home")
    public InventoryPage backToHome() {
        log.info("Going back to home page");
        click(backHomeButton);
        return new InventoryPage();
    }
    
    /**
     * Returns to the products page (alias for backToHome)
     * 
     * @return InventoryPage
     */
    @Step("Back to products")
    public InventoryPage backToProducts() {
        return backToHome();
    }
    
    /**
     * Checks if the page is loaded
     * 
     * @return True if page is loaded, false otherwise
     */
    @Step("Check if checkout complete page is loaded")
    public boolean isLoaded() {
        return isDisplayed(completeContainer) && isDisplayed(backHomeButton);
    }
    
    /**
     * Checks if the order is confirmed by verifying the header text
     * 
     * @return True if the order is confirmed, false otherwise
     */
    @Step("Check if order is confirmed")
    public boolean isOrderConfirmed() {
        return getConfirmationHeader().contains("THANK YOU FOR YOUR ORDER");
    }
} 