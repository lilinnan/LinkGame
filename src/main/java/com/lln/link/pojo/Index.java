package com.lln.link.pojo;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/12 0:15
 */

public class Index {
    private int i;
    private int j;

    public Index(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public String toString() {
        return "Index{" +
                "i=" + i +
                ", j=" + j +
                '}';
    }

    public boolean equals(int i, int j) {
        return this.getI() == i && this.getJ() == j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Index index = (Index) o;

        if (i != index.i) {
            return false;
        }
        return j == index.j;
    }

    @Override
    public int hashCode() {
        int result = i;
        result = 31 * result + j;
        return result;
    }
}
