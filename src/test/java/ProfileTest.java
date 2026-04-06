import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class ProfileTest {

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
        System.out.println("--- Profile Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Profile Testing class finished ---");
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

    // Test 1: Profile Page Loads
    @Test(priority = 1)
    public void testProfilePageLoads() {

    }

    // Test 2: Edit Profile – Name Update
    @Test(priority = 2)
    public void testEditProfileNameUpdate() {

    }

    // Test 3: Edit Profile – Bio Update
    @Test(priority = 3)
    public void testEditProfileBioUpdate() {

    }

    // Test 4: Profile Displays Boards
    @Test(priority = 4)
    public void testProfileDisplaysBoards() {

    }

    // Test 5: Profile Pins Tab Displays Pins
    @Test(priority = 5)
    public void testProfileDisplaysPins() {

    }

    // Test 6: Profile URL Matches Username
    @Test(priority = 6)
    public void testProfileURLMatchesUsername() {

    }

}
