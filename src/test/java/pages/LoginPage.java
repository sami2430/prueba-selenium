package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String URL = "http://192.168.80.43:10100";

    private By emailInput = By.xpath("//input[@type='email']");
    private By passwordInput = By.xpath("//input[@type='password']");
    private By loginButton = By.xpath("//button[@type='submit']");

    private By createNewUserButton =
            By.xpath("//button[.//text()[contains(.,'Crear Nuevo Usuario')]]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
    }

    public void login(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    public void clickCreateNewUser() {
        wait.until(ExpectedConditions.elementToBeClickable(createNewUserButton)).click();
    }
}
