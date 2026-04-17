import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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
    // --------------------------------------------------------------------
// Helper Functions
    private void openLogIn(){
        driver.get("https://www.pinterest.com/");

        WebElement login_btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/header/div[1]/nav/div[3]/div[2]")));
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

    // Test 1: Settings Page Visible
    @Test(priority = 1)
    public void testSettingsPage() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

        WebElement settingsAndSupport = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='VerticalNavContent']/div/div/div[2]")));
        settingsAndSupport.click();
        WebElement settings = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='settings']/div/div[1]/div/div/span")));
        settings.click();

        // Test
        Assert.assertTrue(driver.getCurrentUrl().contains("/settings"),
                "Expected navigation to the settings page.");

    }

    // Test 2: Notification Panel Opens
    @Test(priority = 2)
    public void testNotificationPanelOpens() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

        WebElement settingsAndSupport = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='VerticalNavContent']/div/div/div[2]")));
        settingsAndSupport.click();
        WebElement settings = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='settings']/div/div[1]/div/div/span")));
        settings.click();
        WebElement noti = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div[2]/div/div[1]/nav/div[7]/a/div/div/div")));
        noti.click();

        // Test
        Assert.assertTrue(driver.getCurrentUrl().contains("/notifications"),
                "Expected navigation to the notification page.");


    }

    // Test 3: Get to Account Management
    @Test(priority = 3)
    public void testAccountManagement() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

        WebElement settingsAndSupport = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='VerticalNavContent']/div/div/div[2]")));
        settingsAndSupport.click();
        WebElement settings = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='settings']/div/div[1]/div/div/span")));
        settings.click();
        WebElement account = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div[2]/div/div[1]/nav/div[2]/a/div/div/div[1]")));
        account.click();

        // Test
        Assert.assertTrue(driver.getCurrentUrl().contains("/account-settings/"),
                "Expected navigation to the account management page.");


    }

    // Test 4: Change Password Fields Appear
    @Test(priority = 4)
    public void testChangePasswordField() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

        WebElement settingsAndSupport = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='VerticalNavContent']/div/div/div[2]")));
        settingsAndSupport.click();
        WebElement settings = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='settings']/div/div[1]/div/div/span")));
        settings.click();
        WebElement account = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div[2]/div/div[1]/nav/div[2]/a/div/div/div[1]")));
        account.click();
        WebElement change = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div[2]/div/div[2]/div[2]/div[3]/div[2]/button")));
        change.click();

        // Test
        WebElement title = driver.findElement(By.xpath("/html/body/div[8]/div/div/div/div[2]/div/div[1]/div/h1"));
        Assert.assertTrue(title.isDisplayed(), "Change password title was not displayed.");
        Assert.assertEquals(title.getText().trim(), "Change your password", "Change password title text was incorrect.");

    }

    // Test 5: Privacy Settings Toggle Changes State
    @Test(priority = 5)
    public void testPrivacySettingsToggle() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

        WebElement settingsAndSupport = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='VerticalNavContent']/div/div/div[2]")));
        settingsAndSupport.click();
        WebElement settings = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='settings']/div/div[1]/div/div/span")));
        settings.click();
        WebElement priv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div[2]/div/div[1]/nav/div[3]/a/div")));
        priv.click();

        WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div[2]/div/div[2]/form/div/div[1]/div/div/div/div[2]/div")));
        toggle.click();

        WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div[2]/div/div[2]/form/div/div[3]/div/div/div[2]/div/div/div[2]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", save);

        Assert.assertTrue(save.isDisplayed(), "Save button was not displayed — toggle may not have changed.");
    }

}
