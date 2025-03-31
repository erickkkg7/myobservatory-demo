import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DriverAction {
    private AppiumDriver driver;
    private WebDriverWait wait;

    // Constructor to initialize driver and WebDriverWait
    public DriverAction(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Explicit wait time
    }

    // Method to click an element by AppiumBy
    public void clickElement(AppiumBy by) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    // Method to click an element By (for regular locators)
    public void clickElement(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    // Method to check if an element is displayed (AppiumBy)
    public boolean isElementDisplayed(AppiumBy by) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // New method: Get visible text by exact match
    public String getVisibleTextByExactMatch(String exactText) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"" + exactText + "\")")
        ));
        return element.getText().trim();
    }
}
