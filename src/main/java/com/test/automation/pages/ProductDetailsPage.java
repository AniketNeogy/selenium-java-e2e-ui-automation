package com.test.automation.pages;

import com.test.automation.pages.base.BasePage;
import com.test.automation.pages.components.HeaderComponent;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for the product details page
 */
@Slf4j
public class ProductDetailsPage extends BasePage {
    
    @FindBy(className = "inventory_details_container")
    private WebElement detailsContainer;
    
    @FindBy(className = "inventory_details_name")
    private WebElement productNameElement;
    
    @FindBy(className = "inventory_details_desc")
    private WebElement productDescriptionElement;
    
    @FindBy(className = "inventory_details_price")
    private WebElement productPriceElement;
    
    @FindBy(css = "button[id^='add-to-cart']")
    private WebElement addToCartButton;
    
    @FindBy(css = "button[id^='remove-']")
    private WebElement removeButton;
    
    @FindBy(id = "back-to-products")
    private WebElement backButton;
    
    private final HeaderComponent header;
    
    /**
     * Constructor for the ProductDetailsPage
     */
    public ProductDetailsPage() {
        super();
        this.header = new HeaderComponent(driver);
        log.info("ProductDetailsPage initialized");
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
     * Gets the product name
     * 
     * @return Product name
     */
    @Step("Get product name")
    public String getProductName() {
        return getText(productNameElement);
    }
    
    /**
     * Gets the product description
     * 
     * @return Product description
     */
    @Step("Get product description")
    public String getProductDescription() {
        return getText(productDescriptionElement);
    }
    
    /**
     * Gets the product price
     * 
     * @return Product price as string (with $ sign)
     */
    @Step("Get product price")
    public String getProductPrice() {
        return getText(productPriceElement);
    }
    
    /**
     * Adds the product to the cart
     */
    @Step("Add product to cart")
    public void addToCart() {
        log.info("Adding product to cart: {}", getProductName());
        click(addToCartButton);
    }
    
    /**
     * Removes the product from the cart
     */
    @Step("Remove product from cart")
    public void removeFromCart() {
        log.info("Removing product from cart: {}", getProductName());
        click(removeButton);
    }
    
    /**
     * Checks if the product is in the cart
     * 
     * @return True if the product is in the cart, false otherwise
     */
    @Step("Check if product is in cart")
    public boolean isProductInCart() {
        return isDisplayed(removeButton);
    }
    
    /**
     * Navigates back to the inventory page
     * 
     * @return InventoryPage
     */
    @Step("Go back to products")
    public InventoryPage goBackToProducts() {
        log.info("Going back to products");
        click(backButton);
        return new InventoryPage();
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
     * Checks if the page is loaded
     * 
     * @return True if page is loaded, false otherwise
     */
    @Step("Check if product details page is loaded")
    public boolean isLoaded() {
        return isDisplayed(detailsContainer);
    }
} 