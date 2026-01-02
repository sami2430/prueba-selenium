package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    private LoginPage loginPage;

    // =========================
    // CASO: Login exitoso
    // =========================

    @Given("el usuario esta en la pagina de login")
    public void el_usuario_esta_en_la_pagina_de_login() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
    }

    @When("confirma sus credenciales de acceso")
    public void ingresa_email_y_contrasena_validos() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(
            "scastro@sentra.cl",
            "123"
        );
    }

    /**
     * Valida el login exitoso con validaciones apropiadas
     * Requerimientos: 4.1, 4.2
     */
    @Then("se muestra la pantalla principal")
    public void se_muestra_la_pantalla_principal() {
        assertTrue("El login debe ser exitoso - el usuario debe ser redirigido a la página principal", 
                   loginPage.isLoginSuccessful());
        
        assertTrue("Los elementos específicos del usuario deben mostrarse después del login exitoso", 
                   loginPage.areUserElementsDisplayed());
    }

    // =========================
    // CASO: Login erróneo
    // =========================

    /**
     * Paso para ingresar credenciales de login inválidas
     * Requerimientos: 4.4
     */
    @When("ingresa credenciales invalidas")
    public void ingresa_credenciales_invalidas() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login("usuario@invalido.com", "passwordIncorrecto");
    }

    /**
     * Paso para ingresar credenciales vacías
     * Requerimientos: 4.4
     */
    @When("ingresa credenciales vacias")
    public void ingresa_credenciales_vacias() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login("", "");
    }

    /**
     * Valida escenarios de error en el login
     * Requerimientos: 4.4
     */
    @Then("se muestra un mensaje de error de login")
    public void se_muestra_un_mensaje_de_error_de_login() {
        assertTrue("Debe mostrarse un mensaje de error de login para credenciales inválidas", 
                   loginPage.hasLoginError());
    }

    /**
     * Valida que el usuario permanece en la página de login después de un login fallido
     * Requerimientos: 4.4
     */
    @Then("permanece en la pagina de login")
    public void permanece_en_la_pagina_de_login() {
        assertTrue("El usuario debe permanecer en la página de login después de un intento fallido", 
                   loginPage.isStillOnLoginPage());
    }
}
