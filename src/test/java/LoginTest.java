import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {

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


    // Test 1: Valid Login
    @Test(priority = 1)
    public void testValidLogin() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

       WebElement settings = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='VerticalNavContent']/div/div/div[2]/div/div[1]/div/button/div/div")));
       settings.click();
    }

    // Test 2: Invalid Password
    @Test(priority = 2)
    public void testInvalidPasswordLogin(){
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "WrongPassword");
        clickLogIn();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='password-error']/div/span[1]")));
        Assert.assertTrue(error.isDisplayed(), "Expected error message for invalid password.");
    }

    // Test 3: Invalid Email
    @Test(priority = 3)
    public void testInvalidEmailLogin(){
        openLogIn();
        enterCredentials("wrong@email.com", "Test123910!");
        clickLogIn();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='password-error']/div/span[1]")));
        Assert.assertTrue(error.isDisplayed(), "Expected error message for invalid email.");
    }

    // Test 4: Empty Email
    @Test(priority = 4)
    public void testEmptyEmailField(){
        openLogIn();
        enterCredentials("", "Test123910!");
        clickLogIn();
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='email-error']")));
        Assert.assertTrue(error.isDisplayed(), "Expected error message for empty email.");
    }

    // Test 5: Empty Password Field
    @Test(priority = 5)
    public void testEmptyPasswordField() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "");
        clickLogIn();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='password-error']/div/span[1]")));

    }

    // Test 6: Both Fields Empty
    @Test(priority = 6)
    public void testEmptyBothFields() {
        openLogIn();
        enterCredentials("", "");
        clickLogIn();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='email-error']")));

    }

    // Test 7: Forgot Password Link Navigates
    @Test(priority = 7)
    public void testForgotPasswordLinkNavigates() {
        openLogIn();
        WebElement forgot_password = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div[2]/div/div/div/div/div/div[3]/div[1]/div/div[1]/div[1]/form/div[5]/div/div/a")));
        forgot_password.click();

        Assert.assertTrue(driver.getCurrentUrl().contains("password/reset"),
                "Expected navigation to the password-reset page.");
    }

    // Test 8: Logout Successfully
    @Test(priority = 8)
    public void testLogoutSuccessfully() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

        WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='HeaderContent']/div/div/div[2]/div/div/div/div[3]/div[1]/div/button")));
        profile.click();
        driver.findElement(By.xpath("//*[@id='HeaderAccountOptionsFlyout-item-3']/div/div/div[1]/div/div/span")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("https://www.pinterest.com/"),
                "Expected navigation to the landing page.");
    }
// --------------------------------------------------------------------
}
