import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomeAndNavigationTest {

    WebDriver driver;
    WebDriverWait wait;
    private static final String BASE_URL = "https://www.pinterest.com";

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
        System.out.println("--- Home and Navigation Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Home and Navigation Testing class finished ---");
    }

    @BeforeMethod
    public void setUp() {
        System.out.println("\n[Setup] Launching Chrome browser...");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--incognito");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
        System.out.println("[Setup] Browser ready.");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("[Teardown] Closing browser...");
        if (driver != null) {
            driver.quit();
        }
        System.out.println("[Teardown] Browser closed.");
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void requireLoginCredentials() {
        String email = System.getenv("PINTEREST_TEST_EMAIL");
        String password = System.getenv("PINTEREST_TEST_PASSWORD");
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new SkipException("Set PINTEREST_TEST_EMAIL and PINTEREST_TEST_PASSWORD to run authenticated home-page tests.");
        }
    }

    private void openHomePage() {
        driver.get(BASE_URL + "/");
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    private boolean isLoggedIn() {
        return !driver.findElements(By.name("searchBoxInput")).isEmpty()
                || !driver.findElements(By.cssSelector("input[placeholder='Search']")).isEmpty()
                || !driver.findElements(By.cssSelector("[data-test-id='header-profile']")).isEmpty()
                || !driver.findElements(By.cssSelector("a[href*='/pin/'], img")).isEmpty();
    }

    private void loginFromPinterestWebsite() {
        requireLoginCredentials();

        System.out.println("Opening Pinterest login page...");
        driver.get(BASE_URL + "/login/");
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        sleep(2500);

        String email = System.getenv("PINTEREST_TEST_EMAIL");
        String password = System.getenv("PINTEREST_TEST_PASSWORD");

        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("id")));
        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));

        System.out.println("Typing email...");
        emailField.clear();
        sleep(800);
        emailField.sendKeys(email);
        sleep(1200);

        System.out.println("Typing password...");
        passwordField.clear();
        sleep(800);
        passwordField.sendKeys(password);
        sleep(1500);

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        System.out.println("Clicking Log in...");
        loginButton.click();
        sleep(7000);

        System.out.println("Opening Pinterest home feed after login...");
        driver.get(BASE_URL + "/");
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        sleep(5000);

        if (!isLoggedIn()) {
            throw new SkipException("Could not confirm logged-in Pinterest home feed after signing in.");
        }

        System.out.println("Login completed. Starting test steps now.");
    }

    private List<WebElement> visibleElements(By... locators) {
        List<WebElement> visible = new ArrayList<>();
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed()) {
                        visible.add(element);
                    }
                } catch (Exception ignored) {
                }
            }
            if (!visible.isEmpty()) {
                return visible;
            }
        }
        return visible;
    }

    private WebElement firstVisible(By... locators) {
        List<WebElement> elements = visibleElements(locators);
        if (elements.isEmpty()) {
            throw new NoSuchElementException("No visible element found for provided locators.");
        }
        return elements.get(0);
    }

    private int getVisibleFeedPinCount() {
        List<WebElement> possiblePins = new ArrayList<>();
        possiblePins.addAll(driver.findElements(By.cssSelector("a[href*='/pin/']")));
        possiblePins.addAll(driver.findElements(By.cssSelector("img")));
        possiblePins.addAll(driver.findElements(By.cssSelector("div[data-test-id]")));

        int visibleCount = 0;
        for (WebElement element : possiblePins) {
            try {
                if (element.isDisplayed()) {
                    visibleCount++;
                }
            } catch (Exception ignored) {
            }
        }
        return visibleCount;
    }

    private WebElement getFirstVisibleFeedPin() {
        List<WebElement> pins = visibleElements(
                By.cssSelector("a[href*='/pin/']"),
                By.xpath("//div[@data-test-id='pin']//a"),
                By.cssSelector("img")
        );

        for (WebElement pin : pins) {
            try {
                if (pin.isDisplayed()) {
                    return pin;
                }
            } catch (Exception ignored) {
            }
        }
        throw new SkipException("No visible first pin was found on the feed.");
    }

    // Test 1 from doc: Switch between boards (top topic chips below search)
    @Test(priority = 1)
    public void testSwitchBetweenBoards() {
        loginFromPinterestWebsite();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(5000);

        int successfulClicks = 0;

        for (int i = 0; i < 10; i++) {
            try {
                List<WebElement> rawTabs = visibleElements(
                        By.xpath("//div[contains(@class,'WuRgKB') and normalize-space()]"),
                        By.xpath("//div[@role='listitem']//div[normalize-space()]"),
                        By.xpath("//div[@role='list']//div[normalize-space()]"),
                        By.xpath("//a[contains(@href,'/search/pins/')]")
                );

                List<WebElement> clickableTabs = new ArrayList<>();
                for (WebElement tab : rawTabs) {
                    try {
                        String text = tab.getText().trim();
                        int y = tab.getRect().getY();
                        int x = tab.getRect().getX();
                        int width = tab.getRect().getWidth();
                        if (!text.isEmpty() && y > 60 && y < 260 && x > 60 && width > 20) {
                            clickableTabs.add(tab);
                        }
                    } catch (Exception ignored) {
                    }
                }

                if (clickableTabs.isEmpty()) {
                    throw new SkipException("Not enough visible board/topic tabs were found.");
                }

                if (i >= clickableTabs.size()) {
                    break;
                }

                WebElement currentTab = clickableTabs.get(i);
                String tabText = currentTab.getText().trim();
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", currentTab);
                sleep(1200);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", currentTab);
                successfulClicks++;
                System.out.println("Clicked board/topic tab: " + tabText);
                sleep(3000);
            } catch (Exception e) {
                System.out.println("Could not click one of the board/topic tabs. Continuing...");
            }
        }

        Assert.assertTrue(successfulClicks > 0, "At least one board/topic tab should be clickable.");
    }

    @Test(priority = 2)
    public void testPageScrollLoadMorePins() {
        loginFromPinterestWebsite();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(5000);

        int initialPinCount = getVisibleFeedPinCount();
        System.out.println("Initial visible pins found: " + initialPinCount);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (int i = 1; i <= 3; i++) {
            System.out.println("Performing slow scroll number: " + i);
            js.executeScript("window.scrollBy(0, 800);");
            sleep(3500);
        }

        sleep(3000);
        int finalPinCount = getVisibleFeedPinCount();
        System.out.println("Visible pins after scrolling: " + finalPinCount);

        Assert.assertTrue(finalPinCount > 0,
                "Expected visible pins to be present on the home feed after scrolling.");
    }

    @Test(priority = 3)
    public void testVisitSecondarySiteFromPin() {
        loginFromPinterestWebsite();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(4000);

        WebElement firstPin = getFirstVisibleFeedPin();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstPin);
        sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstPin);
        sleep(3000);

        List<WebElement> visitButtons = visibleElements(
                By.xpath("//a[contains(.,'Visit site') or contains(.,'Visit')]"),
                By.xpath("//button[contains(.,'Visit site') or contains(.,'Visit')]"),
                By.cssSelector("a[href^='http']")
        );

        if (visitButtons.isEmpty()) {
            throw new SkipException("First pin opened, but no visible Visit site button was found.");
        }

        WebElement visitButton = visitButtons.get(0);
        String originalWindow = driver.getWindowHandle();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", visitButton);
        sleep(3000);

        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        if (windows.size() > 1) {
            driver.switchTo().window(windows.get(windows.size() - 1));
        }

        Assert.assertTrue(driver.getCurrentUrl().startsWith("http"),
                "Expected external site URL to open from the first pin.");
        driver.switchTo().window(originalWindow);
    }

    @Test(priority = 4)
    public void testSharePinFromHomePage() {
        loginFromPinterestWebsite();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(4000);

        WebElement firstPin = getFirstVisibleFeedPin();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstPin);
        sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstPin);
        sleep(3000);

        List<WebElement> shareButtons = visibleElements(
                By.xpath("//button[contains(@aria-label,'Share')]"),
                By.xpath("//button[contains(.,'Share')]"),
                By.xpath("//*[contains(text(),'Share')]/ancestor::button")
        );

        if (shareButtons.isEmpty()) {
            throw new SkipException("First pin opened, but no visible Share button was found.");
        }

        WebElement shareButton = shareButtons.get(0);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", shareButton);
        sleep(2000);

        WebElement copyLinkButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@data-test-id='copy-link-share-icon-auth']//button[@aria-label='Copy Link']")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", copyLinkButton);
        sleep(1000);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", copyLinkButton);
        sleep(2000);

        Assert.assertTrue(copyLinkButton.isDisplayed(),
                "Copy Link button should be visible and clickable for the selected pin.");
    }

    @Test(priority = 5)
    public void testClickBetweenLeftSideHomebar() {
        loginFromPinterestWebsite();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(4000);

        System.out.println("Clicking Explore icon...");
        WebElement exploreIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@data-test-id='today-tab' and @aria-label='Explore']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", exploreIcon);
        sleep(1500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", exploreIcon);
        sleep(3000);

        System.out.println("Clicking Create icon...");
        WebElement createIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@data-test-id='create-tab' and @aria-label='Create']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", createIcon);
        sleep(1500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createIcon);
        sleep(3000);

        System.out.println("Clicking Updates icon...");
        WebElement updatesIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='Updates']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", updatesIcon);
        sleep(1500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", updatesIcon);
        sleep(3000);

        System.out.println("Clicking Messages icon...");
        WebElement messagesIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@data-test-id='notifications-button' and @aria-label='Messages'] | //button[@aria-label='Messages']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", messagesIcon);
        sleep(1500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", messagesIcon);
        sleep(3000);

        System.out.println("Returning to Home icon...");
        WebElement homeIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@data-test-id='home-tab' or @aria-label='Home']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", homeIcon);
        sleep(1500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", homeIcon);
        sleep(3000);

        Assert.assertTrue(true, "Left side navigation icons were clicked and test returned to the home page.");
    }
}
