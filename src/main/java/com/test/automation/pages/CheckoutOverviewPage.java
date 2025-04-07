package com.test.automation.pages;

import com.test.automation.pages.base.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Page object for the checkout overview page (step two)
 */
@Slf4j
public class CheckoutOverviewPage extends BasePage {
    
    @FindBy(className = "cart_list")
    private WebElement cartList;
    
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;
    
    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;
    
    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;
    
    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;
    
    @FindBy(id = "cancel")
    private WebElement cancelButton;
    
    @FindBy(id = "finish")
    private WebElement finishButton;
    
    /**
     * Constructor for the CheckoutOverviewPage
     */
    public CheckoutOverviewPage() {
        super();
        log.info("CheckoutOverviewPage initialized");
    }
    
    /**
     * Gets a list of all product names in the checkout
     * 
     * @return List of product names
     */
    @Step("Get all product names in checkout")
    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement item : cartItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            names.add(nameElement.getText());
        }
        return names;
    }
    
    /**
     * Gets a map of product names to prices
     * 
     * @return Map of product names to prices
     */
    @Step("Get product prices in checkout")
    public Map<String, String> getProductPrices() {
        Map<String, String> prices = new HashMap<>();
        for (WebElement item : cartItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            WebElement priceElement = item.findElement(By.className("inventory_item_price"));
            prices.put(nameElement.getText(), priceElement.getText());
        }
        return prices;
    }
    
    /**
     * Gets the subtotal amount
     * 
     * @return Subtotal amount as string
     */
    @Step("Get subtotal")
    public String getSubtotal() {
        String subtotalText = getText(subtotalLabel);
        return subtotalText.substring(subtotalText.indexOf("$"));
    }
    
    /**
     * Gets the tax amount
     * 
     * @return Tax amount as string
     */
    @Step("Get tax")
    public String getTax() {
        String taxText = getText(taxLabel);
        return taxText.substring(taxText.indexOf("$"));
    }
    
    /**
     * Gets the total amount
     * 
     * @return Total amount as string
     */
    @Step("Get total")
    public String getTotal() {
        String totalText = getText(totalLabel);
        return totalText.substring(totalText.indexOf("$"));
    }
    
    /**
     * Cancels the checkout and returns to the inventory page
     * 
     * @return InventoryPage
     */
    @Step("Cancel checkout")
    public InventoryPage cancel() {
        log.info("Canceling checkout");
        click(cancelButton);
        return new InventoryPage();
    }
    
    /**
     * Cancels the checkout and returns to the inventory page (alias for cancel)
     * 
     * @return InventoryPage
     */
    @Step("Cancel checkout")
    public InventoryPage cancelCheckout() {
        return cancel();
    }
    
    /**
     * Completes the checkout
     * 
     * @return CheckoutCompletePage
     */
    @Step("Finish checkout")
    public CheckoutCompletePage finish() {
        log.info("Finishing checkout");
        click(finishButton);
        return new CheckoutCompletePage();
    }
    
    /**
     * Completes the checkout (alias for finish)
     * 
     * @return CheckoutCompletePage
     */
    @Step("Finish checkout")
    public CheckoutCompletePage finishCheckout() {
        return finish();
    }
    
    /**
     * Checks if the page is loaded
     * 
     * @return True if page is loaded, false otherwise
     */
    @Step("Check if checkout overview page is loaded")
    public boolean isLoaded() {
        return isDisplayed(cartList) && isDisplayed(finishButton);
    }
} 