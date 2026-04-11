import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class ProfileTest {

// Set Up
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
// --------------------------------------------------------------------
// Helper Functions
private void openLogIn(){
    driver.get("https://www.pinterest.com/");

    WebElement login_btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/header/div[1]/nav/div[3]/div[2]/button/div/div")));
    login_btn.click();

}

    private void enterCredentials(String email, String password) {
        WebElement email_field = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='email']")));
        email_field.sendKeys(email);
        WebElement password_field = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='password']")));
        password_field.sendKeys(password);

    }

    private void clickLogIn(){
        driver.findElement(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div[2]/div/div/div/div/div/div[3]/div[1]/div/div[1]/div[1]/form/div[7]/button")).click();
    }

// --------------------------------------------------------------------

// Tests
    // Test 1: Profile Page Loads
    @Test(priority = 1)
    public void testProfilePageLoads() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

        WebElement profile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='HeaderContent']/div/div/div[2]/div/div/div/div[2]/div")));
        profile.click();
        WebElement profile_link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[1]/div[1]/div[2]")));
        profile_link.click();

        Assert.assertTrue(driver.getCurrentUrl().contains("/_profile/"),
                "Expected navigation to the profile page.");

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
