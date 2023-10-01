package com.lln.link.component;

import com.lln.link.image.AbstractGameImageLoader;
import com.lln.link.style.ButtonStyle;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * 要使用这个按钮，需要传入的参数为图片的前缀名称
 * <p>
 * 后缀分别为：
 * 正常情况：       前缀_simple.png
 * 鼠标放在上面情况：前缀_over.png
 * 鼠标按下去的情况：前缀_press.png
 *
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/15 17:15
 */

public class ImageButton extends JButton {
    private BufferedImage simpleImage;
    private BufferedImage rollOverImage;
    private BufferedImage pressImage;
    private String prefix;

    public ImageButton(String prefix) {
        //这个按钮不需要其他的内容，文字也是多余的
        super();
        this.prefix = prefix;
        init();
    }

    public BufferedImage getSimpleImage() {
        return simpleImage;
    }

    public BufferedImage getRollOverImage() {
        return rollOverImage;
    }

    public BufferedImage getPressImage() {
        return pressImage;
    }

    private void init() {
        //取消焦点获取
        setFocusable(false);
        //初始化图片
        simpleImage = AbstractGameImageLoader.getImage(prefix + "_simple");
        rollOverImage = AbstractGameImageLoader.getImage(prefix + "_over");
        pressImage = AbstractGameImageLoader.getImage(prefix + "_press");
        setUI(new ButtonStyle());
        setOpaque(false);
    }

    @Override
    public boolean contains(int x, int y) {
        if (super.contains(x, y)) {
            //如果，在范围内
            //这时拿到了坐标
            //需要根据按钮与图片比例转换
            int imageWidth = simpleImage.getWidth();
            int imageHeight = simpleImage.getHeight();
            int imageX = (int) (imageWidth * 1.0 / getWidth() * x);
            int imageY = (int) (imageHeight * 1.0 / getHeight() * y);
            int color = simpleImage.getRGB(imageX, imageY);
            return ((color >> 24) & 0XFF) != 0;
        }
        return false;
    }
}
