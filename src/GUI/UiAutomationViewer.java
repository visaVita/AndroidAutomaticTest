package GUI;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import regex.processMatch;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class UiAutomationViewer extends JFrame implements TreeSelectionListener {
    private JLayeredPane  ShowDump;
    private JLayeredPane  TreePane;
    private JPanel        Blank_panel;
    private ImgPanel      Dump_panel;
    private JPanel        NorthPanel;
    private JPanel        EastPanel;
    private JScrollPane   cPane;
    private JScrollPane   attrPane;
    private JScrollPane   CasePane;
    private JTree         cTree;
    private JTable        aTable;

    private JToolBar      toolBar;
    private JButton       dumpBtn;
    private JButton       saveBtn;

    private DefaultMutableTreeNode rootTreeNode;

    private Vector headVector;

    private processMatch  process;

    private String fileName;

    private UiAutomationViewer(){
        process = new processMatch();
        CreateFrame();
    }

    private void CreateFrame(){

        this.setTitle("生成用例");
        this.setSize(830,730);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setBackground(new Color(0xffffff));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        toolBar = new JToolBar();
        dumpBtn = new JButton(new ImageIcon("res\\file-image.png"));
        dumpBtn.addActionListener(new dumpBtnListener());
        dumpBtn.setPreferredSize(new Dimension(32,32));
        saveBtn = new JButton(new ImageIcon("res\\save.png"));
        saveBtn.addActionListener(new saveBtnListener());
        saveBtn.setPreferredSize(new Dimension(32,32));

        toolBar.add(dumpBtn);
        toolBar.add(saveBtn);
        toolBar.setPreferredSize(new Dimension(800,32));
        toolBar.setFloatable(false);

        headVector = new Vector();
        headVector.add(0,"属性名");
        headVector.add(1,"属性值");

        NorthPanel = new JPanel(new BorderLayout());
        NorthPanel.setPreferredSize(new Dimension(830,500));
        EastPanel  = new JPanel(new BorderLayout());
        EastPanel.setPreferredSize(new Dimension(300,500));

        ShowDump = new JLayeredPane();
        ShowDump.setPreferredSize(new Dimension(500,500));

        Blank_panel = new JPanel();
        Blank_panel.setPreferredSize(new Dimension(500,500));
        Blank_panel.setOpaque(false);
        Blank_panel.setBounds(0,0,500,500);

        Dump_panel = new ImgPanel();
        //Dump_panel.setPreferredSize(new Dimension(500,500));
        Dump_panel.setBounds(0,0,500,500);

        /*
        TreePane = new JLayeredPane();
        TreePane.setPreferredSize(new Dimension(300,300));
        TreePane.add(cTree,100,0);
        */
        //cTree = new JTree();
        cPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cPane.setPreferredSize(new Dimension(300,170));
        cPane.setViewportView(new JLabel("树"));

        attrPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        attrPane.setPreferredSize(new Dimension(300,300));
        attrPane.setViewportView(new JLabel("属性"));

        ShowDump.add(Blank_panel,new Integer(200));
        ShowDump.add(Dump_panel,new Integer(100));

        CasePane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        CasePane.setPreferredSize(new Dimension(800,200));
        CasePane.setViewportView(new JLabel("用例"));

        NorthPanel.add(Dump_panel,BorderLayout.CENTER);
        NorthPanel.add(EastPanel,BorderLayout.EAST);

        EastPanel.add(attrPane,BorderLayout.SOUTH);
        EastPanel.add(cPane,BorderLayout.CENTER);

        this.add(toolBar,BorderLayout.NORTH);
        this.add(NorthPanel,BorderLayout.CENTER);
        this.add(CasePane,BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void showScreenCap(){

        if (fileName!=null){

        }

        String fileLoc = "G:\\Android_Automatic_Testing\\screen_dump\\";
        Date date = new Date();
        String[] dim = {""};
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");
        fileName = format.format(date);
        int a = 0;
        try {
            if (!process.getAdbState()) {
                System.out.println("连接失败。弹出错误");
                return;
            }
            dim = process.getResolution();
            process.getScreenDump(fileName);
            process.getSreenCap(fileName);
            while (!processMatch.isExist(fileLoc + fileName + ".png") && !processMatch.isExist(fileLoc + fileName + ".xml")) {
                Thread.sleep(2000);
                if (++a == 10) ;
                //todo throw an exception
                break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int imgWidth = (int)getImgSize(dim);
        if (imgWidth==0.0){
            //System.out.println(dim[0]+"    "+dim[1]);
            System.out.println("计算宽度失败");
        }

        Dump_panel.removeAll();
        Dump_panel.setImg("G:\\Android_Automatic_Testing\\screen_dump\\"+fileName+".png",imgWidth,500);
        Dump_panel.paintComponent(Dump_panel.getGraphics());

        this.getContentPane().validate();
    }

    private double getImgSize(String[] dim){
        if (dim.length!=2)
            return 0.0;
        double w = Double.parseDouble(dim[0]);
        double h = Double.parseDouble(dim[1]);
        double width;
        if (h>w)
            width = w*(500/h);
        else
            width = 500;
        //todo 这里应该返回一个二元组，表示宽高，以解决横屏时截图显示不正确的问题
        return width;
    }

    private void buildTree(){

        rootTreeNode = new DefaultMutableTreeNode(" ");

        SAXReader reader = new SAXReader();

        try {
            Document document = reader.read(new File("G:\\Android_Automatic_Testing\\screen_dump\\"+fileName+".xml"));
            Element root = document.getRootElement();
            generateTree(root,rootTreeNode);
            cTree = new JTree(rootTreeNode);
            cTree.setBounds(10,0,300,170);
            cTree.setBorder(new RoundBorder(new Color(19,192,192),1,false));
            cTree.addTreeSelectionListener(this::valueChanged);
            cTree.setRootVisible(false);
            //EastPanel.remove(cPane);
            //EastPanel.add(cTree,BorderLayout.CENTER);
            cPane.setViewportView(cTree);

            cPane.validate();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void generateTree(Element e,DefaultMutableTreeNode parentNode){
        List<Attribute> listAttr=e.attributes();//当前节点的所有属性的list

        MyTreeNode TreeNode = new MyTreeNode();
        Map<String,String> attributes = new LinkedHashMap<>();

        //Map<String,String> attrMap = new HashMap<String,String>();
        for(Attribute attr:listAttr){//遍历当前节点的所有属性
            String name=attr.getName();//属性名称
            String value=attr.getValue();//属性的值
            TreeNode.addAttr(name,value);
            //attrMap.put(name,value);
        }
        //添加完属性后，必须执行setDisplayName()方法
        TreeNode.setDisplayName();
        parentNode.add(TreeNode);

        //递归遍历当前节点所有的子节点
        List<Element> listElement=e.elements();//所有一级子节点的list
        for(Element node:listElement){//遍历所有一级子节点
            if (node==null)
                continue;
            generateTree(node,TreeNode);//递归
        }

    }

    private void visitAllNodes(MyTreeNode node,int[] d,Point point){
        if (node.getChildCount()>0){
            for (Enumeration e = node.children();e.hasMoreElements();){
                MyTreeNode n = (MyTreeNode) e.nextElement();
                if (n.hasBound){
                    if (n.x<point.x){

                    }
                }
                visitAllNodes(n,d,point);
            }
        }
    }

    private MyTreeNode FindNode(MyTreeNode node,Point point){
        if (node.getChildCount()>0){
            for (Enumeration e = node.children();e.hasMoreElements();){
                MyTreeNode n = (MyTreeNode) e.nextElement();
                //if (n.hasBound){
                //    return n;
                //}
                FindNode(n,point);
            }
        }
        return null;
    }

    public static void main(String[] args){
        UiAutomationViewer a = new UiAutomationViewer();

    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (e.getSource()==cTree){
            MyTreeNode node = (MyTreeNode) cTree.getLastSelectedPathComponent();


            aTable = new JTable(new MyTableModel((LinkedHashMap<String, String>) node.getmAttributes()));

            aTable.setRowHeight(30);
            aTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            aTable.getColumnModel().getColumn(1).setPreferredWidth(250);
            //aTable.setPreferredScrollableViewportSize(new Dimension(300,300));
            attrPane.setViewportView(aTable);
        }
        attrPane.validate();
    }

    private class saveBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //todo
        }
    }

    private class dumpBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                if(process.getAdbState()){
                    System.out.println("进入截图");
                    showScreenCap();
                    buildTree();
                }else {
                    //todo throw an exception
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class myMouseListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            MyTreeNode treeNode = (MyTreeNode)cTree.getModel().getRoot();

            FindNode(treeNode,e.getPoint());
            //if (e.getX())
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}

