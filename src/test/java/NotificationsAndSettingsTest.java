import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;

public class NotificationsAndSettingsTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("=== TEST SUITE STARTING ===");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("=== TEST SUITE COMPLETE ===");
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println("--- Notification and Settings Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Notification and Settings Testing class finished ---");
    }

    @BeforeMethod
    public void setUp() {
        // Runs before EACH test method — initializes a fresh browser session
        System.out.println("\n[Setup] Launching Chrome browser...");
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.out.println("[Setup] Browser ready.");
    }

    @AfterMethod
    public void tearDown() {
        // Runs after EACH test method — safely closes the browser
        System.out.println("[Teardown] Closing browser...");
        if (driver != null) {
            driver.quit();
        }
        System.out.println("[Teardown] Browser closed.");
    }

    // Test 1: Notification Bell Visible
    @Test(priority = 1)
    public void testNotificationBellVisible() {

    }

    // Test 2: Notification Panel Opens
    @Test(priority = 2)
    public void testNotificationPanelOpens() {

    }

    // Test 3: Settings Page Loads
    @Test(priority = 3)
    public void testSettingsPageLoads() {

    }

    // Test 4: Change Email Field Activates
    @Test(priority = 4)
    public void testChangeEmailField() {

    }

    // Test 5: Change Password Fields Appear
    @Test(priority = 5)
    public void testChangePasswordField() {

    }

    // Test 6: Privacy Settings Toggle Changes State
    @Test(priority = 6)
    public void testPrivacySettingsToggle() {

    }

}
