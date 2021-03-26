package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

abstract public class SearchPageObject extends MainPageObject {
    protected static String
            SEARCH_INIT_ELEMENT,
            SEARCH_INPUT,
            SEARCH_RESULT_BY_SUBSTRING_TPL,
            SEARCH_CANCEL_BUTTON,
            SEARCH_RESULT_ELEMENT,
            SEARCH_EMPTY_RESULT_ELEMENT,
            SEARCH_ARTICLE_TITLE,
            SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION_TPL;

    public SearchPageObject(RemoteWebDriver driver) {
        super(driver);
    }

    /*TEMPLATES METHOTDS*/
    private static String getResultSearchElement(String substring) {
        return SEARCH_RESULT_BY_SUBSTRING_TPL.replace("{SUBSTRING}", substring);
    }

    private static String getResultSearchByTitleAndDescription(String title, String description) {
        return SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION_TPL.replace("{TITLE}", title).replace("{DESCRIPTION}", description);
    }
    /*TEMPLATES METHOTDS*/

    public void waitForElementByTitleAndDescription(String title, String description) {
        String element_xpath = getResultSearchByTitleAndDescription(title, description);
        waitForElementPresent(element_xpath, "Can't find search result with title: " + title + " and description: " + description);

    }

    public void initSearchInput() {
        this.waitForElementAndClick(SEARCH_INIT_ELEMENT, "Can't find search init element", 5);
        this.waitForElementPresent(SEARCH_INIT_ELEMENT, "Can't find search input after clicking");
    }

    public void typeSearchLine(String search_line) {
        this.waitForElementAndSendKeys(SEARCH_INPUT, search_line, "Can't find and type into search input", 5);
    }

    public void initSearchInputAndTypeSearchLine(String search_line) {
        initSearchInput();
        typeSearchLine(search_line);
    }

    public void waitForSearchResult(String substring) {
        String search_result_xpath = getResultSearchElement(substring);
        System.out.println(search_result_xpath);
        this.waitForElementPresent(search_result_xpath, "Can't find search result with substring: " + substring);
    }

    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(SEARCH_CANCEL_BUTTON, "Can't find search cancel button", 5);
    }

    public void waitForCancelButtonToDissappear() {
        this.waitForElementNotPresent(SEARCH_CANCEL_BUTTON, "Search cancel button is still present", 5);
    }

    public void waitForSearchResultsToDissappear() {
        this.waitForElementNotPresent(SEARCH_RESULT_ELEMENT, "Search results are still present", 5);
    }

    public void clickCancelSearch() {
        this.waitForElementAndClick(SEARCH_CANCEL_BUTTON, "Can't find and click search cancel button", 5);
    }

    public void clickByArticleWithSubstring(String substring) {
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementAndClick(
                search_result_xpath,
                "Can't find and click search result with substring: " + substring,
                10);

    }

    public int getAmountOfFoundArticles() {
        this.waitForElementPresent(SEARCH_RESULT_ELEMENT, "Can't find anything by reqest", 15);
        return this.getAmountOfElements(SEARCH_RESULT_ELEMENT);
    }

    public void waitForEmptyResultsLabel() {
        this.waitForElementPresent(SEARCH_EMPTY_RESULT_ELEMENT, "Can't find empty result label", 15);
    }

    public void assertThereIsNoResultsOfSearch() {
        this.assertElementNotPresent(SEARCH_RESULT_ELEMENT, "We supposed not to find any results");
    }

    public List<WebElement> getAllSearchTitles() {
        By by = getLocatorByString(SEARCH_ARTICLE_TITLE);
        return driver.findElements(by);
    }

    public void assertSearchFieldContainsRightText() {
        assertElementHasText(SEARCH_INIT_ELEMENT,
                "Search Wikipedia", "Search field doesn't contains expected text");
    }

    public void openArticle(String search_line, String article_title) {
        initSearchInput();
        typeSearchLine(search_line);
        clickByArticleWithSubstring(article_title);
    }
}
