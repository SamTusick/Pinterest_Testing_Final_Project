import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class BoardManagementTest {

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
        System.out.println("--- Board Management Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Board Management Testing class finished ---");
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

    // Test 1: Create New Board
    @Test(priority = 1)
    public void testCreateNewBoard() {

    }

    // Test 2: Create Board With Empty Name
    @Test(priority = 2)
    public void testCreateBoardWithEmptyName() {

    }

    // Test 3: Create Secret Board
    @Test(priority = 3)
    public void testCreateSecretBoard() {

    }

    // Test 4: Delete Board
    @Test(priority = 4)
    public void testDeleteBoard() {

    }

    // Test 5: Edit Board Name
    @Test(priority = 5)
    public void testEditBoardName() {

    }

    // Test 6: Board Appears on Profile
    @Test(priority = 6)
    public void testBoardAppearsOnProfile() {

    }

    // Test 7: Duplicate Board Name
    @Test(priority = 7)
    public void testDuplicateBoardName() {

    }


}
