package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import pages.RegisterUserModal;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class RegisterUserSteps {

    private LoginPage loginPage;
    private RegisterUserModal registerUserModal;

    @Given("el usuario se encuentra en la pantalla de login")
    public void el_usuario_se_encuentra_en_la_pantalla_de_login() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
    }

    @When("abre el formulario de crear nuevo usuario")
    public void abre_el_formulario_de_crear_nuevo_usuario() {
        loginPage.clickCreateNewUser();
        registerUserModal = new RegisterUserModal(DriverManager.getDriver());
    }

    @When("ingresa datos validos de registro")
    public void ingresa_datos_validos_de_registro() {
        String email = "test_" + System.currentTimeMillis() + "@mail.com";

        registerUserModal.fillName("Test");
        registerUserModal.fillLastName("User");
        registerUserModal.fillEmail(email);
        registerUserModal.fillPassword("Password123");
        registerUserModal.submit();
    }

    /**
     * Validates successful user registration
     * Requirements: 3.1, 3.2
     */
    @Then("el usuario se registra exitosamente")
    public void el_usuario_se_registra_exitosamente() {
        assertTrue("Registration should be successful - success message should be displayed or page should redirect", 
                   registerUserModal.isRegistrationSuccessful());
    }

    /**
     * Step for entering invalid registration data
     * Requirements: 3.4
     */
    @When("ingresa datos invalidos de registro")
    public void ingresa_datos_invalidos_de_registro() {
        // Use invalid data - empty email or duplicate email
        registerUserModal.fillName("Test");
        registerUserModal.fillLastName("User");
        registerUserModal.fillEmail(""); // Invalid: empty email
        registerUserModal.fillPassword("Password123");
        registerUserModal.submit();
    }

    /**
     * Step for entering duplicate email registration data
     * Requirements: 3.4
     */
    @When("ingresa un email ya registrado")
    public void ingresa_un_email_ya_registrado() {
        // Use a known existing email
        registerUserModal.fillName("Test");
        registerUserModal.fillLastName("User");
        registerUserModal.fillEmail("scastro@sentra.cl"); // Existing user email
        registerUserModal.fillPassword("Password123");
        registerUserModal.submit();
    }

    /**
     * Validates registration error scenarios
     * Requirements: 3.4
     */
    @Then("se muestra un mensaje de error de registro")
    public void se_muestra_un_mensaje_de_error_de_registro() {
        assertTrue("Registration error message should be displayed", 
                   registerUserModal.hasRegistrationError());
    }

    /**
     * Validates that registration was prevented
     * Requirements: 3.4
     */
    @Then("el registro no se completa")
    public void el_registro_no_se_completa() {
        // Registration should fail - either error message shown or modal still open
        boolean hasError = registerUserModal.hasRegistrationError();
        boolean registrationFailed = !registerUserModal.isRegistrationSuccessful();
        
        assertTrue("Registration should be prevented when invalid data is provided", 
                   hasError || registrationFailed);
    }
}
