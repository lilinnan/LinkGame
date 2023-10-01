package com.lln.link.frame;

import com.lln.link.Context;
import com.lln.link.conf.GameConfig;
import com.lln.link.panel.GamePanel;
import com.lln.link.panel.SettingPanel;
import com.lln.link.panel.StartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/12 13:45
 */

public class AppFrame extends JFrame {
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Context context;

    private CardLayout cardLayout;
    private Container contentPane;

    private GamePanel gamePanel;
    private StartPanel startPanel;
    private SettingPanel settingPanel;


    public AppFrame(Context context) {
        super();
        this.context = context;
        initFrame();
    }


    private void initFrame() {
        this.setTitle("连连看");
        this.setSize(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setVisible(true);
        //设置完成后，获取边框数据
        Insets insets = this.getInsets();
        //计算新的边框数据
        int newWidth = GameConfig.WINDOW_WIDTH + insets.left + insets.right;
        int newHeight = GameConfig.WINDOW_HEIGHT + insets.top + insets.bottom;
        //重新设置大小
        this.setSize(newWidth, newHeight);
        //进行居中
        this.setLocationRelativeTo(null);

        contentPane = this.getContentPane();
        cardLayout = new CardLayout();

        //进行布局设置
        this.getContentPane().setLayout(cardLayout);
        startPanel = new StartPanel(context);
        contentPane.add(startPanel, "start");

        gamePanel = new GamePanel(context);
        contentPane.add(gamePanel, "game");

        settingPanel = new SettingPanel(context);
        contentPane.add(settingPanel, "setting");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Insets in = getInsets();
                int width = getWidth() - in.left - in.right;
                int height = getHeight() - in.top - in.bottom;
                GameConfig.change(width, height);

                startPanel.computeParam();
                startPanel.repaint();

                gamePanel.computeParam();
                gamePanel.repaint();

                settingPanel.computeParam();
                settingPanel.repaint();
            }
        });
    }


    public void showGame(boolean startThread) {
        cardLayout.show(contentPane, "game");
        gamePanel.requestFocus();
        if (!startThread) {
            return;
        }
        gamePanel.startGame();
    }

    public void showStart() {
        cardLayout.show(contentPane, "start");
        startPanel.requestFocus();
    }

    public void showSetting() {
        cardLayout.show(contentPane, "setting");
        settingPanel.requestFocus();
        settingPanel.initSliderBarValue();
    }

}
