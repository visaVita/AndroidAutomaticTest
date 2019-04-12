package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImgPanel extends JPanel {
    private String filePath;
    private int    width;
    private int    height;
    ImgPanel(String filePath,int width,int height){
        super();
        this.filePath = filePath;
        this.width    = width;
        this.height   = height;
    }

    ImgPanel(){
        super();
    }

    public void setImg(String filePath,int width,int height){
        this.filePath = filePath;
        this.width    = width;
        this.height   = height;
    }

    protected void paintComponent(Graphics g){//重写paintComponent方法以实现jPanel加背景
        super.paintComponent(g);
        if(filePath!=null) {
            ImageIcon image = new ImageIcon(filePath);        //获取图像
            Image img = image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

            image.setImage(img);
            image.paintIcon(this, g, (500-width)/2, 0);
        }
    }

    protected void paintDashedRect(int x,int y, int width, int height, Graphics g){
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(Color.RED);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke dash = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,3,new float[]{15,2,8,2},0);
        g2d.setStroke(dash);
        g2d.drawRect(x, y, width, height);
        g2d.dispose();
        //g2d.setColor(new Color(255,255,255,0));
    }

    protected void paintRect(int x,int y, int width,int height,Graphics g){
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.RED);
        g2d.drawRect(x, y, width, height);
        g2d.dispose();
    }

}
