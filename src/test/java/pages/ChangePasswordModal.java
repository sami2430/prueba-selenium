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
}
