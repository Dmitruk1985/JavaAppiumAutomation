package lib;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class Platform {
    private static final String
            PLATFORM_IOS = "ios",
            PLATFORM_ANDROID = "android",
            APPIUM_URL = "http://127.0.0.1:4723/wd/hub";

    public AppiumDriver getDriver() throws Exception {
        URL url = new URL(APPIUM_URL);
        if (isAndroid()) return new AndroidDriver(url, getAndroidDesiredCapabilities());
        else if (isIOS()) return new IOSDriver(url, getAndroidDesiredCapabilities());
        else throw new Exception("Cann't detect type of Driver. Platform value: " + getPlatformVar());

    }

    public boolean isAndroid() {
        return isPlatform(PLATFORM_ANDROID);
    }

    public boolean isIOS() {
        return isPlatform(PLATFORM_IOS);
    }

    private DesiredCapabilities getAndroidDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "and80");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "D:\\Documents\\IT\\Testing\\Mobile_Automation\\github\\JavaAppiumAutomation\\apks\\org.wikipedia.apk");
        capabilities.setCapability("orientation", "PORTRAIT");
        return capabilities;
    }

    private DesiredCapabilities getIOSDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("deviceName", "iPhone SE");
        capabilities.setCapability("platformVersion", "11.3");
        capabilities.setCapability("app", "D:\\Documents\\IT\\Testing\\Mobile_Automation\\github\\JavaAppiumAutomation\\apks\\Wikipedia.app");
        capabilities.setCapability("orientation", "PORTRAIT");
        return capabilities;
    }

    private boolean isPlatform(String my_platform) {
        String platform = getPlatformVar();
        return my_platform.equals(platform);
    }

    private String getPlatformVar() {
        return System.getenv("PLATFORM");
    }

}
