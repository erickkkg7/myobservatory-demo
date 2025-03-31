import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {
    private AppiumDriver driver;

    public DateUtils(AppiumDriver driver) {
        this.driver = driver;
    }

    // Get the current system date in "dd/MMM/yyyy" format
    public String getCurrentSystemDate() {
        LocalDate today = LocalDate.now();
        return today.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy"));
    }

    // Get the date after N days in "d MMM" format by inputting the integer
    public String getDayAfterDays(int daysAhead) {
        LocalDate future = LocalDate.now().plusDays(daysAhead);
        return future.format(DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH));
    }

    // Extract the date from the app's text
    public String getDateFromApp(String resourceId) {
        WebElement dateElement = driver.findElement(new AppiumBy.ByAndroidUIAutomator(
                "new UiSelector().resourceId(\"" + resourceId + "\")"
        ));

        String fullText = dateElement.getText().trim();
        System.out.println("Raw App Text: " + fullText);

        Pattern pattern = Pattern.compile("\\d{2}/[A-Za-z]{3}/\\d{4}");
        Matcher matcher = pattern.matcher(fullText);
        if (matcher.find()) {
            return matcher.group(0); // Extracts something like "31/Mar/2025"
        } else {
            throw new RuntimeException("Date not found in app text!");
        }
    }

    // Compare full date strings
    public boolean compareDates(String appDate, String systemDate) {
        return appDate.equals(systemDate);
    }
}
