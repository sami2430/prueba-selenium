package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.LoginPage;
import pages.RegisterUserModal;
import utils.DriverManager;

public class RegisterUserSteps {

    private LoginPage loginPage;
    private RegisterUserModal registerUserModal;

    @Given("el usuario se encuentra en la pantalla de login")
    public void el_usuario_se_encuentra_en_la_pantalla_de_login() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open("http://192.168.80.43:10100");
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
}
