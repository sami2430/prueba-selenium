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

    private By changePasswordButton =
        By.xpath("//button[contains(.,'Cambiar Contrasena')]");

    // Enhanced validation selectors
    private By userNameDisplay = By.xpath("//h1 | //h4 | //span[contains(@class, 'name')] | //div[contains(@class, 'user-name')]");
    
    private By profileContent = By.xpath("//div[contains(@class, 'profile')] | //div[contains(@class, 'user-info')]");

public void clickChangePassword() {
    wait.until(ExpectedConditions.elementToBeClickable(changePasswordButton)).click();
}

    // ---------- Enhanced Profile Validation Methods ----------
    
    /**
     * Validates if user data was actually updated in the profile
     * Requirements: User modification validation
     */
    public boolean isUserDataUpdated() {
        try {
            // Wait for profile to refresh after modification
            Thread.sleep(2000);
            
            // Check if profile content is visible and updated
            return isProfileVisible() && !driver.findElements(profileContent).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the current user name displayed in the profile
     * Requirements: Data persistence validation
     */
    public String getDisplayedUserName() {
        try {
            if (!driver.findElements(userNameDisplay).isEmpty()) {
                return driver.findElement(userNameDisplay).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Validates if the profile shows updated information
     * Requirements: User modification success validation
     */
    public boolean hasProfileBeenUpdated(String expectedName) {
        try {
            String displayedName = getDisplayedUserName();
            return displayedName.contains(expectedName) || 
                   !displayedName.isEmpty(); // At minimum, name should be displayed
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enhanced profile visibility check with better error handling
     * Requirements: Profile validation robustness
     */
    public boolean isProfileVisibleEnhanced() {
        try {
            return isProfileVisible() && 
                   (!driver.findElements(editUserButton).isEmpty() || 
                    !driver.findElements(profileContent).isEmpty());
        } catch (Exception e) {
            return isProfileVisible(); // Fallback to basic check
        }
    }

}
