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
            By.xpath("//div[contains(text(),'modificado exitosamente')]");

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
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage))
                .isDisplayed();
    }
}
