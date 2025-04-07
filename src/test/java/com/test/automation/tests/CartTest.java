package com.test.automation.tests;

import com.test.automation.constants.SauceConstants;
import com.test.automation.pages.CartPage;
import com.test.automation.pages.CheckoutInformationPage;
import com.test.automation.pages.InventoryPage;
import com.test.automation.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for Shopping Cart functionality
 */
@Feature("Shopping Cart")
public class CartTest extends BaseTest {
    
    private static final Logger log = LoggerFactory.getLogger(CartTest.class);
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    
    /**
     * Login before each test
     */
    @BeforeMethod
    public void setupTest() {
        log.info("Logging in before cart test");
        loginPage = new LoginPage();
        loginPage.login(SauceConstants.STANDARD_USER, SauceConstants.STANDARD_PASSWORD);
        inventoryPage = new InventoryPage();
    }
    
    /**
     * Test to verify that cart is initially empty
     */
    @Test
    @Description("Verify that cart is initially empty")
    @Severity(SeverityLevel.NORMAL)
    @Story("Cart Display")
    public void testCartInitiallyEmpty() {
        log.info("Testing cart is initially empty");
        
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page is not loaded");
        Assert.assertTrue(cartPage.isEmpty(), "Cart is not empty on initial load");
        Assert.assertEquals(cartPage.getItemCount(), 0, "Cart item count is not 0 on initial load");
    }
    
    /**
     * Test to verify that products can be removed from cart
     */
    @Test
    @Description("Verify that products can be removed from cart")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Remove from Cart")
    public void testRemoveProductFromCart() {
        log.info("Testing removing product from cart");
        
        // Check if products are available
        int productCount = inventoryPage.getProductCount();
        Assert.assertTrue(productCount > 0, "No products available in inventory");
        
        // Add product to cart
        String productName = inventoryPage.getFirstProductName();
        inventoryPage.addProductToCartByIndex(0);
        
        // Go to cart
        CartPage cartPage = inventoryPage.goToCart();
        Assert.assertTrue(cartPage.isProductInCart(productName), 
                "Product '" + productName + "' not found in cart");
        
        // Remove product from cart
        cartPage.removeProduct(productName);
        
        // Verify cart is empty
        Assert.assertTrue(cartPage.isEmpty(), "Cart is not empty after removing product");
        Assert.assertEquals(cartPage.getItemCount(), 0, "Cart item count is not 0 after removing product");
    }
    
    /**
     * Test to verify that continuing shopping from cart returns to inventory
     */
    @Test
    @Description("Verify that continuing shopping from cart returns to inventory")
    @Severity(SeverityLevel.NORMAL)
    @Story("Navigation")
    public void testContinueShopping() {
        log.info("Testing continue shopping functionality");
        
        // Go to cart
        CartPage cartPage = inventoryPage.goToCart();
        
        // Continue shopping
        InventoryPage returnedInventoryPage = cartPage.continueShopping();
        
        // Verify returned to inventory page
        Assert.assertTrue(returnedInventoryPage.isLoaded(), 
                "Inventory page is not loaded after continuing shopping");
    }
    
    /**
     * Test to verify that checkout can be initiated from cart
     */
    @Test
    @Description("Verify that checkout can be initiated from cart")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Checkout")
    public void testCheckoutFromCart() {
        log.info("Testing checkout from cart");
        
        // Check if products are available
        int productCount = inventoryPage.getProductCount();
        Assert.assertTrue(productCount > 0, "No products available in inventory");
        
        // Add product to cart
        inventoryPage.addProductToCartByIndex(0);
        
        // Go to cart and checkout
        CartPage cartPage = inventoryPage.goToCart();
        CheckoutInformationPage checkoutPage = cartPage.checkout();
        
        // Verify checkout page is loaded
        Assert.assertTrue(checkoutPage.isLoaded(), 
                "Checkout information page is not loaded after clicking checkout");
    }
    
    /**
     * Test to verify that checkout cannot be initiated from empty cart
     */
    @Test
    @Description("Verify that checkout can be initiated from empty cart")
    @Severity(SeverityLevel.NORMAL)
    @Story("Checkout")
    public void testCheckoutFromEmptyCart() {
        log.info("Testing checkout from empty cart");
        
        // Go to cart
        CartPage cartPage = inventoryPage.goToCart();
        
        // Verify cart is empty
        Assert.assertTrue(cartPage.isEmpty(), "Cart is not empty on initial load");
        
        // Try to checkout and verify possible behaviors
        // Note: Some sites allow checkout with empty cart, others don't
        // For SauceDemo, the checkout button is enabled even with empty cart
        CheckoutInformationPage checkoutPage = cartPage.checkout();
        Assert.assertTrue(checkoutPage.isLoaded(), 
                "Checkout information page is not loaded after clicking checkout from empty cart");
    }
} 