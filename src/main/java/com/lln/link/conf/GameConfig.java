package com.lln.link.conf;

import com.lln.link.image.AbstractGameImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 15:43
 */

public class GameConfig {

    /**
     * 最大类型数量，即，每个块的图片有几种
     */
    public static final int MAX_TYPE = 16;
    /**
     * 最大关卡数量
     */
    public static final int MAX_LEVEL = 2;
    public static final Color FILL_GRAY_COLOR = new Color(156, 156, 156, 113);
    public static final Color FILL_RED_COLOR = new Color(0, 255, 0, 113);

    /**
     * 选多少个块就可以启动连接
     */
    public static final int SELECT_OK_SIZE = 2;


    public static int WINDOW_WIDTH = 192 * 6;
    public static int WINDOW_HEIGHT = 108 * 6;
    public static double PROPORTION_WIDTH;
    public static double PROPORTION_HEIGHT;


    static {
        change();
    }


    public static void change(int width, int height) {
        WINDOW_WIDTH = width;
        WINDOW_HEIGHT = height;
        change();
    }

    private static void change() {
        BufferedImage bg = (BufferedImage) AbstractGameImageLoader.getImage("background");
        PROPORTION_WIDTH = WINDOW_WIDTH * 1.0 / bg.getWidth();
        PROPORTION_HEIGHT = WINDOW_HEIGHT * 1.0 / bg.getHeight();
    }
}
