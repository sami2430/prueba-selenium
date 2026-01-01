package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String URL = "http://192.168.80.43:10100";

    private By emailInput = By.xpath("//input[@type='email']");
    private By passwordInput = By.xpath("//input[@type='password']");
    private By loginButton = By.xpath("//button[@type='submit']");

    private By createNewUserButton =
            By.xpath("//button[.//text()[contains(.,'Crear Nuevo Usuario')]]");

    // Login validation selectors
    private By errorMessage = 
            By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'error') or contains(text(), 'incorrecto')]");
    
    // Home page indicators (after successful login)
    private By homePageIndicators = By.xpath("//button[contains(text(), 'NUEVA TAREA')] | //h1[contains(text(), 'Tareas')] | //table");
    
    // User-specific elements that appear after login
    private By userElements = By.xpath("//button[contains(text(), 'Perfil')] | //span[contains(@class, 'user')] | //div[contains(@class, 'profile')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
    }

    public void login(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    public void clickCreateNewUser() {
        wait.until(ExpectedConditions.elementToBeClickable(createNewUserButton)).click();
    }

    // ---------- Login Validation Methods ----------
    
    /**
     * Validates if login was successful by checking for home page redirection
     * Requirements: 4.1, 4.2
     */
    public boolean isLoginSuccessful() {
        try {
            // Wait for page transition after login
            Thread.sleep(2000);
            
            // Check if we're redirected to home page (URL contains home, tasks, or main)
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("home") || currentUrl.contains("tasks") || currentUrl.contains("main")) {
                return true;
            }
            
            // Check if home page elements are visible
            if (!driver.findElements(homePageIndicators).isEmpty()) {
                return true;
            }
            
            // Check if we're no longer on login page
            if (driver.findElements(loginButton).isEmpty()) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates if user-specific elements are displayed after login
     * Requirements: 4.3
     */
    public boolean areUserElementsDisplayed() {
        try {
            // Look for user-specific elements or any elements that indicate successful login
            return !driver.findElements(userElements).isEmpty() || 
                   !driver.findElements(homePageIndicators).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates if login failed by checking for error messages
     * Requirements: 4.4
     */
    public boolean hasLoginError() {
        try {
            return !driver.findElements(errorMessage).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the error message text if present
     * Requirements: 4.4
     */
    public String getErrorMessage() {
        try {
            WebElement errorElement = driver.findElement(errorMessage);
            return errorElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Validates that we're still on the login page (login failed)
     * Requirements: 4.4
     */
    public boolean isStillOnLoginPage() {
        try {
            // Check if login form elements are still present
            return !driver.findElements(loginButton).isEmpty() && 
                   !driver.findElements(emailInput).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
