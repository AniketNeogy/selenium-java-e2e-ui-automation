package com.test.automation.pages;

import com.test.automation.pages.base.BasePage;
import com.test.automation.pages.components.HeaderComponent;
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
 * Page object for the shopping cart page
 */
@Slf4j
public class CartPage extends BasePage {
    
    @FindBy(className = "cart_list")
    private WebElement cartList;
    
    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;
    
    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;
    
    @FindBy(id = "checkout")
    private WebElement checkoutButton;
    
    private final HeaderComponent header;
    
    /**
     * Constructor for the CartPage
     */
    public CartPage() {
        super();
        this.header = new HeaderComponent(driver);
        log.info("CartPage initialized");
    }
    
    /**
     * Gets the header component
     * 
     * @return HeaderComponent instance
     */
    public HeaderComponent getHeader() {
        return header;
    }
    
    /**
     * Gets the number of items in the cart
     * 
     * @return Number of items
     */
    @Step("Get number of items in cart")
    public int getNumberOfItems() {
        return cartItems.size();
    }
    
    /**
     * Gets the number of items in the cart (alias for getNumberOfItems)
     * 
     * @return Number of items
     */
    @Step("Get item count")
    public int getItemCount() {
        return getNumberOfItems();
    }
    
    /**
     * Gets a list of all product names in the cart
     * 
     * @return List of product names
     */
    @Step("Get all product names in cart")
    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement item : cartItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            names.add(nameElement.getText());
        }
        return names;
    }
    
    /**
     * Checks if a product is in the cart
     * 
     * @param productName Name of the product
     * @return True if product is in cart, false otherwise
     */
    @Step("Check if product is in cart: {0}")
    public boolean isProductInCart(String productName) {
        return getAllProductNames().contains(productName);
    }
    
    /**
     * Gets a map of product names to prices
     * 
     * @return Map of product names to prices
     */
    @Step("Get product prices in cart as map")
    public Map<String, String> getProductPricesMap() {
        Map<String, String> prices = new HashMap<>();
        for (WebElement item : cartItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            WebElement priceElement = item.findElement(By.className("inventory_item_price"));
            prices.put(nameElement.getText(), priceElement.getText());
        }
        return prices;
    }
    
    /**
     * Gets the price of a product in the cart
     * 
     * @param productName Name of the product
     * @return Price of the product as a double, or 0.0 if product not found
     */
    @Step("Get price of product: {0}")
    public double getProductPrice(String productName) {
        for (WebElement item : cartItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            if (nameElement.getText().equals(productName)) {
                WebElement priceElement = item.findElement(By.className("inventory_item_price"));
                String priceText = priceElement.getText().replace("$", "").trim();
                return Double.parseDouble(priceText);
            }
        }
        log.warn("Product not found in cart: {}", productName);
        return 0.0;
    }
    
    /**
     * Removes a product from the cart by name
     * 
     * @param productName Name of the product
     * @return True if product was removed, false if product not found
     */
    @Step("Remove product from cart: {0}")
    public boolean removeProduct(String productName) {
        log.info("Removing product from cart: {}", productName);
        for (WebElement item : cartItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            if (nameElement.getText().equals(productName)) {
                WebElement removeButton = item.findElement(By.cssSelector("button[id^='remove-']"));
                click(removeButton);
                return true;
            }
        }
        log.warn("Product not found in cart: {}", productName);
        return false;
    }
    
    /**
     * Continues shopping (returns to inventory page)
     * 
     * @return InventoryPage
     */
    @Step("Continue shopping")
    public InventoryPage continueShopping() {
        log.info("Continuing shopping");
        click(continueShoppingButton);
        return new InventoryPage();
    }
    
    /**
     * Proceeds to checkout
     * 
     * @return CheckoutInformationPage
     */
    @Step("Proceed to checkout")
    public CheckoutInformationPage checkout() {
        log.info("Proceeding to checkout");
        click(checkoutButton);
        return new CheckoutInformationPage();
    }
    
    /**
     * Checks if the page is loaded
     * 
     * @return True if page is loaded, false otherwise
     */
    @Step("Check if cart page is loaded")
    public boolean isLoaded() {
        return isDisplayed(cartList);
    }
    
    /**
     * Checks if the cart is empty
     * 
     * @return True if cart is empty, false otherwise
     */
    @Step("Check if cart is empty")
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
    
    /**
     * Gets a list of all product names in the cart (alias for getAllProductNames)
     * 
     * @return List of product names
     */
    @Step("Get product names in cart")
    public List<String> getProductNames() {
        return getAllProductNames();
    }
    
    /**
     * Gets a list of all product prices in the cart as doubles
     * 
     * @return List of product prices as doubles
     */
    @Step("Get product prices in cart")
    public List<Double> getProductPrices() {
        List<Double> prices = new ArrayList<>();
        for (WebElement item : cartItems) {
            WebElement priceElement = item.findElement(By.className("inventory_item_price"));
            String priceText = priceElement.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }
} 