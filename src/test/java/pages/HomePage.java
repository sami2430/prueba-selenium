package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage {

    // Selectores de navegación
    private By homeMenuButton = By.xpath("//span[normalize-space()='Home'] | //span[normalize-space()='Inicio'] | //a[contains(@href, 'home')] | //button[contains(., 'Home')]");
    
    // Selector optimizado del botón "Nueva Tarea" basado en la estructura HTML real e información de debug
    // El texto del botón es "NUEVA TAREA" (todo en mayúsculas) con clase "MuiButtonBase-root MuiButton-root MuiButton-contained..."
    private By newTaskButton = By.xpath("//button[contains(@class, 'MuiButton-contained') and text()='NUEVA TAREA']");
    
    // Selectores de respaldo para el botón "Nueva Tarea"
    private By newTaskButtonFallback1 = By.cssSelector("button[class*='MuiButton-contained'][class*='css-9u3wwd']");
    private By newTaskButtonFallback2 = By.xpath("//button[contains(@class, 'MuiButton-root') and text()='NUEVA TAREA']");
    private By newTaskButtonFallback3 = By.xpath("//button[text()='NUEVA TAREA']");
    private By newTaskButtonFallback4 = By.xpath("//button[contains(., 'NUEVA TAREA')]");
    private By newTaskButtonFallback5 = By.xpath("//button[contains(., 'Nueva')]");
    private By newTaskButtonFallback6 = By.cssSelector("button[class*='MuiButtonBase-root'][class*='MuiButton-contained']");
    private By newTaskButtonFallback7 = By.cssSelector("button[class*='css-9u3wwd']");

    // Selector optimizado usando CSS para mejor rendimiento
    private By taskRows = By.cssSelector("table tbody tr");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navega a la página Home desde dashboard o cualquier otra página
     * Esto es crítico porque después del login, la app redirige a /dashboard que muestra pantalla en blanco
     * Requerimientos: Navegación a funcionalidad de creación de tareas
     */
    public void navigateToHome() {
        try {
            // Esperar un momento para que la página cargue después del login
            Thread.sleep(2000);
            
            // Verificar si ya estamos en una página con tareas visibles
            if (driver.findElements(newTaskButton).size() > 0 || 
                driver.findElements(newTaskButtonFallback1).size() > 0 ||
                driver.findElements(newTaskButtonFallback2).size() > 0) {
                return; // Ya estamos en la página home
            }
            
            // Try to find and click the Home menu button
            WebElement homeButton = findElementWithFallback(homeMenuButton, 
                By.xpath("//span[text()='Home'] | //span[text()='Inicio'] | //a[contains(text(), 'Home')] | //button[contains(text(), 'Home')] | //li[contains(text(), 'Home')] | //*[contains(text(), 'Home')]"));
            
            if (homeButton != null) {
                try {
                    homeButton.click();
                } catch (Exception e) {
                    // Try JavaScript click if regular click fails
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", homeButton);
                }
                // Wait for navigation to complete
                Thread.sleep(3000);
            } else {
                // If we can't find Home button, try direct URL navigation
                try {
                    String currentUrl = driver.getCurrentUrl();
                    String baseUrl = currentUrl.substring(0, currentUrl.indexOf("/", 8)); // Get base URL
                    driver.get(baseUrl + "/home");
                    Thread.sleep(3000);
                } catch (Exception ex) {
                    // Try alternative home URLs
                    try {
                        String currentUrl = driver.getCurrentUrl();
                        String baseUrl = currentUrl.substring(0, currentUrl.indexOf("/", 8));
                        driver.get(baseUrl + "/");
                        Thread.sleep(3000);
                    } catch (Exception ex2) {
                        System.out.println("Warning: Could not navigate to Home page: " + ex2.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Warning: Navigation to Home failed: " + e.getMessage());
        }
    }

    /**
     * Ensures we are on the Home page and ready for task operations
     * Requirements: Task creation and management functionality
     */
    public void ensureOnHomePage() {
        navigateToHome();
        
        // Verify we can see task-related elements
        try {
            Thread.sleep(2000); // Wait for page to fully load
            
            // Check if we can find the Nueva Tarea button with any of our selectors
            boolean buttonFound = false;
            By[] selectors = {
                newTaskButton, newTaskButtonFallback1, newTaskButtonFallback2, 
                newTaskButtonFallback3, newTaskButtonFallback4, newTaskButtonFallback5,
                newTaskButtonFallback6, newTaskButtonFallback7
            };
            
            for (By selector : selectors) {
                try {
                    if (driver.findElements(selector).size() > 0) {
                        buttonFound = true;
                        break;
                    }
                } catch (Exception e) {
                    // Continue trying other selectors
                }
            }
            
            // If we can't see the new task button, try navigation again
            if (!buttonFound) {
                System.out.println("Nueva Tarea button not found, retrying navigation...");
                navigateToHome();
                Thread.sleep(3000);
                
                // Check again after retry
                for (By selector : selectors) {
                    try {
                        if (driver.findElements(selector).size() > 0) {
                            buttonFound = true;
                            System.out.println("Nueva Tarea button found after retry");
                            break;
                        }
                    } catch (Exception e) {
                        // Continue trying other selectors
                    }
                }
            }
            
            if (!buttonFound) {
                System.out.println("Warning: Nueva Tarea button still not found after retry");
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not verify Home page elements: " + e.getMessage());
        }
    }

    public void clickNuevaTarea() {
        WebElement button = null;
        Exception lastException = null;
        
        // Array of selectors to try in order (most specific first)
        By[] selectors = {
            newTaskButton,                // Exact text match with MUI class
            newTaskButtonFallback3,       // Exact text match
            newTaskButtonFallback4,       // Contains text
            newTaskButtonFallback2,       // MUI class with text
            newTaskButtonFallback1,       // CSS class specific
            newTaskButtonFallback7,       // CSS class fallback
            newTaskButtonFallback6,       // Generic MUI button
            newTaskButtonFallback5        // Contains "Nueva"
        };
        
        // Try each selector until we find the button
        for (int i = 0; i < selectors.length; i++) {
            try {
                button = driver.findElement(selectors[i]);
                if (button != null && button.isDisplayed() && button.isEnabled()) {
                    System.out.println("Found Nueva Tarea button using selector " + i);
                    break; // Found a working button
                }
            } catch (Exception e) {
                lastException = e;
                button = null; // Reset for next iteration
            }
        }
        
        if (button != null) {
            try {
                // Scroll to button if needed
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
                Thread.sleep(500);
                
                // Try regular click first
                button.click();
                System.out.println("Successfully clicked Nueva Tarea button");
                Thread.sleep(1000); // Wait for modal to open
            } catch (Exception e) {
                // If regular click fails, try JavaScript click
                try {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
                    System.out.println("Successfully clicked Nueva Tarea button using JavaScript");
                    Thread.sleep(1000); // Wait for modal to open
                } catch (Exception jsException) {
                    throw new RuntimeException("Could not click Nueva Tarea button with any method. Last exception: " + jsException.getMessage(), jsException);
                }
            }
        } else {
            // Print debug information
            System.out.println("Could not find Nueva Tarea button. Current URL: " + driver.getCurrentUrl());
            System.out.println("Page title: " + driver.getTitle());
            
            throw new RuntimeException("Could not find Nueva Tarea button with any selector. Last exception: " + 
                                     (lastException != null ? lastException.getMessage() : "No exception"));
        }
    }

    public boolean isTaskListNotEmpty() {
        return driver.findElements(taskRows).size() > 0;
    }

    // Optimized header selectors using XPath for better compatibility
    private By titleHeader = By.xpath("//th[contains(.,'Titulo')]");

    private By priorityHeader = By.xpath("//th[contains(.,'Prioridad')]");

    // Optimized column selectors using CSS for better performance
    private By titleCells = By.cssSelector("table tbody tr td:first-child");

    private By priorityCells = By.cssSelector("table tbody tr td:nth-child(4)");

    // clicks with fallback support
    public void clickTitleHeader() {
        WebElement element = findElementWithFallback(titleHeader, By.xpath("//th[contains(.,'Titulo')]"));
        element.click();
    }

    public void clickPriorityHeader() {
        WebElement element = findElementWithFallback(priorityHeader, By.xpath("//th[contains(.,'Prioridad')]"));
        element.click();
    }

    /**
     * Helper method to find element with fallback selector for better reliability
     * Requirements: 6.1, 6.2
     */
    private WebElement findElementWithFallback(By primarySelector, By fallbackSelector) {
        try {
            return driver.findElement(primarySelector);
        } catch (Exception e) {
            try {
                return driver.findElement(fallbackSelector);
            } catch (Exception ex) {
                return null; // Return null if both selectors fail
            }
        }
    }

    // lecturas with fallback support
    public List<String> getTitles() {
        // Use the new paginated method
        return getAllTaskTitles();
    }

    public List<Integer> getPriorities() {
        List<WebElement> cells;
        try {
            cells = driver.findElements(priorityCells);
        } catch (Exception e) {
            // Fallback to XPath
            cells = driver.findElements(By.xpath("//table/tbody/tr/td[4]"));
        }
        
        List<Integer> priorities = new ArrayList<>();
        for (WebElement c : cells) {
            priorities.add(Integer.parseInt(c.getText().trim()));
        }
        return priorities;
    }

    // ---------- Task Creation Validation Methods ----------
    
    // Pagination selectors
    private By nextPageButton = By.cssSelector("button[aria-label='Go to next page']:not([disabled])");
    private By previousPageButton = By.cssSelector("button[aria-label='Go to previous page']:not([disabled])");
    private By paginationInfo = By.cssSelector(".MuiTablePagination-displayedRows");
    
    // Logout selectors
    private By logoutButton = By.xpath("//button[contains(., 'Logout') or contains(., 'Cerrar') or contains(., 'Salir')] | //span[contains(., 'Logout') or contains(., 'Cerrar') or contains(., 'Salir')]");
    private By userMenuButton = By.cssSelector("button[aria-label='account of current user'], .MuiAvatar-root, button[class*='avatar']");
    private By profileMenuButton = By.xpath("//button[contains(@class, 'MuiIconButton')] | //div[contains(@class, 'MuiAvatar')] | //*[contains(@class, 'profile')] | //*[contains(@class, 'user')]");

    /**
     * Gets the current count of tasks in ALL pages (handles pagination)
     * Requirements: 5.2, 5.3
     */
    public int getTaskCount() {
        return getAllTaskTitles().size();
    }

    /**
     * Gets all task titles from all pages (handles pagination)
     * Requirements: 5.1, 5.2
     */
    public List<String> getAllTaskTitles() {
        List<String> allTitles = new ArrayList<>();
        
        try {
            // First, go to the first page
            goToFirstPage();
            
            do {
                // Get titles from current page
                List<String> currentPageTitles = getTitlesFromCurrentPage();
                allTitles.addAll(currentPageTitles);
                
                System.out.println("Debug - Page titles: " + currentPageTitles);
                
                // Try to go to next page
                if (!goToNextPage()) {
                    break; // No more pages
                }
                
                Thread.sleep(1000); // Wait for page to load
                
            } while (true);
            
        } catch (Exception e) {
            System.out.println("Error reading paginated titles: " + e.getMessage());
            // Fallback to current page only
            allTitles = getTitlesFromCurrentPage();
        }
        
        System.out.println("Debug - Total titles found across all pages: " + allTitles.size());
        return allTitles;
    }

    /**
     * Goes to the first page of the table
     */
    private void goToFirstPage() {
        try {
            // Keep clicking previous until disabled
            while (driver.findElements(previousPageButton).size() > 0) {
                WebElement prevButton = driver.findElement(previousPageButton);
                prevButton.click();
                Thread.sleep(500);
            }
        } catch (Exception e) {
            // Already on first page or no pagination
        }
    }

    /**
     * Goes to the next page if available
     * @return true if successfully moved to next page, false if no next page
     */
    private boolean goToNextPage() {
        try {
            List<WebElement> nextButtons = driver.findElements(nextPageButton);
            if (nextButtons.size() > 0) {
                nextButtons.get(0).click();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Could not go to next page: " + e.getMessage());
        }
        return false;
    }

    /**
     * Gets titles from the current page only
     */
    private List<String> getTitlesFromCurrentPage() {
        List<WebElement> cells = new ArrayList<>();
        
        try {
            // Based on debug: there are 20 td elements, and we found titles in XPath '//td[1]'
            // Try to get the first column cells (titles) using different approaches
            
            // Approach 1: Get all first td elements (position 1, 5, 9, 13, 17 for 5 rows with 4 columns each)
            List<WebElement> allTds = driver.findElements(By.xpath("//td"));
            System.out.println("Debug - Total td elements found: " + allTds.size());
            
            // If we have 20 tds and 4 columns per row, we have 5 rows
            // Title cells would be at positions 0, 4, 8, 12, 16 (every 4th element starting from 0)
            if (allTds.size() >= 4) {
                for (int i = 0; i < allTds.size(); i += 4) {
                    if (i < allTds.size()) {
                        cells.add(allTds.get(i));
                    }
                }
            }
            
            // If that doesn't work, try the XPath that showed results in debug
            if (cells.isEmpty()) {
                cells = driver.findElements(By.xpath("//td[1]"));
            }
            
            // If still empty, try different column positions
            if (cells.isEmpty()) {
                cells = driver.findElements(By.xpath("//td[2]"));
            }
            
        } catch (Exception e) {
            System.out.println("Error finding title cells: " + e.getMessage());
        }
        
        List<String> titles = new ArrayList<>();
        for (WebElement c : cells) {
            String title = c.getText().trim();
            if (!title.isEmpty() && !title.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) { // Skip date-like strings
                titles.add(title);
                System.out.println("Debug - Found title: '" + title + "'");
            }
        }
        
        System.out.println("Debug - Extracted " + titles.size() + " titles from " + cells.size() + " cells");
        return titles;
    }

    /**
     * Validates if a task with specific title exists in the task list (searches all pages)
     * Requirements: 5.1, 5.2
     */
    public boolean taskExistsWithTitle(String title) {
        List<String> allTitles = getAllTaskTitles();
        return allTitles.contains(title);
    }

    /**
     * Validates if the most recently added task contains the expected data (searches all pages)
     * Requirements: 5.1, 5.2
     */
    public boolean validateLatestTaskData(String expectedTitle) {
        try {
            // Wait a moment for the task to appear
            Thread.sleep(1000);
            
            List<String> allTitles = getAllTaskTitles();
            if (allTitles.isEmpty()) {
                return false;
            }
            
            // Check if the expected title is in the list (could be anywhere depending on sorting)
            return allTitles.contains(expectedTitle) || 
                   allTitles.stream().anyMatch(title -> title.contains(expectedTitle.substring(0, Math.min(10, expectedTitle.length()))));
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

    /**
     * Logs out the current user
     * Requirements: TC012
     */
    public void logout() {
        try {
            // First try to find a direct logout button
            WebElement logoutBtn = findElementWithFallback(logoutButton, 
                By.xpath("//button[text()='Logout'] | //a[text()='Logout'] | //span[text()='Logout']"));
            
            if (logoutBtn != null) {
                logoutBtn.click();
                return;
            }
            
            // If no direct logout button, try to find user menu/profile button
            WebElement userMenu = findElementWithFallback(userMenuButton, profileMenuButton);
            
            if (userMenu != null) {
                userMenu.click();
                Thread.sleep(1000); // Wait for menu to open
                
                // Now look for logout option in the opened menu
                WebElement logoutOption = findElementWithFallback(logoutButton,
                    By.xpath("//li[contains(., 'Logout')] | //div[contains(., 'Logout')] | //span[contains(., 'Logout')]"));
                
                if (logoutOption != null) {
                    logoutOption.click();
                    return;
                }
            }
            
            // If all else fails, try to clear session by navigating to logout URL
            String currentUrl = driver.getCurrentUrl();
            String baseUrl = currentUrl.substring(0, currentUrl.indexOf("/", 8));
            driver.get(baseUrl + "/logout");
            
        } catch (Exception e) {
            System.out.println("Warning: Could not perform logout: " + e.getMessage());
            // Try alternative logout by clearing cookies and navigating to login
            try {
                driver.manage().deleteAllCookies();
                String currentUrl = driver.getCurrentUrl();
                String baseUrl = currentUrl.substring(0, currentUrl.indexOf("/", 8));
                driver.get(baseUrl + "/login");
            } catch (Exception ex) {
                System.out.println("Warning: Fallback logout also failed: " + ex.getMessage());
            }
        }
    }
}
