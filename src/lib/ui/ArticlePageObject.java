package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import lib.ui.factories.SearchPageObjectFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject {

    SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);

    protected static String
            TITLE,
            FOOTER_ELEMENT,
            OPTIONS_BUTTON,
            OPTIONS_ADD_TO_MY_LIST_BUTTON,
            ADD_TO_MY_LIST_OVERLAY,
            MY_LIST_NAME_INPUT,
            MY_LIST_OK_BUTTON,
            CLOSE_ARTICLE_BUTTON,
            FOLDER_ADD_ARTICLE_TPL;

    public ArticlePageObject(AppiumDriver driver) {
        super(driver);
    }

    private static String getFolderName(String substring) {
        return FOLDER_ADD_ARTICLE_TPL.replace("{FOLDER}", substring);
    }

    public WebElement waitForTitleElement() {
        return this.waitForElementPresent(TITLE, "Can't find article title on page", 15);
    }

    public String getArticleTitle() {
        WebElement title_element = waitForTitleElement();
        if(Platform.getInstance().isAndroid()) return title_element.getAttribute("text");
        else return title_element.getAttribute("name");
    }

    public void swipeToFooter() {
        if(Platform.getInstance().isAndroid()) this.swipeUpToFindElement(FOOTER_ELEMENT, "Can't find the end of article", 40);
        else swipeUpTillElementAppear(FOOTER_ELEMENT, "Can't find the end of article", 40);
    }

    public void addArticleToMyList(String name_of_folder) {

        this.waitForElementAndClick(
                OPTIONS_BUTTON,
                "Can't find button to open article options",
                5);

        this.waitForElementAndClick(
                OPTIONS_ADD_TO_MY_LIST_BUTTON,
                "Can't find option to add article to list",
                5);

        this.waitForElementAndClick(
                ADD_TO_MY_LIST_OVERLAY,
                "Can't find 'GOT IT' button",
                5);

        this.waitForElementAndClear(
                MY_LIST_NAME_INPUT,
                "Can't find input to save name",
                5);

        this.waitForElementAndSendKeys(
                MY_LIST_NAME_INPUT,
                name_of_folder,
                "Can't put text into article input",
                5);

        this.waitForElementAndClick(
                MY_LIST_OK_BUTTON,
                "Can't press 'OK' button",
                5);
    }

    public void closeArticle() {
        this.waitForElementAndClick(
                CLOSE_ARTICLE_BUTTON,
                "Can't close article",
                5);
    }

    public void openArticleAndAddItToMyList(String searchName, String article_title, String name_of_folder, boolean firstArticle) {

        searchPageObject.openArticle(searchName, article_title);
        waitForTitleElement();
        this.waitForElementAndClick(
                OPTIONS_BUTTON,
                "Can't find button to open article options",
                5);

        this.waitForElementAndClick(
                OPTIONS_ADD_TO_MY_LIST_BUTTON,
                "Can't find option to add article to list",
                5);

        //Если добавляем статью в первый раз
        if (firstArticle) {
            this.waitForElementAndClick(
                    ADD_TO_MY_LIST_OVERLAY,
                    "Can't find 'GOT IT' button",
                    5);

            this.waitForElementAndClear(
                    MY_LIST_NAME_INPUT,
                    "Can't find input to save name",
                    5);

            this.waitForElementAndSendKeys(
                    MY_LIST_NAME_INPUT,
                    name_of_folder,
                    "Can't put text into article input",
                    5);

            this.waitForElementAndClick(
                    MY_LIST_OK_BUTTON,
                    "Can't press 'OK' button",
                    5);
        } else {
            //Выбираем ранее созданный список
            String folder_xpath = getFolderName(name_of_folder);
            waitForElementAndClick(
                    folder_xpath,
                    "Can't find list " + name_of_folder,
                    5);

        }
        closeArticle();
    }

    public void assertArticleHasTitle() {
        By by = getLocatorByString(TITLE);
        assertElementPresent(by, "Can't find articles's title");
    }


}
