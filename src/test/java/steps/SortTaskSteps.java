package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.TasksPage;
import utils.DriverManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SortTaskSteps {

    private TasksPage tasksPage;

    @When("ordena las tareas por prioridad")
    public void ordena_las_tareas_por_prioridad() {
        tasksPage = new TasksPage(DriverManager.getDriver());
        tasksPage.clickPriorityHeader();
    }

    @Then("las tareas quedan ordenadas por prioridad")
    public void valida_orden_por_prioridad() {
        List<Integer> actual = tasksPage.getPriorities();
        List<Integer> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        assertEquals(expected, actual);
    }
}
