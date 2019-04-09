package demo;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class dragDemo extends JFrame{
    private static final long serialVersionUID = 1L;
    private static String a;
    private JTextField field;
    public dragDemo(){

        this.setTitle("拖拽文件至文本框显示文件路径");
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea LogField = new JTextArea("a");
        LogField.setLineWrap(true);
        LogField.setWrapStyleWord(true);

        JScrollPane p = new JScrollPane();
        p.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p.setViewportView(LogField);
        p.setBounds(50,100,300,100);
        this.add(p);

        field = new JTextField();
        field.setBounds(50, 50, 300, 30);

        field.setTransferHandler(new TransferHandler()
        {
            private static final long serialVersionUID = 1L;
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
                    if (filepath.endsWith(".doc")||filepath.endsWith(".docx")) {
                        System.out.println(filepath);
                        field.setText(filepath);
                        return true;
                    }else {
                        field.setText("不支持的文件类型");
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

        this.add(field);
        this.setVisible(true);
        for (int i = 0;i< 30;i++){
            LogField.append(i+"：aaa\n\r");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {

        //new dragDemo();

        System.out.println(a==null);
        a = "";
        System.out.println(a.isEmpty());
    }

}
