package com.lln.link.pojo;

import com.lln.link.conf.GameConfig;

/**
 * 这是一个点，包含x与y，可以存放一些坐标
 *
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 21:27
 */

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return (int) (x * GameConfig.PROPORTION_WIDTH);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return (int) (y * GameConfig.PROPORTION_HEIGHT);
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        if (x != point.x) {
            return false;
        }
        return y == point.y;
    }

    public boolean equals(int i, int j) {
        return this.getX() == i && this.getY() == j;
    }


    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
