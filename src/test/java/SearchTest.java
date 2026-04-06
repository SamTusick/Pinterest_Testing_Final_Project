import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class SearchTest {

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
        System.out.println("--- Search Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Search Testing class finished ---");
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

    // Test 1: Basic Keyword Search
    @Test(priority = 1)
    public void testBasicKeywordSearch() {

    }

    // Test 2: Empty Search Query
    @Test(priority = 2)
    public void testEmptySearchQuery() {

    }

    // Test 3: Search Results Not Empty
    @Test(priority = 3)
    public void testSearchResultsNotEmpty() {

    }


    // Test 4: Search Suggestions Appear
    @Test(priority = 4)
    public void testSearchSuggestionsAppear() {

    }

    // Test 5: Search With Special Characters
    @Test(priority = 5)
    public void testSearchWithSpecialCharacters() {

    }

    // Test 6: Search Filter by Pins
    @Test(priority = 6)
    public void testSearchFilterByPins() {

    }

    // Test 7: Search Filter by People
    @Test(priority = 7)
    public void testSearchFilterByPeople() {

    }

    // Test 8: Search Results URL Contains Keyword
    @Test(priority = 8)
    public void testSearchResultsContainKeyword() {

    }
}
