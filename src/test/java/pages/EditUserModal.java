package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EditUserModal {

    private WebDriver driver;
    private WebDriverWait wait;

    private By nameInput =
            By.xpath("//label[text()='Nombre']/following::input[1]");


    private By lastNameInput =
            By.xpath("//div[@role='dialog']//label[text()='Apellido']/following::input[1]");

    private By emailInput =
            By.xpath("//label[text()='Email']/following::input[1]");

    private By saveButton =
        By.xpath("//button[contains(.,'Modificar')]");



    private By successMessage =
    By.xpath("//div[contains(@class,'MuiAlert-message') and contains(text(),'Datos de usuario')]");

    // Selectores de validación mejorados
    private By errorMessage = By.cssSelector(".error, .alert-error, .alert-danger, [class*='error']");
    
    private By validationErrors = By.xpath("//div[contains(@class, 'error') or contains(@class, 'invalid') or contains(text(), 'requerido') or contains(text(), 'obligatorio')]");
    
    private By modalDialog = By.cssSelector("div[role='dialog'], .modal, [class*='modal']");


    public EditUserModal(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void updateName(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput)).clear();
        driver.findElement(nameInput).sendKeys(name);
    }

    public void updateLastName(String lastName) {
        driver.findElement(lastNameInput).clear();
        driver.findElement(lastNameInput).sendKeys(lastName);
    }

    public void updateEmail(String email) {
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
    }

    public void saveChanges() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public boolean isSuccessMessageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ---------- Métodos de Validación Mejorados ----------
    
    /**
     * Valida si hay errores de validación en el formulario
     * Requerimientos: 3.4 (manejo de errores de validación)
     */
    public boolean hasValidationErrors() {
        try {
            return !driver.findElements(validationErrors).isEmpty() || 
                   !driver.findElements(errorMessage).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene mensajes de error de validación si están presentes
     * Requerimientos: 3.4
     */
    public String getValidationErrorMessage() {
        try {
            if (!driver.findElements(validationErrors).isEmpty()) {
                return driver.findElement(validationErrors).getText();
            }
            if (!driver.findElements(errorMessage).isEmpty()) {
                return driver.findElement(errorMessage).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Valida si el modal aún está abierto (indicando que el guardado falló)
     * Requerimientos: Validación de modificación de usuario
     */
    public boolean isModalStillOpen() {
        try {
            return !driver.findElements(saveButton).isEmpty() || 
                   !driver.findElements(modalDialog).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida si la modificación del usuario fue exitosa verificando el cierre del modal
     * Requerimientos: Validación de éxito de modificación de usuario
     */
    public boolean isModificationSuccessful() {
        try {
            // Esperar a que el modal se cierre o aparezca mensaje de éxito
            Thread.sleep(2000);
            
            return isSuccessMessageVisible() || !isModalStillOpen();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene los valores actuales de los campos del formulario para validación
     * Requerimientos: Validación de persistencia de datos
     */
    public UserFormData getFormData() {
        try {
            String name = driver.findElement(nameInput).getAttribute("value");
            String lastName = driver.findElement(lastNameInput).getAttribute("value");
            String email = driver.findElement(emailInput).getAttribute("value");
            
            return new UserFormData(name, lastName, email);
        } catch (Exception e) {
            return new UserFormData("", "", "");
        }
    }

    /**
     * Clase de datos para información del formulario de usuario
     */
    public static class UserFormData {
        private final String name;
        private final String lastName;
        private final String email;

        public UserFormData(String name, String lastName, String email) {
            this.name = name;
            this.lastName = lastName;
            this.email = email;
        }

        public String getName() { return name; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        
        public boolean isValid() {
            return !name.trim().isEmpty() && !lastName.trim().isEmpty() && 
                   !email.trim().isEmpty() && email.contains("@");
        }
    }

    // ========== METODOS ADICIONALES PARA NUEVOS CASOS ==========

    /**
     * Valida error de campos requeridos
     * Requerimientos: TC014
     */
    public boolean hasRequiredFieldsError() {
        try {
            By requiredError = By.xpath("//div[contains(text(), 'requerido') or contains(text(), 'obligatorio') or contains(text(), 'necesario') or contains(text(), 'vacío')]");
            return !driver.findElements(requiredError).isEmpty() || hasValidationErrors();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida error de formato de email
     * Requerimientos: TC017
     */
    public boolean hasEmailFormatError() {
        try {
            By emailError = By.xpath("//div[contains(text(), 'email') and (contains(text(), 'formato') or contains(text(), 'válido') or contains(text(), 'correcto'))]");
            return !driver.findElements(emailError).isEmpty() || hasValidationErrors();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cancela la modificación (cerrar modal sin guardar)
     * Requerimientos: TC018
     */
    public void cancelChanges() {
        try {
            // Buscar botón de cancelar
            By cancelButton = By.xpath("//button[contains(., 'Cancelar') or contains(., 'Cancel') or contains(., 'Cerrar')]");
            
            if (!driver.findElements(cancelButton).isEmpty()) {
                driver.findElement(cancelButton).click();
            } else {
                // Intentar cerrar modal con botón X o tecla ESC
                By closeButton = By.xpath("//button[contains(@class, 'close') or contains(@aria-label, 'close')]");
                if (!driver.findElements(closeButton).isEmpty()) {
                    driver.findElement(closeButton).click();
                } else {
                    // Intentar tecla ESC
                    driver.findElement(modalDialog).sendKeys(org.openqa.selenium.Keys.ESCAPE);
                }
            }
        } catch (Exception e) {
            System.out.println("Advertencia: No se pudo cancelar el modal: " + e.getMessage());
        }
    }
}
