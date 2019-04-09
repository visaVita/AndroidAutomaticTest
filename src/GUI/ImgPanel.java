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
}
