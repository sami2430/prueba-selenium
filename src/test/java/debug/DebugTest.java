package debug;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.LoginPage;
import pages.HomePage;
import pages.CreateTaskModal;
import utils.DriverManager;

import java.util.List;

import static org.junit.Assert.fail;

public class DebugTest {

    @Test
    public void debugCreateTaskModal() {
        WebDriver driver = null;
        try {
            // Initialize and login
            LoginPage loginPage = new LoginPage(DriverManager.getDriver());
            driver = DriverManager.getDriver();
            
            loginPage.open();
            Thread.sleep(2000);
            
            loginPage.login("scastro@sentra.cl", "123");
            Thread.sleep(3000);
            
            // Navigate to home
            HomePage homePage = new HomePage(driver);
            homePage.navigateToHome();
            Thread.sleep(3000);
            
            // Click Nueva Tarea button
            homePage.clickNuevaTarea();
            Thread.sleep(3000);
            
            System.out.println("=== AFTER OPENING MODAL ===");
            
            // Fill the form first
            List<WebElement> textInputs = driver.findElements(By.xpath("//input[@type='text' and contains(@class, 'MuiInputBase-input')]"));
            if (!textInputs.isEmpty()) {
                textInputs.get(0).sendKeys("Test Title");
                System.out.println("Filled title field");
            }
            
            List<WebElement> textareas = driver.findElements(By.cssSelector("textarea[class*='MuiInputBase-inputMultiline']"));
            if (!textareas.isEmpty()) {
                textareas.get(0).sendKeys("Test Description");
                System.out.println("Filled description field");
            }
            
            List<WebElement> numberInputs = driver.findElements(By.xpath("//input[@type='number' and contains(@class, 'MuiInputBase-input')]"));
            if (!numberInputs.isEmpty()) {
                numberInputs.get(0).sendKeys("3");
                System.out.println("Filled priority field");
            }
            
            Thread.sleep(2000);
            
            // Now find all buttons in the modal
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            System.out.println("Total buttons found after filling form: " + buttons.size());
            
            for (int i = 0; i < buttons.size(); i++) {
                WebElement btn = buttons.get(i);
                String text = btn.getText();
                String className = btn.getAttribute("class");
                boolean isDisplayed = btn.isDisplayed();
                
                System.out.println("Button " + i + ":");
                System.out.println("  Text: '" + text + "'");
                System.out.println("  Class: '" + className + "'");
                System.out.println("  Displayed: " + isDisplayed);
                System.out.println("  ---");
            }
            
            // This will make the test fail so we can see the output
            fail("Debug test completed - check output above");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            fail("Debug test failed: " + e.getMessage());
        } finally {
            if (driver != null) {
                try {
                    DriverManager.quitDriver();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void debugCompleteTaskCreation() {
        WebDriver driver = null;
        try {
            // Initialize and login
            LoginPage loginPage = new LoginPage(DriverManager.getDriver());
            driver = DriverManager.getDriver();
            
            loginPage.open();
            Thread.sleep(2000);
            
            loginPage.login("scastro@sentra.cl", "123");
            Thread.sleep(3000);
            
            // Navigate to home
            HomePage homePage = new HomePage(driver);
            homePage.ensureOnHomePage();
            Thread.sleep(3000);
            
            System.out.println("=== INITIAL STATE ===");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            int initialCount = homePage.getTaskCount();
            System.out.println("Initial task count: " + initialCount);
            
            // List existing tasks (including the manual "aaaa" task)
            List<String> existingTitles = homePage.getTitles();
            System.out.println("Existing tasks (" + existingTitles.size() + "):");
            for (int j = 0; j < existingTitles.size(); j++) {
                System.out.println("  " + j + ": '" + existingTitles.get(j) + "'");
            }
            
            // Check if we can see the manual "aaaa" task (could be "aaaa" or "aaaaa")
            boolean aaaTaskExists = homePage.taskExistsWithTitle("aaaa") || homePage.taskExistsWithTitle("aaaaa");
            System.out.println("Manual 'aaaa'/'aaaaa' task visible: " + aaaTaskExists);
            
            // Click Nueva Tarea button
            homePage.clickNuevaTarea();
            Thread.sleep(3000);
            
            CreateTaskModal modal = new CreateTaskModal(driver);
            
            // Fill ALL the form fields we discovered in the debug
            String testTitle = "dddd"; // Simple title like the manual one
            modal.fillTitle(testTitle);
            modal.fillDescription("Test Description");
            modal.fillPriority(1);
            modal.fillDateWithDefault();
            
            // ADDITIONAL: Try to fill other fields we discovered
            // From debug: Input 0 (select with value '5'), Input 1 (checkbox)
            try {
                // Try to interact with the select field (Input 0)
                WebElement selectField = driver.findElement(By.cssSelector("input.MuiSelect-nativeInput"));
                if (selectField.isDisplayed()) {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].value = '3';", selectField);
                    System.out.println("Set select field to: 3");
                }
            } catch (Exception e) {
                System.out.println("Could not set select field: " + e.getMessage());
            }
            
            try {
                // Try to interact with the checkbox field (Input 1)
                WebElement checkboxField = driver.findElement(By.cssSelector("input.PrivateSwitchBase-input"));
                if (checkboxField.isDisplayed()) {
                    checkboxField.click();
                    System.out.println("Clicked checkbox field");
                }
            } catch (Exception e) {
                System.out.println("Could not click checkbox field: " + e.getMessage());
            }
            
            System.out.println("=== BEFORE SUBMIT ===");
            System.out.println("Form filled with title: " + testTitle);
            
            // Check what values are actually in the form fields
            CreateTaskModal.TaskFormData formData = modal.getFormData();
            System.out.println("Form data before submit:");
            System.out.println("  Title: '" + formData.getTitle() + "'");
            System.out.println("  Description: '" + formData.getDescription() + "'");
            System.out.println("  Priority: '" + formData.getPriority() + "'");
            System.out.println("  Date: '" + formData.getDate() + "'");
            
            // Submit
            modal.submit();
            System.out.println("Submit button clicked");
            
            // Wait and check multiple times for task to appear
            for (int i = 1; i <= 10; i++) {
                Thread.sleep(1000);
                System.out.println("=== CHECK " + i + " (after " + i + " seconds) ===");
                System.out.println("Current URL: " + driver.getCurrentUrl());
                
                // Check if modal is still visible
                boolean modalVisible = modal.isModalStillVisible();
                System.out.println("Modal still visible: " + modalVisible);
                
                // Check task count
                int currentCount = homePage.getTaskCount();
                System.out.println("Current task count: " + currentCount);
                
                // Check if task exists
                boolean taskExists = homePage.taskExistsWithTitle(testTitle);
                System.out.println("Task exists with title '" + testTitle + "': " + taskExists);
                
                // List all task titles
                List<String> titles = homePage.getTitles();
                System.out.println("All task titles (" + titles.size() + "):");
                for (int j = 0; j < titles.size(); j++) {
                    System.out.println("  " + j + ": '" + titles.get(j) + "'");
                }
                
                // If task appeared, break
                if (currentCount > initialCount || taskExists) {
                    System.out.println("SUCCESS: Task appeared after " + i + " seconds!");
                    break;
                }
                
                // If modal closed but no task appeared, there might be an error
                if (!modalVisible && currentCount == initialCount) {
                    System.out.println("WARNING: Modal closed but task count didn't increase");
                    
                    // Try to refresh the page and check again
                    if (i == 5) {
                        System.out.println("Refreshing page to check if task was created...");
                        driver.navigate().refresh();
                        Thread.sleep(3000);
                        homePage.ensureOnHomePage();
                        Thread.sleep(2000);
                    }
                }
            }
            
            fail("Debug test completed - check output above");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            fail("Debug test failed: " + e.getMessage());
        } finally {
            if (driver != null) {
                try {
                    DriverManager.quitDriver();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void debugAllFormFields() {
        WebDriver driver = null;
        try {
            // Initialize and login
            LoginPage loginPage = new LoginPage(DriverManager.getDriver());
            driver = DriverManager.getDriver();
            
            loginPage.open();
            Thread.sleep(2000);
            
            loginPage.login("scastro@sentra.cl", "123");
            Thread.sleep(3000);
            
            // Navigate to home
            HomePage homePage = new HomePage(driver);
            homePage.ensureOnHomePage();
            Thread.sleep(3000);
            
            // Open modal
            homePage.clickNuevaTarea();
            Thread.sleep(3000);
            
            System.out.println("=== ALL FORM FIELDS ANALYSIS ===");
            
            // Find ALL form elements
            List<WebElement> allInputs = driver.findElements(By.tagName("input"));
            List<WebElement> allTextareas = driver.findElements(By.tagName("textarea"));
            List<WebElement> allSelects = driver.findElements(By.tagName("select"));
            List<WebElement> allButtons = driver.findElements(By.tagName("button"));
            
            System.out.println("=== INPUT FIELDS ===");
            for (int i = 0; i < allInputs.size(); i++) {
                WebElement input = allInputs.get(i);
                if (input.isDisplayed()) {
                    String type = input.getAttribute("type");
                    String name = input.getAttribute("name");
                    String id = input.getAttribute("id");
                    String className = input.getAttribute("class");
                    String value = input.getAttribute("value");
                    String placeholder = input.getAttribute("placeholder");
                    String required = input.getAttribute("required");
                    
                    System.out.println("Input " + i + ":");
                    System.out.println("  Type: '" + type + "'");
                    System.out.println("  Name: '" + name + "'");
                    System.out.println("  ID: '" + id + "'");
                    System.out.println("  Class: '" + className + "'");
                    System.out.println("  Value: '" + value + "'");
                    System.out.println("  Placeholder: '" + placeholder + "'");
                    System.out.println("  Required: '" + required + "'");
                    System.out.println("  ---");
                }
            }
            
            System.out.println("=== TEXTAREA FIELDS ===");
            for (int i = 0; i < allTextareas.size(); i++) {
                WebElement textarea = allTextareas.get(i);
                if (textarea.isDisplayed()) {
                    String name = textarea.getAttribute("name");
                    String id = textarea.getAttribute("id");
                    String className = textarea.getAttribute("class");
                    String value = textarea.getAttribute("value");
                    String placeholder = textarea.getAttribute("placeholder");
                    String required = textarea.getAttribute("required");
                    
                    System.out.println("Textarea " + i + ":");
                    System.out.println("  Name: '" + name + "'");
                    System.out.println("  ID: '" + id + "'");
                    System.out.println("  Class: '" + className + "'");
                    System.out.println("  Value: '" + value + "'");
                    System.out.println("  Placeholder: '" + placeholder + "'");
                    System.out.println("  Required: '" + required + "'");
                    System.out.println("  ---");
                }
            }
            
            System.out.println("=== SELECT FIELDS ===");
            for (int i = 0; i < allSelects.size(); i++) {
                WebElement select = allSelects.get(i);
                if (select.isDisplayed()) {
                    String name = select.getAttribute("name");
                    String id = select.getAttribute("id");
                    String className = select.getAttribute("class");
                    String value = select.getAttribute("value");
                    String required = select.getAttribute("required");
                    
                    System.out.println("Select " + i + ":");
                    System.out.println("  Name: '" + name + "'");
                    System.out.println("  ID: '" + id + "'");
                    System.out.println("  Class: '" + className + "'");
                    System.out.println("  Value: '" + value + "'");
                    System.out.println("  Required: '" + required + "'");
                    
                    // Get options
                    List<WebElement> options = select.findElements(By.tagName("option"));
                    System.out.println("  Options (" + options.size() + "):");
                    for (int j = 0; j < options.size(); j++) {
                        WebElement option = options.get(j);
                        String optionValue = option.getAttribute("value");
                        String optionText = option.getText();
                        boolean selected = option.isSelected();
                        System.out.println("    Option " + j + ": value='" + optionValue + "', text='" + optionText + "', selected=" + selected);
                    }
                    System.out.println("  ---");
                }
            }
            
            System.out.println("=== BUTTONS ===");
            for (int i = 0; i < allButtons.size(); i++) {
                WebElement button = allButtons.get(i);
                if (button.isDisplayed()) {
                    String type = button.getAttribute("type");
                    String className = button.getAttribute("class");
                    String text = button.getText();
                    
                    System.out.println("Button " + i + ":");
                    System.out.println("  Type: '" + type + "'");
                    System.out.println("  Class: '" + className + "'");
                    System.out.println("  Text: '" + text + "'");
                    System.out.println("  ---");
                }
            }
            
            // Now fill the form and see what changes
            System.out.println("=== FILLING FORM ===");
            
            CreateTaskModal modal = new CreateTaskModal(driver);
            modal.fillTitle("DEBUG_TITLE");
            modal.fillDescription("DEBUG_DESCRIPTION");
            modal.fillPriority(3);
            modal.fillDateWithDefault();
            
            Thread.sleep(1000);
            
            System.out.println("=== AFTER FILLING - INPUT VALUES ===");
            allInputs = driver.findElements(By.tagName("input"));
            for (int i = 0; i < allInputs.size(); i++) {
                WebElement input = allInputs.get(i);
                if (input.isDisplayed()) {
                    String type = input.getAttribute("type");
                    String value = input.getAttribute("value");
                    System.out.println("Input " + i + " (" + type + "): '" + value + "'");
                }
            }
            
            allTextareas = driver.findElements(By.tagName("textarea"));
            for (int i = 0; i < allTextareas.size(); i++) {
                WebElement textarea = allTextareas.get(i);
                if (textarea.isDisplayed()) {
                    String value = textarea.getAttribute("value");
                    System.out.println("Textarea " + i + ": '" + value + "'");
                }
            }
            
            allSelects = driver.findElements(By.tagName("select"));
            for (int i = 0; i < allSelects.size(); i++) {
                WebElement select = allSelects.get(i);
                if (select.isDisplayed()) {
                    String value = select.getAttribute("value");
                    System.out.println("Select " + i + ": '" + value + "'");
                    
                    List<WebElement> options = select.findElements(By.tagName("option"));
                    for (int j = 0; j < options.size(); j++) {
                        WebElement option = options.get(j);
                        if (option.isSelected()) {
                            System.out.println("  Selected option: '" + option.getText() + "' (value: '" + option.getAttribute("value") + "')");
                        }
                    }
                }
            }
            
            fail("Debug test completed - check output above");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            fail("Debug test failed: " + e.getMessage());
        } finally {
            if (driver != null) {
                try {
                    DriverManager.quitDriver();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }
}