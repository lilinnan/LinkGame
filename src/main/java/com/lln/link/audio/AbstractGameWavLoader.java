package com.lln.link.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 14:22
 */

public abstract class AbstractGameWavLoader {


    public static AudioInputStream load(String name) {
        URL url = AbstractGameWavLoader.class.getResource("/com/lln/link/audio/res/" + name + ".wav");
        try {
            return AudioSystem.getAudioInputStream(url);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
