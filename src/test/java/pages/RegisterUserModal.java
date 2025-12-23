package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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

    // BOTÓN CORRECTO
    private By submitButton =
            By.xpath("//div[@role='dialog']//button[.//text()[contains(.,'Registrar')]]");

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
}
