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

    // Inputs optimizados usando selectores CSS y tipos de input para mejor rendimiento
    private By nameInput = By.cssSelector("div[role='dialog'] input[type='text']:first-of-type");
    
    private By lastNameInput = By.cssSelector("div[role='dialog'] input[type='text']:nth-of-type(2)");
    
    private By emailInput = By.cssSelector("div[role='dialog'] input[type='email']");
    
    private By passwordInput = By.cssSelector("div[role='dialog'] input[type='password']:first-of-type");
    
    private By repeatPasswordInput = By.cssSelector("div[role='dialog'] input[type='password']:last-of-type");

    // Botón de envío optimizado usando selector CSS
    private By submitButton = By.cssSelector("div[role='dialog'] button[type='submit']");

    // Selectores de validación optimizados usando CSS para mejor rendimiento
    private By successMessage = By.cssSelector(".success, .alert-success, [class*='success']");
    
    private By errorMessage = By.cssSelector(".error, .alert-error, .alert-danger, [class*='error']");

    // Selector de modal optimizado
    private By modalDialog = By.cssSelector("div[role='dialog']");

    public void fillName(String name) {
        WebElement element = findElementWithFallback(nameInput, 
            By.xpath("//div[@role='dialog']//label[text()='Nombre']/following::input[1]"));
        wait.until(ExpectedConditions.elementToBeClickable(element)).sendKeys(name);
    }

    public void fillLastName(String lastName) {
        WebElement element = findElementWithFallback(lastNameInput,
            By.xpath("//div[@role='dialog']//label[text()='Apellido']/following::input[1]"));
        element.sendKeys(lastName);
    }

    public void fillEmail(String email) {
        WebElement element = findElementWithFallback(emailInput,
            By.xpath("//div[@role='dialog']//label[text()='E-Mail']/following::input[1]"));
        element.sendKeys(email);
    }

    public void fillPassword(String password) {
        WebElement passwordElement = findElementWithFallback(passwordInput,
            By.xpath("//div[@role='dialog']//label[contains(text(),'Escribe')]/following::input[1]"));
        WebElement repeatElement = findElementWithFallback(repeatPasswordInput,
            By.xpath("//div[@role='dialog']//label[contains(text(),'Repite')]/following::input[1]"));
        
        passwordElement.sendKeys(password);
        repeatElement.sendKeys(password);
    }

    /**
     * Llenar contraseña y confirmar contraseña con valores diferentes
     * Requerimientos: TC002
     */
    public void fillConfirmPassword(String confirmPassword) {
        WebElement repeatElement = findElementWithFallback(repeatPasswordInput,
            By.xpath("//div[@role='dialog']//label[contains(text(),'Repite')]/following::input[1]"));
        
        // Limpiar y llenar con contraseña diferente
        repeatElement.clear();
        repeatElement.sendKeys(confirmPassword);
    }

    public void submit() {
        WebElement element = findElementWithFallback(submitButton,
            By.xpath("//div[@role='dialog']//button[.//text()[contains(.,'Registrar')]]"));
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Método auxiliar para encontrar elemento con selector de respaldo para mejor confiabilidad
     * Requerimientos: 6.1, 6.2
     */
    private WebElement findElementWithFallback(By primarySelector, By fallbackSelector) {
        try {
            return driver.findElement(primarySelector);
        } catch (Exception e) {
            return driver.findElement(fallbackSelector);
        }
    }

    // ---------- Métodos de Validación de Registro ----------
    
    /**
     * Valida si el registro fue exitoso verificando indicadores de éxito
     * Requerimientos: 3.1, 3.2
     */
    public boolean isRegistrationSuccessful() {
        try {
            // Esperar un momento para que se procese el registro
            Thread.sleep(2000);
            
            // Verificar si se muestra mensaje de éxito usando selector optimizado
            try {
                if (!driver.findElements(successMessage).isEmpty()) {
                    return true;
                }
            } catch (Exception e) {
                // Respaldo a XPath si el selector CSS falla
                By fallbackSuccess = By.xpath("//div[contains(@class, 'success') or contains(text(), 'exitoso') or contains(text(), 'registrado')]");
                if (!driver.findElements(fallbackSuccess).isEmpty()) {
                    return true;
                }
            }
            
            // Verificar si el modal se cerró (indica registro exitoso y redirección)
            if (driver.findElements(modalDialog).isEmpty()) {
                return true;
            }
            
            // Verificar si fuimos redirigidos a la página de login o home
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
     * Valida si el registro falló verificando mensajes de error
     * Requerimientos: 3.4
     */
    public boolean hasRegistrationError() {
        try {
            // Verificar si se muestra mensaje de error usando selector optimizado
            try {
                return !driver.findElements(errorMessage).isEmpty();
            } catch (Exception e) {
                // Respaldo a XPath si el selector CSS falla
                By fallbackError = By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'error')]");
                return !driver.findElements(fallbackError).isEmpty();
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene el texto del mensaje de error si está presente
     * Requerimientos: 3.4
     */
    public String getErrorMessage() {
        try {
            try {
                WebElement errorElement = driver.findElement(errorMessage);
                return errorElement.getText();
            } catch (Exception e) {
                // Respaldo a XPath si el selector CSS falla
                By fallbackError = By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'error')]");
                WebElement errorElement = driver.findElement(fallbackError);
                return errorElement.getText();
            }
        } catch (Exception e) {
            return "";
        }
    }

    // ========== METODOS DE VALIDACION ESPECIFICOS ==========

    /**
     * Valida error de contraseñas no coincidentes
     * Requerimientos: TC002
     */
    public boolean hasPasswordMismatchError() {
        try {
            // Buscar mensajes de error específicos de contraseña
            By passwordError = By.xpath("//div[contains(text(), 'contraseña') and (contains(text(), 'coincid') or contains(text(), 'igual') or contains(text(), 'diferent'))]");
            return !driver.findElements(passwordError).isEmpty() || hasRegistrationError();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida error de campo requerido
     * Requerimientos: TC003, TC004, TC006
     */
    public boolean hasRequiredFieldError() {
        try {
            // Buscar mensajes de error de campos requeridos
            By requiredError = By.xpath("//div[contains(text(), 'requerido') or contains(text(), 'obligatorio') or contains(text(), 'necesario') or contains(text(), 'vacío')]");
            return !driver.findElements(requiredError).isEmpty() || hasRegistrationError();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida error de formato de email
     * Requerimientos: TC005
     */
    public boolean hasEmailFormatError() {
        try {
            // Buscar mensajes de error de formato de email
            By emailError = By.xpath("//div[contains(text(), 'email') and (contains(text(), 'formato') or contains(text(), 'válido') or contains(text(), 'correcto'))]");
            return !driver.findElements(emailError).isEmpty() || hasRegistrationError();
        } catch (Exception e) {
            return false;
        }
    }
}
