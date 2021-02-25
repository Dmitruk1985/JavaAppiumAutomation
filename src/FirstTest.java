import lib.CoreTestCase;
import lib.ui.*;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FirstTest extends CoreTestCase {

    @Test
    public void testCancelSearchTest() {
        //Кликаем по полю поиска
        mainPageObject.waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        //Вводим значение "Java"
        mainPageObject.waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Java", "Cannot find search input", 5);

        String itemXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_container']";

        //Дожидаемся появления результата поиска
        mainPageObject.waitForElementPresent(By.xpath(itemXpath), "Can't locate result item", 5);

        List<WebElement> listOfItems = driver.findElements(By.xpath(itemXpath));

        //Проверяем, что найдено более 1 результата
        Assert.assertTrue("List has <=1 result", listOfItems.size() > 1);

        //Очищаем поле поиска
        mainPageObject.waitForElementAndClick(By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search", 5);

        //Дожидаемся исчезновения результата поиска
        mainPageObject.waitForElementNotPresent(By.xpath(itemXpath), "Result item is still present", 5);

        listOfItems = driver.findElements(By.xpath(itemXpath));

        //Проверяем, что результаты поиска исчезли
        Assert.assertTrue("List of results is not empty", listOfItems.size() == 0);
    }

    @Test
    public void testAllSearchResultsHaveKeyWordTest() {
        //Кликаем по полю поиска
        mainPageObject.waitForElementAndClick(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Can't locate search field", 5);

        //Вводим значение "Java"
        mainPageObject.waitForElementAndSendKeys(By.xpath("//*[contains(@text, 'Search…')]"),
                "Java", "Cannot find search input", 5);

        String headerXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title']";

        //Дожидаемся появления результата поиска
        mainPageObject.waitForElementPresent(By.xpath(headerXpath), "Can't locate result item", 5);

        //Получаем список всех заголовков результатов
        List<WebElement> headersList = driver.findElements(By.xpath(headerXpath));

        //Проверяем для каждого заголовка наличие в нем ключевого слова
        for (int i = 0; i < headersList.size(); i++) {
            Assert.assertTrue("Serch result №" + headersList.get(i) + " doesn't contains key word",
                    headersList.get(i).getAttribute("text").contains("Java"));
        }
    }

    @Test
    public void testSearchFieldContainsRightTextTest() {
        mainPageObject.assertElementHasText(By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Search Wikipedia", "Search field doesn't contains expected text");
    }
 
    @Test
    public void testSaveTwoArticlesToMyList() {
        String listName = "MyList";
        String firstTitle = "Java (programming language)";
        String secondTitle = "Java";

        //Добавляем в список 2 статьи
        mainPageObject.addArticleToMyList("Java", firstTitle, listName, true);
        mainPageObject.addArticleToMyList("Java", secondTitle, listName, false);

        //Открываем раздел со списками
        mainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Can't find 'My lists' button",
                5);

        //Выбираем нужный список
        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + listName + "']"),
                "Can't find created folder",
                5);

        //Удаляем первую статью из списка (через свайп плохо работает, реализовано через кнопки)
        //Нажимаем кнопку "опции" у заданной статьи
        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title' and @text='" + firstTitle + "']" +
                        "/ancestor::*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@content-desc='More options']"),
                "Can't find options button",
                5);

        //Нажимаем кнопку "удалить"
        mainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/reading_list_item_remove_text"),
                "Can't find options button",
                5);

        //Убеждаемся, что статья удалена
        mainPageObject.waitForElementNotPresent(
                By.xpath("//*[@text='" + firstTitle + "']"),
                "Article " + firstTitle + " is not deleted",
                5);

        //Убеждаемся, что вторая статья осталась, и кликаем по ней
        mainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + secondTitle + "']"),
                "Can't find article " + secondTitle,
                5);

        WebElement title_element = mainPageObject.waitForElementPresent(By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title", 15);
        String secondTitleAfterDeleting = title_element.getAttribute("text");

        //Проверяем, что заголовок не изменился
        Assert.assertEquals("Article's title is wrong", secondTitle, secondTitleAfterDeleting);
    }

    @Test
    public void testAssertTitlePresent() {
        mainPageObject.openArticle("Java", "Java (programming language)");
        mainPageObject.assertElementPresent(By.id("org.wikipedia:id/view_page_title_text"), "Can't find articles's title");
    }
}
