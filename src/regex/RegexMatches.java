package regex;

import TestFunctions.TestExecutor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.String;

public class RegexMatches {
    private ArrayList<String> regStr = new ArrayList<>();

    //正则目录
    private static int MAX_APP_ADRESS = 2;
    private static int MAX_CLICK_ADRESS = 5;
    private static int MAX_SWIPE_ADRESS = 7;
    private static int MAX_ZOOM_ADRESS = 9;
    private static int MAX_INPUT_ADRESS = 10;

    //控件目录
    static String all_control = "按钮|图片按钮|单选按钮|复选按钮|展示文本框|可编辑文本框|图片框|进度条";

    //- 正则表达式部分
    //--- App部分
    private String p_install = "^安装(\\w+)App$";
    private String p_uninstall = "^卸载(\\w+)App$";
    private String p_start = "^打开([a-zA-Z0-9]+)(App)$";
    //--- 动作部分
    //-----点击动作
    private String p_click = "^单击(\\w+)("+all_control+")$";
    private String p_db_click = "^双击(\\w+)("+all_control+")$";
    private String p_long_press = "^长按(\\w+)("+all_control+")$";
    //-----滑动动作
    private String p_swipe_lr = "^(向左|向右)滑动$";
    private String p_swipe_ud = "^(向上|向下)滑动$";
    //后面应该补充关于滑动程度的处理，现在先搁置
    //-----缩放动作
    private String p_zoom_in  = "(放大)";
    private String p_zoom_out = "(缩小)";
    //后面应该补充关于缩放程度的处理，现在先搁置
    //-----输入动作
    private String p_input = "在("+ all_control+")中输入(\\w+)";

    //最终的解析结果
    public String AppName;
    public String ConName;
    public String ConType;
    public String SwipeTo;
    public String ZoomAt;
    public String Input;

    //executor
    //TestExecutor executor;


    public RegexMatches(){
        regStr.add(0,getP_install());
        regStr.add(1,getP_uninstall());
        regStr.add(2,getP_start());
        regStr.add(3,getP_click());
        regStr.add(4,getP_db_click());
        regStr.add(5,getP_long_press());
        regStr.add(6,getP_swipe_lr());
        regStr.add(7,getP_swipe_ud());
        regStr.add(8,getP_zoom_in());
        regStr.add(9,getP_zoom_out());
        regStr.add(10,getP_input());

        //executor = new TestExecutor();
    }

    //get()方法开始
    public String getAll_control(){
        return all_control;
    }

    public String getP_install() {
        return p_install;
    }

    public String getP_click() {
        return p_click;
    }

    public String getP_db_click() {
        return p_db_click;
    }

    public String getP_long_press() {
        return p_long_press;
    }

    public String getP_start() {
        return p_start;
    }

    public String getP_swipe_lr() {
        return p_swipe_lr;
    }

    public String getP_swipe_ud() {
        return p_swipe_ud;
    }

    public String getP_uninstall() {
        return p_uninstall;
    }

    public String getP_zoom_in() {
        return p_zoom_in;
    }

    public String getP_zoom_out() {
        return p_zoom_out;
    }

    public String getP_input() {
        return p_input;
    }

    public ArrayList<String> getRegStr() {
        return regStr;
    }

    //get()方法结束

    //匹配方法开始

    public int match(String str_case){
        int pos = 0;
        for (; pos < 11;pos++){
            if (Pattern.matches(regStr.get(pos),str_case)) {
                Pattern pattern = Pattern.compile(regStr.get(pos));
                Matcher matcher = pattern.matcher(str_case);

                if (matcher.find()){
                    if (pos <= MAX_APP_ADRESS){
                        AppName = matcher.group(1);           //App名
                        //executor.initialize(AppName);

                        return 1;
                    }else if (pos <= MAX_CLICK_ADRESS){
                        ConName = matcher.group(1);           //控件名（或者控件标志）
                        ConType = matcher.group(2);           //控件类型
                        //executor.Operation();
                        return 2;
                    }else if (pos <= MAX_SWIPE_ADRESS){
                        SwipeTo = matcher.group(1);           //滑动方向
                        return 3;
                    }else if (pos <= MAX_ZOOM_ADRESS){
                        ZoomAt  = matcher.group(1);           //缩放类型
                        return 4;
                    }else if (pos <= MAX_INPUT_ADRESS){
                        ConName = matcher.group(1);           //控件名
                        Input   = matcher.group(2);           //输入内容
                        return 5;
                    }

                }
            }
        }
        //todo throw an exception
        return 0;
    }
}
