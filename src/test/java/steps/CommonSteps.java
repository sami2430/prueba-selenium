package steps;

import io.cucumber.java.en.Given;
import pages.LoginPage;
import utils.DriverManager;

public class CommonSteps {

    @Given("el usuario ha iniciado sesion correctamente")
    public void usuario_logueado() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
        loginPage.login("scastro@sentra.cl", "123");
    }
}
