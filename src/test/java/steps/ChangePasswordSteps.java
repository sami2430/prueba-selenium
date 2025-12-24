package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.ProfilePage;
import pages.ChangePasswordModal;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class ChangePasswordSteps {

    private ProfilePage profilePage;
    private ChangePasswordModal changePasswordModal;

    @When("el usuario abre el formulario de cambiar contrasena")
    public void open_change_password() {
        profilePage = new ProfilePage(DriverManager.getDriver());
        profilePage.clickChangePassword();
        changePasswordModal = new ChangePasswordModal(DriverManager.getDriver());
    }

    @When("ingresa contrasena actual y nueva contrasena valida")
    public void valid_password_change() {
        changePasswordModal.fillPasswords("123", "Nueva123", "Nueva123");
        changePasswordModal.submit();
    }

    @When("ingresa contrasenas nuevas distintas")
    public void invalid_password_change() {
        changePasswordModal.fillPasswords("Nueva123", "Test123", "Otro123");
        changePasswordModal.submit();
    }

    @Then("se muestra mensaje de contrasena modificada correctamente")
    public void success_message() {
        assertTrue(changePasswordModal.isSuccessVisible());
    }

    @Then("se muestra mensaje de error de contrasena")
    public void error_message() {
        assertTrue(changePasswordModal.isErrorVisible());
    }
}
