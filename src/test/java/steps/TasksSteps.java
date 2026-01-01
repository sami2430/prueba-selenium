package steps;

import io.cucumber.java.en.Then;
import pages.TasksPage;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class TasksSteps {

    private TasksPage tasksPage;



    @Then("se muestra la lista de tareas")
    public void se_muestra_la_lista_de_tareas() {
        tasksPage = new TasksPage(DriverManager.getDriver());
        assertTrue(tasksPage.hasTasks());
    }

    @Then("cada tarea tiene titulo fecha creacion fecha vencimiento y prioridad")
    public void cada_tarea_tiene_datos_completos() {
    assertTrue(tasksPage.allTasksHaveTitle());
    assertTrue(tasksPage.allTasksHaveCreationDate());
    assertTrue(tasksPage.allTasksHaveEndDate());
    assertTrue(tasksPage.allTasksHavePriority());
    }

}
