import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class LoginTest {

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
        System.out.println("--- Login Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Login Testing class finished ---");
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

    // Test 1: Valid Login
    @Test(priority = 1)
    public void testValidLogin(){

    }

    // Test 2: Invalid Password
    @Test(priority = 2)
    public void testInvalidPasswordLogin(){

    }

    // Test 3: Invalid Email
    @Test(priority = 3)
    public void testInvalidEmailLogin(){

    }

    // Test 4: Empty Email
    @Test(priority = 4)
    public void testEmptyEmailField(){

    }

    // Test 5: Empty Password Field
    @Test(priority = 5)
    public void testEmptyPasswordField() {

    }

    // Test 6: Both Fields Empty
    @Test(priority = 6)
    public void testEmptyBothFields() {

    }

    // Test 7: Login Button Present
    @Test(priority = 7)
    public void testLoginButtonDisabledInitially() {

    }

    // Test 8: Forgot Password Link Navigates
    @Test(priority = 8)
    public void testForgotPasswordLinkNavigates() {

    }

    // Test 9: Logout Successfully
    @Test(priority = 9)
    public void testLogoutSuccessfully() {

    }

}
