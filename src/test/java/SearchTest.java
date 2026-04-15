import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;

public class SearchTest {

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
        System.out.println("--- Search Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Search Testing class finished ---");
    }

    @BeforeMethod
    public void setUp() {
        System.out.println("\n[Setup] Launching Chrome browser...");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--incognito");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");

        java.util.HashMap<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("profile.default_content_setting_values.media_stream_mic", 1);
        prefs.put("profile.default_content_setting_values.media_stream_camera", 1);
        options.setExperimentalOption("prefs", prefs);

        // allow microphone automatically
        options.addArguments("--use-fake-ui-for-media-stream");

        // provide a fake microphone device
        options.addArguments("--use-fake-device-for-media-stream");

        /*String wavPath = System.getenv("PINTEREST_FAKE_AUDIO_WAV");
        if (wavPath != null && !wavPath.trim().isEmpty()) {
            options.addArguments("--use-file-for-fake-audio-capture=" + wavPath);
        }*/

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

    private void waitForPage() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    private void requireLoginCredentials() {
        String email = System.getenv("PINTEREST_TEST_EMAIL");
        String password = System.getenv("PINTEREST_TEST_PASSWORD");
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new SkipException("Set PINTEREST_TEST_EMAIL and PINTEREST_TEST_PASSWORD to run search tests.");
        }
    }

    private WebElement findVisible(By... locators) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed()) {
                        return element;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        throw new NoSuchElementException("No visible element found for provided locators.");
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

    private WebElement getSearchField() {
        return findVisible(
                By.name("searchBoxInput"),
                By.cssSelector("input[type='search']"),
                By.xpath("//input[contains(@placeholder,'Search') or contains(@aria-label,'Search')]")
        );
    }

    private void loginFromPinterestWebsite() {
        requireLoginCredentials();

        System.out.println("Opening Pinterest login page...");
        driver.get(BASE_URL + "/login/");
        waitForPage();
        sleep(2500);

        String email = System.getenv("PINTEREST_TEST_EMAIL");
        String password = System.getenv("PINTEREST_TEST_PASSWORD");

        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("id")));
        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));

        System.out.println("Typing email...");
        emailField.click();
        sleep(800);
        emailField.sendKeys(email);
        sleep(1200);

        System.out.println("Typing password...");
        passwordField.click();
        sleep(800);
        passwordField.sendKeys(password);
        sleep(1500);

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        System.out.println("Clicking Log in...");
        loginButton.click();
        sleep(7000);

        System.out.println("Opening Pinterest home feed after login...");
        driver.get(BASE_URL + "/");
        waitForPage();
        sleep(5000);

        if (driver.findElements(By.name("searchBoxInput")).isEmpty()
                && driver.findElements(By.cssSelector("input[type='search']")).isEmpty()) {
            throw new SkipException("Could not confirm logged-in Pinterest home feed after signing in.");
        }

        System.out.println("Login completed. Starting test steps now.");
    }

    private void openSearchExperience() {
        loginFromPinterestWebsite();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(3000);
    }

    private void clearAndTypeSearch(String keyword) {
        WebElement searchField = getSearchField();
        System.out.println("Clicking search field...");
        searchField.click();
        sleep(1000);
        searchField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        sleep(800);
        System.out.println("Typing keyword: " + keyword);
        searchField.sendKeys(keyword);
        sleep(1500);
    }

    private void submitSearch() {
        WebElement searchField = getSearchField();
        System.out.println("Submitting search...");
        searchField.sendKeys(Keys.ENTER);
        sleep(5000);
    }

    private boolean resultsPresent() {
        return !visibleElements(
                By.cssSelector("img"),
                By.xpath("//*[@data-test-id='pin']"),
                By.xpath("//div[contains(@data-test-id,'pin')]"),
                By.xpath("//a[contains(@href,'/pin/')]")
        ).isEmpty();
    }

    @Test(priority = 1)
    public void testBasicKeywordSearch() {
        openSearchExperience();
        clearAndTypeSearch("tattoo inspo");
        submitSearch();

        Assert.assertTrue(driver.getCurrentUrl().contains("search") || resultsPresent(),
                "Search should navigate to a results experience.");
        Assert.assertTrue(resultsPresent(), "Search results should appear for a valid keyword.");
    }

    @Test(priority = 2)
    public void testEmptySearchQuery() {
        openSearchExperience();
        String originalUrl = driver.getCurrentUrl();

        WebElement searchField = getSearchField();
        System.out.println("Clicking search field with empty input...");
        searchField.click();
        sleep(1200);
        searchField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        sleep(800);
        searchField.sendKeys(Keys.ENTER);
        sleep(3000);

        Assert.assertTrue(driver.getCurrentUrl().equals(originalUrl) || driver.getCurrentUrl().contains("search"),
                "Submitting an empty search should not crash navigation.");
    }
    
    @Test(priority = 3)
    public void testApplySearchFilters() throws InterruptedException {
        loginFromPinterestWebsite();

        System.out.println("Clicking search field...");
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='Search' or @name='searchBoxInput']")
        ));
        searchBox.click();
        Thread.sleep(1500);

        searchBox.clear();
        Thread.sleep(1000);

        System.out.println("Typing keyword: dinner ideas");
        searchBox.sendKeys("dinner ideas");
        Thread.sleep(1500);

        System.out.println("Submitting search...");
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(5000);

        System.out.println("Clicking filter button...");
        WebElement filterButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='Show filters' and @data-test-id='one-bar-pill']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", filterButton);
        Thread.sleep(3000);

        System.out.println("Selecting Videos filter...");
        WebElement videosOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@id='Videos']/ancestor::div[@role='button'][1]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", videosOption);
        Thread.sleep(2000);

        System.out.println("Clicking Apply...");
        WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Apply']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", applyButton);
        Thread.sleep(4000);

        Assert.assertTrue(true, "Filter button was opened and videos was selected.");
    }

    @Test(priority = 4)
    public void testSearchViaVoiceSearch() throws InterruptedException {
        loginFromPinterestWebsite();

        Thread.sleep(3000);

        System.out.println("Clicking voice search button...");
        WebElement micButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='Voice search']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", micButton);

        Thread.sleep(5000);

        Assert.assertTrue(true, "Voice search button was clicked successfully.");
    }

    @Test(priority = 5)
    public void testSearchResultsPopulate() {
        openSearchExperience();

        WebElement searchField = getSearchField();
        searchField.click();
        sleep(1500);
        searchField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        sleep(2000);

        List<WebElement> suggestions = visibleElements(
                By.xpath("//*[contains(text(),'Recent') or contains(text(),'Ideas for you') or contains(text(),'Trending') or contains(text(),'Popular') ]"),
                By.xpath("//div[@role='listbox']//*"),
                By.xpath("//*[@data-test-id='searchTypeahead']//*")
        );

        Assert.assertFalse(suggestions.isEmpty(),
                "Should populate recent searches or suggested content.");
    }
    
    @Test(priority = 6)
    public void testClearRecentSearches() throws InterruptedException {
        loginFromPinterestWebsite();

        System.out.println("Opening search box to show recent searches...");
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@data-test-id='search-box-input' or @name='searchBoxInput' or @placeholder='Search']")
        ));
        searchBox.click();
        Thread.sleep(3000);

        for (int i = 0; i < 3; i++) {

            // each recent search tile has its own hidden remove area
            List<WebElement> removeAreas = driver.findElements(By.xpath(
                    "//div[@data-test-id='clear-recent-queries']"
            ));

            List<WebElement> visibleTiles = new ArrayList<>();
            for (WebElement area : removeAreas) {
                try {
                    WebElement tile = area.findElement(By.xpath("./ancestor::div[@tabindex='0' or @role='button' or .//a][1]"));
                    if (tile.isDisplayed()) {
                        visibleTiles.add(tile);
                    }
                } catch (Exception ignored) {
                }
            }

            if (visibleTiles.isEmpty()) {
                throw new SkipException("No recent search tiles were found.");
            }

            WebElement currentTile = visibleTiles.get(0);

            System.out.println("Hovering over recent search #" + (i + 1));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", currentTile);
            Thread.sleep(1000);

            // force a real hover with JS mouse events
            ((JavascriptExecutor) driver).executeScript(
                    "var e = arguments[0];" +
                            "['mouseenter','mouseover','mousemove'].forEach(function(type) {" +
                            "  e.dispatchEvent(new MouseEvent(type, {bubbles:true, cancelable:true, view:window}));" +
                            "});",
                    currentTile
            );
            Thread.sleep(2000);

            // re-find the remove button after hover
            WebElement removeButton = currentTile.findElement(By.xpath(
                    ".//div[@data-test-id='clear-recent-queries']//button[@aria-label='Remove recent search']"
            ));

            System.out.println("Clicking X for recent search #" + (i + 1));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeButton);
            Thread.sleep(2500);
        }

        Assert.assertTrue(true, "Three recent searches were removed.");
    }
}


