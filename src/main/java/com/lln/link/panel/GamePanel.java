package com.lln.link.panel;

import com.lln.link.Context;
import com.lln.link.Res;
import com.lln.link.conf.GameConfig;
import com.lln.link.image.AbstractGameImageLoader;
import com.lln.link.level.Level;
import com.lln.link.pojo.Point;
import com.lln.link.pojo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 13:24
 */

public class GamePanel extends AbstractPanel implements MouseListener, KeyListener {

    /**
     * 游戏对象
     */
    private Game game;

    /**
     * 游戏地图
     */
    private GameMap gameMap;

    /**
     * 关卡
     */
    private Level level;

    /**
     * 游戏背景
     */
    private Background background;

    /**
     * 方块宽度
     */
    private int blockWidth;
    /**
     * 方块高度
     */
    private int blockHeight;

    /**
     * 当前组合方块宽度
     */
    private int blockWidthForCurrentGroup;

    /**
     * 当前组合方块高度
     */
    private int blockHeightForCurrentGroup;


    /**
     * 方块开始坐标的x
     */
    private int startX;
    /**
     * 方块开始坐标的y
     */
    private int startY;
    /**
     * 方块结束坐标的x
     */
    private int endX;
    /**
     * 方块结束坐标的y
     */
    private int endY;

    /**
     * 当前组合开始坐标的X
     */
    private int startXForCurrentGroup;
    /**
     * 当前组合开始坐标的Y
     */
    private int startYForCurrentGroup;


    /**
     * 选择列表
     */
    private ArrayList<Index> selectList;

    /**
     * 中转点列表
     */
    private ArrayList<Index> transferIndexes;

    private boolean connecting = false;

    /**
     * 帮助列表
     */
    private ArrayList<Index> helpIndexes;
    /**
     * 帮助中否
     */
    private boolean helping = false;

    /**
     * 要画帮助吗
     */
    private boolean drawHelp = false;

    /**
     * 暂停着吗
     */
    private boolean isPause = false;


    private ArrayList<Integer> clearCache;


    private String mj = "38384040373937396665";
    private String mjCache = "";


    /**
     * 初始化
     */
    public GamePanel(Context context) {
        super(context);
        addMouseListener(this);
        addKeyListener(this);
        game = new Game();
        initParam();
    }

    public void startGame() {
        //打开时间控制器
        timeControl();
    }

    public void pause() {
        isPause = true;
        mjCache = "";
        repaint();
    }

    public void cancelPause() {
        isPause = false;
        repaint();
    }

    /**
     * 初始化一些参数
     */
    private void initParam() {
        //读取关卡
        level = game.getLevel();
        //读取关卡中游戏地图中的地图块
        gameMap = level.getGameMap();
        //游戏背景
        background = level.getBackground();
        computeParam();
        selectList = new ArrayList<>();
    }

    @Override
    public void computeParam() {
        Point leftTop = background.getLeftTop();
        Point rightBottom = background.getRightBottom();
        blockWidth = (rightBottom.getX() - leftTop.getX()) / gameMap.getMaxJ();
        blockHeight = (rightBottom.getY() - leftTop.getY()) / gameMap.getMaxI();
        startX = leftTop.getX();
        startY = leftTop.getY();
        endX = startX + gameMap.getMaxJ() * blockWidth;
        endY = startY + gameMap.getMaxI() * blockHeight;
        Point leftTopForCurrentGroup = background.getLeftTopForCurrentGroup();
        Point rightBottomForCurrentGroup = background.getRightBottomForCurrentGroup();
        blockWidthForCurrentGroup = (rightBottomForCurrentGroup.getX() - leftTopForCurrentGroup.getX()) / 2;
        blockHeightForCurrentGroup = (rightBottomForCurrentGroup.getY() - leftTopForCurrentGroup.getY());
        startXForCurrentGroup = leftTopForCurrentGroup.getX();
        startYForCurrentGroup = leftTopForCurrentGroup.getY();
    }

