package regex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class processMatch {
    private static Process aaptProcess;
    private static Process adbProcess;

    private String adb_devices_str;

    private String app_name_str;
    private String app_pac_str;
    private String app_act_str;

    private static String packageRegex  = "^package:name='(\\w+\\.+)'";
    private static String activityRegex = "launchable-activity: name='(\\w+\\.+)'";
    private static String devicesRegex  = "^(\\w*\\d*)( +)device$";
    //todo:补上获取设备信息的正则表达式

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
        aaptProcess = Runtime.getRuntime().exec(cmd_str);

        PrintWriter appWriter = new PrintWriter(aaptProcess.getOutputStream(),true);
        appWriter.print(app_name_str);
        appWriter.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(aaptProcess.getInputStream(),"GB2312"));
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
        adbProcess = Runtime.getRuntime().exec(cmd_str);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(adbProcess.getInputStream(),"GB2312"));
        while ((process_msg_str = bufferedReader.readLine())!= null){
            System.out.println(process_msg_str);
            Matcher devMatcher = devPattern.matcher(process_msg_str);
            if (devMatcher.find()){
                adb_devices_str = devMatcher.group(1);
            }
        }
        bufferedReader.close();
    }

    public String getAdb_devices_str() {
        return adb_devices_str;
    }
}
