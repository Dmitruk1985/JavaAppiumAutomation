package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ArticlePageObject extends MainPageObject {

    SearchPageObject searchPageObject = new SearchPageObject(driver);

    private static final String
            TITLE = "id:org.wikipedia:id/view_page_title_text",
            FOOTER_ELEMENT = "xpath://*[@text='View page in browser']",
            OPTIONS_BUTTON = "xpath://android.widget.ImageView[@content-desc='More options']",
            OPTIONS_ADD_TO_MY_LIST_BUTTON = "xpath://*[@text='Add to reading list']",
            ADD_TO_MY_LIST_OVERLAY = "id:org.wikipedia:id/onboarding_button",
            MY_LIST_NAME_INPUT = "id:org.wikipedia:id/text_input",
            MY_LIST_OK_BUTTON = "xpath://*[@text='OK']",
            CLOSE_ARTICLE_BUTTON = "xpath://android.widget.ImageButton[@content-desc='Navigate up']",
            FOLDER_ADD_ARTICLE_TPL = "xpath://android.widget.TextView[@text='{FOLDER}']";

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
        return title_element.getAttribute("text");
    }

    public void swipeToFooter() {
        this.swipeUpToFindElement(FOOTER_ELEMENT, "Can't find the end of article", 20);
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