    /**
     * 时间控制，用于管理游戏中的时间
     */
    public void timeControl() {
        context.getGameThreadPool().execute(() -> {
            while (true) {
                repaint();
                int time = game.getTime();
                if (time <= 0) {
                    alert("时间已用尽，游戏结束");
                    gameOver();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isPause) {
                    game.addTime(-1);
                    game.addCombo(-1);
                }

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //画背景
        g.drawImage(background.getImage(), 0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT, null);
        //画文字
        drawString(g);
        //画时间
        drawTime(g);
        //draw combo
        drawCombo(g);
        //画当前组
        drawGroup(g);
        //画方块
        drawBlock(g);
        //如果有中转点，就要画线
        if (transferIndexes != null) {
            drawLine(g);
        }
        if (isPause) {
            g.drawImage(AbstractGameImageLoader.getImage("pause"), 0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT, null);
        }
        if (drawHelp) {
            drawHelp(g, false);
        }
    }

    private int getX(int j) {
        return j * blockWidth + startX;
    }

    private int getY(int i) {
        return i * blockHeight + startY;
    }

    private int getXForCurrentGroup(int j) {
        return j * blockWidthForCurrentGroup + startXForCurrentGroup;
    }

    private int getYForCurrentGroup() {
        return startYForCurrentGroup;
    }

    private void drawBlock(Graphics g) {
        for (int i = 0; i < gameMap.getMaxI(); i++) {
            for (int j = 0; j < gameMap.getMaxJ(i); j++) {
                Image image = AbstractGameImageLoader.getImage(gameMap.getType(i, j));
                int x = getX(j);
                int y = getY(i);
                if (gameMap.isClear(i, j)) {
                    continue;
                }
                g.drawImage(image, x, y, blockWidth, blockHeight, null);
                if (gameMap.isSelect(i, j)) {
                    g.setColor(GameConfig.FILL_GRAY_COLOR);
                    g.fillRect(x, y, blockWidth, blockHeight);
                }
            }
        }
    }

    private void drawGroup(Graphics g) {
        int type1 = -1;
        int type2 = -1;
        if (clearCache != null && clearCache.size() > 0) {
            if (clearCache.size() == 1) {
                type2 = clearCache.get(0);
            } else {
                type1 = clearCache.get(0);
                type2 = clearCache.get(1);
            }
        }
        g.drawImage(AbstractGameImageLoader.getImage(type1), getXForCurrentGroup(0),
                getYForCurrentGroup(), blockWidthForCurrentGroup, blockHeightForCurrentGroup, null);
        g.drawImage(AbstractGameImageLoader.getImage(type2), getXForCurrentGroup(1),
                getYForCurrentGroup(), blockWidthForCurrentGroup, blockHeightForCurrentGroup, null);
    }

    /**
     * draw combo
     *
     * @param g g
     */
    private void drawCombo(Graphics g) {
        int allCombo = 100;
        int combo = game.getCombo();
        ImageInfo comboInfo = background.getCombo();
        drawStrip(g, comboInfo.getX(), comboInfo.getY(), comboInfo.getWidth(), comboInfo.getHeight(), combo * 1.0 / allCombo);
    }

    /**
     * 画时间
     *
     * @param g g
     */
    private void drawTime(Graphics g) {
        int allTime = level.getTime();
        int time = game.getTime();
        ImageInfo timeInfo = background.getTime();
        drawStrip(g, timeInfo.getX(), timeInfo.getY(), timeInfo.getWidth(), timeInfo.getHeight(), time * 1.0 / allTime);
    }


    /**
     * 画一个进度条
     * <p>
     * <p>
     * 真鸡巴难搞，cao
     *
     * @param g       g
     * @param x       坐标x
     * @param y       坐标Y
     * @param width   宽度
     * @param height  高度
     * @param percent 百分比
     */
    private void drawStrip(Graphics g, int x, int y, int width, int height, double percent) {

        Graphics2D graphics2D = (Graphics2D) g;

        float w = 4;
        double tx = x + w / 2;
        double ty = y + w / 2;
        //线粗了画的位置会有变化，一定要移动半个，才会让图画到正确的位置上
        graphics2D.translate(tx, ty);

        int arc = 4;
        graphics2D.setStroke(new BasicStroke(w));

        //画外框
        graphics2D.setColor(new Color(0x2e5797));
        //因为如果有了线宽，画出来的矩形高度会为高度+线宽，所以这里减去一个线宽
        graphics2D.drawRoundRect(0, 0, (int) (width - w), (int) (height - w), arc, arc);

        //画完以后，开始画底色，两个方向均移动半个线宽，因为一开始已经一移动了半个线宽
        graphics2D.translate(w / 2, w / 2);

        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRoundRect(0, 0, (int) (width - 2 * w), (int) (height - 2 * w), (int) (arc - w), (int) (arc - w));
        //底色完成

        //准备画进度条
        int ww = (int) ((width - 2 * w) * percent);

        //上进度条
        GradientPaint paint = new GradientPaint(0, 0, new Color(0x6cbadf), 0, height - 2 * w, new Color(0x074274), true);
        graphics2D.setPaint(paint);
        graphics2D.fillRoundRect(0, 0, ww, (int) (height - 2 * w), (int) (arc - w), (int) (arc - w));

        //再移回去
        graphics2D.translate(-w / 2, -w / 2);
        graphics2D.translate(-tx, -ty);
    }


    /**
     * 画字
     *
     * @param g g
     */
    private void drawString(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        TextInfo comboText = background.getComboText();
        graphics2D.setFont(comboText.getFont());
        graphics2D.setColor(comboText.getColor());
        graphics2D.drawString("连击", comboText.getX(), comboText.getY());

        TextInfo timeText = background.getTimeText();
        graphics2D.setFont(timeText.getFont());
        graphics2D.setColor(timeText.getColor());
        graphics2D.drawString("时间", timeText.getX(), timeText.getY());

        TextInfo score = background.getScore();
        graphics2D.setFont(score.getFont());
        graphics2D.setColor(score.getColor());
        graphics2D.drawString("得分：" + game.getScore(), score.getX(), score.getY());

        TextInfo help = background.getHelp();
        graphics2D.setFont(help.getFont());
        graphics2D.setColor(help.getColor());
        graphics2D.drawString("提示(F3):" + game.getHelp(), help.getX(), help.getY());


        TextInfo refresh = background.getRefresh();
        graphics2D.setFont(refresh.getFont());
        graphics2D.setColor(refresh.getColor());
        graphics2D.drawString("洗牌(F5):" + game.getRefresh(), refresh.getX(), refresh.getY());

        //鸽了
//        TextInfo title = background.getTitle();
//        graphics2D.setFont(title.getFont());
//        graphics2D.setColor(title.getColor());
//        graphics2D.drawString(level.getTitle(), title.getX(), title.getY());
    }

    /**
     * 画线
     * <p>
     * 这里的逻辑是画从一个选中的点到他的延伸点，再
     * 从他的延伸点到另一个点的延伸点再从另一个点的
     * 延伸点到选中的点
     *
     * @param g g
     */
    private void drawLine(Graphics g) {
        Index index1 = selectList.get(0);
        Index index2 = selectList.get(1);
        Index transferIndex1 = transferIndexes.get(0);
        Index transferIndex2 = transferIndexes.get(1);

        Graphics2D graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.setColor(Color.GREEN);

        int x1 = index1.getJ() * blockWidth + blockWidth / 2 + startX;
        int y1 = index1.getI() * blockHeight + blockHeight / 2 + startY;
        int x2 = transferIndex1.getJ() * blockWidth + blockWidth / 2 + startX;
        int y2 = transferIndex1.getI() * blockHeight + blockHeight / 2 + startY;
        graphics.drawLine(x1, y1, x2, y2);

        x1 = transferIndex1.getJ() * blockWidth + blockWidth / 2 + startX;
        y1 = transferIndex1.getI() * blockHeight + blockHeight / 2 + startY;
        x2 = transferIndex2.getJ() * blockWidth + blockWidth / 2 + startX;
        y2 = transferIndex2.getI() * blockHeight + blockHeight / 2 + startY;
        graphics.drawLine(x1, y1, x2, y2);

        x1 = transferIndex2.getJ() * blockWidth + blockWidth / 2 + startX;
        y1 = transferIndex2.getI() * blockHeight + blockHeight / 2 + startY;
        x2 = index2.getJ() * blockWidth + blockWidth / 2 + startX;
        y2 = index2.getI() * blockHeight + blockHeight / 2 + startY;
        graphics.drawLine(x1, y1, x2, y2);
    }

    private synchronized void drawHelp(Graphics g, boolean isNeedImg) {
        for (Index index : helpIndexes) {
            if (gameMap.isClear(index)) {
                continue;
            }
            int i = index.getI();
            int j = index.getJ();
            int x = getX(j);
            int y = getY(i);
            if (isNeedImg) {
                g.drawImage(AbstractGameImageLoader.getImage(gameMap.getType(i, j)), x, y, blockWidth, blockHeight, null);
            }
            if (drawHelp) {
                g.setColor(GameConfig.FILL_RED_COLOR);
                g.fillRect(x, y, blockWidth, blockHeight);
            }
            if (gameMap.isSelect(index)) {
                g.setColor(GameConfig.FILL_GRAY_COLOR);
                g.fillRect(x, y, blockWidth, blockHeight);
            }
        }
    }

    /**
     * 操作一个块，被点击后调用
     *
     * @param i i
     * @param j j
     */
    private void operateBlock(int i, int j) {
        //是否被清除掉了
        if (gameMap.isClear(i, j) || gameMap.isWall(i, j)) {
            return;
        }
        //看看这个块是否被选中了
        if (gameMap.isSelect(i, j)) {
            //是选中的，进行清空选择状态
            clearSelect(new Index(i, j), true);
        } else {
            //没选中，进行选中操作
            select(i, j);
            //既然选中了，就设置选择状态并到选择列表中
            selectList.add(new Index(i, j));
            if (selectList.size() == GameConfig.SELECT_OK_SIZE) {
                context.getGameThreadPool().execute(() -> {
                    //稍微等一下下再连接
                    //目的是为了能让用户得到点击反馈
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ArrayList<Index> result = gameMap.connection(selectList.get(0), selectList.get(1));
                    if (result == null) {
                        clearFail();
                    } else {
                        connectionOk(result);
                    }
                });
            }
        }
        //repaint();
        //这里不进行重绘了，因为重绘会对全部数据进行绘制，太卡了
    }

    private void select(int i, int j) {
        gameMap.select(i, j);
        //进行选中效果填充
        Graphics g = getGraphics();
        g.setColor(GameConfig.FILL_GRAY_COLOR);
        g.fillRect(getX(j), getY(i), blockWidth, blockHeight);
    }

    /**
     * 清除选中
     *
     * @param index    一个坐标
     * @param isRemove 是否在清除选中后在列表中移除
     */
    private void clearSelect(Index index, boolean isRemove) {
        gameMap.unSelect(index);

        if (isRemove) {
            selectList.removeIf(value -> gameMap.blockEquals(index, value));
        }

        //清除
        Graphics g = getGraphics();
        int i = index.getI();
        int j = index.getJ();
        int x = getX(j);
        int y = getY(i);
        //g.clearRect(x, y, blockWidth, blockHeight);
        g.drawImage(AbstractGameImageLoader.getImage(gameMap.getType(i, j)), x, y, blockWidth, blockHeight, null);
    }

    /**
     * 这里先将点存起来，然后去paint里面画
     *
     * @param indexes 中转点
     */
    private void connectionOk(ArrayList<Index> indexes) {
        connecting = true;
        //先存起来
        transferIndexes = indexes;
        //这里不进行重绘了，过于消耗资源,只画线就行
        //repaint();
        drawLine(getGraphics());

        context.getGameThreadPool().execute(() -> {
            //稍微等一下
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //清除中转点
            transferIndexes = null;
            //消除成功
            clearSuccess();
            //消除成功后会repaint
        });
    }

    /**
     * 消除失败
     */
    private void clearFail() {
        unSelectAll();
        context.startEffect(Res.Audio.FAIL);
    }

    private void unSelectAll() {
        //拿到两个选中的点，取消选中状态
        for (Index index : selectList) {
            clearSelect(index, false);
        }
        selectList.clear();
        //repaint();
        //这里不进行重绘了
        //调用清除选中会处理好这件事情
    }

    /**
     * 消除成功
     */
    private void clearSuccess() {
        //播放一次消除成功音效
        context.startEffect(Res.Audio.CLEAR);

        game.addCombo(5);

        game.addScore(game.getCombo() / 3 * 2);

        for (int i = 0; i < GameConfig.SELECT_OK_SIZE; i++) {
            gameMap.clear(selectList.get(i));
        }
        //清除选中列表
        unSelectAll();

        clearCache = gameMap.getClearCache();
        /*
        这里要进行技能释放
         */

        //重新绘制界面，这里的重绘是必须的，因为清除刚刚画的线并不容易啊
        repaint();
        connecting = false;
        if (gameMap.isClearLevel()) {
            toNextLevel();
            return;
        }
        //看看有没有路
        if (gameMap.getHelp() != null) {
            return;
        }
        //没路了，看看有没有提示
        if (game.getHelp() > 0) {
            alert("无路可走了，请洗牌");
            return;
        }
        //都没提示了，死了
        alert("无路可走了，游戏结束！");
        gameOver();
    }

    /**
     * 进入下一关
     */
    private void toNextLevel() {
        if (game.toNextLevel()) {
            context.startEffect(Res.Audio.NEXT);
            initParam();
            clearCache = null;
            repaint();
            return;
        }
        context.startEffect(Res.Audio.ALL_OVER);
        alert("您已通关");
        gameOver();
    }

    /**
     * 提示框
     *
     * @param message 提示信息
     */
    private void alert(String message) {
        JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.PLAIN_MESSAGE);
    }

    private boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(this, message, "提示", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

    /**
     * 鼠标被点击事件
     *
     * @param e 事件对象
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isPause) {
            return;
        }
        //点击鼠标后，判断当前是否在游戏区域中
        if (!isInGame(e.getX(), e.getY())) {
            return;
        }
        //连接中，不允许操作
        if (connecting) {
            return;
        }
        //在游戏区域中，
        int i = (e.getX() - startX) / blockWidth;
        int j = (e.getY() - startY) / blockHeight;
        //因为是坐标计算的，所以在操作的时候，i和j是反的
        operateBlock(j, i);
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (isPause) {
            mjCache += e.getKeyCode();
            if (mj.equals(mjCache)) {
                game.setHelp(99);
                game.setRefresh(99);
                mj = "NULL";
                mjCache = "";
            }
            if (!(keyCode == KeyEvent.VK_P || keyCode == KeyEvent.VK_F11)) {
                return;
            }
        }
        switch (keyCode) {
            case KeyEvent.VK_F11:
                fullScreen();
                break;
            case KeyEvent.VK_F3:
                help();
                break;
            case KeyEvent.VK_F5:
                refresh();
                break;
            case KeyEvent.VK_ESCAPE:
                exit();
                break;
            case KeyEvent.VK_P:
                pauseGame();
                break;
            case KeyEvent.VK_M:
                pauseGame();
                context.showSetting();
                break;
            default:
                break;
        }
    }


    public void pauseGame() {
        if (isPause) {
            cancelPause();
            return;
        }
        pause();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void help() {
        if (helping) {
            return;
        }
        if (game.getHelp() <= 0) {
            context.startEffect(Res.Audio.FAIL);
            //alert("您已经没有提示次数了");
            return;
        }
        ArrayList<Index> help = gameMap.getHelp();
        if (help == null) {
            unSelectAll();
            context.startEffect(Res.Audio.FAIL);
            alert("无路可走了，请洗牌");
            return;
        }
        helpIndexes = help;
        //重绘，因为需要更新数据
        game.addHelp(-1);
        repaint();
        context.getGameThreadPool().execute(() -> {
            context.startEffect(Res.Audio.HELP);
            helping = true;
            for (int i = 0; i < 5; i++) {
                drawHelp = i % 2 == 0;
                drawHelp(getGraphics(), true);
                try {
                    Thread.sleep(350);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //不用画提示了
            //repaint();
            drawHelp = false;
            drawHelp(getGraphics(), true);
            helping = false;
        });
    }

    private void refresh() {
        if (game.getRefresh() <= 0) {
            context.startEffect(Res.Audio.FAIL);
            //alert("您已经没有洗牌次数了");
            return;
        }
        context.startEffect(Res.Audio.REFRESH);
        gameMap.refresh();
        game.addRefresh(-1);
        unSelectAll();
        repaint();
    }

    private void exit() {
        pauseGame();
        if (confirm("是否退出游戏？")) {
            System.exit(0);
        }
    }


    /**
     * 是否为有效区域
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return 是否在有效区域中
     */
    private boolean isInGame(int x, int y) {
        return x >= startX && x <= endX && y >= startY && y <= endY;
    }


    private void gameOver() {
        System.exit(0);
    }
}
