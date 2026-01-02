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

    // Optimized selectors using CSS and input types for better performance
    private By emailInput = By.cssSelector("input[type='email']");
    private By passwordInput = By.cssSelector("input[type='password']");
    private By loginButton = By.cssSelector("button[type='submit']");

    private By createNewUserButton = By.xpath("//button[.//text()[contains(.,'Crear Nuevo Usuario')]]");

    // Optimized login validation selectors using CSS
    private By errorMessage = By.cssSelector(".error, .alert-error, .alert-danger, [class*='error']");
    
    // Home page indicators (after successful login) - using XPath for compatibility
    private By homePageIndicators = By.xpath("//button[contains(text(), 'NUEVA TAREA')] | //h1[contains(text(), 'Tareas')] | //table");
    
    // User-specific elements that appear after login - using XPath for compatibility
    private By userElements = By.xpath("//button[contains(text(), 'Perfil')] | //span[contains(@class, 'user')] | //div[contains(@class, 'profile')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
    }

    public void login(String email, String password) {
        WebElement emailElement = findElementWithFallback(emailInput, By.xpath("//input[@type='email']"));
        WebElement passwordElement = findElementWithFallback(passwordInput, By.xpath("//input[@type='password']"));
        WebElement loginButtonElement = findElementWithFallback(loginButton, By.xpath("//button[@type='submit']"));
        
        wait.until(ExpectedConditions.visibilityOf(emailElement)).sendKeys(email);
        passwordElement.sendKeys(password);
        loginButtonElement.click();
    }

    public void clickCreateNewUser() {
        WebElement element = findElementWithFallback(createNewUserButton,
            By.xpath("//button[.//text()[contains(.,'Crear Nuevo Usuario')]]"));
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Helper method to find element with fallback selector for better reliability
     * Requirements: 6.1, 6.2
     */
    private WebElement findElementWithFallback(By primarySelector, By fallbackSelector) {
        try {
            return driver.findElement(primarySelector);
        } catch (Exception e) {
            return driver.findElement(fallbackSelector);
        }
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
            
            // Check if home page elements are visible using optimized selectors
            try {
                if (!driver.findElements(homePageIndicators).isEmpty()) {
                    return true;
                }
            } catch (Exception e) {
                // Fallback to XPath
                By fallbackHome = By.xpath("//button[contains(text(), 'NUEVA TAREA')] | //h1[contains(text(), 'Tareas')] | //table");
                if (!driver.findElements(fallbackHome).isEmpty()) {
                    return true;
                }
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
            // Look for user-specific elements using optimized selectors
            try {
                return !driver.findElements(userElements).isEmpty() || 
                       !driver.findElements(homePageIndicators).isEmpty();
            } catch (Exception e) {
                // Fallback to XPath
                By fallbackUser = By.xpath("//button[contains(text(), 'Perfil')] | //span[contains(@class, 'user')] | //div[contains(@class, 'profile')]");
                By fallbackHome = By.xpath("//button[contains(text(), 'NUEVA TAREA')] | //h1[contains(text(), 'Tareas')] | //table");
                return !driver.findElements(fallbackUser).isEmpty() || 
                       !driver.findElements(fallbackHome).isEmpty();
            }
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
            try {
                return !driver.findElements(errorMessage).isEmpty();
            } catch (Exception e) {
                // Fallback to XPath
                By fallbackError = By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'error') or contains(text(), 'incorrecto')]");
                return !driver.findElements(fallbackError).isEmpty();
            }
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
            try {
                WebElement errorElement = driver.findElement(errorMessage);
                return errorElement.getText();
            } catch (Exception e) {
                // Fallback to XPath
                By fallbackError = By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'error') or contains(text(), 'incorrecto')]");
                WebElement errorElement = driver.findElement(fallbackError);
                return errorElement.getText();
            }
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

    /**
     * Validates if we are currently on the login page
     * Requirements: TC012
     */
    public boolean isOnLoginPage() {
        try {
            // Check URL contains login or auth
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("login") || currentUrl.contains("auth")) {
                return true;
            }
            
            // Check if login form elements are present
            return !driver.findElements(loginButton).isEmpty() && 
                   !driver.findElements(emailInput).isEmpty() &&
                   !driver.findElements(passwordInput).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // Convenience methods for filling forms
    public void fillEmail(String email) {
        WebElement emailElement = findElementWithFallback(emailInput, By.xpath("//input[@type='email']"));
        wait.until(ExpectedConditions.visibilityOf(emailElement)).clear();
        emailElement.sendKeys(email);
    }

    public void fillPassword(String password) {
        WebElement passwordElement = findElementWithFallback(passwordInput, By.xpath("//input[@type='password']"));
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    public void submit() {
        WebElement loginButtonElement = findElementWithFallback(loginButton, By.xpath("//button[@type='submit']"));
        loginButtonElement.click();
    }
}
