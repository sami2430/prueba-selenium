package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.HomePage;
import pages.LoginPage;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class LogoutSteps {

    private HomePage homePage;
    private LoginPage loginPage;

    @Given("el usuario esta logueado en el sistema")
    public void el_usuario_esta_logueado_en_el_sistema() {
        // Primero hacer login con credenciales válidas
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
        
        // Usar credenciales de usuario existente para pruebas de logout
        loginPage.fillEmail("scastro@sentra.cl");
        loginPage.fillPassword("123456");
        loginPage.submit();
        
        // Navegar a la página home después del login
        homePage = new HomePage(DriverManager.getDriver());
        homePage.navigateToHome();
        homePage.ensureOnHomePage();
    }

    @When("hace click en logout")
    public void hace_click_en_logout() {
        homePage.logout();
    }

    @Then("el usuario es redirigido a la pantalla de login")
    public void el_usuario_es_redirigido_a_la_pantalla_de_login() {
        loginPage = new LoginPage(DriverManager.getDriver());
        assertTrue("El usuario debe ser redirigido a la página de login después del logout", 
                   loginPage.isOnLoginPage());
    }

    @Then("la sesion se cierra correctamente")
    public void la_sesion_se_cierra_correctamente() {
        // Verificar que la sesión se cerró comprobando que no podemos acceder a páginas protegidas
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        assertTrue("La sesión debe cerrarse - debe estar en la página de login", 
                   currentUrl.contains("login") || currentUrl.contains("auth"));
    }

    @When("intenta acceder a una pagina protegida")
    public void intenta_acceder_a_una_pagina_protegida() {
        // Intentar acceder a la página home directamente
        DriverManager.getDriver().get("http://localhost:3000/home");
    }

    @Then("es redirigido automaticamente al login")
    public void es_redirigido_automaticamente_al_login() {
        // Esperar un momento para posible redirección
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        assertTrue("Debe ser redirigido al login al acceder a página protegida sin sesión", 
                   currentUrl.contains("login") || currentUrl.contains("auth"));
    }
}