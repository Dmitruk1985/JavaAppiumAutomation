package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.ArticlePageObject;
import lib.ui.MyListsPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.MyListsPageObjectFactory;
import lib.ui.factories.NavigationUIFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class MyListTests extends CoreTestCase {
    private static final String name_of_folder = "Learning programming";
    @Test
    public void testSaveFirstArticleToMyList() {
        SearchPageObject searchPageObject = SearchPageObjectFactory.get(driver);
        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        NavigationUI navigationUI = NavigationUIFactory.get(driver);
        MyListsPageObject myListsPageObject = MyListsPageObjectFactory.get(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");
        articlePageObject.waitForTitleElement();
        String article_title = articlePageObject.getArticleTitle();
        if(Platform.getInstance().isAndroid()) articlePageObject.addArticleToMyList(name_of_folder);
        else articlePageObject.addArticlesToMySaved();
        articlePageObject.closeArticle();
        navigationUI.clickMyLists();
        if(Platform.getInstance().isAndroid()) myListsPageObject.openFolderByName(name_of_folder);
        myListsPageObject.swipeByArticleToDelete(article_title);
    }

    @Test
    public void testSaveTwoArticlesToMyList() {

        ArticlePageObject articlePageObject = ArticlePageObjectFactory.get(driver);
        NavigationUI navigationUI = NavigationUIFactory.get(driver);
        MyListsPageObject myListsPageObject = MyListsPageObjectFactory.get(driver);
        String name_of_folder = "MyList";
        String firstTitle = "Java (programming language)";
        String secondTitle = "Java";

        articlePageObject.openArticleAndAddItToMyList("Java", firstTitle, name_of_folder, true);
        articlePageObject.openArticleAndAddItToMyList("Java", secondTitle, name_of_folder, false);
        navigationUI.clickMyLists();
        myListsPageObject.openFolderByName(name_of_folder);
        myListsPageObject.clickOptionsButtonByTitle(firstTitle);
        myListsPageObject.clickDeleteOption();
        myListsPageObject.assertArticleIsNotPresentInMyList(firstTitle);
        myListsPageObject.openArticle(secondTitle);
        String secondTitleAfterDeleting = articlePageObject.getArticleTitle();
        assertEquals("Article's title was changed", secondTitle, secondTitleAfterDeleting);
    }
}
