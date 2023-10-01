package com.lln.link.pojo;

/**
 * 这是一个块
 *
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 13:32
 */

public class Block {
    private int type;
    private boolean select;
    private boolean clear;


    public Block(int type) {
        this.type = type;
        this.select = false;
        this.clear = false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    @Override
    public String toString() {
        return "Block{" +
                "type=" + type +
                ", select=" + select +
                ", clear=" + clear +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Block block = (Block) o;
        return type == block.type;
    }

    @Override
    public int hashCode() {
        return type;
    }
}
