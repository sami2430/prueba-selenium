package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.HomePage;
import pages.CreateTaskModal;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class CreateTaskSteps {

    private HomePage homePage;
    private CreateTaskModal createTaskModal;

    @When("el usuario abre el formulario de nueva tarea")
    public void abre_formulario_nueva_tarea() {
        homePage = new HomePage(DriverManager.getDriver());
        homePage.clickNuevaTarea();
        createTaskModal = new CreateTaskModal(DriverManager.getDriver());
    }

    @When("ingresa datos validos de la tarea")
    public void ingresa_datos_validos() {
        createTaskModal.fillTitle("Tarea QA");
        createTaskModal.fillDescription("Descripcion automatizada");
        createTaskModal.fillPriority(2);
    }

    @When("guarda la nueva tarea")
    public void guarda_tarea() {
        createTaskModal.submit();
    }

    @Then("la tarea se muestra en la lista de tareas")
    public void valida_tarea_creada() {
        assertTrue(homePage.isTaskListNotEmpty());
    }

    @When("intenta crear una tarea sin titulo")
    public void crear_tarea_sin_titulo() {
        createTaskModal.fillDescription("Sin titulo");
        createTaskModal.fillPriority(1);
        createTaskModal.submit();
    }

    @Then("la tarea no se crea")
    public void valida_tarea_no_creada() {
        assertTrue(createTaskModal.isModalStillVisible());
    }
}
