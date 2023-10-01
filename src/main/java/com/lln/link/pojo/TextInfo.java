package com.lln.link.pojo;

import com.lln.link.conf.GameConfig;

import java.awt.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/10 22:11
 */

public class TextInfo {
    private Font font;

    private Point position;

    private Color color;

    public TextInfo(Font font, Point position, Color color) {
        this.font = font;
        this.position = position;
        this.color = color;
    }

    public Font getFont() {
        return new Font(font.getName(), font.getStyle(), (int) (font.getSize() * GameConfig.PROPORTION_HEIGHT));
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    @Override
    public String toString() {
        return "TextInfo{" +
                "font=" + font +
                ", position=" + position +
                '}';
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
