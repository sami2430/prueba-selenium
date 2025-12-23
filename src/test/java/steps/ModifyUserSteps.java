package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import pages.ProfilePage;
import pages.EditUserModal;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class ModifyUserSteps {

    private LoginPage loginPage;
    private ProfilePage profilePage;
    private EditUserModal editUserModal;

    @Given("el usuario ha iniciado sesion correctamente")
    public void el_usuario_ha_iniciado_sesion_correctamente() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
        loginPage.login("scastro@sentra.cl", "123");
    }

    @Given("el usuario se encuentra en la pantalla de perfil")
public void el_usuario_se_encuentra_en_la_pantalla_de_perfil() {
    profilePage = new ProfilePage(DriverManager.getDriver());
    profilePage.goToProfile();
    assertTrue(profilePage.isProfileVisible());
}

@When("el usuario abre el formulario de edicion de datos")
public void el_usuario_abre_el_formulario_de_edicion_de_datos() {
    profilePage.openEditUserModal();
    editUserModal = new EditUserModal(DriverManager.getDriver());
}




    @When("modifica su nombre y apellido con datos validos")
    public void modifica_su_nombre_y_apellido_con_datos_validos() {
        editUserModal.updateName("Sthefanie QA");
        editUserModal.updateLastName("Castro Test");
    }

    @When("guarda los cambios del perfil")
    public void guarda_los_cambios_del_perfil() {
        editUserModal.saveChanges();
    }

    @Then("se muestra un mensaje de confirmacion de modificacion exitosa")
    public void se_muestra_un_mensaje_de_confirmacion_de_modificacion_exitosa() {
        assertTrue(editUserModal.isSuccessMessageVisible());
    }

    @Then("los datos actualizados se reflejan en el perfil del usuario")
    public void los_datos_actualizados_se_reflejan_en_el_perfil_del_usuario() {
        assertTrue(profilePage.isProfileVisible());
        DriverManager.quitDriver();
    }
}
