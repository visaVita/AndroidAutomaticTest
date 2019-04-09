package GUI;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Vector;

public class MyTreeNode extends DefaultMutableTreeNode {
    private Vector userMsg;

    MyTreeNode(Object o){
        super(o);
    }

    MyTreeNode(){
        super();
    }

    public void setUserMsg(Vector userMsg) {
        this.userMsg = userMsg;
    }

    public Vector getUserMsg() {
        return userMsg;
    }
}
