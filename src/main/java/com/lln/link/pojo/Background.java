package com.lln.link.pojo;

import java.awt.*;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 15:51
 */

public class Background {
    private Point leftTop;
    private Point rightBottom;
    private Point leftTopForCurrentGroup;
    private Point rightBottomForCurrentGroup;
    private TextInfo comboText;
    private ImageInfo combo;
    private TextInfo timeText;
    private ImageInfo time;
    private TextInfo score;
    private TextInfo help;
    private TextInfo refresh;
    private TextInfo title;
    private Image image;

    public Background() {
    }

    public Point getLeftTopForCurrentGroup() {
        return leftTopForCurrentGroup;
    }

    public void setLeftTopForCurrentGroup(Point leftTopForCurrentGroup) {
        this.leftTopForCurrentGroup = leftTopForCurrentGroup;
    }

    public Point getRightBottomForCurrentGroup() {
        return rightBottomForCurrentGroup;
    }

    public void setRightBottomForCurrentGroup(Point rightBottomForCurrentGroup) {
        this.rightBottomForCurrentGroup = rightBottomForCurrentGroup;
    }

    public Point getLeftTop() {
        return leftTop;
    }

    public void setLeftTop(Point leftTop) {
        this.leftTop = leftTop;
    }

    public Point getRightBottom() {
        return rightBottom;
    }

    public void setRightBottom(Point rightBottom) {
        this.rightBottom = rightBottom;
    }

    public TextInfo getComboText() {
        return comboText;
    }

    public void setComboText(TextInfo comboText) {
        this.comboText = comboText;
    }

    public ImageInfo getCombo() {
        return combo;
    }

    public void setCombo(ImageInfo combo) {
        this.combo = combo;
    }

    public TextInfo getTimeText() {
        return timeText;
    }

    public void setTimeText(TextInfo timeText) {
        this.timeText = timeText;
    }

    public ImageInfo getTime() {
        return time;
    }

    public void setTime(ImageInfo time) {
        this.time = time;
    }

    public TextInfo getScore() {
        return score;
    }

    public void setScore(TextInfo score) {
        this.score = score;
    }

    public TextInfo getHelp() {
        return help;
    }

    public void setHelp(TextInfo help) {
        this.help = help;
    }

    public TextInfo getRefresh() {
        return refresh;
    }

    public void setRefresh(TextInfo refresh) {
        this.refresh = refresh;
    }

    public TextInfo getTitle() {
        return title;
    }

    public void setTitle(TextInfo title) {
        this.title = title;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
