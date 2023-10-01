package com.lln.link.level;

import com.lln.link.pojo.Background;
import com.lln.link.pojo.GameMap;
import com.lln.link.pojo.Index;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 13:58
 */

public interface Level {

    /**
     * 返回当前关卡是第几关
     *
     * @return 第几关
     */
    int getLevelNum();

    /**
     * 每个关卡背景图片不同，在这里进行初始化
     *
     * @return 背景图片
     */
    Background getBackground();


    /**
     * 消除完成后，进行本项操作
     *
     * @param gameMap 游戏地图
     */
    void change(GameMap gameMap);


    /**
     * 返回关卡名称
     *
     * @return 关卡名称
     */
    String getTitle();


    /**
     * 获取本关卡的游戏地图
     *
     * @return 本关的游戏地图
     */
    GameMap getGameMap();

    /**
     * 获取本关的游戏时间（单位：ms）
     *
     * @return 游戏时间
     */
    int getTime();

}
