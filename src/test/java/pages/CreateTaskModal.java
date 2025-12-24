package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CreateTaskModal extends BasePage {

    private By titleInput =
        By.xpath("//input[@placeholder='Titulo']");

    private By descriptionInput =
        By.xpath("//textarea");

    private By priorityInput =
        By.xpath("//input[@type='number']");

    private By saveButton =
        By.xpath("//button[contains(.,'CREAR TAREA')]");

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
}
