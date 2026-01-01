package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CreateTaskModal extends BasePage {

    private By titleInput =
        By.xpath("//input[@placeholder='Titulo']");

    private By descriptionInput =
        By.xpath("//textarea");

    private By priorityInput =
        By.xpath("//input[@type='number']");

    private By saveButton =
        By.xpath("//button[contains(.,'CREAR TAREA')]");

    // Validation selectors
    private By errorMessage =
        By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'error')]");
    
    private By modalDialog =
        By.xpath("//div[@role='dialog'] | //div[contains(@class, 'modal')]");

    public CreateTaskModal(WebDriver driver) {
        super(driver);
    }

    public void fillTitle(String title) {
        type(titleInput, title);
    }

    public void fillDescription(String description) {
        type(descriptionInput, description);
    }

    public void fillPriority(int priority) {
        type(priorityInput, String.valueOf(priority));
    }

    public void submit() {
        click(saveButton);
    }

    public boolean isModalStillVisible() {
        return driver.findElements(saveButton).size() > 0;
    }

    // ---------- Enhanced Task Creation Validation Methods ----------
    
    /**
     * Validates if task creation was successful by checking if modal closed
     * Requirements: 5.1, 5.2
     */
    public boolean isTaskCreationSuccessful() {
        try {
            // Wait for modal to close after successful creation
            Thread.sleep(2000);
            
            // Check if modal is no longer visible (successful creation)
            return driver.findElements(saveButton).isEmpty() || 
                   driver.findElements(modalDialog).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates if task creation failed by checking for error messages
     * Requirements: 5.4 (if applicable)
     */
    public boolean hasTaskCreationError() {
        try {
            return !driver.findElements(errorMessage).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the error message text if present
     * Requirements: 5.4 (if applicable)
     */
    public String getErrorMessage() {
        try {
            WebElement errorElement = driver.findElement(errorMessage);
            return errorElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Validates that all required fields are filled
     * Requirements: 5.1
     */
    public boolean areRequiredFieldsFilled() {
        try {
            WebElement titleElement = driver.findElement(titleInput);
            WebElement descriptionElement = driver.findElement(descriptionInput);
            WebElement priorityElement = driver.findElement(priorityInput);
            
            return !titleElement.getAttribute("value").trim().isEmpty() &&
                   !descriptionElement.getAttribute("value").trim().isEmpty() &&
                   !priorityElement.getAttribute("value").trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the current values from the form fields
     * Requirements: 5.2
     */
    public TaskFormData getFormData() {
        try {
            String title = driver.findElement(titleInput).getAttribute("value");
            String description = driver.findElement(descriptionInput).getAttribute("value");
            String priority = driver.findElement(priorityInput).getAttribute("value");
            
            return new TaskFormData(title, description, priority);
        } catch (Exception e) {
            return new TaskFormData("", "", "");
        }
    }

    /**
     * Data class for task form information
     */
    public static class TaskFormData {
        private final String title;
        private final String description;
        private final String priority;

        public TaskFormData(String title, String description, String priority) {
            this.title = title;
            this.description = description;
            this.priority = priority;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPriority() { return priority; }
    }
}
