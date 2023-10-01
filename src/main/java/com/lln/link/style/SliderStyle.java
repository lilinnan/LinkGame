package com.lln.link.style;

import com.lln.link.pojo.Point;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/13 23:57
 */

public class SliderStyle extends BasicSliderUI {
    private Color leftColor = new Color(0xcb2121);
    private Color rightColor = new Color(0xabaaaa);
    private Color buttonColor = new Color(0x70a3d1);

    public SliderStyle(JSlider b) {
        super(b);
        b.setOpaque(false);
        b.setFocusable(false);
    }


    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        g2d.setColor(buttonColor);
        g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Rectangle trackBounds = trackRect;

        //这个是轨道长度
        int trackWidth = trackBounds.width;
        //这个是圆角半径
        int d = thumbRect.height / 2;
        int r = d / 2;
        //将开始位置移动到轨道高度的1/4处，目的是为了画一个直径为指示物一半的⚪
        g2d.translate(trackBounds.x, trackBounds.height / 4);
        //开始画左边
        //先设置颜色
        g2d.setColor(leftColor);
        //画圆角
        g2d.fillOval(0, 0, d, d);
        //画长条
        //用当前标识物位置+标识物宽度的一半-开始位置-偏移数据
        int leftRectWidth = thumbRect.x + thumbRect.width / 2 - r - trackBounds.x;
        g2d.fillRect(r, 0, leftRectWidth, d);

        //开始画右边
        //先设置颜色
        g2d.setColor(rightColor);
        g2d.fillOval(trackWidth - d, 0, d, d);

        int rightRectX = thumbRect.x + thumbRect.width / 2 - trackBounds.x;
        int rightRectWidth = trackBounds.width - rightRectX - r;
        g2d.fillRect(rightRectX, 0, rightRectWidth, d);

        g2d.translate(-trackBounds.x, -trackBounds.height / 4);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(slider.getHeight(), slider.getHeight());
    }

}