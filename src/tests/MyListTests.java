package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.MyListsPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class MyListTests extends CoreTestCase {
    @Test
    public void testSaveFirstArticleToMyList() {
        SearchPageObject searchPageObject = new SearchPageObject(driver);
        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        NavigationUI navigationUI = new NavigationUI(driver);
        MyListsPageObject myListsPageObject = new MyListsPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");
        articlePageObject.waitForTitleElement();
        String article_title = articlePageObject.getArticleTitle();
        String name_of_folder = "Learning programming";
        articlePageObject.addArticleToMyList(name_of_folder);
        articlePageObject.closeArticle();
        navigationUI.clickMyLists();
        myListsPageObject.openFolderByName(name_of_folder);
        myListsPageObject.swipeByArticleToDelete(article_title);
    }

    @Test
    public void testSaveTwoArticlesToMyList() {

        ArticlePageObject articlePageObject = new ArticlePageObject(driver);
        NavigationUI navigationUI = new NavigationUI(driver);
        MyListsPageObject myListsPageObject = new MyListsPageObject(driver);
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
