import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class HomeAndNavigationTest {

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
        System.out.println("--- Home and Navigation Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Home and Navigation Testing class finished ---");
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

    // Test 1: Home Page Loads
    @Test(priority = 1)
    public void testHomePageLoads() {

    }

    // Test 2: Home Page Pins Visible
    @Test(priority = 2)
    public void testHomePagePinsVisible() {

    }

    // Test 3: Logo Navigates to Home
    @Test(priority = 3)
    public void testLogoNavigatesToHome() {

    }

    // Test 4: Profile Menu Opens
    @Test(priority = 4)
    public void testProfileMenuOpens() {

    }

    // Test 5: Navigate to Profile Page
    @Test(priority = 5)
    public void testNavigateToProfilePage() {

    }

    // Test 6: Navigate to Saved Pins
    @Test(priority = 6)
    public void testNavigateToSavedPins() {

    }

    // Test 7: Search Bar Is Visible
    @Test(priority = 7)
    public void testSearchBarIsVisible() {

    }

    // Test 8: Footer Links Present
    @Test(priority = 8)
    public void testFooterLinksPresent() {

    }

    // Test 9: Scroll Loads More Pins (Infinite Scroll)
    @Test(priority = 9)
    public void testPageScrollLoadMorePins() {

    }
}
