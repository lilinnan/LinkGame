package com.lln.link.panel;

import com.lln.link.Context;

import javax.swing.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/12 14:26
 */

public abstract class AbstractPanel extends JPanel {
    protected Context context;

    private static boolean isFullScreen;

    private static int prevX;
    private static int prevY;
    private static int prevWidth;
    private static int prevHeight;

    public AbstractPanel(Context context) {
        this.context = context;
    }


    /**
     * 屏幕大小变化时，需要重新计算某些参数
     * 在这里计算，以便于调用
     */
    protected abstract void computeParam();

    /**
     * 全屏
     */
    protected void fullScreen() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (isFullScreen) {
            frame.setBounds(prevX, prevY, prevWidth, prevHeight);
            frame.dispose();
            frame.setUndecorated(false);
            frame.setVisible(true);
            isFullScreen = false;
        } else {
            prevX = frame.getX();
            prevY = frame.getY();
            prevWidth = frame.getWidth();
            prevHeight = frame.getHeight();
            frame.dispose();
            frame.setUndecorated(true);
            frame.setBounds(0, 0, getToolkit().getScreenSize().width, getToolkit()
                    .getScreenSize().height);
            frame.setVisible(true);
            isFullScreen = true;
        }
        this.requestFocus();
    }
}
