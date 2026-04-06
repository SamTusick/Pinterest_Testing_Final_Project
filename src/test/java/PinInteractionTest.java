import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class PinInteractionTest {

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
        System.out.println("--- Pin Interaction Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Pin Interaction Testing class finished ---");
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

    // Test 1: Pin Opens on Click
    @Test(priority = 1)
    public void testPinOpensOnClick() {

    }

    // Test 2: Pin Detail Displays Image
    @Test(priority = 2)
    public void testPinDetailDisplaysImage() {

    }

    // Test 3: Pin Detail Displays Title or Description
    @Test(priority = 3)
    public void testPinDetailDisplaysTitle() {

    }

    // Test 4: Save Pin to Board
    @Test(priority = 4)
    public void testSavePinToBoard() {

    }

    // Test 5: Visit Link Button Present
    @Test(priority = 5)
    public void testVisitLinkButtonPresent() {

    }

    // Test 6: Close Pin Detail and Return to Feed
    @Test(priority = 6)
    public void testClosePinDetail() {

    }

    // Test 7: Share Button Present
    @Test(priority = 7)
    public void testShareButtonPresent() {

    }

}
