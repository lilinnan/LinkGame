package com.lln.link.level;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 19:28
 */

public class LevelGenerator {

    public static Level getLevel(int level) {
        if (level == 1) {
            return new SimpleLevel();
        }
        return new SimpleLevel2();
    }
}
