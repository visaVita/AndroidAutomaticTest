package regex;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class processMatch{
    private static Process aaptProcess;
    private static Process adbProcess;
    private static Process uiautomatotProcess;

    private String adb_devices_str;

    private String app_name_str;
    private String app_pac_str;
    private String app_act_str;

    private final static String packageRegex    = "^package: name='([0-9a-zA-Z\\p{Punct}]*)'(.*)";
    private final static String activityRegex   = "^launchable-activity: name='([0-9a-zA-Z\\p{Punct}]*)'(.*)";
    private final static String devicesRegex    = "^([a-zA-Z0-9]*)(\\t)device$";
    private final static String resolutionRegex = "^Physical size: (.*)";

    /*
     * 构造函数：
     * 参数：String    appName
     * 当调用aapt时使用它
     */
    public processMatch(String appName) {
        this.app_name_str = appName;
    }
    /*
     * 当调用adb时使用它
     */
    public processMatch(){

    }

    public void setApp_name_str(String app_name_str) {
        this.app_name_str = app_name_str;
    }

    public String getApp_name_str(){
        return app_name_str;
    }

    public String getApp_pac_str() {
        return app_pac_str;
    }

    public void setApp_pac_str(String app_pac_str) {
        this.app_pac_str = app_pac_str;
    }

    public String getApp_act_str() {
        return app_act_str;
    }

    public void setApp_act_str(String app_act_str) {
        this.app_act_str = app_act_str;
    }

    public void getAaptMsg()throws IOException {
        String app_msg_str;
        Pattern pacPattern = Pattern.compile(packageRegex);
        Pattern actPattern = Pattern.compile(activityRegex);
        //这里可以改变路径，但是现在暂时用现存的
        String[] cmd_str = new String[]{"cmd.exe","/C","G:\\Android_Automatic_Testing\\aapt_help.bat"};
        Process process = Runtime.getRuntime().exec(cmd_str);

        PrintWriter appWriter = new PrintWriter(process.getOutputStream(),true);
        appWriter.print(app_name_str);
        appWriter.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GB2312"));
        while ((app_msg_str = bufferedReader.readLine()) != null) {
            System.out.println(app_msg_str);
            Matcher pacMatcher = pacPattern.matcher(app_msg_str);
            Matcher actMatcher = actPattern.matcher(app_msg_str);
            if (pacMatcher.find()) {
                app_pac_str = pacMatcher.group(1);
            }else if(actMatcher.find()){
                app_act_str = actMatcher.group(1);
            }
        }

        bufferedReader.close();

    }

    public void getAdbDevice() throws IOException {
        String process_msg_str;
        Pattern devPattern = Pattern.compile(devicesRegex);
        String[] cmd_str = new String[]{"cmd.exe","/C","adb devices"};
        Process process = Runtime.getRuntime().exec(cmd_str);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GB2312"));
        while ((process_msg_str = bufferedReader.readLine())!= null){
            System.out.println(process_msg_str);
            Matcher devMatcher = devPattern.matcher(process_msg_str);
            if (devMatcher.find()){
                adb_devices_str = devMatcher.group(1);
            }
        }
        bufferedReader.close();

    }

    //todo 修改函数返回类型为boolean，然后判断获取是否成功
    public boolean getScreenDump(String xmlName) throws IOException, InterruptedException {
        String[] cmd_str = new String[]{"cmd.exe","/C","src\\myScript\\ScreenDump.bat"};
        Process process = Runtime.getRuntime().exec(cmd_str);

        PrintWriter uiWriter = new PrintWriter(process.getOutputStream(),true);
        uiWriter.print(xmlName);
        uiWriter.close();

        Thread.sleep(2000);
        return isExist("G:\\Android_Automatic_Testing\\screen_dump\\"+xmlName+".xml");
    }

    public String getAdb_devices_str() {
        return adb_devices_str;
    }


    public boolean getSreenCap(String imgName) throws IOException, InterruptedException {
        String[] cmd_str = new String[]{"cmd.exe","/C","src\\myScript\\ScreenCap.cmd"};
        Process process = Runtime.getRuntime().exec(cmd_str);

        PrintWriter uiWriter = new PrintWriter(process.getOutputStream(),true);
        uiWriter.print(imgName);
        uiWriter.close();

        Thread.sleep(2000);
        return isExist("G:\\Android_Automatic_Testing\\screen_dump\\"+imgName+".png");
    }

    public String[] getResolution() throws IOException {
        boolean  flag = false;
        String   res;
        String[] resolution = new String[2];
        String[] cmd_str = new String[]{"cmd.exe","/C","adb shell wm size"};
        Pattern  resPattern = Pattern.compile(resolutionRegex);

        Process process = Runtime.getRuntime().exec(cmd_str);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((res = bufferedReader.readLine()) != null) {
            System.out.println(res);
            Matcher resMatcher = resPattern.matcher(res);
            if (resMatcher.find()) {
                res = resMatcher.group(1);
                flag = true;
                break;
            }
        }
        bufferedReader.close();
        if (!flag)
            return null;
        resolution = res.split("x");
        return resolution;
    }

    public boolean getAdbState() throws IOException {
        String  res = "";
        String[] cmd_str = new String[]{"cmd.exe","/C","adb get-state"};
        Process process = Runtime.getRuntime().exec(cmd_str);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GB2312"));
        while ((res = bufferedReader.readLine()) != null){

            switch (res){
                case "device"  :return true;
                case "offline" :return false;
                case "unknown" :return false;
                default:return false;
            }
        }

        return false;
    }

    public static boolean isExist(String Path){
        File file = new File(Path);
        if (!file.exists())
            return false;
        return true;
    }

    public static void main(String[] args){
        String a = "Physical size: 1200x1920";
        //Pattern pattern = Pattern.compile(packageRegex);
        Pattern pattern = Pattern.compile("^Physical size: (.*)");
        Matcher matcher = pattern.matcher(a);
        System.out.println(matcher.find());
        System.out.println(matcher.group(1));
    }
}
