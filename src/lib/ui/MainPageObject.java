package lib.ui;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import lib.Platform;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.regex.Pattern;

public class MainPageObject {
    protected RemoteWebDriver driver;

    public MainPageObject(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitForElementPresent(String locator, String error_message, long timeoutInSeconds) {
        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public WebElement waitForElementPresent(String locator, String error_message) {

        return waitForElementPresent(locator, error_message, 5);
    }

    public WebElement waitForElementAndClick(String locator, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(String locator, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    public boolean waitForElementNotPresent(String locator, String error_message, long timeoutInSeconds) {
        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public WebElement waitForElementAndClear(String locator, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    public void assertElementHasText(String locator, String expected_text, String error_message) {
        WebElement element = waitForElementPresent(locator, "Can't find element", 5);
        String actual_text = element.getAttribute("text");
        Assert.assertTrue(error_message, actual_text.contains(expected_text));
    }

    public void swipeUp(int timeOfSwipe) {
        if (driver instanceof AppiumDriver) {
            TouchAction action = new TouchAction((AppiumDriver) driver);
            Dimension size = driver.manage().window().getSize();
            int x = size.width / 2;
            int start_y = (int) (size.height * 0.8);
            int end_y = (int) (size.height * 0.2);
            action.press(x, start_y).waitAction(timeOfSwipe).moveTo(x, end_y).release().perform();
        } else {
            System.out.println("Method swipeUp() do nothing for current platform");
        }
    }

    public void swipeUpQuick() {
        swipeUp(200);
    }

    public void swipeUpToFindElement(String locator, String error_message, int max_swipes) {
        int already_swiped = 0;
        By by = getLocatorByString(locator);
        while (driver.findElements(by).size() == 0) {
            if (already_swiped > max_swipes) {
                waitForElementPresent(locator, "Can't find elemend by swiping up\n" + error_message, 0);
                return;
            }
            swipeUpQuick();
            ++already_swiped;
        }
    }

    public void swipeElementToLeft(String locator, String error_message) {
        if (driver instanceof AppiumDriver) {
            WebElement element = waitForElementPresent(locator, error_message, 10);
            int left_x = element.getLocation().getX();
            int right_x = left_x + element.getSize().getWidth();
            int upper_y = element.getLocation().getY();
            int lower_y = upper_y + element.getSize().getHeight();
            int middle_y = (upper_y + lower_y) / 2;
            TouchAction action = new TouchAction((AppiumDriver) driver);
            action.press(right_x, middle_y);
            action.waitAction(300);
            if (Platform.getInstance().isAndroid()) action.moveTo(left_x, middle_y);
            else {
                int offset_x = (-1 * element.getSize().getWidth());
                action.moveTo(offset_x, 0);
            }
            action.release();
            action.perform();
        } else {
            System.out.println("Method swipeElementToLeft do nothing for current platform");
        }
    }

    public int getAmountOfElements(String locator) {
        By by = getLocatorByString(locator);
        List elements = driver.findElements(by);
        return elements.size();
    }

    public void assertElementNotPresent(String locator, String error_message) {
        int amount_of_elements = getAmountOfElements(locator);
        if (amount_of_elements > 0) {
            String default_message = "An element '" + locator + "' supposed to be not present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    public String waitForElementAndGetAttribute(String locator, String attribute, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(locator, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }


    public void assertElementPresent(By by, String error_message) {
        List<WebElement> elements = driver.findElements(by);
        Assert.assertTrue(error_message, elements.size() > 0);
    }

    public void assertAllElementsContainText(List<WebElement> elements, String text) {
        for (int i = 0; i < elements.size(); i++) {
            Assert.assertTrue("Element № " + elements.get(i) + " doesn't contains text: " + text,
                    elements.get(i).getAttribute("text").contains(text));
        }
    }

    protected By getLocatorByString(String locator_with_type) {
        String[] exploted_locator = locator_with_type.split(Pattern.quote(":"), 2);
        String by_type = exploted_locator[0];
        String locator = exploted_locator[1];
        if (by_type.equals("xpath")) return By.xpath(locator);
        else if (by_type.equals("id")) return By.id(locator);
        else if (by_type.equals("css")) return By.cssSelector(locator);
        else if (by_type.equals("link")) return By.linkText(locator);
        else throw new IllegalArgumentException("Cann't get type of locator: " + locator);
    }

    public boolean isElementLocatedOntheScreen(String locator) {

        int element_location_by_y = waitForElementPresent(locator, "Cann't find element by locator", 1).getLocation().getY();
        if (Platform.getInstance().isMw()) {
            JavascriptExecutor JSExecutor = (JavascriptExecutor) driver;
            Object js_result = JSExecutor.executeScript("return window.pageYOffset");
            element_location_by_y -= Integer.parseInt(js_result.toString());

        }
        int screen_size_by_y = driver.manage().window().getSize().getHeight();
        return element_location_by_y < screen_size_by_y;
    }

    public void swipeUpTillElementAppear(String locator, String error_message, int max_swipes) {
        int already_swiped = 0;
        while (isElementLocatedOntheScreen(locator)) {
            if (already_swiped > max_swipes) Assert.assertTrue(error_message, isElementLocatedOntheScreen(locator));
            swipeUpQuick();
            ++already_swiped;
        }
    }

    public void clickElementToTheRightUpperConner(String locator, String error_message) {
        if (driver instanceof AppiumDriver) {
            WebElement element = waitForElementPresent(locator + "/..", error_message);
            int right_x = element.getLocation().getX();
            int upper_y = element.getLocation().getY();
            int lower_y = upper_y + element.getSize().getHeight();
            int middle_y = (upper_y + lower_y) / 2;
            int width = element.getSize().getWidth();
            int point_to_click_x = (right_x + width) - 3;
            int point_to_click_y = middle_y;
            TouchAction action = new TouchAction((AppiumDriver) driver);
            action.tap(point_to_click_x, point_to_click_y).perform();
        } else {
            System.out.println("Method clickElementToTheRightUpperConner() do nothing for current platform");
        }
    }

    public void scrollWebPageUp() {
        if (Platform.getInstance().isMw()) {
            JavascriptExecutor JSExecutor = (JavascriptExecutor) driver;
            JSExecutor.executeScript("window.scrollBy(0, 250)");
        } else
            System.out.println("Method scrollWebPageUp() do nothing for platform: " + Platform.getInstance().getPlatformVar());
    }

    public void scrollWebPageTillElementNotVisible(String locator, String error_message, int max_swipes) {
        int already_swiped = 0;
        WebElement element = waitForElementPresent(locator, error_message);
        while (!isElementLocatedOntheScreen(locator)) {
            scrollWebPageUp();
            already_swiped++;
            if (already_swiped > max_swipes) {
                Assert.assertTrue(error_message, element.isDisplayed());
            }
        }
    }

    public boolean isElementPresent(String locator) {
        return getAmountOfElements(locator) > 0;
    }

    public void tryClickElementWithFewAttempts(String locator, String error_message, int amount_of_attempts) {
        int current_attempts = 0;
        boolean need_more_attempts = true;
        while (need_more_attempts) {
            try {
                waitForElementAndClick(locator, error_message, 1);
                need_more_attempts = false;
            } catch (Exception e) {
                if (current_attempts > amount_of_attempts) waitForElementAndClick(locator, error_message, 1);
            }
            ++current_attempts;
        }
    }
}
