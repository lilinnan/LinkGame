package com.lln.link.pojo;

import com.lln.link.conf.GameConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * 这是一个游戏地图
 *
 * @author LiLinnan
 * @version 1.0
 * @date 2020/2/3 13:38
 */

public class GameMap {
    /**
     * 打乱次数
     */
    private final static int TIME = 100000;
    /**
     * 将每种类型的数据分别保存起来，主要是用于提示计算
     */
    private HashMap<Integer, ArrayList<Index>> cache;
    /**
     * 游戏地图
     */
    private Block[][] blocks;

    /**
     * 有效块的个数
     */
    private int validSize;
    /**
     * 消除列表，用于判断连击
     */
    private ArrayList<Integer> clearCache = new ArrayList<>();
    /**
     * 是否需要将被消除的块的类型添加到消除列表中
     */
    private boolean needAdd;
    /**
     * 墙的位置
     */
    private HashMap<Integer, HashSet<Index>> walls;
    /**
     * 消除后调用
     */
    private OnClearedListener onClearedListener;


    public GameMap(int width, int height, HashMap<Integer, HashSet<Index>> walls) {
        blocks = new Block[height][width];
        this.walls = walls;
        validSize = width * height - getWallsSize();
        initMap();
    }


    public void setOnClearedListener(OnClearedListener onClearedListener) {
        this.onClearedListener = onClearedListener;
    }

    private int getWallsSize() {
        int wallsSize = 0;
        if (walls == null) {
            return wallsSize;
        }
        for (HashSet<Index> value : walls.values()) {
            wallsSize += value.size();
        }
        return wallsSize;
    }

