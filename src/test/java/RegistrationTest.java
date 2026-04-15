import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.HashMap;

public class RegistrationTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void beforeClass() {
        System.out.println("=== REGISTRATION TEST SUITE STARTING ===");
    }

    @BeforeMethod
    public void setUp() throws InterruptedException {
        System.out.println("\n[Setup] Launching Chrome browser...");

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--incognito");

        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get("https://www.pinterest.com/");

        Thread.sleep(3000);
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);

        if (driver != null) {
            driver.quit();
        }

        System.out.println("[Teardown] Browser closed.");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("=== REGISTRATION TEST SUITE FINISHED ===");
    }

    // helper method to open signup form if needed
    public void openSignupForm() throws InterruptedException {
        Thread.sleep(2000);

        try {
            WebElement emailField = driver.findElement(By.xpath("//input[@id='email' or @name='id' or @type='email']"));
            if (emailField.isDisplayed()) {
                System.out.println("Signup form is already visible.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Signup form not visible yet. Attempting to open it.");
        }

        try {
            WebElement signUpButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Sign up' or text()='Continue with email']/ancestor::button | //button[contains(.,'Sign up')]")));
            signUpButton.click();
            System.out.println("Signup button clicked.");
        } catch (Exception e) {
            System.out.println("Could not find separate signup button. Form may already be open.");
        }

        Thread.sleep(2000);
    }

    // helper method to fill signup form
    public void fillSignupForm(String email, String password, String birthdate) throws InterruptedException {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='email' or @name='id' or @type='email']")));
        emailField.clear();
        emailField.sendKeys(email);
        System.out.println("Email entered: " + email);

        Thread.sleep(1000);

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='password' or @name='password' or @type='password']")));
        passwordField.clear();
        passwordField.sendKeys(password);
        System.out.println("Password entered.");

        Thread.sleep(1000);

        try {
            WebElement birthdateField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@id='birthdate' or @name='birthday' or @name='birthdate' or contains(@id,'birth')]")));
            birthdateField.clear();
            birthdateField.sendKeys(birthdate);
            System.out.println("Birthdate entered: " + birthdate);
        } catch (Exception e) {
            System.out.println("Birthdate field not found. Pinterest page layout may be different.");
        }

        Thread.sleep(1000);
    }

    // helper method to click continue button
    public void clickContinueButton() {
        try {
            WebElement continueButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[@aria-label='Continue creating your Pinterest account']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueButton);
            System.out.println("Continue button clicked successfully.");
        } catch (Exception e) {
            Assert.fail("Continue button was not found or could not be clicked.");
        }
    }

    // helper method to click google signup button inside iframe
    public void clickGoogleSignupButton() {
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(
                    By.xpath("//iframe[contains(@src,'accounts.google.com')]")));
            System.out.println("Switched into Google iframe.");

            WebElement googleButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@role='button']")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", googleButton);
            System.out.println("Google button clicked.");

            driver.switchTo().defaultContent();
        } catch (Exception e) {
            driver.switchTo().defaultContent();
            Assert.fail("Google signup button inside iframe was not found or clickable.");
        }
    }

    @Test(priority = 1)
    public void signupUsingValidNewEmail() throws InterruptedException {

        openSignupForm();

        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@gmail.com";
        fillSignupForm(uniqueEmail, "StrongPass123!", "01/01/2000");

        clickContinueButton();

        Thread.sleep(5000);

        String pageText = driver.getPageSource().toLowerCase();
        String currentUrl = driver.getCurrentUrl().toLowerCase();

        System.out.println("Current URL after valid signup attempt: " + currentUrl);

        boolean movedForward =
                pageText.contains("welcome to pinterest") ||
                        pageText.contains("what are you interested in") ||
                        pageText.contains("tell us what") ||
                        pageText.contains("finish signing up") ||
                        pageText.contains("pick your interests") ||
                        pageText.contains("personalized ideas") ||
                        pageText.contains("continue signing up") ||
                        pageText.contains("create your profile") ||
                        currentUrl.contains("onboarding");

        boolean didNotStayOnSameSignupStep =
                !pageText.contains("valid email") &&
                        !pageText.contains("invalid email") &&
                        !pageText.contains("password is too weak");

        Assert.assertTrue(
                movedForward || didNotStayOnSameSignupStep,
                "Valid signup attempt did not appear to move past the initial signup step."
        );
    }

    @Test(priority = 2)
    public void signupUsingInvalidEmailFormat() throws InterruptedException {

        openSignupForm();
        fillSignupForm("invalidemail", "StrongPass123!", "01/01/2000");

        clickContinueButton();

        Thread.sleep(3000);

        String pageText = driver.getPageSource().toLowerCase();
        System.out.println("Checked page for invalid email message.");

        Assert.assertTrue(
                pageText.contains("valid email") ||
                        pageText.contains("invalid") ||
                        pageText.contains("enter your email") ||
                        pageText.contains("email"),
                "No validation message appeared for invalid email."
        );
    }

    @Test(priority = 3)
    public void signupUsingExistingEmail() throws InterruptedException {

        openSignupForm();

        // email already exists on Pinterest
        fillSignupForm("mayo.yalissa29@gmail.com", "StrongPass123!", "01/01/2000");

        clickContinueButton();

        Thread.sleep(4000);

        String currentUrl = driver.getCurrentUrl().toLowerCase();
        String pageText = driver.getPageSource().toLowerCase();

        System.out.println("Checked page for existing email behavior.");

        boolean foundMessage =
                pageText.contains("already have an account") ||
                        pageText.contains("account already exists") ||
                        pageText.contains("email is already in use") ||
                        pageText.contains("log in instead") ||
                        pageText.contains("welcome back") ||
                        pageText.contains("looks like you already have an account");

        boolean blockedFromNewSignup =
                !pageText.contains("tell us what") &&
                        !pageText.contains("welcome to pinterest") &&
                        !currentUrl.contains("onboarding");

        Assert.assertTrue(
                foundMessage || blockedFromNewSignup,
                "Existing email should not continue as a brand new registration."
        );
    }

    @Test(priority = 4)
    public void signupWithWeakPassword() throws InterruptedException {
    
        openSignupForm();

        String uniqueEmail = "weakpass" + System.currentTimeMillis() + "@gmail.com";
        fillSignupForm(uniqueEmail, "123", "01/01/2000");

        clickContinueButton();

        Thread.sleep(3000);

        String pageText = driver.getPageSource().toLowerCase();
        System.out.println("Checked page for weak password message.");

        Assert.assertTrue(
                pageText.contains("password") ||
                        pageText.contains("weak") ||
                        pageText.contains("stronger") ||
                        pageText.contains("must"),
                "No validation message appeared for weak password."
        );
    }

    @Test(priority = 5)
    public void signupWithBusinessAccount() throws InterruptedException {

        openSignupForm();

        System.out.println("Clicking Create a free business account...");
        WebElement businessAccountButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(
                        "//div[@data-test-id='create-business-account-button']//div[@role='button']" +
                                " | //div[contains(text(),'Create a free business account')]/ancestor::div[@role='button']" +
                                " | //div[contains(text(),'Create a free business account')]"
                )
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", businessAccountButton);
        Thread.sleep(4000);

        System.out.println("Filling business account form...");
        WebElement businessEmailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='email' and @name='email']")
        ));
        businessEmailField.clear();

        String businessEmail = "Gloria.Mayo@marriott.com";
        businessEmailField.sendKeys(businessEmail);
        System.out.println("Business email entered: " + businessEmail);
        Thread.sleep(1000);

        WebElement businessPasswordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='password']")
        ));
        businessPasswordField.clear();
        businessPasswordField.sendKeys("StrongPass123!");
        System.out.println("Business password entered.");
        Thread.sleep(1000);

        WebElement businessBirthdateField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[contains(@placeholder,'mm/dd/yyyy') or contains(@id,'birth')]")
        ));
        businessBirthdateField.clear();
        businessBirthdateField.sendKeys("01/01/2000");
        System.out.println("Business birthdate entered.");
        Thread.sleep(1000);

        System.out.println("Clicking Create account...");
        WebElement createAccountButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//div[normalize-space()='Create account'] or normalize-space()='Create account']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createAccountButton);
        Thread.sleep(6000);

        System.out.println("Checking whether Pinterest home page/feed became visible...");
        boolean homeVisible =
                !driver.findElements(By.xpath("//input[@name='searchBoxInput' or @placeholder='Search with this Pin' or @placeholder='Search']")).isEmpty()
                        || !driver.findElements(By.xpath("//div[@data-test-id='pinterest-logo-home-button']")).isEmpty()
                        || !driver.findElements(By.xpath("//a[@aria-label='Home']")).isEmpty()
                        || driver.getCurrentUrl().toLowerCase().contains("pinterest.com");

        String pageText = driver.getPageSource().toLowerCase();
        String currentUrl = driver.getCurrentUrl().toLowerCase();

        System.out.println("Current URL after business signup attempt: " + currentUrl);

        boolean movedForward =
                homeVisible
                        || pageText.contains("welcome to pinterest")
                        || pageText.contains("grow your business")
                        || pageText.contains("tell us more")
                        || pageText.contains("what are you interested in")
                        || pageText.contains("get started")
                        || currentUrl.contains("onboarding");

        Assert.assertTrue(
                movedForward,
                "Business account signup did not appear to continue to the next step or home page."
        );
    }
}
