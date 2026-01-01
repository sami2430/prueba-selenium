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
 * Property-Based Test for Task Creation Functionality
 * Feature: selenium-test-improvements, Property 8: Task Creation Success
 * Validates: Requirements 5.1, 5.2, 5.3
 */
public class TaskCreationPropertyTest {

    private HomePage homePage;
    private CreateTaskModal createTaskModal;
    private LoginPage loginPage;
    private WebDriver driver;
    private Random random;

    @Before
    public void setUp() {
        // Use the existing DriverManager to get driver instance
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        random = new Random();
        
        // Login before each test
        loginPage.open();
        loginPage.login("scastro@sentra.cl", "123");
    }

    @After
    public void tearDown() {
        // Use DriverManager's quit method
        DriverManager.quitDriver();
    }

    /**
     * Property 8: Task Creation Success
     * For any valid task data, after successful creation, the task should appear in the task list 
     * with all fields correctly populated and the task count should increase by one
     * Validates: Requirements 5.1, 5.2, 5.3
     * 
     * This property-based test runs 30 iterations with randomly generated valid task data
     */
    @Test
    public void testTaskCreationSuccessProperty() {
        // Run property test with 30 iterations (reduced due to web UI overhead)
        for (int iteration = 0; iteration < 30; iteration++) {
            try {
                // Generate random valid task data for this iteration
                TaskData taskData = generateValidTaskData();
                
                // Get initial task count
                int initialCount = homePage.getTaskCount();
                
                // Open task creation modal
                homePage.clickNuevaTarea();
                createTaskModal = new CreateTaskModal(driver);
                
                // Fill form with valid data
                createTaskModal.fillTitle(taskData.getTitle());
                createTaskModal.fillDescription(taskData.getDescription());
                createTaskModal.fillPriority(taskData.getPriority());
                
                // Submit the form
                createTaskModal.submit();
                
                // Test the property: valid task creation should succeed
                assertTrue("Iteration " + iteration + ": Task creation should be successful for task: " + taskData.getTitle(), 
                           createTaskModal.isTaskCreationSuccessful());
                
                // Verify task appears in the list and count increased
                assertTrue("Iteration " + iteration + ": Task should appear in list and count should increase for task: " + taskData.getTitle(), 
                           homePage.isTaskCreationSuccessful(initialCount, taskData.getTitle()));
                
                // Verify task count increased by exactly one
                int currentCount = homePage.getTaskCount();
                assertTrue("Iteration " + iteration + ": Task count should increase by one. Expected: " + (initialCount + 1) + ", Actual: " + currentCount, 
                           currentCount == initialCount + 1);
                
            } catch (Exception e) {
                // If there's an exception, fail the test with context
                throw new AssertionError("Iteration " + iteration + " failed with exception: " + e.getMessage(), e);
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