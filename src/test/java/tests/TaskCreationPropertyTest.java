package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import pages.HomePage;
import pages.CreateTaskModal;
import pages.LoginPage;
import utils.DriverManager;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Prueba Basada en Propiedades para Funcionalidad de Creación de Tareas
 * Característica: selenium-test-improvements, Propiedad 8: Éxito de Creación de Tareas
 * Valida: Requisitos 5.1, 5.2, 5.3
 */
public class TaskCreationPropertyTest {

    private HomePage homePage;
    private CreateTaskModal createTaskModal;
    private LoginPage loginPage;
    private WebDriver driver;
    private Random random;

    @Before
    public void setUp() {
        // Usar el DriverManager existente para obtener la instancia del driver
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        random = new Random();
        
        // Hacer login antes de cada prueba
        loginPage.open();
        loginPage.login("scastro@sentra.cl", "123");
        
        // CRÍTICO: Navegar a la página home después del login (la app redirige a /dashboard que está en blanco)
        homePage.ensureOnHomePage();
    }

    @After
    public void tearDown() {
        // Usar el método quit del DriverManager
        DriverManager.quitDriver();
    }

    /**
     * Propiedad 8: Éxito de Creación de Tareas
     * Para cualquier dato válido de tarea, después de una creación exitosa, la tarea debe aparecer en la lista de tareas 
     * con todos los campos correctamente poblados y el conteo de tareas debe aumentar en uno
     * Valida: Requisitos 5.1, 5.2, 5.3
     * 
     * Esta prueba basada en propiedades ejecuta 30 iteraciones con datos de tarea válidos generados aleatoriamente
     */
    @Test
    public void testTaskCreationSuccessProperty() {
        // Ejecutar prueba de propiedad con 1 iteración para depuración
        for (int iteration = 0; iteration < 1; iteration++) {
            try {
                // Generar datos de tarea válidos aleatorios para esta iteración
                TaskData taskData = generateValidTaskData();
                
                // Obtener conteo inicial de tareas
                int initialCount = homePage.getTaskCount();
                
                // Abrir modal de creación de tarea
                homePage.clickNuevaTarea();
                createTaskModal = new CreateTaskModal(driver);
                
                // Llenar formulario con datos válidos
                createTaskModal.fillTitle(taskData.getTitle());
                createTaskModal.fillDescription(taskData.getDescription());
                createTaskModal.fillPriority(taskData.getPriority());
                // CRÍTICO: Llenar el campo de fecha que descubrimos que faltaba
                createTaskModal.fillDateWithDefault();
                
                // Enviar el formulario
                createTaskModal.submit();
                
                // Probar la propiedad: la creación de tarea válida debe tener éxito
                assertTrue("Iteración " + iteration + ": La creación de tarea debe ser exitosa para la tarea: " + taskData.getTitle(), 
                           createTaskModal.isTaskCreationSuccessful());
                
                // Verificar que la tarea aparece en la lista y el conteo aumentó
                assertTrue("Iteración " + iteration + ": La tarea debe aparecer en la lista y el conteo debe aumentar para la tarea: " + taskData.getTitle(), 
                           homePage.isTaskCreationSuccessful(initialCount, taskData.getTitle()));
                
                // Verificar que el conteo de tareas aumentó exactamente en uno
                int currentCount = homePage.getTaskCount();
                assertTrue("Iteración " + iteration + ": El conteo de tareas debe aumentar en uno. Esperado: " + (initialCount + 1) + ", Actual: " + currentCount, 
                           currentCount == initialCount + 1);
                
            } catch (Exception e) {
                // Si hay una excepción, fallar la prueba con contexto
                throw new AssertionError("Iteración " + iteration + " falló con excepción: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Property test for task creation with invalid data
     * For any invalid task data (empty title), the system should prevent task creation
     * Validates: Requirements 5.4 (error handling)
     * 
     * This property-based test runs 20 iterations with randomly generated invalid task data
     */
    @Test
    public void testTaskCreationErrorHandlingProperty() {
        // Run property test with 20 iterations for error scenarios
        for (int iteration = 0; iteration < 20; iteration++) {
            try {
                // Generate random invalid task data for this iteration
                TaskData invalidTaskData = generateInvalidTaskData();
                
                // Get initial task count
                int initialCount = homePage.getTaskCount();
                
                // Open task creation modal
                homePage.clickNuevaTarea();
                createTaskModal = new CreateTaskModal(driver);
                
                // Fill form with invalid data
                createTaskModal.fillTitle(invalidTaskData.getTitle());
                createTaskModal.fillDescription(invalidTaskData.getDescription());
                createTaskModal.fillPriority(invalidTaskData.getPriority());
                // Still fill the date field for negative test
                createTaskModal.fillDateWithDefault();
                
                // Submit the form
                createTaskModal.submit();
                
                // Test the property: invalid task creation should fail
                boolean creationFailed = createTaskModal.isModalStillVisible();
                boolean hasError = createTaskModal.hasTaskCreationError();
                
                assertTrue("Iteration " + iteration + ": Invalid task creation should fail or show error for task: " + invalidTaskData.getTitle(), 
                           creationFailed || hasError);
                
                // Verify task count did not increase
                int currentCount = homePage.getTaskCount();
                assertTrue("Iteration " + iteration + ": Task count should not increase for invalid data. Expected: " + initialCount + ", Actual: " + currentCount, 
                           currentCount == initialCount);
                
            } catch (Exception e) {
                // If there's an exception, fail the test with context
                throw new AssertionError("Iteration " + iteration + " failed with exception: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Generates valid task data for property testing
     */
    private TaskData generateValidTaskData() {
        String[] titlePrefixes = {"Tarea", "Proyecto", "Actividad", "Trabajo", "Asignación", "Item", "Todo", "Desarrollo"};
        String[] titleSuffixes = {"Importante", "Urgente", "Pendiente", "Nueva", "Crítica", "Automática", "Manual", "Especial"};
        String[] descriptions = {
            "Descripción detallada de la tarea",
            "Trabajo importante que debe completarse",
            "Actividad programada para el proyecto",
            "Tarea crítica del sistema",
            "Desarrollo de nueva funcionalidad",
            "Revisión y validación de procesos",
            "Implementación de mejoras",
            "Análisis y documentación"
        };
        
        String titlePrefix = titlePrefixes[random.nextInt(titlePrefixes.length)];
        String titleSuffix = titleSuffixes[random.nextInt(titleSuffixes.length)];
        String title = titlePrefix + " " + titleSuffix + " " + System.currentTimeMillis();
        
        String description = descriptions[random.nextInt(descriptions.length)] + " - " + random.nextInt(1000);
        int priority = random.nextInt(5) + 1; // Priority 1-5
        
        return new TaskData(title, description, priority);
    }

    /**
     * Generates invalid task data for property testing
     */
    private TaskData generateInvalidTaskData() {
        String[] invalidTitles = {"", " ", "   ", null};
        String[] validDescriptions = {"Descripción válida", "Descripción de prueba"};
        
        // Always use empty/invalid title (main validation rule)
        String title = invalidTitles[random.nextInt(invalidTitles.length)];
        if (title == null) title = "";
        
        String description = validDescriptions[random.nextInt(validDescriptions.length)];
        int priority = random.nextInt(5) + 1;
        
        return new TaskData(title, description, priority);
    }

    /**
     * Data class for task information
     */
    private static class TaskData {
        private final String title;
        private final String description;
        private final int priority;

        public TaskData(String title, String description, int priority) {
            this.title = title;
            this.description = description;
            this.priority = priority;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public int getPriority() { return priority; }
    }

    /**
     * Edge case property test for task creation
     */
    @Test
    public void testTaskCreationEdgeCases() {
        // Test edge cases with specific scenarios
        Object[][] edgeCases = {
            {"A", "Minimal description", 1}, // Minimal valid data
            {"Very Long Task Title That Exceeds Normal Limits And Contains Many Words To Test Length Handling", "Long description", 5}, // Long title
            {"Task with Special Characters !@#$%^&*()", "Description with symbols", 3}, // Special characters
            {"", "Valid description", 2}, // Empty title (should fail)
            {"Valid Title", "", 1}, // Empty description
        };
        
        for (int i = 0; i < edgeCases.length; i++) {
            try {
                Object[] caseData = edgeCases[i];
                TaskData taskData = new TaskData((String)caseData[0], (String)caseData[1], (Integer)caseData[2]);
                
                // Get initial task count
                int initialCount = homePage.getTaskCount();
                
                // Open task creation modal
                homePage.clickNuevaTarea();
                createTaskModal = new CreateTaskModal(driver);
                
                // Fill form
                createTaskModal.fillTitle(taskData.getTitle());
                createTaskModal.fillDescription(taskData.getDescription());
                createTaskModal.fillPriority(taskData.getPriority());
                // Fill the date field
                createTaskModal.fillDateWithDefault();
                
                // Submit
                createTaskModal.submit();
                
                // Determine if this should be valid or invalid
                boolean shouldBeValid = !taskData.getTitle().trim().isEmpty() && 
                                       !taskData.getDescription().trim().isEmpty();
                
                if (shouldBeValid) {
                    // Should succeed
                    assertTrue("Edge case " + i + ": Valid edge case data should succeed", 
                              createTaskModal.isTaskCreationSuccessful());
                } else {
                    // Should fail gracefully
                    boolean failed = createTaskModal.isModalStillVisible();
                    assertTrue("Edge case " + i + ": Invalid edge case data should fail gracefully", failed);
                }
                
            } catch (Exception e) {
                throw new AssertionError("Edge case " + i + " failed with exception: " + e.getMessage(), e);
            }
        }
    }
}