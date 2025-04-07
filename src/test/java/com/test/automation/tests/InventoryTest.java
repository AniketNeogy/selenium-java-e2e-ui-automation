package com.test.automation.tests;

import com.test.automation.constants.SauceConstants;
import com.test.automation.pages.CartPage;
import com.test.automation.pages.InventoryPage;
import com.test.automation.pages.LoginPage;
import com.test.automation.pages.ProductDetailsPage;
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

import java.util.List;

/**
 * Test class for Inventory page functionality
 */
@Feature("Inventory")
public class InventoryTest extends BaseTest {
    
    private static final Logger log = LoggerFactory.getLogger(InventoryTest.class);
    
    /**
     * Login before each test
     */
    @BeforeMethod
    public void setupTest() {
        log.info("Logging in before inventory test");
        LoginPage loginPage = new LoginPage();
        loginPage.login(SauceConstants.STANDARD_USER, SauceConstants.STANDARD_PASSWORD);
    }
    
    /**
     * Test to verify the inventory page is displayed after login
     */
    @Test
    @Description("Verify that inventory page is displayed after login")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Inventory Display")
    public void testInventoryPageDisplayed() {
        log.info("Testing inventory page display");
        
        InventoryPage inventoryPage = new InventoryPage();
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page is not loaded");
        Assert.assertTrue(inventoryPage.getProductCount() > 0, "No products displayed on inventory page");
    }
    
    /**
     * Test to verify that products can be sorted by price (low to high)
     */
    @Test
    @Description("Verify that products can be sorted by price (low to high)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Sorting")
    public void testSortProductsByPriceLowToHigh() {
        log.info("Testing product sorting - price low to high");
        
        InventoryPage inventoryPage = new InventoryPage();
        inventoryPage.selectSortOption(SauceConstants.SORT_PRICE_ASC);
        
        List<Double> prices = inventoryPage.getAllProductPricesAsDoubles();
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1), 
                    "Products are not sorted correctly by price low to high");
        }
    }
    
    /**
     * Test to verify that products can be sorted by price (high to low)
     */
    @Test
    @Description("Verify that products can be sorted by price (high to low)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Sorting")
    public void testSortProductsByPriceHighToLow() {
        log.info("Testing product sorting - price high to low");
        
        InventoryPage inventoryPage = new InventoryPage();
        inventoryPage.selectSortOption(SauceConstants.SORT_PRICE_DESC);
        
        List<Double> prices = inventoryPage.getAllProductPricesAsDoubles();
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i + 1), 
                    "Products are not sorted correctly by price high to low");
        }
    }
    
    /**
     * Test to verify that products can be sorted by name (A to Z)
     */
    @Test
    @Description("Verify that products can be sorted by name (A to Z)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Sorting")
    public void testSortProductsByNameAToZ() {
        log.info("Testing product sorting - name A to Z");
        
        InventoryPage inventoryPage = new InventoryPage();
        inventoryPage.selectSortOption(SauceConstants.SORT_NAME_ASC);
        
        List<String> names = inventoryPage.getAllProductNames();
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0, 
                    "Products are not sorted correctly by name A to Z");
        }
    }
    
    /**
     * Test to verify that products can be sorted by name (Z to A)
     */
    @Test
    @Description("Verify that products can be sorted by name (Z to A)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Sorting")
    public void testSortProductsByNameZToA() {
        log.info("Testing product sorting - name Z to A");
        
        InventoryPage inventoryPage = new InventoryPage();
        inventoryPage.selectSortOption(SauceConstants.SORT_NAME_DESC);
        
        List<String> names = inventoryPage.getAllProductNames();
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) >= 0, 
                    "Products are not sorted correctly by name Z to A");
        }
    }
    
    /**
     * Test to verify that product can be added to cart from inventory page
     */
    @Test
    @Description("Verify that product can be added to cart from inventory page")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add to Cart")
    public void testAddProductToCart() {
        log.info("Testing adding product to cart from inventory page");
        
        InventoryPage inventoryPage = new InventoryPage();
        String productName = inventoryPage.getFirstProductName();
        
        // Add the product to cart
        inventoryPage.addProductToCart(0);
        
        // Verify cart badge shows 1 item
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, 
                "Cart badge doesn't show 1 item after adding product");
        
        // Go to cart and verify the product is there
        CartPage cartPage = inventoryPage.getHeaderComponent().goToCart();
        Assert.assertTrue(cartPage.isProductInCart(productName), 
                "Product '" + productName + "' not found in cart after adding from inventory");
    }
    
    /**
     * Test to verify that product can be removed from cart on inventory page
     */
    @Test
    @Description("Verify that product can be removed from cart on inventory page")
    @Severity(SeverityLevel.NORMAL)
    @Story("Remove from Cart")
    public void testRemoveProductFromCart() {
        log.info("Testing removing product from cart on inventory page");
        
        InventoryPage inventoryPage = new InventoryPage();
        
        // Add the product to cart
        inventoryPage.addProductToCart(0);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1, 
                "Cart badge doesn't show 1 item after adding product");
        
        // Remove the product from cart
        inventoryPage.removeProductFromCart(0);
        
        // Verify cart badge is not displayed (count is 0)
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 0, 
                "Cart badge still shows after removing product");
    }
    
    /**
     * Test to verify that product details page can be opened
     */
    @Test
    @Description("Verify that product details page can be opened")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Details")
    public void testOpenProductDetails() {
        log.info("Testing opening product details page");
        
        InventoryPage inventoryPage = new InventoryPage();
        String productName = inventoryPage.getFirstProductName();
        
        // Go to product details page
        ProductDetailsPage detailsPage = inventoryPage.openProductDetails(0);
        
        // Verify product details page is loaded with correct product
        Assert.assertTrue(detailsPage.isLoaded(), "Product details page is not loaded");
        Assert.assertEquals(detailsPage.getProductName(), productName, 
                "Product name on details page doesn't match selected product");
    }
} 