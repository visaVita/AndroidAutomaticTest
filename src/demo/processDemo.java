package demo;

import java.io.*;
import java.util.regex.*;

/*
这个Demo展示了使用多线程（读写分离）或者单线程进行对已经写好的批处理文件的操作交互，目前看来是先写再读比较合适，
先读再写会出现BufferReader阻塞在待输入的那一行的问题。
 */

public class processDemo {
    private static Process aaptProcess;
    private String app_name_str;
    public final static readThread  readT  = new readThread();
    public final static writeThread writeT = new writeThread();
    private static String packageRegex  = "^package:name='(\\w+\\.+)'";
    private static String activityRegex = "launchable-activity: name='(\\w+\\.+)'";

    public void setApp_name_str(String app_name_str) {
        this.app_name_str = app_name_str;
    }

    public String getApp_name_str(){
        return app_name_str;
    }

    public static class writeThread extends Thread{
        public void run(){
            PrintWriter appWriter = new PrintWriter(aaptProcess.getOutputStream(),true);
            //appWriter.print(getApp_name_str());
            try {
                Thread.sleep(2000);
                    synchronized (writeT) {
                        while (readT.flag)
                            wait();
                    }
                    System.out.println("写线程");
                    appWriter.print("Maxjia");
                    readT.setFlag(true);
                    synchronized (readT){
                        readT.notifyAll();
                    }
                appWriter.close();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static class readThread extends Thread{
        private boolean flag = false;
        public void setFlag(boolean flag){
            this.flag = flag;
        }
        public boolean getFlag(){
            return flag;
        }
        public void run(){
            String app_msg_str;
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(aaptProcess.getInputStream(),"GB2312"));
                Thread.sleep(1000);
                synchronized (readT) {
                    while (!flag)
                        readT.wait();
                }
                System.out.println("读线程");
                while ((app_msg_str = bufferedReader.readLine()) != null) {
                    System.out.println(app_msg_str);
                }
                Thread.sleep(1000);
                bufferedReader.close();
            }catch (UnsupportedEncodingException unsupported){
                unsupported.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)throws IOException,InterruptedException {
        String app_msg_str;
        Pattern pacPattern = Pattern.compile(packageRegex);
        Pattern actPattern = Pattern.compile(activityRegex);

        //"G:\\Android_Automatic_Testing\\aapt_help.bat"
        String[] cmd_str = new String[]{"cmd.exe","/C","G:\\Android_Automatic_Testing\\aapt_help.bat"};
        aaptProcess = Runtime.getRuntime().exec(cmd_str);

        PrintWriter appWriter = new PrintWriter(aaptProcess.getOutputStream(),true);
        appWriter.print("Maxjia");
        appWriter.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(aaptProcess.getInputStream(),"GB2312"));
        while ((app_msg_str = bufferedReader.readLine()) != null) {
            System.out.println(app_msg_str);
            Matcher pacMatcher = pacPattern.matcher(app_msg_str);
            Matcher actMatcher = actPattern.matcher(app_msg_str);
            if (pacMatcher.find()) {
                pacMatcher.group(1);
            }else if(actMatcher.find()){
                actMatcher.group(1);
            }
        }
        Thread.sleep(1000);
        bufferedReader.close();

        //readT.start();
        //writeT.start();

    }
}
