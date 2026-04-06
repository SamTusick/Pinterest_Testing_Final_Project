import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class RegistrationTest {

    WebDriver driver;

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
        System.out.println("--- Registration Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Registration Testing class finished ---");
    }

    @BeforeMethod
    public void setUp() {
        // Runs before EACH test method — initializes a fresh browser session
        System.out.println("\n[Setup] Launching Chrome browser...");
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
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

    // Test 1: Valid Registration
    @Test(priority = 1)
    public void testValidRegistration() {

    }

    // Test 2: Duplicate Email Registration
    @Test(priority = 2)
    public void testDuplicateEmailRegistration() {

    }

    // Test 3: Invalid Email Format
    @Test(priority = 3)
    public void testInvalidEmailFormat() {

    }

    // Test 4: Short Password
    @Test(priority = 4)
    public void testShortPassword() {

    }

    // Test 5: Empty Name Field (if applicable)
    @Test(priority = 5)
    public void testEmptyNameField() {

    }

    // Test 6: Empty Email Field on Register
    @Test(priority = 6)
    public void testEmptyEmailFieldOnRegister() {

    }

    // Test 7: Empty Password Field on Register
    @Test(priority = 7)
    public void testEmptyPasswordFieldOnRegister() {

    }

    // Test 8: Age Limit Validation (under 18)
    @Test(priority = 8)
    public void testAgeLimitValidation() {

    }

    // Test 9: Continue Button Present
    @Test(priority = 9)
    public void testContinueButtonPresent() {

    }
}
