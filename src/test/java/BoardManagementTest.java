import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class BoardManagementTest {

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

    private void login(){
        // Log In
        openLogIn();
        enterCredentials("sjtusick6535@eagle.fgcu.edu", "Test123910!");
        //enterCredentials("stusick@outlook.com", "SamuelTusick05!");
        clickLogIn();
    }

    private void clickBoards(){
        WebElement boards = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='VerticalNavContent']/div/div/div[1]/div[4]/div")));
        boards.click();
    }

// --------------------------------------------------------------------

// Tests

    // Test 1: Create New Board
    @Test(priority = 1)
    public void testCreateNewBoard() {
        // Log In
        login();

        // Create new board
        clickBoards();
        WebElement create = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[2]/div/div/div/div[3]/div")));
        create.click();
        driver.findElement(By.xpath("//*[@id='board_actions-item-1']/div/div/div[1]/div/div/span")).click();

        // Insert new board data
        WebElement name = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='board-name']")));
        name.sendKeys("TestBoard");
        driver.findElement(By.xpath("/html/body/div[11]/div/div/div/div[2]/div/div[3]/div/div/button")).click();

        WebElement done_btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[10]/div/div/div/div[2]/div/div[3]/div/div/div/button")));
        done_btn.click();

        // Test
        Assert.assertTrue(driver.getCurrentUrl().contains("/testboard/"),
                "Expected navigation to new board.");

    }

    // Test 2: Create Secret Board
    @Test(priority = 2)
    public void testCreateSecretBoard() {
        login();

        clickBoards();
        WebElement create = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div/div[2]/div/div/div/div[3]/div")));
        create.click();
        driver.findElement(By.xpath("//*[@id='board_actions-item-1']/div/div/div[1]/div/div/span")).click();

        // Insert new board data
        WebElement name = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='board-name']")));
        name.sendKeys("SecretBoard");
        driver.findElement(By.xpath("//*[@id='secret-board-toggle']")).click();
        driver.findElement(By.xpath("/html/body/div[11]/div/div/div/div[2]/div/div[3]/div/div/button")).click();

        WebElement done_btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[10]/div/div/div/div[2]/div/div[3]/div/div/div/button")));
        done_btn.click();

        // Test
        Assert.assertTrue(driver.getCurrentUrl().contains("/secretboard/"),
                "Expected navigation to new secret board.");

    }

    // Test 3: Delete Board
    @Test(priority = 3)
    public void testDeleteBoard() {
        login();

        clickBoards();

        // Click into board
        WebElement board = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[2]/div/div/div/div/div[1]/div/div/div/div/div/div/div/div[1]")));
        board.click();
        WebElement dots = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='__PWS_ROOT__']/div[1]/div/div[3]/div/div/div/div/div[1]/div/div[2]/div/div/div[2]/div/div[2]")));
        dots.click();
        WebElement edit_board = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='board_options-item-0']/div/div/div[1]/div/div")));
        edit_board.click();

        // Delete board
        WebElement delete_name = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[10]/div/div/div/div[2]/div/div[2]/div/form/div/div[5]")));
        delete_name.click();
        WebElement delete_btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[10]/div/div/div[2]/div/div[3]/div/div/div/div[2]")));
        delete_btn.click();

        driver.navigate().refresh();
        clickBoards();

        // Test
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/"),
                "Expected navigation to home page.");

    }

    // Test 4: Edit Board Name
    @Test(priority = 4)
    public void testEditBoardName() {

    }

    // Test 5: Board Appears on Profile
    @Test(priority = 5)
    public void testBoardAppearsOnProfile() {

    }

    // Test 6: Duplicate Board Name
    @Test(priority = 6)
    public void testDuplicateBoardName() {

    }


}
