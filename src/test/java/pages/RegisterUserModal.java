package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class RegisterUserModal {

    private WebDriver driver;
    private WebDriverWait wait;

    public RegisterUserModal(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Inputs por label visible (MUI-safe)
    private By nameInput =
            By.xpath("//div[@role='dialog']//label[text()='Nombre']/following::input[1]");

    private By lastNameInput =
            By.xpath("//div[@role='dialog']//label[text()='Apellido']/following::input[1]");

    private By emailInput =
            By.xpath("//div[@role='dialog']//label[text()='E-Mail']/following::input[1]");

    private By passwordInput =
            By.xpath("//div[@role='dialog']//label[contains(text(),'Escribe')]/following::input[1]");

    private By repeatPasswordInput =
            By.xpath("//div[@role='dialog']//label[contains(text(),'Repite')]/following::input[1]");

    // boton correcto
    private By submitButton =
            By.xpath("//div[@role='dialog']//button[.//text()[contains(.,'Registrar')]]");

    // Validation selectors for registration success
    private By successMessage =
            By.xpath("//div[contains(@class, 'success') or contains(text(), 'exitoso') or contains(text(), 'registrado')]");
    
    private By errorMessage =
            By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'error')]");

    // Modal dialog selector to check if modal is still open
    private By modalDialog =
            By.xpath("//div[@role='dialog']");

    public void fillName(String name) {
        wait.until(ExpectedConditions.elementToBeClickable(nameInput)).sendKeys(name);
    }

    public void fillLastName(String lastName) {
        driver.findElement(lastNameInput).sendKeys(lastName);
    }

    public void fillEmail(String email) {
        driver.findElement(emailInput).sendKeys(email);
    }

    public void fillPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(repeatPasswordInput).sendKeys(password);
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    // ---------- Registration Validation Methods ----------
    
    /**
     * Validates if registration was successful by checking for success indicators
     * Requirements: 3.1, 3.2
     */
    public boolean isRegistrationSuccessful() {
        try {
            // Wait a moment for the registration to process
            Thread.sleep(2000);
            
            // Check if success message is displayed
            if (!driver.findElements(successMessage).isEmpty()) {
                return true;
            }
            
            // Check if modal closed (indicates successful registration and redirect)
            if (driver.findElements(modalDialog).isEmpty()) {
                return true;
            }
            
            // Check if we were redirected to login page or home page
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("login") || currentUrl.contains("home") || currentUrl.contains("tasks")) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates if registration failed by checking for error messages
     * Requirements: 3.4
     */
    public boolean hasRegistrationError() {
        try {
            // Check if error message is displayed
            return !driver.findElements(errorMessage).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the error message text if present
     * Requirements: 3.4
     */
    public String getErrorMessage() {
        try {
            WebElement errorElement = driver.findElement(errorMessage);
            return errorElement.getText();
        } catch (Exception e) {
            return "";
        }
    }
}
