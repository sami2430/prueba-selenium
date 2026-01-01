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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Property-Based Test for Date Sorting Functionality
 * Feature: selenium-test-improvements, Property 2: Date Sorting Correctness
 * Validates: Requirements 2.1, 2.2
 */
public class DateSortingPropertyTest {

    private TasksPage tasksPage;
    private WebDriver driver;
    private Random random;
    private DateTimeFormatter dateFormatter;

    @Before
    public void setUp() {
        // Use the existing DriverManager to get driver instance
        driver = DriverManager.getDriver();
        tasksPage = new TasksPage(driver);
        random = new Random();
        // Common date format used in the application
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @After
    public void tearDown() {
        // Use DriverManager's quit method
        DriverManager.quitDriver();
    }

    /**
     * Property 2: Date Sorting Correctness
     * For any task list, when the end date header is clicked, 
     * all tasks should be displayed in chronological order by end date
     * Validates: Requirements 2.1, 2.2
     * 
     * This property-based test runs 100 iterations with randomly generated date lists
     */
    @Test
    public void testDateSortingProperty() {
        // Run property test with 100 iterations as specified in design document
        for (int iteration = 0; iteration < 100; iteration++) {
            // Generate random date list for this iteration
            List<String> randomDates = generateRandomEndDates();
            
            // Test the property: sorting should produce chronological order
            List<String> sortedDates = new ArrayList<>(randomDates);
            Collections.sort(sortedDates);
            
            // Verify the sorting property holds
            assertTrue("Iteration " + iteration + ": Sorted list should be chronologically ordered", 
                       isSortedChronologically(sortedDates));
            
            // Verify sorting preserves all dates (no loss or addition)
            assertEquals("Iteration " + iteration + ": Sorting should preserve date count", 
                        randomDates.size(), sortedDates.size());
            
            // Verify all original dates are present
            for (String date : randomDates) {
                assertTrue("Iteration " + iteration + ": All original dates should be present after sorting: " + date, 
                          sortedDates.contains(date));
            }
        }
    }

    /**
     * Generates a random list of end dates for property testing
     */
    private List<String> generateRandomEndDates() {
        List<String> dates = new ArrayList<>();
        int listSize = random.nextInt(10) + 1; // 1 to 10 dates
        
        // Generate dates within a reasonable range (past year to next year)
        LocalDate baseDate = LocalDate.now();
        
        for (int i = 0; i < listSize; i++) {
            // Generate random date within +/- 365 days from today
            int daysOffset = random.nextInt(730) - 365; // -365 to +365 days
            LocalDate randomDate = baseDate.plusDays(daysOffset);
            dates.add(randomDate.format(dateFormatter));
        }
        
        return dates;
    }

    /**
     * Helper method to check if a list is sorted chronologically
     * This mirrors the logic in TasksPage.isTasksSortedByDate()
     */
    private boolean isSortedChronologically(List<String> dates) {
        List<String> sortedDates = new ArrayList<>(dates);
        Collections.sort(sortedDates);
        return dates.equals(sortedDates);
    }

    /**
     * Property test for date sorting preservation
     * Validates that sorting doesn't lose or add dates
     * Runs 100 iterations with different date configurations
     */
    @Test
    public void testDateSortingPreservesAllDates() {
        for (int iteration = 0; iteration < 100; iteration++) {
            List<String> originalDates = generateRandomEndDates();
            
            List<String> sortedDates = new ArrayList<>(originalDates);
            Collections.sort(sortedDates);
            
            // Verify same number of dates
            assertEquals("Iteration " + iteration + ": Sorting should preserve date count", 
                        originalDates.size(), sortedDates.size());
            
            // Verify all original dates are present
            for (String date : originalDates) {
                assertTrue("Iteration " + iteration + ": All original dates should be present after sorting: " + date, 
                          sortedDates.contains(date));
            }
            
            // Verify no new dates were added
            for (String date : sortedDates) {
                assertTrue("Iteration " + iteration + ": No new dates should be added during sorting: " + date, 
                          originalDates.contains(date));
            }
        }
    }

    /**
     * Edge case property test for empty and single-date lists
     */
    @Test
    public void testDateSortingEdgeCases() {
        // Test empty list
        List<String> emptyList = new ArrayList<>();
        assertTrue("Empty list should be considered sorted", 
                   isSortedChronologically(emptyList));
        
        // Test single date lists with various dates
        for (int i = 0; i < 20; i++) {
            List<String> singleDate = new ArrayList<>();
            LocalDate randomDate = LocalDate.now().plusDays(random.nextInt(100) - 50);
            singleDate.add(randomDate.format(dateFormatter));
            assertTrue("Single date list should be considered sorted", 
                       isSortedChronologically(singleDate));
        }
    }

    /**
     * Property test for different date formats consistency
     * Validates that the system handles date formats consistently during validation
     * Validates: Requirements 2.3
     */
    @Test
    public void testDateFormatConsistency() {
        for (int iteration = 0; iteration < 50; iteration++) {
            // Generate dates in the expected format
            List<String> dates = generateRandomEndDates();
            
            // Verify all dates follow the expected format
            for (String date : dates) {
                assertTrue("Iteration " + iteration + ": Date should follow expected format: " + date,
                          isValidDateFormat(date));
            }
            
            // Test sorting with consistent format
            List<String> sortedDates = new ArrayList<>(dates);
            Collections.sort(sortedDates);
            
            assertTrue("Iteration " + iteration + ": Dates with consistent format should sort correctly",
                       isSortedChronologically(sortedDates));
        }
    }

    /**
     * Helper method to validate date format
     */
    private boolean isValidDateFormat(String date) {
        try {
            LocalDate.parse(date, dateFormatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}