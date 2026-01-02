package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.TasksPage;
import pages.HomePage;
import utils.DriverManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SortTaskSteps {

    private TasksPage tasksPage;
    private HomePage homePage;

    /**
     * Ensures we're on the correct page before sorting operations
     */
    private void ensureOnTasksPage() {
        if (homePage == null) {
            homePage = new HomePage(DriverManager.getDriver());
            homePage.ensureOnHomePage();
        }
        if (tasksPage == null) {
            tasksPage = new TasksPage(DriverManager.getDriver());
        }
    }

    @When("ordena las tareas por prioridad")
    public void ordena_las_tareas_por_prioridad() {
        ensureOnTasksPage();
        tasksPage.clickPriorityHeader();
    }

    @When("ordena las tareas por titulo")
    public void ordena_las_tareas_por_titulo() {
        ensureOnTasksPage();
        tasksPage.sortByTitle();
    }

    @When("ordena las tareas por fecha termino")
    public void ordena_las_tareas_por_fecha_termino() {
        ensureOnTasksPage();
        tasksPage.sortByEndDate();
    }

    @Then("las tareas quedan ordenadas por prioridad")
    public void valida_orden_por_prioridad() {
        List<Integer> actual = tasksPage.getPriorities();
        List<Integer> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        assertEquals(expected, actual);
    }

    @Then("las tareas quedan ordenadas alfabeticamente por titulo")
    public void las_tareas_quedan_ordenadas_alfabeticamente_por_titulo() {
        assertTrue("Tasks should be sorted alphabetically by title", tasksPage.isTasksSortedByTitle());
    }

    @Then("las tareas quedan ordenadas por fecha termino")
    public void las_tareas_quedan_ordenadas_por_fecha_termino() {
        assertTrue("Tasks should be sorted chronologically by end date", tasksPage.isTasksSortedByDate());
    }
}
