package GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTreeNode extends DefaultMutableTreeNode {

    private final Map<String,String> mAttributes = new LinkedHashMap<>();

    private static Pattern BoundPattern = Pattern.compile("\\[-?(\\d+),-?(\\d+)\\]\\[-?(\\d+),-?(\\d+)\\]");
    public int x,y,width,height;

    public int getWidth() {
        return width;
    }

    boolean hasBound = false;
    private String displayName;


    MyTreeNode(Object o){
        super(o);
    }

    MyTreeNode(){
        super();
    }

    //在添加完属性后，必须调用setName()方法
    public void addAttr(String key,String value) {
        mAttributes.put(key,value);
        if (key.equals("bounds")){
            setBounds(value);
        }
    }

    public void setDisplayName(){
        String index,content_desc,className,text,bound;
        String dispalyName = "";
        if (mAttributes.containsKey("index")&&!(mAttributes.get("index").isEmpty())){
            dispalyName = "(" + mAttributes.get("index") + ")";
        }
        if (mAttributes.containsKey("class")&&!(mAttributes.get("class").isEmpty())){
            String c = mAttributes.get("class");
            c = c.replace("android.widget.","");
            c = c.replace("android.view.","");
            dispalyName = dispalyName + c;
        }
        if (mAttributes.containsKey("text")&&!(mAttributes.get("text").isEmpty())){
            dispalyName = dispalyName + ":" + mAttributes.get("text");
        }
        if (mAttributes.containsKey("content-desc")&&!(mAttributes.get("content-desc").isEmpty())){
            dispalyName = dispalyName + "{" + mAttributes.get("content-desc") + "}";
        }
        if (hasBound){
            dispalyName = dispalyName + " " + mAttributes.get("bounds");
        }
        this.displayName = dispalyName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public Map<String, String> getmAttributes() {
        return mAttributes;
    }

    public void setBounds(String bounds){
        Matcher m = BoundPattern.matcher(bounds);
        if (m.matches()){
            x        = Integer.parseInt(m.group(1));
            y        = Integer.parseInt(m.group(2));
            width    = Integer.parseInt(m.group(3)) - x;
            height   = Integer.parseInt(m.group(4)) - y;
            hasBound = true;
        }
        else throw new RuntimeException("非法的控件边界");
    }


}
