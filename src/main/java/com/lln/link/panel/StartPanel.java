package com.lln.link.panel;

import com.lln.link.Context;
import com.lln.link.conf.GameConfig;
import com.lln.link.image.AbstractGameImageLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/12 14:03
 */

public class StartPanel extends AbstractPanel implements KeyListener, MouseListener {

    public StartPanel(Context context) {
        super(context);
        this.context = context;
        this.addKeyListener(this);
        this.addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(AbstractGameImageLoader.getImage("start_background"), 0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT, null);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F11:
                fullScreen();
                break;
            case KeyEvent.VK_ENTER:
                startGame();
                break;
            case KeyEvent.VK_M:
                context.showSetting();
                break;
            default:
                break;
        }
    }

    private void startGame() {
        context.showGame(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void computeParam() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        startGame();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
