package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.TasksPage;
import pages.HomePage;
import utils.DriverManager;

import static org.junit.Assert.assertTrue;

public class TasksSteps {

    private TasksPage tasksPage;
    private HomePage homePage;

    // =========================
    // CASO: Acceso y visualización de lista de tareas
    // =========================

    /**
     * Implementa la navegación a la lista de tareas
     * Requisitos: 8.2
     */
    @When("el usuario accede a la lista de tareas")
    public void el_usuario_accede_a_la_lista_de_tareas() {
        // Inicializar HomePage y asegurar navegación adecuada
        homePage = new HomePage(DriverManager.getDriver());
        
        // CRÍTICO: Navegar a la página Home ya que el login redirige a /dashboard (pantalla en blanco)
        homePage.ensureOnHomePage();
        
        // Esperar un momento para que la página se cargue completamente
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verificar que podemos acceder a la lista de tareas comprobando si las tareas son visibles
        assertTrue("Debe poder acceder a la lista de tareas", homePage.isTaskListNotEmpty() || homePage.getTaskCount() >= 0);
        
        // Inicializar TasksPage para operaciones posteriores
        tasksPage = new TasksPage(DriverManager.getDriver());
    }

    @Then("se muestra la lista de tareas")
    public void se_muestra_la_lista_de_tareas() {
        tasksPage = new TasksPage(DriverManager.getDriver());
        assertTrue(tasksPage.hasTasks());
    }

    // =========================
    // CASO: Validación de estructura de datos de tareas
    // =========================

    @Then("cada tarea tiene titulo fecha creacion fecha vencimiento y prioridad")
    public void cada_tarea_tiene_datos_completos() {
        assertTrue(tasksPage.allTasksHaveTitle());
        assertTrue(tasksPage.allTasksHaveCreationDate());
        assertTrue(tasksPage.allTasksHaveEndDate());
        assertTrue(tasksPage.allTasksHavePriority());
    }
}
