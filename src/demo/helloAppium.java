package demo;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class helloAppium {
    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        AppiumDriver driver;

        DesiredCapabilities cap = new DesiredCapabilities();

        cap.setCapability("deviceName","8511aab2");
        cap.setCapability("platformVersion","8.1.0");
        cap.setCapability("platformName","android");
        cap.setCapability("app","G:\\Android_Automatic_Testing\\test app\\Maxjia.apk");

        cap.setCapability("appPackage","com.dotamax.app");
        cap.setCapability("appActivity","com.max.app.module.welcome.WelcomeActivity");
        cap.setCapability("deviceReadyTimeout",60);
        //cap.setCapability("unicodeKeyboard",true);
        //cap.setCapability("resetKeyboard",true);
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"),cap);
        Thread.sleep(5000);
        driver.findElementByXPath("//node()[@text='跳过登录']").click();
        //driver.findElementById("com.dotamax.app:id/pass_login").click();
        Thread.sleep(2000);
        //driver.findElementById("com.dotamax.app:id/iv_csgo").click();           //点击csgo图标\
        //Thread.sleep(2000);
        //driver.findElementById("com.dotamax.app:id/bt_check_gametype").click();
        //Thread.sleep(5000);
        driver.quit();
    }



}
