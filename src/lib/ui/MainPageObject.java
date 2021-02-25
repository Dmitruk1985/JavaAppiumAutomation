package lib.ui;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MainPageObject {
    protected AppiumDriver driver;

    public MainPageObject(AppiumDriver driver) {
        this.driver = driver;
    }

    public WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public WebElement waitForElementPresentById(String id, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        By by = By.id(id);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public WebElement waitForElementPresent(By by, String error_message) {

        return waitForElementPresent(by, error_message, 5);
    }

    public WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    public boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    public void assertElementHasText(By by, String expected_text, String error_message) {
        WebElement element = waitForElementPresent(by, "Can't find element", 5);
        String actual_text = element.getAttribute("text");
        Assert.assertTrue(error_message, actual_text.contains(expected_text));
    }

    public void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);
        action.press(x, start_y).waitAction(timeOfSwipe).moveTo(x, end_y).release().perform();
    }

    public void swipeUpQuick() {
        swipeUp(200);
    }

    public void swipeUpToFindElement(By by, String error_message, int max_swipes) {
        int already_swiped = 0;
        while (driver.findElements(by).size() == 0) {
            if (already_swiped > max_swipes) {
                waitForElementPresent(by, "Can't find elemend by swiping up\n" + error_message, 0);
                return;
            }
            swipeUpQuick();
            ++already_swiped;
        }
    }

    public void swipeElementToLeft(By by, String error_message) {
        WebElement element = waitForElementPresent(by, error_message, 10);
        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;
        TouchAction action = new TouchAction(driver);
        action
                .press(right_x, middle_y)
                .waitAction(300)
                .moveTo(left_x, middle_y)
                .perform();
    }

    public int getAmountOfElements(By by) {
        List elements = driver.findElements(by);
        return elements.size();
    }

    public void assertElementNotPresent(By by, String error_message) {
        int amount_of_elements = getAmountOfElements(by);
        if (amount_of_elements > 0) {
            String default_message = "An element '" + by.toString() + "' supposed to be not present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    public String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

    public void openArticle(String searchValue, String articleTitle) {

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field",
                5);

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                searchValue,
                "Cannot find search input",
                5);

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + articleTitle + "']"),
                "Can't locate search field",
                5);

    }

    public void addArticleToMyList(String searchName, String title, String listName, boolean firstArticle) {

        //Кликаем по полю поиска
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field",
                5);

        //Вводим название статьи
        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                searchName,
                "Cannot find search input",
                5);

        //Кликаем по нужной статье
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='" + title + "']"),
                "Can't locate search field",
                5);

        WebElement title_element = waitForElementPresent(By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title", 15);

        //Открываем меню "Опции"
        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Can't find button to open article options",
                5);

        //Выбираем пункт "Добавить в список"
        waitForElementAndClick(
                By.xpath("//*[@text='Add to reading list']"),
                "Can't find option to add article to list",
                5);

        //Если добавляем статью в первый раз
        if (firstArticle) {
            //Нажимаем кнопку "GOT IT"
            waitForElementAndClick(
                    By.id("org.wikipedia:id/onboarding_button"),
                    "Can't find 'GOT IT' button",
                    5);

            //Очищаем поле для ввода названия списка
            waitForElementAndClear(
                    By.id("org.wikipedia:id/text_input"),
                    "Can't find input to save name", 5);

            //Вводим название списка
            waitForElementAndSendKeys(
                    By.id("org.wikipedia:id/text_input"),
                    listName,
                    "Can't put text into article input",
                    5);

            //Нажимаем кнопку "ОК"
            waitForElementAndClick(
                    By.xpath("//*[@text='OK']"),
                    "Can't press 'OK' button",
                    5);
        } else {
            //Выбираем ранее созданный список
            waitForElementAndClick(
                    By.xpath("//android.widget.TextView[@text='" + listName + "']"),
                    "Can't find list " + listName,
                    5);

        }

        //Закрываем статью
        waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Can't close article",
                5);
    }

    public void assertElementPresent(By by, String error_message) {
        List<WebElement> elements = driver.findElements(by);
        Assert.assertTrue(error_message, elements.size()>0);
    }
}
