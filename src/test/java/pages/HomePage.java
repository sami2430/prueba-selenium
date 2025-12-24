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
}
