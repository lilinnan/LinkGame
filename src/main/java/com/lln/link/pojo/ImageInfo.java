package com.lln.link.pojo;

import java.awt.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/10 22:24
 */

public class ImageInfo {
    private Point size;
    private Point position;
    private Image image;

    public ImageInfo(Point size, Point position, Image image) {
        this.size = size;
        this.position = position;
        this.image = image;
    }


    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getWidth() {
        return size.getX();
    }

    public int getHeight() {
        return size.getY();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "size=" + size +
                ", position=" + position +
                ", image=" + image +
                '}';
    }
}
