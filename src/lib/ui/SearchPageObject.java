package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPageObject extends MainPageObject {
    private static final String
            SEARCH_INIT_ELEMENT = "//*[contains(@text, 'Search Wikipedia')]",
            SEARCH_INPUT = "//*[contains(@text, 'Search…')]",
            SEARCH_RESULT_BY_SUBSTRING_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='{SUBSTRING}']",
            SEARCH_CANCEL_BUTTON = "org.wikipedia:id/search_close_btn",
            SEARCH_RESULT_ELEMENT = "//*[@resource-id='org.wikipedia:id/search_results_list']//*[@resource-id='org.wikipedia:id/page_list_item_container']",
            SEARCH_EMPTY_RESULT_ELEMENT = "//*[@text='No results found']",
            SEARCH_ARTICLE_TITLE = "//*[@resource-id='org.wikipedia:id/page_list_item_title']",
            SEARCH_RESULT_BY_TITLE_AND_DESCRIPTION_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_title' and @text='{TITLE}']" +
                    "/following-sibling::*[@resource-id='org.wikipedia:id/page_list_item_description' and @text='{DESCRIPTION}']";

    public SearchPageObject(AppiumDriver driver) {
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
        waitForElementPresent(By.xpath(element_xpath), "Can't find search result with title: " + title + " and description: " + description);

    }

    public void initSearchInput() {
        this.waitForElementAndClick(By.xpath(SEARCH_INIT_ELEMENT), "Can't find search init element", 5);
        this.waitForElementPresent(By.xpath(SEARCH_INIT_ELEMENT), "Can't find search input after clicking");
    }

    public void typeSearchLine(String search_line) {
        this.waitForElementAndSendKeys(By.xpath(SEARCH_INPUT), search_line, "Can't find and type into search input", 5);
    }

    public void initSearchInputAndTypeSearchLine(String search_line) {
        initSearchInput();
        typeSearchLine(search_line);
    }

    public void waitForSearchResult(String substring) {
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementPresent(By.xpath(search_result_xpath), "Can't find search result with substring: " + substring);
    }

    public void waitForCancelButtonToAppear() {
        this.waitForElementPresent(By.id(SEARCH_CANCEL_BUTTON), "Can't find search cancel button", 5);
    }

    public void waitForCancelButtonToDissappear() {
        this.waitForElementNotPresent(By.id(SEARCH_CANCEL_BUTTON), "Search cancel button is still present", 5);
    }

    public void waitForSearchResultsToDissappear() {
        this.waitForElementNotPresent(By.xpath(SEARCH_RESULT_ELEMENT), "Search results are still present", 5);
    }

    public void clickCancelSearch() {
        this.waitForElementAndClick(By.id(SEARCH_CANCEL_BUTTON), "Can't find and click search cancel button", 5);
    }

    public void clickByArticleWithSubstring(String substring) {
        String search_result_xpath = getResultSearchElement(substring);
        this.waitForElementAndClick(
                By.xpath(search_result_xpath),
                "Can't find and click search result with substring: " + substring,
                10);

    }

    public int getAmountOfFoundArticles() {
        this.waitForElementPresent(By.xpath(SEARCH_RESULT_ELEMENT), "Can't find anything by reqest", 15);
        return this.getAmountOfElements(By.xpath(SEARCH_RESULT_ELEMENT));
    }

    public void waitForEmptyResultsLabel() {
        this.waitForElementPresent(By.xpath(SEARCH_EMPTY_RESULT_ELEMENT), "Can't find empty result label", 15);
    }

    public void assertThereIsNoResultsOfSearch() {
        this.assertElementNotPresent(By.xpath(SEARCH_RESULT_ELEMENT), "We supposed not to find any results");
    }

    public List<WebElement> getAllSearchTitles() {
        return driver.findElements(By.xpath(SEARCH_ARTICLE_TITLE));
    }

    public void assertSearchFieldContainsRightText() {
        assertElementHasText(By.xpath(SEARCH_INIT_ELEMENT),
                "Search Wikipedia", "Search field doesn't contains expected text");
    }

    public void openArticle(String search_line, String article_title) {
        initSearchInput();
        typeSearchLine(search_line);
        clickByArticleWithSubstring(article_title);
    }
}
