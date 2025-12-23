package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Titulo del perfil (nombre del usuario)
    private By nameText = By.xpath("//h1 | //h4");

    // Boton PERFIL del menu lateral
    private By profileMenu =
            By.xpath("//span[normalize-space()='Perfil']");

    // Boton EDITAR DATOS USUARIO
    private By editUserButton =
            By.xpath("//button[normalize-space()='Editar Datos Usuario']");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void goToProfile() {
        WebElement perfil = wait.until(
                ExpectedConditions.presenceOfElementLocated(profileMenu)
        );
        perfil.click();
    }

    public boolean isProfileVisible() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(nameText)
        ).isDisplayed();
    }

    public void clickEditUserButton() {
        WebElement button = wait.until(
                ExpectedConditions.presenceOfElementLocated(editUserButton)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", button
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", button
        );
    }
}
