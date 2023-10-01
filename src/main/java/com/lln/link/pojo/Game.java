package com.lln.link.pojo;

import com.lln.link.conf.GameConfig;
import com.lln.link.level.Level;
import com.lln.link.level.LevelGenerator;

/**
 * 这是一局游戏
 *
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 21:32
 */

public class Game {
    /**
     * 分数
     */
    private int score;

    /**
     * 生命
     */
    private int life;


    /**
     * 提示数量
     */
    private int help;

    /**
     * 洗牌数量
     */
    private int refresh;

    /**
     * 关卡
     */
    private Level level;


    /**
     * 当前剩余时间
     */
    private int time;


    /**
     * current combo
     * every cleared add 5
     * max 100
     * min 0
     */
    private int combo;


    /**
     * 默认构造方法创建的关卡是第一关
     */
    public Game() {
        level = LevelGenerator.getLevel(1);
        refresh = 5;
        help = 5;
        initParam();
    }

    private void initParam() {
        time = level.getTime();
    }


    public boolean toNextLevel() {
        int levelNum = level.getLevelNum();
        if (levelNum >= GameConfig.MAX_LEVEL) {
            return false;
        }
        levelNum++;
        level = LevelGenerator.getLevel(levelNum);
        initParam();
        return true;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getHelp() {
        return help;
    }

    public void setHelp(int help) {
        this.help = help;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getRefresh() {
        return refresh;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void addTime(int num) {
        this.time += num;
    }

    public void addRefresh(int num) {
        this.refresh += num;
    }

    public void addHelp(int num) {
        this.help += num;
    }

    public void addCombo(int num) {
        combo += num;
        if (combo < 0) {
            combo = 0;
        }
        if (combo > 100) {
            combo = 100;
        }
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public void addScore(int num) {
        score += num;
        if (score < 0) {
            score = 0;
        }
    }
}