    private void initMap() {
        //新建缓存
        cache = new HashMap<>(GameConfig.MAX_TYPE);
        //创建有效区域的大小
        int[] num = new int[validSize];
        //创建随机对象
        Random random = new Random();
        //设置步长为2
        int step = 2;
        //生成对，步长为2，每一个随机数保存两次
        for (int i = 0; i < num.length; i += step) {
            int randomNum = random.nextInt(GameConfig.MAX_TYPE) + 1;
            num[i] = randomNum;
            num[i + 1] = randomNum;
        }
        //打乱
        for (int i = 0; i < TIME; i++) {
            int index = random.nextInt(num.length);
            int index2 = random.nextInt(num.length);
            int temp = num[index];
            num[index] = num[index2];
            num[index2] = temp;
        }

        if (walls != null) {
            //将墙的数据添加上去
            for (Integer type : walls.keySet()) {
                HashSet<Index> indexHashSet = walls.get(type);
                for (Index index : indexHashSet) {
                    blocks[index.getI()][index.getJ()] = new Block(type);
                }
            }
        }
        //在生成的列表中的索引
        int index = 0;
        //开始填充除了墙以外的块
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                //有东西，是墙，跳过
                if (blocks[i][j] != null) {
                    continue;
                }
                //读出类型
                int type = num[index++];
                //创建块
                blocks[i][j] = new Block(type);
                //放入缓存中
                putToCache(type, new Index(i, j));
            }
        }

        //如果无解，进行洗牌操作
        if (getHelp() == null) {
            refresh();
        }
    }

    /**
     * 将数据放入
     *
     * @param type  块的类型
     * @param index 块的位置
     */
    private void putToCache(int type, Index index) {
        ArrayList<Index> indexArrayList = cache.computeIfAbsent(type, k -> new ArrayList<>());
        indexArrayList.add(index);
    }

    private Block getBlock(int i, int j) {
        return blocks[i][j];
    }

    public int getMaxI() {
        return blocks.length;
    }

    public int getMaxJ() {
        return blocks[0].length;
    }

    public int getMaxJ(int i) {
        return blocks[i].length;
    }

    /**
     * 消除一个块
     *
     * @param i i
     * @param j j
     */
    public void clear(int i, int j) {
        //拿到块
        Block b = getBlock(i, j);
        //设置为清除状态
        b.setClear(true);
        //有效块-1
        validSize--;
        //接下来对是否取反进行一次取反，然后就可以达到步进为2的操作
        //两次清除执行一次下面的操作
        needAdd = !needAdd;
        if (needAdd) {
            return;
        }
        //调用一次消除完成
        clearOk(getType(i, j));
    }

    public void clear(Index index) {
        clear(index.getI(), index.getJ());
    }


    public void select(int i, int j) {
        getBlock(i, j).setSelect(true);
    }

    public void select(Index index) {
        select(index.getI(), index.getJ());
    }


    public void unSelect(int i, int j) {
        getBlock(i, j).setSelect(false);
    }

    public void unSelect(Index index) {
        unSelect(index.getI(), index.getJ());
    }


    public boolean isSelect(int i, int j) {
        return getBlock(i, j).isSelect();
    }

    public boolean isSelect(Index index) {
        return isSelect(index.getI(), index.getJ());
    }

    /**
     * 对当前的牌进行一个洗牌操作
     */
    public void refresh() {
        //初始化缓存
        cache = new HashMap<>(GameConfig.MAX_TYPE);
        //创建一个有效大小的int数组
        int[] num = new int[validSize];
        //创建随机数对象
        Random random = new Random();
        //初始化索引位置
        int numIndex = 0;
        //读取之前的数据，
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                //有效的块才需要进行数据读取
                if (isUsefulBlock(i, j)) {
                    num[numIndex++] = getType(i, j);
                }
            }
        }
        //打乱
        for (int i = 0; i < TIME; i++) {
            int index = random.nextInt(num.length);
            int index2 = random.nextInt(num.length);
            int temp = num[index];
            num[index] = num[index2];
            num[index2] = temp;
        }
        //最后出界了，减个1
        numIndex--;
        //赋值
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                Block block = getBlock(i, j);
                //有效了才需要进行修改
                if (isUsefulBlock(block)) {
                    int type = num[numIndex--];
                    block.setType(type);
                    //将新的数据放入缓存
                    putToCache(type, new Index(i, j));
                }
            }
        }
        //如果无解，再洗
        if (getHelp() == null) {
            refresh();
        }
    }

    /**
     * 返回当前块是否有效
     *
     * @param index 坐标
     * @return 有效否
     */
    public boolean isUsefulBlock(Index index) {
        return isUsefulBlock(index.getI(), index.getJ());
    }

    /**
     * 返回当前块是否有效
     *
     * @param i i
     * @param j j
     * @return 有效否
     */
    public boolean isUsefulBlock(int i, int j) {
        //如果出界，也是无效的
        if (isClear(i, j)) {
            return false;
        }
        return isUsefulBlock(getBlock(i, j));
    }

    /**
     * 返回当前块是否有效
     *
     * @param block 块对象
     * @return 有效否
     */
    public boolean isUsefulBlock(Block block) {
        if (isClear(block)) {
            return false;
        }
        return !isWall(block);
    }

    /**
     * 判断两个块是否相同
     *
     * @param index1 块1坐标
     * @param index2 块2坐标
     * @return 相同否？
     */
    public boolean blockEquals(Index index1, Index index2) {
        return getBlock(index1.getI(), index1.getJ()).equals(getBlock(index2.getI(), index2.getJ()));
    }

    public int getType(int i, int j) {
        return getType(getBlock(i, j));
    }

    public int getType(Index index) {
        return getType(index.getI(), index.getJ());
    }

    public int getType(Block block) {
        return block.getType();
    }


    /**
     * 获取一条帮助
     *
     * @return 可以连接的坐标
     */
    public ArrayList<Index> getHelp() {
        //如果都没有有效块了，证明消除完了，无法帮助
        if (validSize == 0) {
            return null;
        }
        //遍历全部数据
        for (int i = 0; i <= GameConfig.MAX_TYPE; i++) {
            //获取类型i的缓存
            ArrayList<Index> indexes = cache.get(i);
            //如果没有，下一步
            if (indexes == null) {
                continue;
            }
            //有东西，有了最少有俩，不然就是错的
            //进行排列组合，1 2 3  1 2 3  1-2 1-3 2-3
            //找出一条可以连接的，返回即可
            for (int j = 0; j < indexes.size() - 1; j++) {
                for (int k = j + 1; k < indexes.size(); k++) {
                    Index index1 = indexes.get(j);
                    //发现是无效块，跳过
                    if (!isUsefulBlock(index1)) {
                        continue;
                    }
                    //发现是无效块，跳过
                    Index index2 = indexes.get(k);
                    if (!isUsefulBlock(index2)) {
                        continue;
                    }
                    if (isConnection(index1, index2)) {
                        ArrayList<Index> result = new ArrayList<>();
                        result.add(index1);
                        result.add(index2);
                        return result;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 两个点之间能连接吗
     *
     * @param index1 1的下标
     * @param index2 2的下标
     * @return 能不能
     */
    private boolean isConnection(Index index1, Index index2) {
        return connection(index1, index2) != null;
    }


    /**
     * 连接
     *
     * @return 中转点列表
     */
    public ArrayList<Index> connection(Index index1, Index index2) {
        //首先判断，看两个块的类型是否相同
        if (!blockEquals(index1, index2)) {
            //不同就为连接失败
            return null;
        }
        //首先，进行水平延伸消除判断
        //先延伸
        //再判断
        ArrayList<Index> indexesH1 = horizonExtend(index1);
        ArrayList<Index> indexesH2 = horizonExtend(index2);
        ArrayList<Index> resultH = horizonConnection(indexesH1, index1, indexesH2, index2);
        if (resultH != null) {
            return resultH;
        }
        //水平延伸消除失败，进行垂直延伸消除判断
        ArrayList<Index> indexesV1 = verticalExtend(index1);
        ArrayList<Index> indexesV2 = verticalExtend(index2);
        return verticalConnection(indexesV1, index1, indexesV2, index2);
    }


    /**
     * 进行水平连接判断
     *
     * @param indexesH1 1列表
     * @param indexesH2 2列表
     * @return 中转点列表
     */
    private ArrayList<Index> horizonConnection(ArrayList<Index> indexesH1, Index p1, ArrayList<Index> indexesH2, Index p2) {
        for (Index indexH1 : indexesH1) {
            for (Index indexH2 : indexesH2) {
                //进行延伸点的遍历，发现在一条线上
                if (indexH1.getJ() == indexH2.getJ()) {
                    //对i进行排序，保证遍历正常
                    int min = indexH1.getI();
                    int max = indexH2.getI();
                    if (min > max) {
                        min = min ^ max;
                        max = min ^ max;
                        min = min ^ max;
                    }
                    boolean flag = true;
                    //j是相同的，随便取哪个的都行
                    int j = indexH1.getJ();
                    for (int i = min; i <= max; i++) {
                        //如果是点本身，就认为消除了，不需要判断
                        if (p1.equals(i, j) || p2.equals(i, j)) {
                            continue;
                        }
                        //有一个没有消除了的，就证明中间有障碍物
                        if (!isClear(i, j)) {
                            flag = false;
                            break;
                        }
                    }
                    //可以消除
                    if (flag) {
                        //将数据保存并返回
                        ArrayList<Index> result = new ArrayList<>();
                        result.add(indexH1);
                        result.add(indexH2);
                        //一定要按正常顺序添加，否则画线可能会出错（￣︶￣）↗　
                        return result;
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 进行垂直连接判断
     *
     * @param indexesV1 1列表
     * @param indexesV2 2列表
     * @return 中转点列表
     */
    private ArrayList<Index> verticalConnection(ArrayList<Index> indexesV1, Index p1, ArrayList<Index> indexesV2, Index p2) {
        for (Index indexV1 : indexesV1) {
            for (Index indexV2 : indexesV2) {
                if (indexV1.getI() == indexV2.getI()) {
                    int min = indexV1.getJ();
                    int max = indexV2.getJ();
                    if (min > max) {
                        min = min ^ max;
                        max = min ^ max;
                        min = min ^ max;
                    }
                    boolean flag = true;
                    int i = indexV1.getI();
                    for (int j = min; j <= max; j++) {
                        //如果是点本身，就认为消除了，不需要判断
                        if (p1.equals(i, j) || p2.equals(i, j)) {
                            continue;
                        }
                        if (!isClear(i, j)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        ArrayList<Index> result = new ArrayList<>();
                        result.add(indexV1);
                        result.add(indexV2);
                        //一定要按正常顺序添加，否则画线会出错（￣︶￣）↗　
                        return result;
                    }
                    break;
                }
            }
        }
        return null;
    }


    /**
     * 垂直延伸
     *
     * @param index 被延伸点
     * @return 延伸结果
     */
    private ArrayList<Index> verticalExtend(Index index) {
        ArrayList<Index> indexes = new ArrayList<>();
        indexes.add(index);
        indexes.addAll(verticalExtendTop(index));
        indexes.addAll(verticalExtendBottom(index));
        return indexes;
    }

    /**
     * 向上垂直延伸
     *
     * @param index 被延伸点
     * @return 延伸结果
     */
    private ArrayList<Index> verticalExtendTop(Index index) {
        ArrayList<Index> indexes = new ArrayList<>();
        for (int i = index.getI() - 1; i >= -1; i--) {
            int j = index.getJ();
            if (isClear(i, j)) {
                indexes.add(new Index(i, j));
                continue;
            }
            break;
        }
        return indexes;
    }

    /**
     * 向下垂直延伸
     *
     * @param index 被延伸点
     * @return 延伸结果
     */
    private ArrayList<Index> verticalExtendBottom(Index index) {
        ArrayList<Index> indexes = new ArrayList<>();
        int max = getMaxI() + 1;
        int j = index.getJ();
        for (int i = index.getI() + 1; i <= max; i++) {
            if (isClear(i, j)) {
                indexes.add(new Index(i, j));
                continue;
            }
            break;
        }
        return indexes;
    }


    /**
     * 水平延伸
     *
     * @param index 点
     * @return 延伸结果
     */
    private ArrayList<Index> horizonExtend(Index index) {
        ArrayList<Index> indexes1 = new ArrayList<>();
        indexes1.add(index);
        indexes1.addAll(horizonExtendLeft(index));
        indexes1.addAll(horizonExtendRight(index));
        return indexes1;
    }

    /**
     * 向左水平延伸
     *
     * @param index 被延伸点
     * @return 延伸结果
     */
    private ArrayList<Index> horizonExtendLeft(Index index) {
        ArrayList<Index> indexes = new ArrayList<>();
        int i = index.getI();
        for (int j = index.getJ() - 1; j >= -1; j--) {
            if (isClear(i, j)) {
                indexes.add(new Index(i, j));
                continue;
            }
            break;
        }
        return indexes;
    }

    /**
     * 向右水平延伸
     *
     * @param index 被延伸点
     * @return 延伸结果
     */
    private ArrayList<Index> horizonExtendRight(Index index) {
        ArrayList<Index> indexes = new ArrayList<>();
        int max = getMaxJ(index.getI()) + 1;
        for (int j = index.getJ() + 1; j <= max; j++) {
            int i = index.getI();
            if (isClear(i, j)) {
                indexes.add(new Index(i, j));
                continue;
            }
            break;
        }
        return indexes;
    }


    /**
     * 判断是否为空的
     *
     * @param i i
     * @param j j
     * @return 是否为空
     */
    public boolean isClear(int i, int j) {
        if (i >= 0 && i < getMaxI() && j >= 0 && j < getMaxJ(i)) {
            return isClear(getBlock(i, j));
        }
        return true;
    }


    public boolean isClear(Index index) {
        return isClear(index.getI(), index.getJ());
    }

    public boolean isClear(Block block) {
        return block == null || block.isClear();
    }

    /**
     * 是否成功过关
     *
     * @return 是否成功过关
     */
    public boolean isClearLevel() {
        for (int i = 0; i < getMaxI(); i++) {
            for (int j = 0; j < getMaxJ(i); j++) {
                if (!(isClear(i, j) || isWall(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Integer> getClearCache() {
        return clearCache;
    }

    public boolean isWall(int i, int j) {
        return isWall(getType(i, j));
    }

    public boolean isWall(Block block) {
        return isWall(block.getType());
    }

    private boolean isWall(int type) {
        return type < 0;
    }

    /**
     * 如果进行了自定义操作，那么就要对缓存进行刷新
     * 这是为了保障帮助功能正常.
     */
    public void refreshCache() {
        cache = new HashMap<>(GameConfig.MAX_TYPE);
        validSize = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (!isUsefulBlock(i, j)) {
                    continue;
                }
                int type = getType(i, j);
                putToCache(type, new Index(i, j));
                validSize++;
            }
        }
    }

    /**
     * 清除完成调用
     */
    private void clearOk(int type) {
        //如果清除缓存的大小大于等于2，就需要移除0上的数据
        //消除缓存中只留两个数据
        if (clearCache.size() >= 2) {
            clearCache.remove(0);
        }
        //添加即可
        clearCache.add(type);
        //如果存在监听事件
        //就需要执行监听事件
        if (onClearedListener != null) {
            onClearedListener.change(blocks, cache);
            //因为本来是照正常套路来的，但是重新定义了规则，你不知道会发生什么，所有进行刷新操作
            refreshCache();
        }
    }

    public interface OnClearedListener {
        /**
         * @param blocks 地图数据块
         * @param cache  缓存，key为类型，value为类型所在的位置
         */
        void change(Block[][] blocks, HashMap<Integer, ArrayList<Index>> cache);
    }
}
