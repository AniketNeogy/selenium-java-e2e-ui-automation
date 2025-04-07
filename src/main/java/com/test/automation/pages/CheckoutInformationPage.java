package com.test.automation.pages;

import com.test.automation.pages.base.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for the checkout information page (step one)
 */
@Slf4j
public class CheckoutInformationPage extends BasePage {
    
    @FindBy(id = "first-name")
    private WebElement firstNameInput;
    
    @FindBy(id = "last-name")
    private WebElement lastNameInput;
    
    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;
    
    @FindBy(id = "cancel")
    private WebElement cancelButton;
    
    @FindBy(id = "continue")
    private WebElement continueButton;
    
    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;
    
    /**
     * Constructor for the CheckoutInformationPage
     */
    public CheckoutInformationPage() {
        super();
        log.info("CheckoutInformationPage initialized");
    }
    
    /**
     * Enters the first name
     * 
     * @param firstName First name
     * @return CheckoutInformationPage instance for method chaining
     */
    @Step("Enter first name: {0}")
    public CheckoutInformationPage enterFirstName(String firstName) {
        log.debug("Entering first name: {}", firstName);
        type(firstNameInput, firstName);
        return this;
    }
    
    /**
     * Enters the last name
     * 
     * @param lastName Last name
     * @return CheckoutInformationPage instance for method chaining
     */
    @Step("Enter last name: {0}")
    public CheckoutInformationPage enterLastName(String lastName) {
        log.debug("Entering last name: {}", lastName);
        type(lastNameInput, lastName);
        return this;
    }
    
    /**
     * Enters the postal code
     * 
     * @param postalCode Postal code
     * @return CheckoutInformationPage instance for method chaining
     */
    @Step("Enter postal code: {0}")
    public CheckoutInformationPage enterPostalCode(String postalCode) {
        log.debug("Entering postal code: {}", postalCode);
        type(postalCodeInput, postalCode);
        return this;
    }
    
    /**
     * Enters all checkout information
     * 
     * @param firstName First name
     * @param lastName Last name
     * @param postalCode Postal code
     * @return CheckoutInformationPage instance for method chaining
     */
    @Step("Enter checkout information")
    public CheckoutInformationPage enterInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return this;
    }
    
    /**
     * Continues to the next checkout step
     * 
     * @return CheckoutOverviewPage if successful, or this page if there are errors
     */
    @Step("Continue to overview")
    public Object continueToOverview() {
        log.info("Continuing to checkout overview");
        click(continueButton);
        
        if (isErrorMessageDisplayed()) {
            return this;
        }
        
        return new CheckoutOverviewPage();
    }
    
    /**
     * Continues to the next checkout step
     * 
     * @return CheckoutOverviewPage if successful, or this page if there are errors
     */
    @Step("Continue checkout")
    public CheckoutOverviewPage continueCheckout() {
        log.info("Continuing to checkout overview");
        click(continueButton);
        
        if (isErrorMessageDisplayed()) {
            log.warn("Error displayed during checkout: {}", getErrorMessageText());
            return null;
        }
        
        return new CheckoutOverviewPage();
    }
    
    /**
     * Cancels the checkout and returns to the cart
     * 
     * @return CartPage
     */
    @Step("Cancel checkout")
    public CartPage cancel() {
        log.info("Canceling checkout");
        click(cancelButton);
        return new CartPage();
    }
    
    /**
     * Cancels the checkout and returns to the cart (alias for cancel)
     * 
     * @return CartPage
     */
    @Step("Cancel checkout")
    public CartPage cancelCheckout() {
        return cancel();
    }
    
    /**
     * Checks if an error message is displayed
     * 
     * @return True if an error message is displayed, false otherwise
     */
    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
    
    /**
     * Checks if an error message is displayed (alias for isErrorMessageDisplayed)
     * 
     * @return True if an error message is displayed, false otherwise
     */
    @Step("Check if error is displayed")
    public boolean isErrorDisplayed() {
        return isErrorMessageDisplayed();
    }
    
    /**
     * Gets the error message text
     * 
     * @return Error message text
     */
    @Step("Get error message text")
    public String getErrorMessageText() {
        return isErrorMessageDisplayed() ? getText(errorMessage) : "";
    }
    
    /**
     * Gets the error message text (alias for getErrorMessageText)
     * 
     * @return Error message text
     */
    @Step("Get error message")
    public String getErrorMessage() {
        return getErrorMessageText();
    }
    
    /**
     * Checks if the page is loaded
     * 
     * @return True if page is loaded, false otherwise
     */
    @Step("Check if checkout information page is loaded")
    public boolean isLoaded() {
        return isDisplayed(firstNameInput) && isDisplayed(lastNameInput) && isDisplayed(postalCodeInput);
    }
} 