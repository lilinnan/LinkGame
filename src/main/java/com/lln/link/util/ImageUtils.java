package com.lln.link.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/16 20:05
 */

public class ImageUtils {

    /**
     * 将图片以一个比较合适的方式进行拉伸
     *
     * @param bufferedImage 原图片
     * @param width         目标宽度
     * @param height        目标高度
     * @param x             指定x方向要拉伸的区域，每两个x值为一个区间
     * @param y             指定y方向要拉伸的区域，每两个y值为一个区间
     * @return 生成好的图片
     */
    public static BufferedImage getImage(BufferedImage bufferedImage, int width, int height, List<Integer> x, List<Integer> y) {
        if (width <= 0) {
            return bufferedImage;
        }
        BufferedImage newImage = new BufferedImage(width, bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = newImage.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());


        //先计算x变与不变的宽度
        int xNotChange = bufferedImage.getWidth();
        for (int i = 0; i < x.size(); i += 2) {
            int x1 = x.get(i);
            int x2 = x.get(i + 1);
            xNotChange -= (x2 - x1);
        }
        int xChange = bufferedImage.getWidth() - xNotChange;
        double proportionX = (width - xNotChange) * 1.0 / xChange;
        boolean flag = false;
        if (x.get(0) == 0) {
            flag = true;
        } else {
            x.add(0, 0);
        }
        if (x.get(x.size() - 1) != bufferedImage.getWidth()) {
            x.add(bufferedImage.getWidth());
        }
        //开始拉伸处理X方向的数据
        int xx = 0;
        for (int i = 0; i < x.size() - 1; i++) {
            int x1 = x.get(i);
            int x2 = x.get(i + 1);
            int w = x2 - x1;
            int h = bufferedImage.getHeight();
            BufferedImage sub = bufferedImage.getSubimage(x1, 0, w, h);
            int realWidth = (int) (flag ? w * proportionX : w);
            g.drawImage(sub, xx, 0, realWidth, h, null);
            xx += realWidth;
            flag = !flag;
        }
        //计算变与不变的区域
        int yNotChange = bufferedImage.getHeight();
        for (int i = 0; i < y.size(); i += 2) {
            int y1 = y.get(i);
            int y2 = y.get(i + 1);
            yNotChange -= (y2 - y1);
        }
        int yChange = bufferedImage.getHeight() - yNotChange;
        double proportionY = (height - yNotChange) * 1.0 / yChange;
        flag = false;
        if (y.get(0) == 0) {
            flag = true;
        } else {
            y.add(0, 0);
        }
        if (y.get(y.size() - 1) != bufferedImage.getHeight()) {
            y.add(bufferedImage.getHeight());
        }

        //再创建一张新图片
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        g = resultImage.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());

        //开始处理y方向的数据
        int yy = 0;
        for (int i = 0; i < y.size() - 1; i++) {
            int y1 = y.get(i);
            int y2 = y.get(i + 1);
            int h = y2 - y1;
            BufferedImage sub = newImage.getSubimage(0, y1, width, h);
            int realHeight = (int) (flag ? h * proportionY : h);
            g.drawImage(sub, 0, yy, width, realHeight, null);
            flag = !flag;
            yy += realHeight;
        }
        return resultImage;
    }

}
