package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import utils.DriverManager;

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

    @Then("se muestra la pantalla principal")
    public void se_muestra_la_pantalla_principal() {
        System.out.println("Login ejecutado correctamente");
        DriverManager.quitDriver();
    
    //El then para poder ver por mas tiempo la pantalla
    /*@Then("se muestra la pantalla principal")
    public void se_muestra_la_pantalla_principal() throws InterruptedException {
        Thread.sleep(5000); // VERIFICACIÓN VISUAL
        DriverManager.quitDriver();*/
    }
}
