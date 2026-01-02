package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.ProfilePage;
import pages.ChangePasswordModal;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class ChangePasswordSteps {

    private ProfilePage profilePage;
    private ChangePasswordModal changePasswordModal;
    
    // Variables para almacenar el estado del cambio de contraseña
    private String oldPassword = "123456";
    private String newPassword = "NuevaPassword123";

    // =========================
    // CASO: Cambio de contraseña exitoso
    // =========================

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

    @When("ingresa nueva contrasena")
    public void ingresa_nueva_contrasena() {
        changePasswordModal.fillPasswords("123456", "NuevaPassword123", "NuevaPassword123");
    }

    @Then("se muestra mensaje de contrasena modificada correctamente")
    public void success_message() {
        assertTrue(changePasswordModal.isSuccessVisible());
    }

    @Given("el usuario ha cambiado su contrasena exitosamente")
    public void el_usuario_ha_cambiado_su_contrasena_exitosamente() {
        // Realizar cambio de contraseña
        profilePage = new ProfilePage(DriverManager.getDriver());
        profilePage.clickChangePassword();
        changePasswordModal = new ChangePasswordModal(DriverManager.getDriver());
        
        changePasswordModal.fillPasswords(oldPassword, newPassword, newPassword);
        changePasswordModal.submit();
        
        // Verificar éxito
        assertTrue("El cambio de contraseña debe ser exitoso", 
                   changePasswordModal.isSuccessVisible());
    }

    // =========================
    // CASO: Cambio de contraseña erróneo
    // =========================

    @When("ingresa contrasenas nuevas distintas")
    public void invalid_password_change() {
        changePasswordModal.fillPasswords("Nueva123", "Test123", "Otro123");
        changePasswordModal.submit();
    }

    @When("deja todos los campos de contrasena vacios")
    public void deja_todos_los_campos_de_contrasena_vacios() {
        // No llenar ningún campo de contraseña
        changePasswordModal.fillPasswords("", "", "");
    }

    @When("intenta guardar el cambio de contrasena")
    public void intenta_guardar_el_cambio_de_contrasena() {
        changePasswordModal.submit();
    }

    @Then("se muestra mensaje de error de contrasena")
    public void error_message() {
        assertTrue(changePasswordModal.isErrorVisible());
    }

    @Then("se muestra mensaje de error de campos requeridos")
    public void se_muestra_mensaje_de_error_de_campos_requeridos() {
        assertTrue("Debe mostrar error de campos requeridos", 
                   changePasswordModal.hasRequiredFieldsError());
    }

    @Then("el cambio no se realiza")
    public void el_cambio_no_se_realiza() {
        assertTrue("El cambio de contraseña no debe completarse", 
                   changePasswordModal.isModalStillOpen() || changePasswordModal.isErrorVisible());
    }

    // =========================
    // CASO: Cancelación de cambio de contraseña
    // =========================

    @When("cancela el cambio de contrasena")
    public void cancela_el_cambio_de_contrasena() {
        changePasswordModal.cancelChange();
    }

    @Then("el formulario se cierra sin cambiar la contrasena")
    public void el_formulario_se_cierra_sin_cambiar_la_contrasena() {
        assertTrue("El modal debe cerrarse después de cancelar", 
                   !changePasswordModal.isModalStillOpen());
    }

    @Then("la contrasena original se mantiene")
    public void la_contrasena_original_se_mantiene() {
        assertTrue("Debe regresar a la página de perfil", 
                   profilePage.isProfileVisible());
    }

    // =========================
    // CASO: Validación de contraseña antigua
    // =========================

    @When("cierra sesion")
    public void cierra_sesion() {
        // Navegar de vuelta al home y hacer logout
        pages.HomePage homePage = new pages.HomePage(DriverManager.getDriver());
        homePage.navigateToHome();
        homePage.logout();
    }

    @When("intenta iniciar sesion con la contrasena antigua")
    public void intenta_iniciar_sesion_con_la_contrasena_antigua() {
        pages.LoginPage loginPage = new pages.LoginPage(DriverManager.getDriver());
        loginPage.fillEmail("scastro@sentra.cl");
        loginPage.fillPassword(oldPassword); // Usar contraseña antigua
        loginPage.submit();
    }

    @Then("el login falla con la contrasena antigua")
    public void el_login_falla_con_la_contrasena_antigua() {
        pages.LoginPage loginPage = new pages.LoginPage(DriverManager.getDriver());
        assertTrue("El login debe fallar con la contraseña antigua", 
                   loginPage.hasLoginError() || loginPage.isStillOnLoginPage());
    }

    @Then("se muestra mensaje de credenciales invalidas")
    public void se_muestra_mensaje_de_credenciales_invalidas() {
        pages.LoginPage loginPage = new pages.LoginPage(DriverManager.getDriver());
        assertTrue("Debe mostrar error de credenciales inválidas", 
                   loginPage.hasLoginError());
    }
}