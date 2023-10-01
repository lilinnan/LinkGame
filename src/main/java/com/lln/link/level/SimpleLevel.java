package com.lln.link.level;

import com.lln.link.image.AbstractGameImageLoader;
import com.lln.link.pojo.Point;
import com.lln.link.pojo.*;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 14:01
 */

public class SimpleLevel implements Level {

    private Background background;

    private GameMap gameMap;

    private HashMap<Integer, HashSet<Index>> walls;

    public SimpleLevel() {
        initBackground();
        initWalls();
        initGameMap();
    }


    private void initBackground() {
        Color color = Color.WHITE;
        background = new Background();
        //左上
        background.setLeftTop(new Point(98, 494));
        //右下
        background.setRightBottom(new Point(3065, 1784));

        background.setLeftTopForCurrentGroup(new Point(458, 30));
        background.setRightBottomForCurrentGroup(new Point(860, 234));

        //连击字体
        background.setComboText(new TextInfo(new Font("微软雅黑", Font.BOLD, 75), new Point(956, 152), color));
        //连击条
        background.setCombo(new ImageInfo(new Point(1182, 60), new Point(1133, 95), AbstractGameImageLoader.getImage("bar")));
        //时间条
        background.setTime(new ImageInfo(new Point(2363, 60), new Point(1133, 173), AbstractGameImageLoader.getImage("bar")));
        //时间字体
        background.setTimeText(new TextInfo(new Font("微软雅黑", Font.BOLD, 75), new Point(956, 236), color));
        //得分
        background.setScore(new TextInfo(new Font("微软雅黑", Font.BOLD, 75), new Point(3593, 152), color));
        background.setHelp(new TextInfo(new Font("微软雅黑", Font.BOLD, 75), new Point(3593, 239), color));
        background.setRefresh(new TextInfo(new Font("微软雅黑", Font.BOLD, 75), new Point(3983, 239), color));
        background.setImage(AbstractGameImageLoader.getImage("background"));
    }

    private void initWalls() {
    }

    private void initGameMap() {
        gameMap = new GameMap(9, 4, walls);
    }


    @Override
    public String getTitle() {
        return "大学第一年";
    }

    @Override
    public int getLevelNum() {
        return 1;
    }

    @Override
    public Background getBackground() {
        return background;
    }

    @Override
    public GameMap getGameMap() {
        return gameMap;
    }

    @Override
    public int getTime() {
        return 10 * 60;
    }

    /**
     * 不进行转换
     *
     * @param gameMap 游戏地图
     */
    @Override
    public void change(GameMap gameMap) {

    }
}
