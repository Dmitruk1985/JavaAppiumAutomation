package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

abstract public class MyListsPageObject extends MainPageObject {

    protected static String
            FOLDER_BY_NAME_TPL,
            ARTICLE_BY_TITLE_TPL,
            OPTIONS_BUTTON_BY_TITLE,
            DELETE_ARTICLE_OPTION;

    public MyListsPageObject(RemoteWebDriver driver) {
        super(driver);
    }

    private static String getFolderXpathByName(String name_of_folder) {
        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", name_of_folder);
    }

    private static String getOptionsButtonXpathByTitle(String article_title) {
        return OPTIONS_BUTTON_BY_TITLE.replace("{TITLE}", article_title);
    }

    private static String getSavedArticleXpathByTitle(String article_title) {
        return ARTICLE_BY_TITLE_TPL.replace("{TITLE}", article_title);
    }

    public void openFolderByName(String name_of_folder) {
        String folder_xpath = getFolderXpathByName(name_of_folder);
        this.waitForElementAndClick(folder_xpath,
                "Can't find folder by name: " + name_of_folder,
                5);
    }

    public void swipeByArticleToDelete(String article_title) {
        this.waitForArticleToAppearByTitle(article_title);
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.swipeElementToLeft(article_xpath,
                "Can't find saved article");
        if(Platform.getInstance().isIOS()) clickElementToTheRightUpperConner(article_xpath, "Cann't find saved article");
        this.waitForArticleToDisappearByTitle(article_title);
    }

    public void waitForArticleToDisappearByTitle(String article_title) {
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementNotPresent(
                article_xpath,
                "Saved article still present: " + article_title,
                15);
    }

    public void waitForArticleToAppearByTitle(String article_title) {
        String article_title_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementPresent(
                article_title_xpath,
                "Cannot find saved article by title: " + article_title,
                15);
    }

    public void clickOptionsButtonByTitle(String article_title) {
        String options_button_xpath = getOptionsButtonXpathByTitle(article_title);
        waitForElementAndClick(
                options_button_xpath,
                "Can't find and click options button for title: " + article_title,
                5);
    }

    public void clickDeleteOption() {
        waitForElementAndClick(
                DELETE_ARTICLE_OPTION,
                "Can't find options button",
                5);
    }

    public void assertArticleIsNotPresentInMyList(String article_title) {
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        waitForElementNotPresent(
                article_xpath,
                "Article is still present in my lists",
                5);
    }

    public void openArticle(String article_title){
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        waitForElementAndClick(
                article_xpath,
                "Can't find and open article: " + article_title,
                5);
    }


}
