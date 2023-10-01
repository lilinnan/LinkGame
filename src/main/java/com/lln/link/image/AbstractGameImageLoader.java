package com.lln.link.image;

import com.lln.link.conf.GameConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 14:22
 */

public abstract class AbstractGameImageLoader {

    /**
     * 将读取到的文件存起来，减少读文件消耗的时间
     */
    private static final HashMap<String, BufferedImage> CACHE = new HashMap<>();

    private static BufferedImage loadImage(String imageName) {
        BufferedImage image = CACHE.get(imageName);
        if (image != null) {
            return image;
        }
        URL url = AbstractGameImageLoader.class.getResource("/com/lln/link/image/res/" + imageName);
        try {
            BufferedImage bufferedImage = ImageIO.read(url);
            CACHE.put(imageName, bufferedImage);
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static BufferedImage getImage(String fileName, String suffix) {
        return loadImage(fileName + "." + suffix);
    }


    public static BufferedImage getImage(String fileName) {
        return getImage(fileName, "png");
    }


    /**
     * 这个是加载方块图片的
     *
     * @param num 图片ID
     * @return 图片
     */
    public static BufferedImage getImage(int num) {
        if (num > GameConfig.MAX_TYPE) {
            num = GameConfig.MAX_TYPE;
        }
        return getImage(Integer.toString(num), "png");
    }
}
