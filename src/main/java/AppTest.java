import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class AppTest {

    AppiumDriver driver;
    DateUtils dateUtils;
    DriverAction action;

    @BeforeClass
    public void setUp() throws MalformedURLException, InterruptedException {
        DesiredCapabilities cap = new DesiredCapabilities();

        cap.setCapability("deviceName", "Pixel 7");
        cap.setCapability("udid", "28051FDH20034J");
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "14");
        cap.setCapability("automationName", "uiAutomator2");
        cap.setCapability("appPackage", "hko.MyObservatory_v1_0");
        cap.setCapability("appActivity", "hko.MyObservatory_v1_0.AgreementPage");

        URL url = new URL("http://127.0.0.1:4723/");
        driver = new AppiumDriver(url, cap);
        action = new DriverAction(driver);
        dateUtils = new DateUtils(driver);

        // Accept permissions, etc.
        action.clickElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"hko.MyObservatory_v1_0:id/btn_agree\")"));
        action.clickElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"hko.MyObservatory_v1_0:id/btn_agree\")"));
        action.clickElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"com.android.permissioncontroller:id/permission_allow_button\")"));
        Thread.sleep(2000);
        action.clickElement(By.id("android:id/button1"));
        action.clickElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"com.android.permissioncontroller:id/permission_deny_button\")"));
        action.clickElement(By.id("hko.MyObservatory_v1_0:id/exit_btn"));
        action.clickElement(By.id("hko.MyObservatory_v1_0:id/exit_btn"));

        boolean isDisplayed = action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"MyObservatory\")"));
        System.out.println(isDisplayed ? "✅ MyObservatory is displayed!" : "❌ MyObservatory is NOT displayed!");

        System.out.println("Open App Execution Ended Here");
    }

    @Test
    public void nineDaysAssertions() {
        System.out.println("Executing 9-Day");

        // Navigate to 9-Day Forecast
        action.clickElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().description(\"Navigate up\")"));
        action.clickElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().className(\"android.widget.LinearLayout\").instance(15)"));
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"Forecast & Warning Services\")"));
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"9-Day Forecast\").instance(1)"));
        action.clickElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().className(\"android.widget.LinearLayout\").instance(19)"));
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"9-Day Forecast\")"));

        // Compare current date
        String systemDate = dateUtils.getCurrentSystemDate();
        String appDate = dateUtils.getDateFromApp("hko.MyObservatory_v1_0:id/mainAppSevenDayUpdateTime");
        System.out.println("System Date: " + systemDate);
        System.out.println("App Date: " + appDate);
        System.out.println(dateUtils.compareDates(appDate, systemDate)
                ? "✅ Date matches!" : "❌ Date does NOT match!");

        // Forecast date + scroll
        String expectedDate = dateUtils.getDayAfterDays(9);
        String scrollableCommand =
                "new UiScrollable(new UiSelector().className(\"android.widget.ScrollView\"))" +
                        ".scrollIntoView(new UiSelector().text(\"" + expectedDate + "\"))";
        driver.findElement(new AppiumBy.ByAndroidUIAutomator(scrollableCommand));

        String forecastText = action.getVisibleTextByExactMatch(expectedDate);
        System.out.println("Forecast Text from App: " + forecastText);
        System.out.println(forecastText.equals(expectedDate)
                ? "✅ 9-Day forecast matches expected date!" : "❌ Mismatch: Expected " + expectedDate + " but got " + forecastText);

        // Forecast detail checks
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"hko.MyObservatory_v1_0:id/sevenday_forecast_Icon\").instance(1)"));
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"hko.MyObservatory_v1_0:id/sevenday_forecast_temp\").instance(1)"));
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"hko.MyObservatory_v1_0:id/sevenday_forecast_rh\").instance(1)"));
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"hko.MyObservatory_v1_0:id/psrIcon\").instance(1)"));
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"hko.MyObservatory_v1_0:id/psrText\").instance(1)"));
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"hko.MyObservatory_v1_0:id/sevenday_forecast_wind\").instance(1)"));
        action.isElementDisplayed(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"Mainly cloudy with isolated showers. Warm with sunny intervals during the day.\")"));

        System.out.println("9-Day Execution Ended Here");
    }
}
