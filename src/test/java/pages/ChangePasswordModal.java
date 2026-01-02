package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ChangePasswordModal {

    private WebDriver driver;
    private WebDriverWait wait;

    private By currentPassword =
            By.xpath("//input[@type='password'][1]");

    private By newPassword =
            By.xpath("//input[@type='password'][2]");

    private By repeatPassword =
            By.xpath("//input[@type='password'][3]");

    private By saveButton =
            By.xpath("//button[contains(.,'Cambiar contrasena')]");

    private By successMessage =
            By.xpath("//div[contains(text(),'contrasena')]");

    private By errorMessage =
            By.xpath("//div[contains(@class,'MuiAlert-message')]");

    public ChangePasswordModal(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillPasswords(String current, String newPass, String repeat) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(currentPassword)).sendKeys(current);
        driver.findElement(newPassword).sendKeys(newPass);
        driver.findElement(repeatPassword).sendKeys(repeat);
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public boolean isSuccessVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
    }

    public boolean isErrorVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
    }

    // ========== METODOS ADICIONALES PARA NUEVOS CASOS ==========

    /**
     * Valida error de campos requeridos
     * Requerimientos: TC021
     */
    public boolean hasRequiredFieldsError() {
        try {
            By requiredError = By.xpath("//div[contains(text(), 'requerido') or contains(text(), 'obligatorio') or contains(text(), 'necesario') or contains(text(), 'vacío')]");
            return !driver.findElements(requiredError).isEmpty() || isErrorVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida si el modal aún está abierto
     * Requerimientos: TC021, TC022
     */
    public boolean isModalStillOpen() {
        try {
            return !driver.findElements(saveButton).isEmpty() || 
                   !driver.findElements(By.cssSelector("div[role='dialog'], .modal")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cancela el cambio de contraseña
     * Requerimientos: TC022
     */
    public void cancelChange() {
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
                    By modalDialog = By.cssSelector("div[role='dialog']");
                    if (!driver.findElements(modalDialog).isEmpty()) {
                        driver.findElement(modalDialog).sendKeys(org.openqa.selenium.Keys.ESCAPE);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Advertencia: No se pudo cancelar el modal de cambio de contraseña: " + e.getMessage());
        }
    }
}
