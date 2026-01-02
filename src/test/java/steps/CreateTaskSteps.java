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

    @When("el usuario abre el formulario de nueva tarea")
    public void abre_formulario_nueva_tarea() {
        homePage = new HomePage(DriverManager.getDriver());
        // CRITICAL: Ensure we're on Home page before trying to create tasks
        homePage.ensureOnHomePage();
        
        // Store initial task count before creating new task
        initialTaskCount = homePage.getTaskCount();
        homePage.clickNuevaTarea();
        createTaskModal = new CreateTaskModal(DriverManager.getDriver());
    }

    @When("ingresa datos validos de la tarea")
    public void ingresa_datos_validos() {
        // Generate unique task data for each test run
        TaskTestData taskData = TestDataGenerator.generateTaskTestData();
        
        createdTaskTitle = taskData.getTitle();
        createTaskModal.fillTitle(taskData.getTitle());
        createTaskModal.fillDescription(taskData.getDescription());
        createTaskModal.fillPriority(taskData.getPriority());
        // CRITICAL: Fill the date field that we discovered was missing
        createTaskModal.fillDateWithDefault();
    }

    @When("guarda la nueva tarea")
    public void guarda_tarea() {
        createTaskModal.submit();
    }

    /**
     * Complete step implementation for task creation with valid data
     * Requirements: 5.1
     */
    @When("el usuario crea una nueva tarea con datos validos")
    public void el_usuario_crea_una_nueva_tarea_con_datos_validos() {
        // Step 0: CRITICAL - Ensure we're on Home page (not dashboard)
        homePage = new HomePage(DriverManager.getDriver());
        homePage.ensureOnHomePage();
        
        // Step 1: Store initial task count
        initialTaskCount = homePage.getTaskCount();
        
        // Step 2: Open the task creation modal
        homePage.clickNuevaTarea();
        createTaskModal = new CreateTaskModal(DriverManager.getDriver());
        
        // Step 3: Generate and fill the form with unique data
        TaskTestData taskData = TestDataGenerator.generateTaskTestData();
        createdTaskTitle = taskData.getTitle();
        
        createTaskModal.fillTitle(taskData.getTitle());
        createTaskModal.fillDescription(taskData.getDescription());
        createTaskModal.fillPriority(taskData.getPriority());
        // CRITICAL: Fill the date field that we discovered was missing
        createTaskModal.fillDateWithDefault();
        
        // Step 4: Submit the form
        createTaskModal.submit();
    }

    /**
     * Enhanced validation for task creation success
     * Requirements: 5.1, 5.2, 5.3
     */
    @Then("la tarea se muestra en la lista de tareas")
    public void valida_tarea_creada() {
        // Verify modal closed (indicates successful creation)
        assertTrue("Task creation modal should close after successful creation", 
                   createTaskModal.isTaskCreationSuccessful());
        
        // Comprehensive validation: task count increased, task appears with correct data
        assertTrue("Task should be successfully created and appear in the task list with correct data", 
                   homePage.isTaskCreationSuccessful(initialTaskCount, createdTaskTitle));
    }

    /**
     * Enhanced validation for "la tarea se muestra en la lista" step with data persistence verification
     * Requirements: 5.1, 5.2
     */
    @Then("la tarea se muestra en la lista")
    public void la_tarea_se_muestra_en_la_lista() {
        // Verify modal closed (indicates successful creation)
        assertTrue("Task creation modal should close after successful creation", 
                   createTaskModal.isTaskCreationSuccessful());
        
        // Verify task appears in the list and task count increased
        assertTrue("Task should appear in the task list", homePage.isTaskListNotEmpty());
        
        // Verify task count increased by one
        int currentCount = homePage.getTaskCount();
        assertTrue("Task count should increase after successful creation. Expected: > " + initialTaskCount + ", Actual: " + currentCount, 
                   currentCount > initialTaskCount);
        
        // If we have a specific task title, verify it exists
        if (createdTaskTitle != null && !createdTaskTitle.isEmpty()) {
            assertTrue("Created task should be visible in the task list with title: " + createdTaskTitle, 
                       homePage.validateLatestTaskData(createdTaskTitle));
        }
    }

    @When("intenta crear una tarea sin titulo")
    public void crear_tarea_sin_titulo() {
        // Generate realistic description but leave title empty
        TaskTestData taskData = TestDataGenerator.generateTaskTestData();
        
        createTaskModal.fillTitle(""); // Empty title for negative test
        createTaskModal.fillDescription(taskData.getDescription());
        createTaskModal.fillPriority(taskData.getPriority());
        // Still fill the date field for negative test
        createTaskModal.fillDateWithDefault();
        createTaskModal.submit();
    }

    /**
     * Enhanced validation for task creation failure
     * Requirements: 5.4 (error handling)
     */
    @Then("la tarea no se crea")
    public void valida_tarea_no_creada() {
        // Verify modal is still visible (creation failed)
        assertTrue("Modal should remain visible when task creation fails", 
                   createTaskModal.isModalStillVisible());
        
        // Verify task count did not increase
        int currentCount = homePage.getTaskCount();
        assertTrue("Task count should not increase when creation fails. Expected: " + initialTaskCount + ", Actual: " + currentCount, 
                   currentCount == initialTaskCount);
    }

    /**
     * Alternative step for task creation failure validation
     * Requirements: 5.4 (error handling)
     */
    @Then("la tarea no es creada")
    public void la_tarea_no_es_creada() {
        // Delegate to the existing implementation
        valida_tarea_no_creada();
    }
}
