import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class FirstTest {
    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "and80");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "D:\\Documents\\IT\\Testing\\Mobile_Automation\\github\\JavaAppiumAutomation\\apks\\org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void firstTest() {

        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Java", "Cannot find search input", 5);

        waitForElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find Java", 15);
    }

    @Test
    public void testCancelSearch() {
        waitForElementAndClick(By.id("org.wikipedia:id/search_container"),
                "Cannot find search input", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Java", "Cannot find search input", 5);

        waitForElementAndClear(By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field", 5);

        waitForElementAndClick(By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search", 5);

        waitForElementNotPresent(By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on the page", 5);
    }

    @Test
    public void compareArticleTitle() {
        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Java", "Cannot find search input", 5);

        waitForElementAndClick(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Can't locate search field", 5);

        WebElement title_element = waitForElementPresent(By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title", 15);

        String article_title = title_element.getAttribute("text");
        Assert.assertEquals("Unexpected title!", "Java (programming language)", article_title);


    }

    @Test
    public void testSwipeArticle() {
        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Appium", "Cannot find search input", 5);

        waitForElementAndClick(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
                "Can't find 'Appium' in search field", 5);

        WebElement title_element = waitForElementPresent(By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title", 15);

        swipeUpToFindElement(By.xpath("//*[@text='View page in browser']"), "Can't find the end of the article", 20);
    }

    @Test
    public void cancelSearchTest() {
        //Кликаем по полю поиска
        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        //Вводим значение "Java"
        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Java", "Cannot find search input", 5);

        String itemXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_container']";

        //Дожидаемся появления результата поиска
        waitForElementPresent(By.xpath(itemXpath), "Can't locate result item", 5);

        List<WebElement> listOfItems = driver.findElements(By.xpath(itemXpath));

        //Проверяем, что найдено более 1 результата
        Assert.assertTrue("List has <=1 result", listOfItems.size() > 1);

        //Очищаем поле поиска
        waitForElementAndClick(By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search", 5);

        //Дожидаемся исчезновения результата поиска
        waitForElementNotPresent(By.xpath(itemXpath), "Result item is still present", 5);

        listOfItems = driver.findElements(By.xpath(itemXpath));

        //Проверяем, что результаты поиска исчезли
        Assert.assertTrue("List of results is not empty", listOfItems.size() == 0);
    }

    @Test
    public void allSearchResultsHaveKeyWordTest() {
        //Кликаем по полю поиска
        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        //Вводим значение "Java"
        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Java", "Cannot find search input", 5);

        String headerXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title']";

        //Дожидаемся появления результата поиска
        waitForElementPresent(By.xpath(headerXpath), "Can't locate result item", 5);

        //Получаем список всех заголовков результатов
        List<WebElement> headersList = driver.findElements(By.xpath(headerXpath));

        //Проверяем для каждого заголовка наличие в нем ключевого слова
        for (int i = 0; i < headersList.size(); i++) {
            Assert.assertTrue("Serch result №" + headersList.get(i) + " doesn't contains key word",
                    headersList.get(i).getAttribute("text").contains("Java"));
        }
    }

    @Test
    public void searchFieldContainsRightTextTest() {
        assertElementHasText(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Search Wikipedia", "Search field doesn't contains expected text");
    }

    @Test
    public void saveFirstArticleToMyList() {
        String name_of_folder = "Learning programming";
        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Java", "Cannot find search input", 5);

        waitForElementAndClick(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Can't locate search field", 5);

        WebElement title_element = waitForElementPresent(By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title", 15);

        waitForElementAndClick(By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Can't find button to open article options", 5);

        waitForElementAndClick(By.xpath("//*[@text='Add to reading list']"),
                "Can't find option to add article to list", 5);

        waitForElementAndClick(By.id("org.wikipedia:id/onboarding_button"),
                "Can't find 'GOT IT' button", 5);

        waitForElementAndClear(By.id("org.wikipedia:id/text_input"),
                "Can't find input to save name", 5);

        waitForElementAndSendKeys(By.id("org.wikipedia:id/text_input"),
                name_of_folder, "Can't put text into article input", 5);

        waitForElementAndClick(By.xpath("//*[@text='OK']"),
                "Can't press 'OK' button", 5);

        waitForElementAndClick(By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Can't close article", 5);

        waitForElementAndClick(By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Can't find 'My lists' button", 5);

        waitForElementAndClick(By.xpath("//*[@text='" + name_of_folder + "']"),
                "Can't find created folder", 5);

        swipeElementToLeft(By.xpath("//*[@text='Java (programming language)']"),
                "Can't find saved article");

        waitForElementNotPresent(By.xpath("//*[@text='Java (programming language)']"),
                "Can't delete saved article", 5);
    }

    @Test
    public void testAmountOfNotEmptySearch() {
        String search_line = "Linkin Park discography";
        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']//*[@resource-id='org.wikipedia:id/page_list_item_container']";

        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                search_line, "Cannot find search input", 5);

        waitForElementPresent(By.xpath(search_result_locator), "Can't find " + search_line, 15);
        int amount_of_search_results = getAmountOfElements(By.xpath(search_result_locator));
        Assert.assertTrue("We found to few results", amount_of_search_results > 0);
    }

    @Test
    public void testAmountOfEmptySearch() {

        String search_line = "thtyujyefwefwefwst";
        String search_result_locator = "//*[@resource-id='org.wikipedia:id/search_results_list']//*[@resource-id='org.wikipedia:id/page_list_item_container']";
        String empty_result_label = "//*[@text='No results found']";

        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                search_line, "Cannot find search input", 5);

        waitForElementPresent(By.xpath(empty_result_label), "Can't find empty result label", 15);
        assertElementNotPresent(By.xpath(search_result_locator), "We have found some results by reqest" + search_line);

    }

    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        String search_line = "Java";


        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field",
                5);

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                search_line,
                "Cannot find search input",
                5);

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Can't find topic" + search_line,
                15);

        String title_before_rotation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Can't find title of article",
                15);

        driver.rotate(ScreenOrientation.LANDSCAPE);
        String title_after_rotation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Can't find title of article",
                15);

        Assert.assertEquals("Article title have been change after rotation", title_before_rotation, title_after_rotation);
        driver.rotate(ScreenOrientation.PORTRAIT);
        String title_after_second_rotation = waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Can't find title of article",
                15);
        Assert.assertEquals("Article title have been change after rotation", title_before_rotation, title_after_second_rotation);
    }

    @Test
    public void testCheckSearchArticleInBackground() {

        waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Java", "Cannot find search input", 5);

        waitForElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Can't locate search field", 5);

        driver.runAppInBackground(2);

        waitForElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Can't find article after returning from background", 5);
    }

    @Test
    public void saveTwoArticlesToMyList() {
        String listName = "MyList";
        String firstTitle = "Java (programming language)";
        String secondTitle = "Java";

        //Добавляем в список 2 статьи
        addArticleToMyList("Java", firstTitle, listName, true);
        addArticleToMyList("Java", secondTitle, listName, false);

        //Открываем раздел со списками
        waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Can't find 'My lists' button",
                5);

        //Выбираем нужный список
        waitForElementAndClick(
                By.xpath("//*[@text='" + listName + "']"),
                "Can't find created folder",
                5);

        //Удаляем первую статью из списка (через свайп плохо работает, реализовано через кнопки)
        //Нажимаем кнопку "опции" у заданной статьи
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title' and @text='" + firstTitle + "']" +
                        "/ancestor::*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@content-desc='More options']"),
                "Can't find options button",
                5);

        //Нажимаем кнопку "удалить"
        waitForElementAndClick(
                By.id("org.wikipedia:id/reading_list_item_remove_text"),
                "Can't find options button",
                5);

        //Убеждаемся, что статья удалена
        waitForElementNotPresent(
                By.xpath("//*[@text='" + firstTitle + "']"),
                "Article " + firstTitle + " is not deleted",
                5);

        //Убеждаемся, что вторая статья осталась, и кликаем по ней
        waitForElementAndClick(
                By.xpath("//*[@text='" + secondTitle + "']"),
                "Can't find article " + secondTitle,
                5);

        WebElement title_element = waitForElementPresent(By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title", 15);
        String secondTitleAfterDeleting = title_element.getAttribute("text");

        //Проверяем, что заголовок не изменился
        Assert.assertEquals("Article's title is wrong", secondTitle, secondTitleAfterDeleting);
    }


    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementPresentById(String id, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        By by = By.id(id);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementPresent(By by, String error_message) {

        return waitForElementPresent(by, error_message, 5);
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    private void assertElementHasText(By by, String expected_text, String error_message) {
        WebElement element = waitForElementPresent(by, "Can't find element", 5);
        String actual_text = element.getAttribute("text");
        Assert.assertTrue(error_message, actual_text.contains(expected_text));
    }

    protected void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);
        action.press(x, start_y).waitAction(timeOfSwipe).moveTo(x, end_y).release().perform();
    }

    protected void swipeUpQuick() {
        swipeUp(200);
    }

    protected void swipeUpToFindElement(By by, String error_message, int max_swipes) {
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

    protected void swipeElementToLeft(By by, String error_message) {
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

    private int getAmountOfElements(By by) {
        List elements = driver.findElements(by);
        return elements.size();
    }

    private void assertElementNotPresent(By by, String error_message) {
        int amount_of_elements = getAmountOfElements(by);
        if (amount_of_elements > 0) {
            String default_message = "An element '" + by.toString() + "' supposed to be not present";
            throw new AssertionError(default_message + " " + error_message);
        }
    }

    private String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }

    private void addArticleToMyList(String searchName, String title, String listName, boolean firstArticle) {

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

}
