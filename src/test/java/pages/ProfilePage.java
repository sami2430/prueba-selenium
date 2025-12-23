package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Texto nombre usuario (confirmar que estamos en perfil)
    private By nameText = By.xpath("//h4");

    // Boton PERFIL en menu lateral
    private By profileMenu =
            By.xpath("//span[normalize-space()='Perfil']");

    // Boton EDITAR DATOS USUARIO
    private By editUserButton =
            By.xpath("//button[normalize-space()='Editar Datos Usuario']");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Ir explicitamente a perfil
    public void goToProfile() {
        wait.until(ExpectedConditions.elementToBeClickable(profileMenu)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameText));
    }

    public boolean isProfileVisible() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(nameText)
        ).isDisplayed();
    }

    public void openEditUserModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(editUserButton));

        // Scroll obligatorio (MUI)
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);",
                        driver.findElement(editUserButton));

        wait.until(ExpectedConditions.elementToBeClickable(editUserButton)).click();
    }
}
