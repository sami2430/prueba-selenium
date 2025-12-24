package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TasksPage extends BasePage {

    // Headers
    private By titleHeader = By.xpath("//th[contains(.,'Titulo')]");
    private By priorityHeader = By.xpath("//th[contains(.,'Prioridad')]");
    private By endDateHeader = By.xpath("//th[contains(.,'Fecha Vencimiento')]");

    // Table rows
    private By tableRows = By.xpath("//table/tbody/tr");

    // Columns
    private By titleCells = By.xpath("//table/tbody/tr/td[1]");
    private By createdDateCells = By.xpath("//table/tbody/tr/td[2]");
    private By endDateCells = By.xpath("//table/tbody/tr/td[3]");
    private By priorityCells = By.xpath("//table/tbody/tr/td[4]");

    public TasksPage(WebDriver driver) {
        super(driver);
    }

    // ---------- Clicks ----------
    public void clickTitleHeader() {
        click(titleHeader);
    }

    public void clickPriorityHeader() {
        click(priorityHeader);
    }

    public void clickEndDateHeader() {
        click(endDateHeader);
    }

    // ---------- Get data ----------
    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        for (WebElement el : driver.findElements(titleCells)) {
            titles.add(el.getText().trim());
        }
        return titles;
    }

    public List<Integer> getPriorities() {
        List<Integer> values = new ArrayList<>();
        for (WebElement el : driver.findElements(priorityCells)) {
            values.add(Integer.parseInt(el.getText().trim()));
        }
        return values;
    }

    public List<String> getEndDates() {
        List<String> dates = new ArrayList<>();
        for (WebElement el : driver.findElements(endDateCells)) {
            dates.add(el.getText().trim());
        }
        return dates;
    }

    // ---------- Validations ----------
    public boolean hasTasks() {
        return driver.findElements(tableRows).size() > 0;
    }

    public boolean allTasksHaveTitle() {
        return driver.findElements(titleCells)
                .stream()
                .allMatch(e -> !e.getText().trim().isEmpty());
    }

    public boolean allTasksHaveCreationDate() {
        return driver.findElements(createdDateCells)
                .stream()
                .allMatch(e -> !e.getText().trim().isEmpty());
    }

    public boolean allTasksHaveEndDate() {
        return driver.findElements(endDateCells)
                .stream()
                .allMatch(e -> !e.getText().trim().isEmpty());
    }

    public boolean allTasksHavePriority() {
        return driver.findElements(priorityCells)
                .stream()
                .allMatch(e -> !e.getText().trim().isEmpty());
    }
}
