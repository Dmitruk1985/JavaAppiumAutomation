package lib.ui.factories;

import io.appium.java_client.AppiumDriver;
import lib.Platform;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import lib.ui.android.AndroidNavigationUI;
import lib.ui.android.AndroidSearchPageObject;
import lib.ui.ios.IOSNavigationUI;
import lib.ui.ios.IOSSearchPageObject;

public class NavigationUIFactory {
    public static NavigationUI get(AppiumDriver driver) {
        if (Platform.getInstance().isAndroid()) return new AndroidNavigationUI(driver);
        else return new IOSNavigationUI(driver);
    }
}
