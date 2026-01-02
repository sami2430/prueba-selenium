package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.HomePage;
import pages.CreateTaskModal;
import utils.DriverManager;
import utils.TestDataGenerator;
import utils.TestDataGenerator.TaskTestData;

import static org.junit.Assert.assertTrue;

public class CreateTaskSteps {

    private HomePage homePage;
    private CreateTaskModal createTaskModal;
    private int initialTaskCount;
    private String createdTaskTitle;

    // =========================
    // CASO: Creación exitosa de tarea
    // =========================

    @When("el usuario abre el formulario de nueva tarea")
    public void abre_formulario_nueva_tarea() {
        homePage = new HomePage(DriverManager.getDriver());
        // CRÍTICO: Asegurar que estamos en la página Home antes de intentar crear tareas
        homePage.ensureOnHomePage();
        
        // Almacenar el conteo inicial de tareas antes de crear una nueva
        initialTaskCount = homePage.getTaskCount();
        homePage.clickNuevaTarea();
        createTaskModal = new CreateTaskModal(DriverManager.getDriver());
    }

    @When("ingresa datos validos de la tarea")
    public void ingresa_datos_validos() {
        // Generar datos únicos de tarea para cada ejecución de prueba
        TaskTestData taskData = TestDataGenerator.generateTaskTestData();
        
        createdTaskTitle = taskData.getTitle();
        createTaskModal.fillTitle(taskData.getTitle());
        createTaskModal.fillDescription(taskData.getDescription());
        createTaskModal.fillPriority(taskData.getPriority());
        // CRÍTICO: Llenar el campo de fecha que descubrimos que faltaba
        createTaskModal.fillDateWithDefault();
    }

    @When("confirma la creacion de la tarea")
    public void guarda_tarea() {
        createTaskModal.submit();
    }

    /**
     * Implementación completa del paso para creación de tarea con datos válidos
     * Requisitos: 5.1
     */
    @When("el usuario crea una nueva tarea con datos validos")
    public void el_usuario_crea_una_nueva_tarea_con_datos_validos() {
        // Paso 0: CRÍTICO - Asegurar que estamos en la página Home (no dashboard)
        homePage = new HomePage(DriverManager.getDriver());
        homePage.ensureOnHomePage();
        
        // Paso 1: Almacenar el conteo inicial de tareas
        initialTaskCount = homePage.getTaskCount();
        
        // Paso 2: Abrir el modal de creación de tarea
        homePage.clickNuevaTarea();
        createTaskModal = new CreateTaskModal(DriverManager.getDriver());
        
        // Paso 3: Generar y llenar el formulario con datos únicos
        TaskTestData taskData = TestDataGenerator.generateTaskTestData();
        createdTaskTitle = taskData.getTitle();
        
        createTaskModal.fillTitle(taskData.getTitle());
        createTaskModal.fillDescription(taskData.getDescription());
        createTaskModal.fillPriority(taskData.getPriority());
        // CRÍTICO: Llenar el campo de fecha que descubrimos que faltaba
        createTaskModal.fillDateWithDefault();
        
        // Paso 4: Enviar el formulario
        createTaskModal.submit();
    }

    /**
     * Validación mejorada para el éxito de creación de tarea
     * Requisitos: 5.1, 5.2, 5.3
     */
    @Then("la tarea se muestra en la lista de tareas")
    public void valida_tarea_creada() {
        // Verificar que el modal se cerró (indica creación exitosa)
        assertTrue("El modal de creación de tarea debe cerrarse después de una creación exitosa", 
                   createTaskModal.isTaskCreationSuccessful());
        
        // Validación integral: conteo de tareas aumentó, tarea aparece con datos correctos
        assertTrue("La tarea debe crearse exitosamente y aparecer en la lista de tareas con datos correctos", 
                   homePage.isTaskCreationSuccessful(initialTaskCount, createdTaskTitle));
    }

    /**
     * Validación mejorada para el paso "la tarea se muestra en la lista" con verificación de persistencia de datos
     * Requisitos: 5.1, 5.2
     */
    @Then("la tarea se muestra en la lista")
    public void la_tarea_se_muestra_en_la_lista() {
        // Verificar que el modal se cerró (indica creación exitosa)
        assertTrue("El modal de creación de tarea debe cerrarse después de una creación exitosa", 
                   createTaskModal.isTaskCreationSuccessful());
        
        // Verificar que la tarea aparece en la lista y el conteo de tareas aumentó
        assertTrue("La tarea debe aparecer en la lista de tareas", homePage.isTaskListNotEmpty());
        
        // Verificar que el conteo de tareas aumentó en uno
        int currentCount = homePage.getTaskCount();
        assertTrue("El conteo de tareas debe aumentar después de una creación exitosa. Esperado: > " + initialTaskCount + ", Actual: " + currentCount, 
                   currentCount > initialTaskCount);
        
        // Si tenemos un título de tarea específico, verificar que existe
        if (createdTaskTitle != null && !createdTaskTitle.isEmpty()) {
            assertTrue("La tarea creada debe ser visible en la lista de tareas con título: " + createdTaskTitle, 
                       homePage.validateLatestTaskData(createdTaskTitle));
        }
    }

    // =========================
    // CASO: Creación errónea de tarea
    // =========================

    @When("intenta crear una tarea sin titulo")
    public void crear_tarea_sin_titulo() {
        // Generar descripción realista pero dejar título vacío
        TaskTestData taskData = TestDataGenerator.generateTaskTestData();
        
        createTaskModal.fillTitle(""); // Título vacío para prueba negativa
        createTaskModal.fillDescription(taskData.getDescription());
        createTaskModal.fillPriority(taskData.getPriority());
        // Aún llenar el campo de fecha para prueba negativa
        createTaskModal.fillDateWithDefault();
        createTaskModal.submit();
    }

    /**
     * Validación mejorada para fallo de creación de tarea
     * Requisitos: 5.4 (manejo de errores)
     */
    @Then("la tarea no se crea")
    public void valida_tarea_no_creada() {
        // Verificar que el modal sigue visible (creación falló)
        assertTrue("El modal debe permanecer visible cuando la creación de tarea falla", 
                   createTaskModal.isModalStillVisible());
        
        // Verificar que el conteo de tareas no aumentó
        int currentCount = homePage.getTaskCount();
        assertTrue("El conteo de tareas no debe aumentar cuando la creación falla. Esperado: " + initialTaskCount + ", Actual: " + currentCount, 
                   currentCount == initialTaskCount);
    }

    /**
     * Paso alternativo para validación de fallo de creación de tarea
     * Requisitos: 5.4 (manejo de errores)
     */
    @Then("la tarea no es creada")
    public void la_tarea_no_es_creada() {
        // Delegar a la implementación existente
        valida_tarea_no_creada();
    }
}
