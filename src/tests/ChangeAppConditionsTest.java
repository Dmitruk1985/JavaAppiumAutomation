package tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.SearchPageObject;
import org.junit.Test;

public class ChangeAppConditionsTest extends CoreTestCase {
    @Test
    public void testChangeScreenOrientationOnSearchResults() {

        SearchPageObject searchPageObject = new SearchPageObject(driver);
        ArticlePageObject articlePageObject = new ArticlePageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.clickByArticleWithSubstring("Object-oriented programming language");
        String title_before_rotation = articlePageObject.getArticleTitle();
        rotateScreenLandscape();
        String title_after_rotation = articlePageObject.getArticleTitle();
        assertEquals("Article title have been change after rotation", title_before_rotation, title_after_rotation);
        rotateScreenPortrait();
        String title_after_second_rotation = articlePageObject.getArticleTitle();
        assertEquals("Article title have been change after rotation", title_before_rotation, title_after_second_rotation);
    }

    @Test
    public void testCheckSearchArticleInBackground() {

        SearchPageObject searchPageObject = new SearchPageObject(driver);

        searchPageObject.initSearchInput();
        searchPageObject.typeSearchLine("Java");
        searchPageObject.waitForSearchResult("Object-oriented programming language");
        backgroundApp(2);
        searchPageObject.waitForSearchResult("Object-oriented programming language");
    }
}
