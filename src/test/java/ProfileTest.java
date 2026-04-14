import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

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
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

        WebElement profile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='HeaderContent']/div/div/div[2]/div/div/div/div[2]/div")));
        profile.click();
        WebElement profile_link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[1]/div[1]/div[2]")));
        profile_link.click();

        WebElement edit_profile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/div/div/div[1]")));
        edit_profile.click();

        String expectedName;
        By firstNameBy  = By.xpath("//*[@id='first_name']");
        By lastNameBy   = By.xpath("//*[@id='last_name']");
        By saveButtonBy = By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div[2]/div/div[2]/div[2]/div[2]/div/div/div/div[2]/div/div/div[2]/button");

        // --- First attempt: "Samuel Tusick" ---
        WebElement first_name = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameBy));
        WebElement last_name  = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameBy));

        first_name.sendKeys(Keys.CONTROL + "a");
        first_name.sendKeys("Samuel");
        last_name.sendKeys(Keys.CONTROL + "a");
        last_name.sendKeys("Tusick");

        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(saveButtonBy));
        boolean saveEnabled = saveButton.isEnabled() &&
                (saveButton.getAttribute("class") == null ||
                        !saveButton.getAttribute("class").contains("disabled"));

        if (!saveEnabled) {
            // Name is already "Samuel Tusick" — re-fetch fields and try alternate
            System.out.println("[Info] Save not clickable — name already set. Trying alternate name.");

            first_name = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameBy));
            last_name  = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameBy));

            first_name.sendKeys(Keys.CONTROL + "a");
            first_name.sendKeys("Sam");
            last_name.sendKeys(Keys.CONTROL + "a");
            last_name.sendKeys("Tusick");

            expectedName = "Sam Tusick";

            // Re-fetch save button too
            saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveButtonBy));
        } else {
            expectedName = "Samuel Tusick";
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);

        // Verify displayed name matches what was saved
        System.out.println("TESTING");
        driver.findElement(By.xpath("//*[@id='HeaderContent']/div/div/div[2]/div/div/div/div[2]")).click();
        WebElement test_profile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[1]/div[1]/div[2]")));
        test_profile.click();
        wait.until(ExpectedConditions.textToBe(
                By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[1]/div/div[2]/div/div[1]/div/div/h1/div"),
                expectedName
        ));

        WebElement displayedName = driver.findElement(
                By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[1]/div/div[2]/div/div[1]/div/div/h1/div")
        );

        Assert.assertEquals(displayedName.getText(), expectedName, "Profile name did not update correctly.");
    }

    // Test 3: Edit Profile – Bio Update
    @Test(priority = 3)
    public void testEditProfileBioUpdate() {
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        clickLogIn();

        WebElement profile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='HeaderContent']/div/div/div[2]/div/div/div/div[2]/div")));
        profile.click();
        WebElement profile_link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[1]/div[1]/div[2]")));
        profile_link.click();

        WebElement edit_profile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[4]/div/div/div[2]/div/div/div[1]")));
        edit_profile.click();

        String expectedBio;
        By bioBy        = By.xpath("//*[@id='about']");
        By saveButtonBy = By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div[2]/div/div[2]/div[2]/div[2]/div/div/div/div[2]/div/div/div[2]/button");

        // --- First attempt ---
        WebElement bio = wait.until(ExpectedConditions.visibilityOfElementLocated(bioBy));
        bio.sendKeys(Keys.CONTROL + "a");
        bio.sendKeys("Testing bio update.");

        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(saveButtonBy));
        boolean saveEnabled = saveButton.isEnabled() &&
                (saveButton.getAttribute("class") == null ||
                        !saveButton.getAttribute("class").contains("disabled"));

        if (!saveEnabled) {
            // Bio is already "Testing bio update." — try alternate
            System.out.println("[Info] Save not clickable — bio already set. Trying alternate bio.");

            bio = wait.until(ExpectedConditions.visibilityOfElementLocated(bioBy));
            bio.sendKeys(Keys.CONTROL + "a");
            bio.sendKeys("Updated bio for testing.");

            expectedBio = "Updated bio for testing.";

            saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveButtonBy));
        } else {
            expectedBio = "Testing bio update.";
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);

        // Navigate back to profile to verify bio
        driver.findElement(By.xpath("//*[@id='HeaderContent']/div/div/div[2]/div/div/div/div[2]")).click();
        WebElement test_profile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[1]/div[1]/div[2]")));
        test_profile.click();

        By bioDisplayLocator = By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[3]/div/span");

        wait.until(ExpectedConditions.textToBe(bioDisplayLocator, expectedBio));

        WebElement displayedBio = driver.findElement(bioDisplayLocator);
        Assert.assertEquals(displayedBio.getText().trim(), expectedBio, "Profile bio did not update correctly.");
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
