package steps;

import io.cucumber.java.en.Given;
import pages.LoginPage;
import pages.HomePage;
import utils.DriverManager;

public class CommonSteps {

    @Given("el usuario ha iniciado sesion correctamente")
    public void usuario_logueado() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
        loginPage.login("scastro@sentra.cl", "123");
    }

    @Given("el usuario se encuentra en la pantalla Home")
    public void el_usuario_se_encuentra_en_la_pantalla_home() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        // Verify we are on the home page - this step ensures we're in the right context for sorting
        homePage.isTaskListNotEmpty();
    }
}
