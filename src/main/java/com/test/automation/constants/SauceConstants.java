package com.test.automation.constants;

/**
 * Constants for Sauce Demo website
 */
public class SauceConstants {
    
    // URLs
    public static final String INVENTORY_PAGE = "inventory.html";
    public static final String ITEM_PAGE = "inventory-item.html";
    public static final String CART_PAGE = "cart.html";
    public static final String CHECKOUT_STEP_ONE = "checkout-step-one.html";
    public static final String CHECKOUT_STEP_TWO = "checkout-step-two.html";
    public static final String CHECKOUT_COMPLETE = "checkout-complete.html";
    
    // User credentials
    public static final String STANDARD_USER = "standard_user";
    public static final String LOCKED_OUT_USER = "locked_out_user";
    public static final String PROBLEM_USER = "problem_user";
    public static final String PERFORMANCE_GLITCH_USER = "performance_glitch_user";
    public static final String ERROR_USER = "error_user";
    public static final String VISUAL_USER = "visual_user";
    public static final String STANDARD_PASSWORD = "secret_sauce";
    
    // Error messages
    public static final String ERROR_INVALID_CREDENTIALS = "Epic sadface: Username and password do not match any user in this service";
    public static final String ERROR_LOCKED_OUT = "Epic sadface: Sorry, this user has been locked out.";
    public static final String ERROR_USERNAME_REQUIRED = "Epic sadface: Username is required";
    public static final String ERROR_PASSWORD_REQUIRED = "Epic sadface: Password is required";
    
    // Product properties
    public static final String PRODUCT_NAME_PREFIX = "Sauce Labs";
    
    // Sort options
    public static final String SORT_NAME_ASC = "az";
    public static final String SORT_NAME_DESC = "za";
    public static final String SORT_PRICE_ASC = "lohi";
    public static final String SORT_PRICE_DESC = "hilo";
    
    // Checkout information
    public static final String FIRST_NAME = "Test";
    public static final String LAST_NAME = "User";
    public static final String POSTAL_CODE = "12345";
    
    // Error messages - Checkout
    public static final String ERROR_FIRST_NAME_REQUIRED = "Error: First Name is required";
    public static final String ERROR_LAST_NAME_REQUIRED = "Error: Last Name is required";
    public static final String ERROR_POSTAL_CODE_REQUIRED = "Error: Postal Code is required";
    
    // Success messages
    public static final String ORDER_COMPLETE_HEADER = "Thank you for your order!";
    public static final String ORDER_COMPLETE_TEXT = "Your order has been dispatched, and will arrive just as fast as the pony can get there!";
    
    private SauceConstants() {
        // Private constructor to prevent instantiation
    }
} 