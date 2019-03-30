package GUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JLabel      TestCaseLabel;
    private JLabel      TestAppLabel;
    private JLabel      ImgLabel;

    private JTextArea   LogField;
    private JScrollPane LogScrollPane;
    private JPanel      BtnPanel;

    private JButton     ResBtn;
    private JButton     FinBtn;
    private JButton     RollBackBtn;

    private String      AppName;
    private String      AppPath;
    private String      CaseName;
    private String      CasePath;
    public MainFrame(){
        this.setTitle("测试系统");
        this.setSize(800,600);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setBackground(new Color(0xffffff));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImgLabel = new JLabel();
        ImgLabel.setPreferredSize(new Dimension(800,150));
        //ImgLabel.setBorder(new RoundBorder(new Color(192,192,192),1,true));
        ImgLabel.setIcon(new ImageIcon("src\\res\\water.jpg"));

        LogField = new JTextArea("日志区域\n\r");
        LogField.setEditable(false);

        LogField.setFont(new Font("宋体",Font.PLAIN,18));
        LogField.setBorder(new RoundBorder(new Color(192,192,192),1,false));
        LogField.setBounds(0,0,800,300);
        CreatePanel();

        LogScrollPane = new JScrollPane();

        LogScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        LogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        LogScrollPane.setPreferredSize(new Dimension(800,300));

        LogScrollPane.setViewportView(LogField);

        this.add(ImgLabel,BorderLayout.NORTH);
        this.add(BtnPanel,BorderLayout.CENTER);
        this.add(LogScrollPane,BorderLayout.SOUTH);

        this.setVisible(true);

    }

    private void CreatePanel(){

        ImageIcon CaseIcon     = new ImageIcon("src\\res\\plus-circle-green.png");
        ImageIcon AppIcon      = new ImageIcon("src\\res\\plus-circle.png");
        ImageIcon ResIcon      = new ImageIcon("src\\res\\search.png");
        ImageIcon FinIcon      = new ImageIcon("src\\res\\play-circle.png");
        ImageIcon RollBackIcon = new ImageIcon("src\\res\\close-circle.png");

        TestCaseLabel = new JLabel(CaseIcon);
        TestAppLabel  = new JLabel(AppIcon);
        ResBtn        = new JButton(ResIcon);
        FinBtn        = new JButton(FinIcon);
        RollBackBtn   = new JButton(RollBackIcon);

        TestCaseLabel.setToolTipText("拖动用例Excel文件到这里");
        TestAppLabel.setToolTipText("拖动待测试应用的.apk文件到这里");
        ResBtn.setToolTipText("获取测试结果");
        FinBtn.setToolTipText("完成配置，开始测试");
        RollBackBtn.setToolTipText("回滚配置");

        TestCaseLabel.setBorder(new RoundBorder(new Color(192,192,192),1,true));
        TestAppLabel.setBorder(new RoundBorder(new Color(192,192,192),1,true));
        ResBtn.setBorder(new RoundBorder(new Color(192,192,192),1,true));
        FinBtn.setBorder(new RoundBorder(new Color(192,192,192),1,true));
        RollBackBtn.setBorder(new RoundBorder(new Color(192,192,192),1,true));
        /*
        TestCaseLabel.setBackground(new Color(0xffffff));
        TestAppLabel.setBackground(new Color(0xffffff));
        ResBtn.setBackground(new Color(0xffffff));
        FinBtn.setBackground(new Color(0xffffff));
        RollBackBtn.setBackground(new Color(0xffffff));
        */
        ResBtn.setContentAreaFilled(false);
        FinBtn.setContentAreaFilled(false);
        RollBackBtn.setContentAreaFilled(false);

        TestCaseLabel.setBounds(32,20,95,95);
        TestAppLabel.setBounds(192,20,95,95);
        FinBtn.setBounds(352,20,95,95);
        RollBackBtn.setBounds(512,20,95,95);
        ResBtn.setBounds(672,20,95,95);

        ResBtn.addActionListener(new ResBtnListener());
        FinBtn.addActionListener(new FinBtnListener());
        RollBackBtn.addActionListener(new RollBackBtnListener());

        InitAdapter();

        BtnPanel = new JPanel();
        BtnPanel.setLayout(null);
        BtnPanel.setPreferredSize(new Dimension(800,150));
        BtnPanel.setBackground(new Color(0xffffff));
        BtnPanel.add(TestAppLabel);
        BtnPanel.add(TestCaseLabel);
        BtnPanel.add(FinBtn);
        BtnPanel.add(RollBackBtn);
        BtnPanel.add(ResBtn);

    }

    private void InitAdapter(){
        TestCaseLabel.setTransferHandler(new TransferHandler(){
            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    System.out.println(filepath);
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    if (filepath.endsWith(".xls")||filepath.endsWith(".xlsx")) {
                        CasePath = filepath;
                        CaseName = filepath.substring(filepath.lastIndexOf("\\")+1,filepath.length());
                        System.out.println(AppPath);
                        updateTextArea("添加用例："+CaseName);
                        return true;
                    }else {
                        updateTextArea("不支持的文件类型，需要.xls或者.xlsx文件");
                        return false;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {

                for (int i = 0; i < flavors.length; i++) {
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });
        TestAppLabel.setTransferHandler(new TransferHandler(){
            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    System.out.println(filepath);
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    if (filepath.endsWith(".apk")) {
                        AppPath = filepath;
                        AppName = filepath.substring(filepath.lastIndexOf("\\")+1,filepath.length());
                        System.out.println(AppPath);

                        updateTextArea("添加App："+AppName);
                        return true;
                    }else {
                        updateTextArea("不支持的文件类型,需要.apk文件");
                        return false;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {

                for (int i = 0; i < flavors.length; i++) {
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void updateTextArea(String s){
        LogField.append(s+"\n\r");
        LogField.setCaretPosition(LogField.getDocument().getLength());
    }

    private class ResBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //todo
        }
    }

    private class FinBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            //先判断配置是否已经完成，在判断是否合法
        }
    }

    private class RollBackBtnListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            AppName  = null;
            AppPath  = null;
            CaseName = null;
            CasePath = null;
            updateTextArea("配置已经清空\n\r");
        }
    }

    public static void main(String[] args){
        new MainFrame();
    }

}
