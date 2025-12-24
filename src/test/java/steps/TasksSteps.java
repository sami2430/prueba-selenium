package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.TasksPage;
import utils.DriverManager;

import java.util.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TasksSteps {

    private TasksPage tasksPage;
    private List<Integer> originalPriorities;
    private List<String> originalDates;

    @When("el usuario ordena las tareas por prioridad")
    public void ordenar_por_prioridad() {
        tasksPage = new TasksPage(DriverManager.getDriver());
        originalPriorities = tasksPage.getPriorities();
        tasksPage.clickPriorityHeader();
    }

    @Then("las tareas quedan ordenadas por prioridad ascendente")
    public void validar_orden_prioridad() {
        List<Integer> sorted = new ArrayList<>(originalPriorities);
        Collections.sort(sorted);
        assertEquals(sorted, tasksPage.getPriorities());
    }


    @When("el usuario ordena las tareas por fecha termino")
    public void ordenar_por_fecha() {
        tasksPage = new TasksPage(DriverManager.getDriver());
        originalDates = tasksPage.getEndDates();
        tasksPage.clickEndDateHeader();
    }

    @Then("las tareas quedan ordenadas por fecha termino")
    public void validar_orden_fecha() {
        List<String> sorted = new ArrayList<>(originalDates);
        Collections.sort(sorted);
        assertEquals(sorted, tasksPage.getEndDates());
    }

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
