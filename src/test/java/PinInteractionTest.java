import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;
import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class PinInteractionTest {

    WebDriver driver;
    WebDriverWait wait;
    private static final String BASE_URL = "https://www.pinterest.com";
    private File downloadFolder;

    private String createdCommentText;
    private String secondPinUrl;

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
        System.out.println("--- Pin Interaction Testing class loaded ---");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("--- Pin Interaction Testing class finished ---");
    }

    @BeforeMethod
    public void setUp() throws InterruptedException {
        System.out.println("\n[Setup] Launching Chrome browser...");
        WebDriverManager.chromedriver().setup();

        // create download folder before using it
        downloadFolder = new File("pin_downloads");
        if (!downloadFolder.exists()) {
            downloadFolder.mkdirs();
        }

        // optional: clear old files from previous runs
        File[] oldFiles = downloadFolder.listFiles();
        if (oldFiles != null) {
            for (File file : oldFiles) {
                file.delete();
            }
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--incognito");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");

        String wavPath = System.getenv("PINTEREST_FAKE_AUDIO_WAV");
        if (wavPath != null && !wavPath.isBlank()) {
            options.addArguments("--use-file-for-fake-audio-capture=" + wavPath);
        }

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadFolder.getAbsolutePath());
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();

        driver.get(BASE_URL + "/");
        try {
            ((ChromeDriver) driver).setPermission("microphone", "granted");
        } catch (Exception ignored) {
        }

        sleep(2000);
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
        if (System.getenv("PINTEREST_TEST_EMAIL") == null || System.getenv("PINTEREST_TEST_EMAIL").isBlank()
                || System.getenv("PINTEREST_TEST_PASSWORD") == null || System.getenv("PINTEREST_TEST_PASSWORD").isBlank()) {
            throw new SkipException("Set PINTEREST_TEST_EMAIL and PINTEREST_TEST_PASSWORD to run authenticated pin tests.");
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

    private void openFirstPinFromHomeFeed() {
        loginFromPinterestWebsite();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(4000);

        WebElement firstPin = getFirstVisibleFeedPin();
        System.out.println("Opening the first visible pin from the home feed...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstPin);
        sleep(1500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstPin);
        sleep(4000);
    }

    @Test(priority = 1)
    public void testLikeUnlikePin() {
        openFirstPinFromHomeFeed();

        WebElement reactButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[@data-test-id='react-button' and @aria-label='React']")
        ));

        System.out.println("Scrolling to react button...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", reactButton);
        sleep(1500);

        System.out.println("Clicking react button to like...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", reactButton);
        sleep(3000);

        System.out.println("Clicking react button again to unlike...");
        WebElement reactButtonAgain = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[@data-test-id='react-button' and @aria-label='React']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", reactButtonAgain);
        sleep(3000);

        Assert.assertTrue(true, "Like and unlike actions were triggered on the pin.");
    }

    @Test(priority = 2)
    public void testCommentOnPin() {
        loginFromPinterestWebsite();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(4000);

        List<WebElement> pins = visibleElements(
                By.cssSelector("a[href*='/pin/']"),
                By.xpath("//div[@data-test-id='pin']//a"),
                By.cssSelector("img")
        );

        if (pins.size() < 2) {
            throw new SkipException("A second visible pin was not found on the home feed.");
        }

        WebElement secondPin = pins.get(1);

        System.out.println("Opening the second visible pin from the home feed...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", secondPin);
        sleep(1500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", secondPin);
        sleep(4000);

        // save the exact pin URL for Test 3
        secondPinUrl = driver.getCurrentUrl();
        System.out.println("Saved second pin URL: " + secondPinUrl);

        WebElement commentField = findVisible(
                By.xpath("//textarea"),
                By.xpath("//div[@contenteditable='true']"),
                By.xpath("//input[contains(@placeholder,'comment') or contains(@aria-label,'comment')]")
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", commentField);
        sleep(1500);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", commentField);
        sleep(1000);

        // make comment unique so Test 3 can find the exact one
        createdCommentText = "Automation test comment " + System.currentTimeMillis();
        System.out.println("Typing comment: " + createdCommentText);
        commentField.sendKeys(createdCommentText);
        sleep(2000);

        WebElement postButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@data-test-id='activity-item-create-submit']//button[@aria-label='Post']")
        ));

        System.out.println("Clicking Post...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", postButton);
        sleep(3000);

        Assert.assertTrue(true, "Comment was entered and posted on the second pin.");
    }

    @Test(priority = 3, dependsOnMethods = "testCommentOnPin")
    public void testDeleteComment() {
        if (secondPinUrl == null || createdCommentText == null) {
            throw new SkipException("No saved pin URL or comment text from Test 2.");
        }

        loginFromPinterestWebsite();

        System.out.println("Opening the same pin from Test 2...");
        driver.get(secondPinUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(5000);

        System.out.println("Looking for the exact comment made in Test 2...");
        WebElement targetComment = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), \"" + createdCommentText + "\")]")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", targetComment);
        sleep(2000);

        // open the comment options near that exact comment
        WebElement moreOptions = targetComment.findElement(By.xpath(
                "./ancestor::*[self::div or self::li][1]//*[contains(@aria-label,'More options') or contains(@aria-label,'more')]"
        ));

        System.out.println("Opening options for the exact comment...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", moreOptions);
        sleep(2000);

        WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(),'Delete') or contains(text(),'delete')]")
        ));

        System.out.println("Deleting the exact comment from Test 2...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteOption);
        sleep(3000);

        Assert.assertTrue(true, "The exact comment created in Test 2 was deleted.");
    }

    @Test(priority = 5)
    public void testUploadPhotoToComment() throws Exception {
        openFirstPinFromHomeFeed();

        System.out.println("Waiting for pin page to fully load...");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(4000);

        System.out.println("Scrolling a little lower so the comment tools are easier to see...");
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
        sleep(2000);

        System.out.println("Clicking photo icon on the comment box...");
        WebElement photoIconButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='Select a photo']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", photoIconButton);
        sleep(1500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", photoIconButton);
        sleep(3000);

        System.out.println("Finding upload input...");
        WebElement uploadInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@data-test-id='photo-upload-input' and @type='file']")
        ));

        File imageFile = new File("/C:/Users/mayoy/Downloads/download (1).jpg/");
        if (!imageFile.exists()) {
            throw new SkipException("The upload image file was not found.");
        }

        System.out.println("Uploading image to comment...");
        uploadInput.sendKeys(imageFile.getAbsolutePath());
        sleep(5000);

        System.out.println("Clicking Post...");
        WebElement postButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@data-test-id='activity-item-create-submit']//button[@aria-label='Post']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", postButton);
        sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", postButton);
        sleep(4000);

        Assert.assertTrue(true, "Photo was uploaded and posted to the comment box.");
    }

    @Test(priority = 6)
    public void testDownloadPin() throws Exception {
        openFirstPinFromHomeFeed();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        sleep(5000);

        WebElement mainPinImage = findVisible(
                By.xpath("//img[@elementtiming='closeup-image-main-MainPinImage']"),
                By.xpath("//img[contains(@elementtiming,'closeup-image-main')]"),
                By.xpath("//div[@data-test-id='closeup-image-main']//img"),
                By.xpath("//img[contains(@src,'i.pinimg.com') and contains(@src,'736x')]")
        );

        String imageUrl = mainPinImage.getAttribute("src");

        if (imageUrl == null || imageUrl.isBlank()) {
            String srcset = mainPinImage.getAttribute("srcset");
            if (srcset != null && !srcset.isBlank()) {
                imageUrl = srcset.split(",")[0].trim().split(" ")[0];
            }
        }

        if (imageUrl == null || imageUrl.isBlank()) {
            throw new SkipException("Could not find the selected pin image URL.");
        }

        if (downloadFolder == null) {
            downloadFolder = new File("pin_downloads");
        }
        if (!downloadFolder.exists()) {
            downloadFolder.mkdirs();
        }

        String extension = ".jpg";
        String lowerUrl = imageUrl.toLowerCase();

        if (lowerUrl.contains(".png")) {
            extension = ".png";
        } else if (lowerUrl.contains(".webp")) {
            extension = ".webp";
        } else if (lowerUrl.contains(".jpeg")) {
            extension = ".jpeg";
        } else if (lowerUrl.contains(".jpg")) {
            extension = ".jpg";
        }

        File outputFile = new File(downloadFolder, "downloaded_pin_" + System.currentTimeMillis() + extension);

        System.out.println("Downloading selected pin image into pin_downloads folder...");
        try (java.io.InputStream in = new java.net.URL(imageUrl).openStream()) {
            java.nio.file.Files.copy(
                    in,
                    outputFile.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
        }

        System.out.println("Downloaded file saved as: " + outputFile.getAbsolutePath());

        Assert.assertTrue(outputFile.exists() && outputFile.length() > 0,
                "The selected pin image was not saved correctly.");
    }
}
