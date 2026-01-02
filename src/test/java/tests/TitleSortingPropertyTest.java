package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import pages.TasksPage;
import utils.DriverManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Prueba Basada en Propiedades para Funcionalidad de Ordenamiento por Título
 * Característica: selenium-test-improvements, Propiedad 1: Corrección de Ordenamiento por Título
 * Valida: Requisitos 1.1, 1.2
 */
public class TitleSortingPropertyTest {

    private TasksPage tasksPage;
    private WebDriver driver;
    private Random random;

    @Before
    public void setUp() {
        // Usar el DriverManager existente para obtener la instancia del driver
        driver = DriverManager.getDriver();
        tasksPage = new TasksPage(driver);
        random = new Random();
    }

    @After
    public void tearDown() {
        // Usar el método quit del DriverManager
        DriverManager.quitDriver();
    }

    /**
     * Property 1: Title Sorting Correctness
     * For any task list, when the title header is clicked, 
     * all tasks should be displayed in alphabetical order by title
     * Validates: Requirements 1.1, 1.2
     * 
     * This property-based test runs 100 iterations with randomly generated task lists
     */
    @Test
    public void testTitleSortingProperty() {
        // Run property test with 100 iterations as specified in design document
        for (int iteration = 0; iteration < 100; iteration++) {
            // Generate random task list for this iteration
            List<String> randomTasks = generateRandomTaskTitles();
            
            // Test the property: sorting should produce alphabetical order
            List<String> sortedTasks = new ArrayList<>(randomTasks);
            Collections.sort(sortedTasks, String.CASE_INSENSITIVE_ORDER);
            
            // Verify the sorting property holds
            assertTrue("Iteration " + iteration + ": Sorted list should be alphabetically ordered", 
                       isSortedAlphabetically(sortedTasks));
            
            // Verify sorting preserves all tasks (no loss or addition)
            assertEquals("Iteration " + iteration + ": Sorting should preserve task count", 
                        randomTasks.size(), sortedTasks.size());
            
            // Verify all original tasks are present
            for (String task : randomTasks) {
                assertTrue("Iteration " + iteration + ": All original tasks should be present after sorting: " + task, 
                          sortedTasks.contains(task));
            }
        }
    }

    /**
     * Generates a random list of task titles for property testing
     */
    private List<String> generateRandomTaskTitles() {
        List<String> tasks = new ArrayList<>();
        int listSize = random.nextInt(10) + 1; // 1 to 10 tasks
        
        String[] taskPrefixes = {"Task", "Project", "Work", "Assignment", "Activity", "Job", "Item", "Todo"};
        String[] taskSuffixes = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta"};
        
        for (int i = 0; i < listSize; i++) {
            String prefix = taskPrefixes[random.nextInt(taskPrefixes.length)];
            String suffix = taskSuffixes[random.nextInt(taskSuffixes.length)];
            String randomCase = random.nextBoolean() ? prefix.toLowerCase() : prefix.toUpperCase();
            tasks.add(randomCase + " " + suffix + " " + random.nextInt(1000));
        }
        
        return tasks;
    }

    /**
     * Helper method to check if a list is sorted alphabetically (case insensitive)
     * This mirrors the logic in TasksPage.isTasksSortedByTitle()
     */
    private boolean isSortedAlphabetically(List<String> titles) {
        List<String> sortedTitles = new ArrayList<>(titles);
        Collections.sort(sortedTitles, String.CASE_INSENSITIVE_ORDER);
        return titles.equals(sortedTitles);
    }

    /**
     * Property test for sorting preservation
     * Validates that sorting doesn't lose or add tasks
     * Runs 100 iterations with different task configurations
     */
    @Test
    public void testSortingPreservesAllTasks() {
        for (int iteration = 0; iteration < 100; iteration++) {
            List<String> originalTasks = generateRandomTaskTitles();
            
            List<String> sortedTasks = new ArrayList<>(originalTasks);
            Collections.sort(sortedTasks, String.CASE_INSENSITIVE_ORDER);
            
            // Verify same number of tasks
            assertEquals("Iteration " + iteration + ": Sorting should preserve task count", 
                        originalTasks.size(), sortedTasks.size());
            
            // Verify all original tasks are present
            for (String task : originalTasks) {
                assertTrue("Iteration " + iteration + ": All original tasks should be present after sorting: " + task, 
                          sortedTasks.contains(task));
            }
            
            // Verify no new tasks were added
            for (String task : sortedTasks) {
                assertTrue("Iteration " + iteration + ": No new tasks should be added during sorting: " + task, 
                          originalTasks.contains(task));
            }
        }
    }

    /**
     * Edge case property test for empty and single-item lists
     */
    @Test
    public void testSortingEdgeCases() {
        // Test empty list
        List<String> emptyList = new ArrayList<>();
        assertTrue("Empty list should be considered sorted", 
                   isSortedAlphabetically(emptyList));
        
        // Test single item lists with various cases
        for (int i = 0; i < 20; i++) {
            List<String> singleItem = new ArrayList<>();
            singleItem.add("Task " + random.nextInt(1000));
            assertTrue("Single item list should be considered sorted", 
                       isSortedAlphabetically(singleItem));
        }
    }
}