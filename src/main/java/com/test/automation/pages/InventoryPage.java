package com.test.automation.pages;

import com.test.automation.pages.base.BasePage;
import com.test.automation.pages.components.HeaderComponent;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Page object for the inventory/products page
 */
@Slf4j
public class InventoryPage extends BasePage {
    
    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;
    
    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;
    
    @FindBy(id = "inventory_container")
    private WebElement inventoryContainer;
    
    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;
    
    private final HeaderComponent header;
    
    /**
     * Constructor for the InventoryPage
     */
    public InventoryPage() {
        super();
        this.header = new HeaderComponent(driver);
        log.info("InventoryPage initialized");
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
     * Gets the header component (alias for getHeader)
     * 
     * @return HeaderComponent instance
     */
    public HeaderComponent getHeaderComponent() {
        return header;
    }
    
    /**
     * Gets the number of products displayed on the page
     * 
     * @return Number of products
     */
    @Step("Get number of products")
    public int getNumberOfProducts() {
        return inventoryItems.size();
    }
    
    /**
     * Gets the number of products displayed on the page (alias for getNumberOfProducts)
     * 
     * @return Number of products
     */
    @Step("Get product count")
    public int getProductCount() {
        return getNumberOfProducts();
    }
    
    /**
     * Gets a list of all product names
     * 
     * @return List of product names
     */
    @Step("Get all product names")
    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement item : inventoryItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            names.add(nameElement.getText());
        }
        return names;
    }
    
    /**
     * Gets the name of the first product
     * 
     * @return Name of the first product
     */
    @Step("Get first product name")
    public String getFirstProductName() {
        if (inventoryItems.isEmpty()) {
            log.warn("No products available to get first product name");
            return "";
        }
        WebElement nameElement = inventoryItems.get(0).findElement(By.className("inventory_item_name"));
        return nameElement.getText();
    }
    
    /**
     * Gets the name of a product at the specified index
     * 
     * @param index Index of the product (0-based)
     * @return Name of the product
     */
    @Step("Get product name at index {0}")
    public String getProductName(int index) {
        if (index < 0 || index >= inventoryItems.size()) {
            log.warn("Invalid product index: {}", index);
            throw new IllegalArgumentException("Invalid product index: " + index);
        }
        WebElement nameElement = inventoryItems.get(index).findElement(By.className("inventory_item_name"));
        return nameElement.getText();
    }
    
    /**
     * Gets the price of the first product as a double
     * 
     * @return Price of the first product
     */
    @Step("Get first product price")
    public double getFirstProductPrice() {
        if (inventoryItems.isEmpty()) {
            log.warn("No products available to get first product price");
            return 0.0;
        }
        WebElement priceElement = inventoryItems.get(0).findElement(By.className("inventory_item_price"));
        String priceText = priceElement.getText().replace("$", "").trim();
        return Double.parseDouble(priceText);
    }
    
    /**
     * Gets a list of all product prices
     * 
     * @return List of product prices as strings (with $ sign)
     */
    @Step("Get all product prices")
    public List<String> getAllProductPrices() {
        List<String> prices = new ArrayList<>();
        for (WebElement item : inventoryItems) {
            WebElement priceElement = item.findElement(By.className("inventory_item_price"));
            prices.add(priceElement.getText());
        }
        return prices;
    }
    
    /**
     * Gets a list of all product prices as doubles
     * 
     * @return List of product prices as doubles
     */
    @Step("Get all product prices as doubles")
    public List<Double> getAllProductPricesAsDoubles() {
        List<Double> prices = new ArrayList<>();
        for (WebElement item : inventoryItems) {
            WebElement priceElement = item.findElement(By.className("inventory_item_price"));
            String priceText = priceElement.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }
    
    /**
     * Sorts products by the specified option
     * 
     * @param sortOption Sort option value
     */
    @Step("Sort products by: {0}")
    public void sortProducts(String sortOption) {
        log.info("Sorting products by: {}", sortOption);
        Select select = new Select(sortDropdown);
        select.selectByValue(sortOption);
    }
    
    /**
     * Sorts products by the specified option (alias for sortProducts)
     * 
     * @param sortOption Sort option value
     */
    @Step("Select sort option: {0}")
    public void selectSortOption(String sortOption) {
        sortProducts(sortOption);
    }
    
    /**
     * Adds a product to the cart by name
     * 
     * @param productName Name of the product
     * @return True if product was added, false if product not found
     */
    @Step("Add product to cart: {0}")
    public boolean addProductToCart(String productName) {
        log.info("Adding product to cart: {}", productName);
        for (WebElement item : inventoryItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            if (nameElement.getText().equals(productName)) {
                WebElement addButton = item.findElement(By.cssSelector("button[id^='add-to-cart']"));
                click(addButton);
                return true;
            }
        }
        log.warn("Product not found: {}", productName);
        return false;
    }
    
    /**
     * Adds a product to the cart by index
     * 
     * @param index Index of the product (0-based)
     */
    @Step("Add product to cart at index {0}")
    public void addProductToCart(int index) {
        if (index < 0 || index >= inventoryItems.size()) {
            log.warn("Invalid product index: {}", index);
            throw new IllegalArgumentException("Invalid product index: " + index);
        }
        log.info("Adding product to cart at index: {}", index);
        WebElement item = inventoryItems.get(index);
        WebElement addButton = item.findElement(By.cssSelector("button[id^='add-to-cart']"));
        click(addButton);
    }
    
    /**
     * Removes a product from the cart by name
     * 
     * @param productName Name of the product
     * @return True if product was removed, false if product not found
     */
    @Step("Remove product from cart: {0}")
    public boolean removeProductFromCart(String productName) {
        log.info("Removing product from cart: {}", productName);
        for (WebElement item : inventoryItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            if (nameElement.getText().equals(productName)) {
                WebElement removeButton = item.findElement(By.cssSelector("button[id^='remove-']"));
                click(removeButton);
                return true;
            }
        }
        log.warn("Product not found: {}", productName);
        return false;
    }
    
    /**
     * Removes a product from the cart by index
     * 
     * @param index Index of the product (0-based)
     */
    @Step("Remove product from cart at index {0}")
    public void removeProductFromCart(int index) {
        if (index < 0 || index >= inventoryItems.size()) {
            log.warn("Invalid product index: {}", index);
            throw new IllegalArgumentException("Invalid product index: " + index);
        }
        log.info("Removing product from cart at index: {}", index);
        WebElement item = inventoryItems.get(index);
        WebElement removeButton = item.findElement(By.cssSelector("button[id^='remove-']"));
        click(removeButton);
    }
    
    /**
     * Gets the current cart badge count (number of items in cart)
     * 
     * @return Number of items in cart, 0 if badge not displayed
     */
    @Step("Get cart badge count")
    public int getCartBadgeCount() {
        try {
            return Integer.parseInt(cartBadge.getText());
        } catch (Exception e) {
            // Badge not displayed when cart is empty
            return 0;
        }
    }
    
    /**
     * Opens the product details page for a product by name
     * 
     * @param productName Name of the product
     * @return ProductDetailsPage for the selected product
     */
    @Step("Open product details: {0}")
    public ProductDetailsPage openProductDetails(String productName) {
        log.info("Opening product details for: {}", productName);
        for (WebElement item : inventoryItems) {
            WebElement nameElement = item.findElement(By.className("inventory_item_name"));
            if (nameElement.getText().equals(productName)) {
                click(nameElement);
                return new ProductDetailsPage();
            }
        }
        log.warn("Product not found: {}", productName);
        throw new IllegalArgumentException("Product not found: " + productName);
    }
    
    /**
     * Opens the product details page for a product by index
     * 
     * @param index Index of the product (0-based)
     * @return ProductDetailsPage for the selected product
     */
    @Step("Open product details at index {0}")
    public ProductDetailsPage openProductDetails(int index) {
        if (index < 0 || index >= inventoryItems.size()) {
            log.warn("Invalid product index: {}", index);
            throw new IllegalArgumentException("Invalid product index: " + index);
        }
        log.info("Opening product details at index: {}", index);
        WebElement item = inventoryItems.get(index);
        WebElement nameElement = item.findElement(By.className("inventory_item_name"));
        click(nameElement);
        return new ProductDetailsPage();
    }
    
    /**
     * Checks if the page is loaded
     * 
     * @return True if page is loaded, false otherwise
     */
    @Step("Check if inventory page is loaded")
    public boolean isLoaded() {
        return isDisplayed(inventoryContainer);
    }
    
    /**
     * Navigates to the cart page
     * 
     * @return CartPage
     */
    @Step("Go to cart")
    public CartPage goToCart() {
        return header.goToCart();
    }
    
    /**
     * Gets the name of a product at the specified index (alias for getProductName)
     * 
     * @param index Index of the product (0-based)
     * @return Name of the product
     */
    @Step("Get product name at index {0}")
    public String getProductNameByIndex(int index) {
        return getProductName(index);
    }
    
    /**
     * Gets the price of a product at the specified index
     * 
     * @param index Index of the product (0-based)
     * @return Price of the product as a double
     */
    @Step("Get product price at index {0}")
    public double getProductPriceByIndex(int index) {
        if (index < 0 || index >= inventoryItems.size()) {
            log.warn("Invalid product index: {}", index);
            throw new IllegalArgumentException("Invalid product index: " + index);
        }
        WebElement priceElement = inventoryItems.get(index).findElement(By.className("inventory_item_price"));
        String priceText = priceElement.getText().replace("$", "").trim();
        return Double.parseDouble(priceText);
    }
    
    /**
     * Adds a product to the cart by index (alias for addProductToCart)
     * 
     * @param index Index of the product (0-based)
     */
    @Step("Add product to cart at index {0}")
    public void addProductToCartByIndex(int index) {
        addProductToCart(index);
    }
    
    /**
     * Gets the current cart count (alias for getCartBadgeCount)
     * 
     * @return Number of items in cart
     */
    @Step("Get cart count")
    public int getCartCount() {
        return getCartBadgeCount();
    }
} 