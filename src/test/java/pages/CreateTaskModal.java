package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CreateTaskModal extends BasePage {

    // Updated selectors based on debug information - avoid ID conflicts
    // Input 2: text field with ID 'title'
    private By titleInput = By.xpath("//input[@type='text' and @id='title' and contains(@class, 'MuiInputBase-input')]");

    // Textarea 0: description field with ID 'title' (confusing but that's what the app uses)
    private By descriptionInput = By.xpath("//textarea[@id='title' and contains(@class, 'MuiInputBase-inputMultiline')]");

    // Input 4: number field with ID 'dueDate' (this is actually priority, confusing naming)
    private By priorityInput = By.xpath("//input[@type='number' and @id='dueDate' and contains(@class, 'MuiInputBase-input')]");

    // Input 3: datetime-local field with ID 'dueDate' (this is the actual date field)
    private By dateInput = By.xpath("//input[@type='datetime-local' and @id='dueDate' and contains(@class, 'MuiInputBase-input')]");

    private By saveButton = By.xpath("//button[text()='CREAR TAREA']");

    // Fallback selectors - more specific to avoid conflicts
    private By titleInputFallback = By.cssSelector("input[type='text'][id='title']");
    private By descriptionInputFallback = By.cssSelector("textarea[id='title']");
    private By priorityInputFallback = By.cssSelector("input[type='number'][id='dueDate']");
    private By dateInputFallback = By.cssSelector("input[type='datetime-local'][id='dueDate']");
    private By saveButtonFallback = By.cssSelector("button[class*='MuiButton-text'][class*='css-1y36nkv']");

    // Optimized validation selectors using CSS
    private By errorMessage = By.cssSelector(".error, .alert-error, .alert-danger, [class*='error']");
    
    private By modalDialog = By.cssSelector("div[role='dialog'], .modal, [class*='modal']");

    public CreateTaskModal(WebDriver driver) {
        super(driver);
    }

    public void fillTitle(String title) {
        WebElement element = findElementWithFallback(titleInput, titleInputFallback);
        element.clear();
        try { Thread.sleep(200); } catch (InterruptedException e) { /* ignore */ }
        element.sendKeys(title);
        // Trigger change event
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
    }

    public void fillDescription(String description) {
        WebElement element = findElementWithFallback(descriptionInput, descriptionInputFallback);
        element.clear();
        try { Thread.sleep(200); } catch (InterruptedException e) { /* ignore */ }
        element.sendKeys(description);
        // Trigger change event
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
    }

    public void fillPriority(int priority) {
        WebElement element = findElementWithFallback(priorityInput, priorityInputFallback);
        element.clear();
        try { Thread.sleep(200); } catch (InterruptedException e) { /* ignore */ }
        element.sendKeys(String.valueOf(priority));
        // Trigger change event
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
    }

    public void fillDate(String date) {
        WebElement element = findElementWithFallback(dateInput, dateInputFallback);
        // Clear the field first and wait a moment
        element.clear();
        try { Thread.sleep(500); } catch (InterruptedException e) { /* ignore */ }
        // Use JavaScript to set the value directly for datetime-local inputs
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", element, date);
        // Trigger change event to ensure the form recognizes the new value
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element);
        System.out.println("Set date field to: " + date);
        
        // Verify the value was set correctly
        String actualValue = element.getAttribute("value");
        System.out.println("Date field actual value after setting: " + actualValue);
    }

    /**
     * Fills the date field with a default future date
     * Format: YYYY-MM-DDTHH:MM (datetime-local format)
     */
    public void fillDateWithDefault() {
        // Use a simple, fixed future date to avoid formatting issues
        String dateString = "2026-01-15T10:00";
        System.out.println("Using fixed date: " + dateString);
        fillDate(dateString);
    }

    public void submit() {
        WebElement element = findElementWithFallback(saveButton, saveButtonFallback);
        click(element);
    }

    public boolean isModalStillVisible() {
        try {
            return driver.findElements(saveButton).size() > 0;
        } catch (Exception e) {
            // Fallback check
            return driver.findElements(saveButtonFallback).size() > 0;
        }
    }

    /**
     * Helper method to find element with fallback selector for better reliability
     * Requirements: 6.1, 6.2
     */
    private WebElement findElementWithFallback(By primarySelector, By fallbackSelector) {
        try {
            return driver.findElement(primarySelector);
        } catch (Exception e) {
            return driver.findElement(fallbackSelector);
        }
    }

    /**
     * Helper method to type text into an element
     */
    private void type(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Helper method to click an element
     */
    private void click(WebElement element) {
        element.click();
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
            
            // Check if modal is no longer visible using optimized selectors
            try {
                return driver.findElements(saveButton).isEmpty() || 
                       driver.findElements(modalDialog).isEmpty();
            } catch (Exception e) {
                // Fallback to alternative selectors
                return driver.findElements(saveButtonFallback).isEmpty() || 
                       driver.findElements(By.xpath("//div[@role='dialog'] | //div[contains(@class, 'modal')]")).isEmpty();
            }
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
            try {
                return !driver.findElements(errorMessage).isEmpty();
            } catch (Exception e) {
                // Fallback to XPath
                By fallbackError = By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'error')]");
                return !driver.findElements(fallbackError).isEmpty();
            }
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
            try {
                WebElement errorElement = driver.findElement(errorMessage);
                return errorElement.getText();
            } catch (Exception e) {
                // Fallback to XPath
                By fallbackError = By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'error')]");
                WebElement errorElement = driver.findElement(fallbackError);
                return errorElement.getText();
            }
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
            WebElement titleElement = findElementWithFallback(titleInput, titleInputFallback);
            WebElement descriptionElement = findElementWithFallback(descriptionInput, descriptionInputFallback);
            WebElement priorityElement = findElementWithFallback(priorityInput, priorityInputFallback);
            WebElement dateElement = findElementWithFallback(dateInput, dateInputFallback);
            
            return !titleElement.getAttribute("value").trim().isEmpty() &&
                   !descriptionElement.getAttribute("value").trim().isEmpty() &&
                   !priorityElement.getAttribute("value").trim().isEmpty() &&
                   !dateElement.getAttribute("value").trim().isEmpty();
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
            WebElement titleElement = findElementWithFallback(titleInput, titleInputFallback);
            WebElement descriptionElement = findElementWithFallback(descriptionInput, descriptionInputFallback);
            WebElement priorityElement = findElementWithFallback(priorityInput, priorityInputFallback);
            WebElement dateElement = findElementWithFallback(dateInput, dateInputFallback);
            
            String title = titleElement.getAttribute("value");
            String description = descriptionElement.getAttribute("value");
            String priority = priorityElement.getAttribute("value");
            String date = dateElement.getAttribute("value");
            
            return new TaskFormData(title, description, priority, date);
        } catch (Exception e) {
            return new TaskFormData("", "", "", "");
        }
    }

    /**
     * Data class for task form information
     */
    public static class TaskFormData {
        private final String title;
        private final String description;
        private final String priority;
        private final String date;

        public TaskFormData(String title, String description, String priority, String date) {
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.date = date;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPriority() { return priority; }
        public String getDate() { return date; }
    }
}
