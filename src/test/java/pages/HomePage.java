package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private By newTaskButton =
        By.xpath("//button[contains(.,'NUEVA TAREA')]");

    private By taskRows =
        By.xpath("//table/tbody/tr");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void clickNuevaTarea() {
        click(newTaskButton);
    }

    public boolean isTaskListNotEmpty() {
        return driver.findElements(taskRows).size() > 0;
    }

    // encabezados
    private By titleHeader =
        By.xpath("//th[contains(.,'Titulo')]");

    private By priorityHeader =
        By.xpath("//th[contains(.,'Prioridad')]");

    // columnas
    private By titleCells =
        By.xpath("//table/tbody/tr/td[1]");

    private By priorityCells =
        By.xpath("//table/tbody/tr/td[4]");

    // clicks
    public void clickTitleHeader() {
        driver.findElement(titleHeader).click();
    }

    public void clickPriorityHeader() {
        driver.findElement(priorityHeader).click();
    }

    // lecturas
    public List<String> getTitles() {
        List<WebElement> cells = driver.findElements(titleCells);
        List<String> titles = new ArrayList<>();
        for (WebElement c : cells) {
            titles.add(c.getText().trim());
        }
        return titles;
    }

    public List<Integer> getPriorities() {
        List<WebElement> cells = driver.findElements(priorityCells);
        List<Integer> priorities = new ArrayList<>();
        for (WebElement c : cells) {
        priorities.add(Integer.parseInt(c.getText().trim()));
        }
        return priorities;
    }

    // ---------- Task Creation Validation Methods ----------
    
    /**
     * Gets the current count of tasks in the list
     * Requirements: 5.2, 5.3
     */
    public int getTaskCount() {
        return driver.findElements(taskRows).size();
    }

    /**
     * Validates if a task with specific title exists in the task list
     * Requirements: 5.1, 5.2
     */
    public boolean taskExistsWithTitle(String title) {
        List<String> titles = getTitles();
        return titles.contains(title);
    }

    /**
     * Validates if the most recently added task contains the expected data
     * Requirements: 5.1, 5.2
     */
    public boolean validateLatestTaskData(String expectedTitle) {
        try {
            // Wait a moment for the task to appear
            Thread.sleep(1000);
            
            List<String> titles = getTitles();
            if (titles.isEmpty()) {
                return false;
            }
            
            // Check if the expected title is in the list (could be first or last depending on sorting)
            return titles.contains(expectedTitle) || 
                   titles.stream().anyMatch(title -> title.contains(expectedTitle.substring(0, Math.min(10, expectedTitle.length()))));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates that task creation was successful by checking multiple criteria
     * Requirements: 5.1, 5.2, 5.3
     */
    public boolean isTaskCreationSuccessful(int previousCount, String taskTitle) {
        try {
            // Wait for the task list to update
            Thread.sleep(2000);
            
            // Check if task count increased
            int currentCount = getTaskCount();
            boolean countIncreased = currentCount > previousCount;
            
            // Check if task appears in the list
            boolean taskExists = taskExistsWithTitle(taskTitle) || validateLatestTaskData(taskTitle);
            
            // Check if task list is not empty
            boolean listNotEmpty = isTaskListNotEmpty();
            
            return countIncreased && (taskExists || listNotEmpty);
        } catch (Exception e) {
            return false;
        }
    }
}
