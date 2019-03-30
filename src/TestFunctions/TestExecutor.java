package TestFunctions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import regex.processMatch;
import TestFunctions.XPathMod;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class TestExecutor {
    //控件目录
    private static String Button = "按钮";
    private static String imageButton = "图片按钮";
    private static String radioBox = "单选按钮";
    private static String checkBox = "复选按钮";
    private static String TextView = "展示文本框";
    private static String EditView = "可编辑文本框";
    private static String imageView = "图片框";
    //private static String progressBar = "进度条";


    private AppiumDriver mDriver;
    private DesiredCapabilities mCap;
    private WaitOptions mWaitOptions;
    private String appName;
    private String appActivity;
    private String appPackage;
    private String device;
    private String serverURL = "http://127.0.0.1:4723/wd/hub";
    final private static String appLoc= "G:\\Android_Automatic_Testing\\test app\\";

    private static Process adbProcess;

    private processMatch process;
    private XPathMod XPathMethod;

    public TestExecutor(){
        this.mCap = new DesiredCapabilities();
    }

    public void initialize(String AppName){
        this.appName = AppName;

        process = new processMatch(appName);
        try {
            process.getAaptMsg();
            process.getAdbDevice();

        }catch (IOException e){
            e.printStackTrace();
        }
        appActivity = process.getApp_act_str();
        appPackage  = process.getApp_pac_str();
        device      = process.getAdb_devices_str();

        mCap.setCapability("deviceName",device);
        mCap.setCapability("platformVersion","8.0");
        mCap.setCapability("automationName","Appium");
        mCap.setCapability("app",appLoc+appName+".apk");
        mCap.setCapability("appPackage",appPackage);
        mCap.setCapability("appActivity",appActivity);

        try{
            this.mDriver = new AndroidDriver(new URL(serverURL),mCap);
        }catch (MalformedURLException e){
            //todo
        }

        this.mWaitOptions = new WaitOptions().withDuration(Duration.ofSeconds(1));
        this.XPathMethod = new XPathMod("");
        //todo
    }

    public void setAppName(){

    }

    public int my_uninstall(){
        return 0;
    }

    public void my_start(String actName){
        mCap.setCapability("appPackage",appPackage);
        mCap.setCapability("appActivity",actName);
    }

    public void my_start(){
        mCap.setCapability("appPackage",appPackage);
        mCap.setCapability("appActivity",appActivity);
    }

    public WebElement Find(String controlName, String LocType) throws XPathExpressionException {

        if (LocType.toLowerCase().equals("id")){
            return mDriver.findElement(By.id(controlName));
        }else if (LocType.toLowerCase().equals("xpath")){
            String xpath = XPathMethod.generateXpathByText(controlName);
            return mDriver.findElementByXPath(xpath);
        }
        //todo
        else if(LocType.toLowerCase().equals("desc|text")){
            String xpath = XPathMethod.generateXpathByContentDesc(controlName);
            return mDriver.findElementByXPath(xpath);
        }
        else {
            //todo throw an exception
            return null;
        }
    }

    //动作区
    public void Operation(String OpType,String Name,String LocType) throws XPathExpressionException {

        WebElement element = Find(Name,LocType);

        if (OpType.equals("click")){
            element.click();
        }
        else if (OpType.equals("clear")){
            element.clear();
        }
    }

    /*
    * 参数：
    *          OpType    String     操作类型
    *          Name      String
    * */
    public void Operation(String OpType,String Name,String LocType,String value) throws XPathExpressionException {
        WebElement element = Find(Name,LocType);
        if (OpType.equals("click")){
            element.click();
        }
        if (OpType.equals("clear")){
            element.clear();
        }
        if (OpType.toLowerCase().equals("sendkeys")){
            element.sendKeys(value);
        }
    }

    public void SwipeUp(){
        int height = mDriver.manage().window().getSize().height;
        int width  = mDriver.manage().window().getSize().width;
        //这里根据页面的宽高来选择滑动位置(往上滑动)
        PointOption startPos = PointOption.point(width*2/3,height*3/4);
        PointOption endPos   = PointOption.point(width*2/3,height/4);

        new TouchAction(mDriver).press(startPos).waitAction(mWaitOptions).moveTo(endPos).perform();
    }

    public void SwipeDown(){
        int height = mDriver.manage().window().getSize().height;
        int width  = mDriver.manage().window().getSize().width;
        //这里根据页面的宽高来选择滑动位置(往下滑动)
        PointOption startPos = PointOption.point(width*2/3,height/4 );
        PointOption endPos   = PointOption.point(width*2/3,height*3/4);

        new TouchAction(mDriver).press(startPos).waitAction(mWaitOptions).moveTo(endPos).perform();
    }

    public void SwipeRight(){
        int height = mDriver.manage().window().getSize().height;
        int width  = mDriver.manage().window().getSize().width;
        //这里根据页面的宽高来选择滑动位置(往右滑动)
        PointOption startPos = PointOption.point(width/3  ,height/4);
        PointOption endPos   = PointOption.point(width*2/3,height/4);

        new TouchAction(mDriver).press(startPos).waitAction(mWaitOptions).moveTo(endPos).perform();
    }

    public void SwipeLeft(){
        int width  = mDriver.manage().window().getSize().width;
        int height = mDriver.manage().window().getSize().height;
        //这里根据页面的宽高来选择滑动位置(往左滑动)
        PointOption startPos = PointOption.point(width*2/3,height/4 );
        PointOption endPos   = PointOption.point(width/3  ,height/4);

        new TouchAction(mDriver).press(startPos).waitAction(mWaitOptions).moveTo(endPos).perform();
    }

    public int swipeTo(String Name,String LocType,int direction) throws XPathExpressionException, InterruptedException {
        int n = 0 ,flag = 0;

        do {
            switch (direction){
                case 1:SwipeUp();     break;
                case 2:SwipeDown();   break;
                case 3:SwipeLeft();   break;
                case 4: SwipeRight(); break;
                default: /* todo:handle error */ flag = -1; return flag;
            }
            n++;
            Thread.sleep(500);
            //todo:加入刷新XML的操作（myUiAtuomator）
            if (n > 20) {
                flag = 0;
                break;
            }
        }while (null == Find(Name,LocType));
        if (flag == 0){
            while (n > 0){
                switch (direction){
                    case 1:SwipeDown();   break;
                    case 2:SwipeUp();     break;
                    case 3:SwipeRight();  break;
                    case 4:SwipeLeft();   break;
                }
            }
        }else {
            flag = 1;
        }
        return flag;
    }

    public void ZoomIn(){
        int width  = mDriver.manage().window().getSize().width;
        int height = mDriver.manage().window().getSize().height;
        PointOption startPos1 = PointOption.point(width*2/5,height*3/5);
        PointOption endPos1   = PointOption.point(width*1/5,height*4/5);

        PointOption startPos2 = PointOption.point(width*3/5,height*2/5);
        PointOption endPos2   = PointOption.point(width*4/5,height*1/5);

        TouchAction leftBelow = new TouchAction(mDriver).press(startPos1).waitAction(mWaitOptions).moveTo(endPos1);
        TouchAction rightUp   = new TouchAction(mDriver).press(startPos2).waitAction(mWaitOptions).moveTo(endPos2);

        new MultiTouchAction(mDriver).add(leftBelow).add(rightUp).perform();
    }

    public void ZoomOut(){
        int height = mDriver.manage().window().getSize().height;
        int width  = mDriver.manage().window().getSize().width;
        PointOption startPos1 = PointOption.point(width*1/5,height*4/5);
        PointOption endPos1   = PointOption.point(width*2/5,height*3/5);

        PointOption startPos2 = PointOption.point(width*4/5,height*1/5);
        PointOption endPos2   = PointOption.point(width*3/5,height*2/5);

        TouchAction rightUp   = new TouchAction(mDriver).press(startPos2).waitAction(mWaitOptions).moveTo(endPos2);
        TouchAction leftBelow = new TouchAction(mDriver).press(startPos1).waitAction(mWaitOptions).moveTo(endPos1);

        new MultiTouchAction(mDriver).add(rightUp).add(leftBelow).perform();
    }

    public void ZoomOutAt(String OpType,String Name) throws XPathExpressionException {
        WebElement element = Find(Name,OpType);
        int x = element.getLocation().getX();
        int y = element.getLocation().getY();
        int height = element.getSize().getHeight();
        int width  = element.getSize().getWidth();
        int midX = x + width/2;
        int midY = y + height/2;

        PointOption startPos = PointOption.point(midX,midY);
        PointOption endPos1  = PointOption.point(x + width/4, y + height/4);
        PointOption endPos2  = PointOption.point(x + width*3/4,y + height*3/4);

        TouchAction Up   = new TouchAction(mDriver).press(startPos).waitAction(mWaitOptions).moveTo(endPos1);
        TouchAction Down = new TouchAction(mDriver).press(startPos).waitAction(mWaitOptions).moveTo(endPos2);
        new MultiTouchAction(mDriver).add(Up).add(Down).perform();
    }

    public void ZoomInAt(String OpType,String Name) throws XPathExpressionException {
        WebElement element = Find(Name,OpType);
        int x = element.getLocation().getX();
        int y = element.getLocation().getY();
        int height = element.getSize().getHeight();
        int width  = element.getSize().getWidth();
        int midX = x + width/2;
        int midY = y + height/2;

        PointOption startPos1 = PointOption.point(x,y);
        PointOption startPos2  = PointOption.point(x + width, y + height);
        PointOption endPos  = PointOption.point(x + width/2,y + height/2);

        TouchAction Up   = new TouchAction(mDriver).press(startPos1).waitAction(mWaitOptions).moveTo(endPos);
        TouchAction Down = new TouchAction(mDriver).press(startPos2).waitAction(mWaitOptions).moveTo(endPos);
        new MultiTouchAction(mDriver).add(Up).add(Down).perform();
    }

    public int waitFor(){

        return 0;
    }

}
