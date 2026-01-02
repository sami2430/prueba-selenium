package steps;

import io.cucumber.java.en.Given;
import pages.LoginPage;
import pages.HomePage;
import utils.DriverManager;

public class CommonSteps {

    // =========================
    // CASO: Configuración inicial de sesión
    // =========================

    @Given("el usuario ha iniciado sesion correctamente")
    public void usuario_logueado() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
        loginPage.login("scastro@sentra.cl", "123");
        
        // CRITICAL FIX: Navigate to Home after login since app redirects to /dashboard (blank screen)
        HomePage homePage = new HomePage(DriverManager.getDriver());
        homePage.navigateToHome();
    }

    // =========================
    // CASO: Navegación a pantalla principal
    // =========================

    @Given("el usuario se encuentra en la pantalla Home")
    public void el_usuario_se_encuentra_en_la_pantalla_home() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        // Ensure we are properly navigated to Home page for task operations
        homePage.ensureOnHomePage();
    }

    @Given("el usuario se encuentra en la pantalla home")
    public void el_usuario_se_encuentra_en_la_pantalla_home_lowercase() {
        // Delegate to the existing implementation
        el_usuario_se_encuentra_en_la_pantalla_home();
    }
}
