package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    private LoginPage loginPage;

    @Given("el usuario esta en la pagina de login")
    public void el_usuario_esta_en_la_pagina_de_login() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
    }

    //el when para probar con una contraseña incorrecta

    /*@When("ingresa email y contrasena validos")
    public void ingresa_email_y_contrasena_validos() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(
            "scastro@sentra.cl",
            "scastro01"
    );*/

    //el when con las credenciales correctas para el login

    @When("ingresa email y contrasena validos")
    public void ingresa_email_y_contrasena_validos() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(
            "scastro@sentra.cl",
            "123"
    );

    }

    /**
     * Validates successful login with proper assertions
     * Requirements: 4.1, 4.2
     */
    @Then("se muestra la pantalla principal")
    public void se_muestra_la_pantalla_principal() {
        assertTrue("Login should be successful - user should be redirected to home page", 
                   loginPage.isLoginSuccessful());
        
        assertTrue("User-specific elements should be displayed after successful login", 
                   loginPage.areUserElementsDisplayed());
    }

    /**
     * Step for entering invalid login credentials
     * Requirements: 4.4
     */
    @When("ingresa credenciales invalidas")
    public void ingresa_credenciales_invalidas() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login("usuario@invalido.com", "passwordIncorrecto");
    }

    /**
     * Step for entering empty credentials
     * Requirements: 4.4
     */
    @When("ingresa credenciales vacias")
    public void ingresa_credenciales_vacias() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login("", "");
    }

    /**
     * Validates login error scenarios
     * Requirements: 4.4
     */
    @Then("se muestra un mensaje de error de login")
    public void se_muestra_un_mensaje_de_error_de_login() {
        assertTrue("Login error message should be displayed for invalid credentials", 
                   loginPage.hasLoginError());
    }

    /**
     * Validates that user remains on login page after failed login
     * Requirements: 4.4
     */
    @Then("permanece en la pagina de login")
    public void permanece_en_la_pagina_de_login() {
        assertTrue("User should remain on login page after failed login attempt", 
                   loginPage.isStillOnLoginPage());
    }
    
    //El then para poder ver por mas tiempo la pantalla
    /*@Then("se muestra la pantalla principal")
    public void se_muestra_la_pantalla_principal() throws InterruptedException {
        Thread.sleep(5000); // VERIFICACIÓN VISUAL
        DriverManager.quitDriver();*/
}
