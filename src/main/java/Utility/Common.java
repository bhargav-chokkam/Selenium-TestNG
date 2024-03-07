package Utility;

import com.aventstack.extentreports.ExtentTest;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.*;

public class Common {
    private static final Properties prop; // Property File
    private static WebDriver driver;
    private static ExtentTest spark; // Spark Reporter
    private static WebDriverWait gWait; // Global Wait

    static {
        prop = new Properties();
        String propPath = "//src//main//java//Utility//propFile.properties";
        FileInputStream file = null;
        try {
            file = new FileInputStream(System.getProperty("user.dir") + propPath);
        } catch (FileNotFoundException e) {
            System.out.println("Property File Not Found : " + e);
        }
        try {
            prop.load(file);
        } catch (IOException e) {
            System.out.println("Properties File Load Failed:" + e.getMessage());
        }
    }

    private Map<String, String> data;

    public Common(ExtentTest spark) {
        Common.spark = spark;
    }

    public Common(String project, String dataFile) {
        String path = System.getProperty("user.dir") + "//src//test//java//com//" + project + "//TestData//" + dataFile + ".json";
        System.out.println("Test Data File Path: " + path);
        try {
            Object temp = new JSONParser().parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) temp;
            data = toMap(jsonObject);
        } catch (ParseException | IOException e) {
            System.out.println("Loading Data JSON Failed: " + e.getMessage());
        }
    }

    public Common() {

    }

    private static String getLocator(WebElement element) {
        // Returns Locator from WebElement
        String returnElement = null;
        try {
            String temp = String.valueOf(element);
            String[] splitData = temp.split(">");
            returnElement = splitData[1];
        } catch (Exception e) {
            System.out.println("Get Locator Method Failed: " + e);
        }
        return returnElement;
    }

    private static Map<String, String> toMap(JSONObject object) {
        Map<String, String> map = new HashMap<>();
        try {
            if (object != null) {
                for (Object o : object.keySet()) {
                    String key = (String) o;
                    Object value = object.get(key);
                    // if (value instanceof JSONArray) {
                    // value = toList((JSONArray) value);
                    // } else if (value instanceof JSONObject) {
                    // value = toMap((JSONObject) value);
                    // }
                    map.put(key, (String) value);
                    System.out.println("Key: " + key + ", Value: " + value);
                }
                System.out.println("Stored JSON Data into Map");
            } else {
                map = null;
                System.out.println("JSON is empty, returning null");
            }
        } catch (Exception e) {
            System.out.println("Failed to process JSON data to Map: " + e);
        }
        return map;
    }

    private static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<>();
        for (Object value : array) {
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public Map<String, String> getDataObject() {
        System.out.println("Requested for Data Object");
        return data;
    }

    public String getScreenshot(String imageName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "//report//" + imageName + ".png";
        try {
            FileUtils.copyFile(source, new File(destination));
            spark.info("Screenshot Captured at Path : " + destination);
        } catch (IOException e) {
            spark.fail("getScreenshot: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return destination;
    }

    public void browserInit() {
        String browser = System.getProperty("browser") != null ? System.getProperty("browser") : prop.getProperty("browser");
        System.out.println("Launching Browser: " + browser);
        int headless = Integer.parseInt(prop.getProperty("headless"));
        switch (browser) {
            case "CHROME":
                ChromeOptions chromeOptions = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.default_directory", System.getProperty("user.dir") + "//report//");
                chromeOptions.setExperimentalOption("prefs", prefs);
                if (headless == 1) {
                    chromeOptions.addArguments("--headless");
                }
                driver = new ChromeDriver(chromeOptions);
            case "FIREFOX":
                FirefoxProfile firefoxProfile = new FirefoxProfile();
                firefoxProfile.setPreference("browser.download.folderList", 2);
                firefoxProfile.setPreference("browser.download.dir", System.getProperty("user.dir") + "//report//");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setProfile(firefoxProfile);
                if (headless == 1) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
            case "EDGE":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setExperimentalOption("prefs", "download.default_directory" + System.getProperty("user.dir") + "//report,"
                        + "download.prompt_for_download=false" + "download.directory_upgrade=true");
                if (headless == 1) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);

            default:
                System.out.println("Unsupported Browser.");
                System.exit(0);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        gWait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(prop.getProperty("baseWaitTime"))));
        spark.info("Browser Opened Successfully");
    }

    public static String readProperty(String Key) {
        String value;
        spark.info("Reading Data from Property File");
        try {
            value = prop.getProperty(Key);
            spark.info("Key: '" + Key + "' Value: '" + value + "'");
        } catch (Exception e) {
            spark.fail("readProperty: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return value;
    }

    public void getUrl(String Url) {
        try {
            driver.get(Url);
            spark.info("Url Launched: " + Url);
        } catch (Exception e) {
            spark.fail("getUrl: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void closeBrowser() {
        try {
            driver.close();
            spark.info("Browser Closed");
        } catch (Exception e) {
            spark.fail("closeBrowser: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public WebDriver getDriverInstance() {
        spark.info("Requested for Driver Object");
        return driver;
    }

    public void writeToJson(String fileName, JSONArray object) {
        // Not tested
        String path = System.getProperty("user.dir") + "//report//" + fileName + ".json";
        try (FileWriter file = new FileWriter(path)) {
            file.write(object.toJSONString());
            file.flush();
        } catch (IOException e) {
            spark.fail(e);
        }
    }

    public void waitUrlToBe(String actualUrl) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.urlToBe(actualUrl));
            spark.info("Waited till Url Matched");
        } catch (TimeoutException e) {
            spark.fail("waitUrlToBe: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // public void waitForGivenTime(Duration time) {
    // WebDriverWait wait = new WebDriverWait(driver, time);
    // try {
    // wait.until(ExpectedConditions.)
    // } catch () {
    //
    // }
    // }

    public String getCurrentUrl() {
        String currentUrl;
        try {
            currentUrl = driver.getCurrentUrl();
            spark.info("Current Url is: " + currentUrl);
        } catch (Exception e) {
            spark.fail("getCurrentUrl: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return currentUrl;
    }

    public String getTitle() {
        String title;
        try {
            title = driver.getTitle();
            spark.info("Current Title is: " + title);
        } catch (Exception e) {
            spark.fail("getTitle: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return title;

    }

    public void refreshBrowser() {
        try {
            driver.navigate().refresh();
            spark.info("Browser Refreshed");
        } catch (Exception e) {
            spark.fail("refreshBrowser: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void navigateTo(String Url) {
        try {
            driver.navigate().to(Url);
            waitUrlToBe(Url);
            spark.info("Navigated to: " + Url);
        } catch (Exception e) {
            spark.fail("navigateTo: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void alertSendKeys(String value) {
        try {
            gWait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().sendKeys(value);
            spark.info("Sent Keys to Alert Box: " + value);
        } catch (Exception e) {
            spark.fail("alertSendKeys: " + e.getMessage());
        }
    }

    public String alertGetText() {
        String text = null;
        try {
            gWait.until(ExpectedConditions.alertIsPresent());
            String getText = driver.switchTo().alert().getText();
            spark.info("Text from Alert: " + getText);
            text = getText;
        } catch (Exception e) {
            spark.fail("alertGetText: " + e.getMessage());
        }
        return text;
    }

    public void alertAccept() {
        try {
            gWait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            spark.info("Accepted Alert");
        } catch (Exception e) {
            spark.fail("alertAccept: " + e.getMessage());
        }
    }

    public void alertDismiss() {
        try {
            gWait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().dismiss();
            spark.info("Dismissed Alert");
        } catch (Exception e) {
            spark.info("Failed to Dismiss on Alert");
        }
    }

    public void switchToFrameByIndex(int index) {
        try {
            gWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
            driver.switchTo().frame(index);
            spark.info("Frame Switched By Index: " + index);
        } catch (Exception e) {
            spark.fail("Frame Switched By Index: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void switchToFrameByNamOrID(String nameOrId) {
        try {
            gWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrId));
            driver.switchTo().frame(nameOrId);
            spark.info("Frame Switched By NamOrID: " + nameOrId);
        } catch (NoSuchFrameException | TimeoutException e) {
            spark.fail("switchToFrameByNamOrID" + e);
            throw new RuntimeException(e);
        }
    }

    public void switchToFrameByWebElement(WebElement element) {
        try {
            gWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
            driver.switchTo().frame(element);
            spark.info("Frame Switched By element: " + getLocator(element));

        } catch (Exception e) {
            spark.info("Frame Switched By element: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void switchToParentFrame() {
        try {
            driver.switchTo().parentFrame();
            spark.info("Frame Switched To Parent");
        } catch (Exception e) {
            spark.fail("Frame Switched To Parent: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void switchToDefaultFrame() {
        try {
            driver.switchTo().defaultContent();
            spark.info("Frame Switched To Default Content");
        } catch (Exception e) {
            spark.info("Frame Switched To Default Content: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void quitBrowser() {
        try {
            driver.quit();
            spark.info("Quit Browser Method Executed");
        } catch (Exception e) {
            spark.fail("Quit Browser Method Executed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendKeysToElement(WebElement element, String value) {
        try {
            waitTillVisibilityOfElement(Duration.ofSeconds(5), element);
            element.sendKeys(value);
            spark.info("Entered: " + value + " Successfully to: " + getLocator(element));
        } catch (Exception e) {
            spark.fail("sendKeysToElement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void trySendKeysToElement(WebElement element, String value) {
        try {
            waitTillVisibilityOfElement(Duration.ofSeconds(5), element);
            element.sendKeys(value);
            spark.info("Entered: " + value + " Successfully to: " + getLocator(element));
        } catch (Exception e) {
            spark.info("trySendKeysToElement not worked");
        }
    }

    public void clickOnElement(WebElement element) {
        try {
            waitTillElementToBeClickable(Duration.ofSeconds(5), element);
            element.click();
            spark.info("Clicked Successfully: " + getLocator(element));
        } catch (Exception e) {
            spark.fail("clickOnElement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void tryClickOnElement(WebElement element) {
        try {
            waitTillElementToBeClickable(Duration.ofSeconds(5), element);
            element.click();
            spark.info("Clicked Successfully: " + getLocator(element));
        } catch (Exception e) {
            spark.info("tryClickOnElement not clicked");
        }
    }

    public void submitForm(WebElement element) {
        try {
            gWait.until(ExpectedConditions.elementToBeClickable(element));
            element.submit();
            spark.info("Submitted Successfully: " + getLocator(element));
        } catch (Exception e) {
            spark.fail("submitForm: " + e.getMessage());
            //throw new RuntimeException(e);
        }
    }

    public String getTextFromElement(WebElement element) {
        String extractedText;
        try {
            extractedText = element.getText();
            spark.info("Text extracted: " + extractedText + " from element: " + getLocator(element));

        } catch (Exception e) {
            spark.fail("getTextFromElement: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return extractedText;
    }

    public void clearElementField(WebElement element) {
        try {
            spark.info("Text before clearing field: " + element.getText());
            element.clear();
            spark.info("Cleared Successfully");
        } catch (Exception e) {
            spark.fail("clearElementField: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean isElementDisplayed(WebElement element) {
        boolean flag;
        try {
            flag = element.isDisplayed();
        } catch (Exception e) {
            spark.fail("isElementDisplayed: " + e.getMessage());
            throw new RuntimeException(e);
        }
        if (flag) {
            spark.info("Element Displayed: " + getLocator(element));
        } else {
            spark.info("Element not Displayed: " + getLocator(element));
        }
        return flag;
    }

    public boolean isElementEnabled(WebElement element) {
        boolean flag;
        try {
            flag = element.isEnabled();
        } catch (Exception e) {
            spark.fail("isElementEnabled: " + e.getMessage());
            throw new RuntimeException(e);
        }
        if (flag) {
            spark.info("Element Enabled: " + getLocator(element));
        } else {
            spark.info("Element not Enabled: " + getLocator(element));
        }
        return flag;
    }

    public boolean isElementSelected(WebElement element) {
        boolean flag;
        try {
            flag = element.isSelected();
        } catch (Exception e) {
            spark.fail("isElementSelected: " + e.getMessage());
            throw new RuntimeException(e);
        }
        if (flag) {
            spark.info("Element Selected: " + getLocator(element));
        } else {
            spark.info("Element not Selected: " + getLocator(element));
        }
        return flag;
    }

    public void waitTillTitleIs(Duration waitTime, String title) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.titleIs(title));
            spark.info("Waited till title is Matched. Title: " + title);

        } catch (TimeoutException e) {
            spark.fail("waitTillTitleIs: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void waitTillVisibilityOfElement(Duration waitTime, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            spark.info("Waited till visibilityOf Element: " + getLocator(element));
        } catch (TimeoutException e) {
            spark.fail("waitTillVisibilityOfElement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void waitTillElementToBeClickable(Duration waitTime, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            spark.info("Waited till elementToBeClickable: " + getLocator(element));
        } catch (TimeoutException e) {
            spark.fail("waitTillElementToBeClickable: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void waitTillTextToBePresentInElementValue(Duration waitTime, WebElement element, String value) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.textToBePresentInElementValue(element, value));
            spark.info("Waited till textToBePresentInElementValue: " + value);
        } catch (Exception e) {
            spark.fail("waitTillTextToBePresentInElementValue: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void waitTillInvisibilityOf(Duration waitTime, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.invisibilityOf(element));
            spark.info("Waited till invisibilityOf: " + getLocator(element));
        } catch (Exception e) {
            spark.fail("waitTillInvisibilityOf: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clickCheckBox(WebElement element) {
        try {
            spark.info("Before Clicking on CheckBox: " + element.isSelected());
            if (!element.isSelected()) {
                element.click();
                spark.info("After Clicking on CheckBox: " + element.isSelected());
            } else {
                spark.info("CheckBox already Checked");
            }
        } catch (Exception e) {
            spark.fail("clickCheckBox: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void UnClickCheckBox(WebElement element) {
        try {
            spark.info("Before Clicking on CheckBox: " + element.isSelected());
            if (element.isSelected()) {
                element.click();
                spark.info("After Clicking on CheckBox: " + element.isSelected());
            } else {
                spark.info("CheckBox already UnChecked");
            }
        } catch (Exception e) {
            spark.fail("UnClickCheckBox: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void rightClickOnElement(WebElement element) {
        Actions action = new Actions(driver);
        try {
            action.contextClick(element);
            action.build().perform();
            spark.info("Right Clicked on given Element: " + getLocator(element));
        } catch (Exception e) {
            spark.fail("rightClickOnElement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void doubleClickOnElement(WebElement element) {
        Actions action = new Actions(driver);
        try {
            action.doubleClick(element);
            action.build().perform();
            spark.info("Double Clicked on given Element: " + getLocator(element));
        } catch (Exception e) {
            spark.fail("doubleClickOnElement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void clickAndHoldOnElement(WebElement element) {
        Actions action = new Actions(driver);
        try {
            action.clickAndHold(element);
            action.build().perform();
            spark.info("Clicked and Held on given Element: " + getLocator(element));
        } catch (Exception e) {
            spark.fail("clickAndHoldOnElement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void dragAndDropByElements(WebElement source, WebElement target) {
        Actions action = new Actions(driver);
        try {
            action.dragAndDrop(source, target);
            action.build().perform();
            spark.info("Dragged and Dropped. Source: " + getLocator(source) + " Target: " + getLocator(target));
        } catch (Exception e) {
            spark.fail("dragAndDropByElements: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void moveMouseToElement(WebElement element) {
        Actions action = new Actions(driver);
        try {
            action.moveToElement(element);
            action.build().perform();
            spark.info("Mouse moved to given element: " + getLocator(element));
        } catch (Exception e) {
            spark.fail("moveMouseToElement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void scrollToElement(WebElement element) {
        Actions action = new Actions(driver);
        try {
            action.scrollToElement(element);
            action.build().perform();
            spark.info("Mouse moved to given element: " + getLocator(element));
        } catch (Exception e) {
            spark.fail("scrollToElement: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void mouseRelease() {
        Actions action = new Actions(driver);
        try {
            action.release();
            action.build().perform();
            spark.info("Mouse Released");
        } catch (Exception e) {
            spark.fail("mouseRelease: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void KeyDownOnKeyBoard(String Key) {
        Actions action = new Actions(driver);
        try {
            switch (Key) {
                case "ALT":
                    action.keyDown(Keys.ALT);
                    break;
                case "CTRL":
                    action.keyDown(Keys.CONTROL);
                    break;
                case "SHIFT":
                    action.keyDown(Keys.SHIFT);
                    break;
            }
            action.build().perform();
            spark.info("Key Pressed Down: " + Key);
        } catch (Exception e) {
            spark.fail("KeyDownOnKeyBoard: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void KeyUpOnKeyBoard(String Key) {
        Actions action = new Actions(driver);
        try {
            switch (Key) {
                case "ALT":
                    action.keyUp(Keys.ALT);
                    break;
                case "CTRL":
                    action.keyUp(Keys.CONTROL);
                    break;
                case "SHIFT":
                    action.keyUp(Keys.SHIFT);
                    break;
            }
            action.build().perform();
            spark.info("Key Released: " + Key);
        } catch (Exception e) {
            spark.fail("KeyUpOnKeyBoard: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void pressKeyStroke(String Key) {
        Actions action = new Actions(driver);
        try {
            switch (Key) {
                case "DELETE":
                    action.sendKeys(Keys.DELETE);
                    break;
                case "SPACE":
                    action.sendKeys(Keys.SPACE);
                    break;
                case "ESCAPE":
                    action.sendKeys(Keys.ESCAPE);
                    break;
                case "F5":
                    action.sendKeys(Keys.F5);
                    break;
                case "ENTER":
                    action.sendKeys(Keys.ENTER);
                    break;
                case "TAB":
                    action.sendKeys(Keys.TAB);
                    break;
            }
            action.build().perform();
            spark.info("Key Pressed: " + Key);
        } catch (Exception e) {
            spark.fail("pressKeyStroke: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void switchWindowByTitle(String title) {
        Map<String, String> map = new HashMap<>();
        try {
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                driver.switchTo().window(window);
                String windowTitle = driver.getTitle();
                map.put(windowTitle, window);
            }
            driver.switchTo().window(map.get(title));
            spark.info("Window Switched to: " + title);
        } catch (Exception e) {
            spark.fail("switchWindowByTitle: " + e.getMessage());
            throw new RuntimeException(e);
        }


    }

    public void uploadFile(String exePath) {
        try {
            Runtime.getRuntime().exec(exePath);
            spark.info("Upload File Executed: " + exePath);
        } catch (IOException e) {
            spark.fail("uploadFile: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void robotKeyPress(String Key) {
        Robot robot;
        try {
            robot = new Robot();
            switch (Key) {
                case "ESCAPE":
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                    break;
                case "CAPS_LOCK":
                    robot.keyPress(KeyEvent.VK_CAPS_LOCK);
                    break;
                case "ENTER":
                    robot.keyPress(KeyEvent.VK_ENTER);
                    break;
                case "SHIFT":
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    break;
                case "TAB":
                    robot.keyPress(KeyEvent.VK_TAB);
                    break;
                case "CONTROL":
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    break;
                case "ALT":
                    robot.keyPress(KeyEvent.VK_ALT);
                    break;
                case "BACK_SPACE":
                    robot.keyPress(KeyEvent.VK_BACK_SPACE);
                    break;
                case "A":
                    robot.keyPress(KeyEvent.VK_A);
                    break;
                case "B":
                    robot.keyPress(KeyEvent.VK_B);
                    break;
                case "C":
                    robot.keyPress(KeyEvent.VK_C);
                    break;
                case "D":
                    robot.keyPress(KeyEvent.VK_D);
                    break;
                case "E":
                    robot.keyPress(KeyEvent.VK_E);
                    break;
                case "F":
                    robot.keyPress(KeyEvent.VK_F);
                    break;
                case "G":
                    robot.keyPress(KeyEvent.VK_G);
                    break;
                case "H":
                    robot.keyPress(KeyEvent.VK_H);
                    break;
                case "I":
                    robot.keyPress(KeyEvent.VK_I);
                    break;
                case "J":
                    robot.keyPress(KeyEvent.VK_J);
                    break;
                case "K":
                    robot.keyPress(KeyEvent.VK_K);
                    break;
                case "L":
                    robot.keyPress(KeyEvent.VK_L);
                    break;
                case "M":
                    robot.keyPress(KeyEvent.VK_M);
                    break;
                case "N":
                    robot.keyPress(KeyEvent.VK_N);
                    break;
                case "O":
                    robot.keyPress(KeyEvent.VK_O);
                    break;
                case "P":
                    robot.keyPress(KeyEvent.VK_P);
                    break;
                case "Q":
                    robot.keyPress(KeyEvent.VK_Q);
                    break;
                case "R":
                    robot.keyPress(KeyEvent.VK_R);
                    break;
                case "S":
                    robot.keyPress(KeyEvent.VK_S);
                    break;
                case "T":
                    robot.keyPress(KeyEvent.VK_T);
                    break;
                case "U":
                    robot.keyPress(KeyEvent.VK_U);
                    break;
                case "V":
                    robot.keyPress(KeyEvent.VK_V);
                    break;
                case "W":
                    robot.keyPress(KeyEvent.VK_W);
                    break;
                case "X":
                    robot.keyPress(KeyEvent.VK_X);
                    break;
                case "Y":
                    robot.keyPress(KeyEvent.VK_Y);
                    break;
                case "Z":
                    robot.keyPress(KeyEvent.VK_Z);
                    break;
                case "0":
                    robot.keyPress(KeyEvent.VK_0);
                    break;
                case "1":
                    robot.keyPress(KeyEvent.VK_1);
                    break;
                case "2":
                    robot.keyPress(KeyEvent.VK_2);
                    break;
                case "3":
                    robot.keyPress(KeyEvent.VK_3);
                    break;
                case "4":
                    robot.keyPress(KeyEvent.VK_4);
                    break;
                case "5":
                    robot.keyPress(KeyEvent.VK_5);
                    break;
                case "6":
                    robot.keyPress(KeyEvent.VK_6);
                    break;
                case "7":
                    robot.keyPress(KeyEvent.VK_7);
                    break;
                case "8":
                    robot.keyPress(KeyEvent.VK_8);
                    break;
                case "9":
                    robot.keyPress(KeyEvent.VK_9);
                    break;
                case ";":
                    robot.keyPress(KeyEvent.VK_SEMICOLON);
                    break;
                case ":":
                    robot.keyPress(KeyEvent.VK_COLON);
                    break;
                case "/":
                    robot.keyPress(KeyEvent.VK_SLASH);
                    break;
                case "-":
                    robot.keyPress(KeyEvent.VK_MINUS);
                    break;
                case "F5":
                    robot.keyPress(KeyEvent.VK_F5);
                    break;
            }
            spark.info("Robot Key Pressed: " + Key);
        } catch (AWTException e) {
            spark.fail("robotKeyPress: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void robotKeyRelease(String Key) {
        Robot robot;
        try {
            robot = new Robot();
            switch (Key) {
                case "ESCAPE":
                    robot.keyRelease(KeyEvent.VK_ESCAPE);
                    break;
                case "CAPS_LOCK":
                    robot.keyRelease(KeyEvent.VK_CAPS_LOCK);
                    break;
                case "ENTER":
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    break;
                case "SHIFT":
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    break;
                case "TAB":
                    robot.keyPress(KeyEvent.VK_TAB);
                    break;
                case "CONTROL":
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    break;
                case "ALT":
                    robot.keyRelease(KeyEvent.VK_ALT);
                    break;
                case "BACK_SPACE":
                    robot.keyRelease(KeyEvent.VK_BACK_SPACE);
                    break;
            }
        } catch (AWTException e) {
            spark.fail("robotKeyRelease: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void robotMouseMove(int x, int y) {
        Robot robot;
        try {
            robot = new Robot();
            robot.mouseMove(x, y);
            spark.info("Moved Mouse: " + x + " : " + y);
        } catch (AWTException e) {
            spark.fail("robotMouseMove: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void robotMousePress(String mouseKey) {
        Robot robot;
        try {
            robot = new Robot();
            switch (mouseKey) {
                case "LEFT-CLICK":
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case "RIGHT-CLICK":
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    break;
                case "MIDDLE-CLICK":
                    robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                    break;
            }
            spark.info("Robot Key Pressed: " + mouseKey);
        } catch (AWTException e) {
            spark.fail("robotMousePress: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void robotMouseRelease(String mouseKey) {
        Robot robot;
        try {
            robot = new Robot();
            switch (mouseKey) {
                case "LEFT-CLICK":
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case "RIGHT-CLICK":
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    break;
                case "MIDDLE-CLICK":
                    robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                    break;
            }
            spark.info("Robot Key Released: " + mouseKey);
        } catch (AWTException e) {
            spark.fail("robotMouseRelease: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void trySelectByVisibleText(Select dropDown, String value) {
        try {
            dropDown.selectByVisibleText(value);
            spark.info("Selected: " + value + " Successfully ");
        } catch (Exception e) {
            spark.info("trySelectByVisibleText not worked" + e.getMessage());
        }
    }

    public void tryDropDownValueClick(List<WebElement> dropDown, String dropDownValue) {
        for (WebElement control : dropDown) {
            String text = control.getText();
            if (text.contains(dropDownValue)) {
                control.click();
                spark.info("Selected: " + dropDownValue + " Successfully ");
                break;
            }
        }
    }

    public ExtentTest getSparkInstance() {
        return spark;
    }

}

