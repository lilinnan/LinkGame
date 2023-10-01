package com.lln.link.panel;

import com.lln.link.Context;
import com.lln.link.Res;
import com.lln.link.component.ImageButton;
import com.lln.link.conf.GameConfig;
import com.lln.link.image.AbstractGameImageLoader;
import com.lln.link.pojo.ImageInfo;
import com.lln.link.pojo.Point;
import com.lln.link.style.SliderStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/13 17:12
 */

public class SettingPanel extends AbstractPanel implements KeyListener {

    private JSlider effect = new JSlider();
    private JSlider music = new JSlider();
    private JButton back = new ImageButton("bt");
    private ImageInfo s1 = new ImageInfo(new Point(500, 50), new Point(300, 270), null);
    private ImageInfo s2 = new ImageInfo(new Point(500, 50), new Point(300, 440), null);
    private ImageInfo s3 = new ImageInfo(new Point(200, 60), new Point(0, 0), null);

    public SettingPanel(Context context) {
        super(context);
        init();
        addKeyListener(this);
    }

    private void init() {
        setLayout(null);
        effect.setUI(new SliderStyle(effect));
        music.setUI(new SliderStyle(music));
        music.addChangeListener(e -> context.setBgmSoundSize(music.getValue()));
        effect.addChangeListener(e -> {
            context.setEffectSoundSize(effect.getValue());
            context.startEffect(Res.Audio.SLIDER_CHANGE);
        });
        add(back);
        add(effect);
        add(music);
        computeParam();
    }

    @Override
    public void computeParam() {
        effect.setBounds(s1.getX(), s1.getY(), s1.getWidth(), s1.getHeight());
        music.setBounds(s2.getX(), s2.getY(), s2.getWidth(), s2.getHeight());
        back.setBounds(s3.getX(), s3.getY(), s3.getWidth(), s3.getHeight());
    }

    public void initSliderBarValue() {
        music.setValue(context.getBgmSoundSize());
        effect.setValue(context.getEffectSoundSize());
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(AbstractGameImageLoader.getImage("setting"), 0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT, null);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F11) {
            fullScreen();
        } else if (e.getKeyCode() == KeyEvent.VK_M) {
            toLast();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void toLast() {
        context.toLastPanel();
    }
}
