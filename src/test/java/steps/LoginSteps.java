package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import utils.DriverManager;

public class LoginSteps {

    @Given("el usuario esta en la pagina de login")
    public void el_usuario_esta_en_la_pagina_de_login() {
        DriverManager.getDriver()
                .get("http://192.168.80.43:10100");
    }

    @When("ingresa email y contrasena validos")
    public void ingresa_email_y_contrasena_validos() {
        System.out.println("Aquí luego escribiremos Selenium real");
    }

    @Then("se muestra la pantalla principal")
    public void se_muestra_la_pantalla_principal() {
        System.out.println("Validación pendiente");
        DriverManager.quitDriver();
    }
}
