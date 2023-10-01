package com.lln.link;

import com.lln.link.audio.AbstractGameWavLoader;
import com.lln.link.frame.AppFrame;
import com.lln.link.thread.GameThreadFactory;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 13:09
 */

public class Context {

    /**
     * 本游戏的线程池
     */
    private final ThreadPoolExecutor gameThreadPool;
    private final AppFrame appFrame;

    private Clip bgm;
    private int bgmSoundSize = 65;
    private int effectSoundSize = 65;
    private FloatControl gainControl;


    private final Stack<String> panelName;

    public Context() {
        gameThreadPool = new ThreadPoolExecutor(5, 5, 1000,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), new GameThreadFactory("连连看"));
        appFrame = new AppFrame(this);
        panelName = new Stack<>();
        showStart();
    }

    public void showGame(boolean flag) {
        appFrame.showGame(flag);
        panelName.push("game");
        startBgm(Res.Audio.GAME_BGM);
    }


    public void showStart() {
        appFrame.showStart();
        panelName.push("start");
        startBgm(Res.Audio.START_BGM);
    }

    public void showSetting() {
        appFrame.showSetting();
        panelName.push("setting");
    }

    public ThreadPoolExecutor getGameThreadPool() {
        return gameThreadPool;
    }

    public void toLastPanel() {
        panelName.pop();
        switch (panelName.pop()) {
            case "game":
                showGame(false);
                return;
            case "start":
                showStart();
                return;
            case "setting":
                showSetting();
                break;
            default:
                break;
        }
    }



    /*
     *下面的部分是关于音乐控制的
     */


    public void startBgm(String name) {
        if (bgm == null) {
            try {
                bgm = AudioSystem.getClip();
                bgm.open(AbstractGameWavLoader.load(name));
                gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(getBgmSoundValue());
                bgm.loop(Integer.MAX_VALUE);
            } catch (LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        } else {
            if (bgm.isRunning()) {
                bgm.stop();
            }
            bgm = null;
            startBgm(name);
        }
    }

    public int getBgmSoundSize() {
        return bgmSoundSize;
    }

    public void setBgmSoundSize(int size) {
        bgmSoundSize = size;
        if (bgm != null && gainControl != null) {
            gainControl.setValue(getBgmSoundValue());
        }
    }

    private float getBgmSoundValue() {
        if (bgm == null || gainControl == null) {
            return 0f;
        }
        float max = gainControl.getMaximum();
        float min = gainControl.getMinimum();
        return (max - min) / 100 * bgmSoundSize + min;
    }


    public void startEffect(String effectName) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AbstractGameWavLoader.load(effectName));
            FloatControl effectControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float max = effectControl.getMaximum();
            float min = effectControl.getMinimum();
            effectControl.setValue((max - min) / 100 * effectSoundSize + min);
            clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }


    public int getEffectSoundSize() {
        return effectSoundSize;
    }

    public void setEffectSoundSize(int effectSoundSize) {
        this.effectSoundSize = effectSoundSize;
    }
}
