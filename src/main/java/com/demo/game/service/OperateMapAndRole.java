package com.demo.game.service;

import com.demo.game.constant.GameMap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 本类是处理游戏数据类
 * 保存关卡地图的元素、元素分布的当前属性
 * 提供修改游戏当前属性的各种方法
 *
 * @author Jimmy
 */
public class OperateMapAndRole {
    /**
     * 角色移动方向
     */
    private final int UP = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;
    /**
     * 游戏关卡
     */
    private int level;
    /**
     * 当前正游戏地图的数据
     */
    private int[][] operateMap = null;
    private List<int[]> endList = null;
    /**
     * 当前地图的长宽
     */
    private int mapWidth;
    private int mapHeight;
    /**
     * 角色是否可以被操纵
     */
    private boolean playRole = true;
    /**
     * 悔棋标记
     */
    private boolean undoFlag;
    /**
     * 设置悔棋可以悔几步
     */
    public static final int RETRACT = 10;
    /**
     * 储存悔棋操作的数据
     */
    private LinkedList<ArrayList<int[]>> backList = new LinkedList<ArrayList<int[]>>();

    /**
     * 选择关卡初始化游戏
     *
     * @param level
     */
    public OperateMapAndRole(int level) {
        this.level = level;
        init();
    }

    private void init() {
        operateMap = GameMap.getMap(level);
        endList = getLocation(GameMap.END);
        mapWidth = operateMap.length;
        mapHeight = operateMap[0].length;
    }

    /**
     * 返回当前游戏关卡
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     * 返回当前地图大小
     * @return
     */
    public int getMapWidth() {
        return mapWidth;
    }
    public int getMapHeight() {
        return mapHeight;
    }

    /**
     * 返回当前地图的数据情况
     * @return
     */
    public int[][] getCurrentMap() {
        return operateMap;
    }

    /**
     * 获得指定元素所有坐标
     *
     * @param moveElement
     * @return
     */
    private List<int[]> getLocation(int moveElement) {
        List<int[]> list = new ArrayList<int[]>();
        int iLength = operateMap.length;
        int jLength = operateMap[0].length;
        for (int i = 0; i < iLength; i++) {
            for (int j = 0; j < jLength; j++) {
                if (operateMap[i][j] == moveElement) {
                    list.add(new int[]{i, j});
                }
            }
        }
        return list;
    }

    /**
     * 根据方向给增减量数组赋值
     *
     * @param direction
     * @return
     */
    private int[] direct(int direction) {
        switch (direction) {
            case UP:
                return new int[]{0, -1};
            case DOWN:
                return new int[]{0, 1};
            case LEFT:
                return new int[]{-1, 0};
            case RIGHT:
                return new int[]{1, 0};
            default:
                throw new RuntimeException("方向设置错误");
        }
    }

    /**
     * 移动元素的通用方法
     *
     * @param moveElement
     * @param direction
     * @param boxLocation
     * @return
     */
    private boolean move(int moveElement, int direction, int[]... boxLocation) {
        List<Integer> cantMoveList = new ArrayList<Integer>();
        cantMoveList.add(GameMap.HOLLOW);
        cantMoveList.add(GameMap.WALL);
        cantMoveList.add(GameMap.BOX);
        cantMoveList.add(GameMap.ENDBOX);
        // 根据输入的移动方向得到坐标增减量
        int[] change = direct(direction);

        // 输入的移动元素是角色
        if (moveElement == GameMap.ROLE) {
            int[] roleLocation = getLocation(GameMap.ROLE).get(0);
            int[] nextLocation = {roleLocation[0] + change[0], roleLocation[1] + change[1]};
            if (!cantMoveList.contains(operateMap[nextLocation[0]][nextLocation[1]])) {
                if (GameMap.getMap(getLevel())[roleLocation[0]][roleLocation[1]] == GameMap.END) {
                    operateMap[roleLocation[0]][roleLocation[1]] = GameMap.END;
                } else {
                    operateMap[roleLocation[0]][roleLocation[1]] = GameMap.PASS;
                }
                operateMap[nextLocation[0]][nextLocation[1]] = GameMap.ROLE;
                if (undoFlag) {
                    undoFlag = false;
                } else {
                    setRecord(roleLocation);
                }
                return true;
            }
        }

        // 输入的移动元素是箱子
        if (moveElement == GameMap.BOX) {
            int[] boxLoc = boxLocation[0];
            int[] nextLocation = boxLocation[1];
            // 对箱子当前坐标的判断
            if (!cantMoveList.contains(operateMap[nextLocation[0]][nextLocation[1]])) {
                if (GameMap.getMap(getLevel())[boxLoc[0]][boxLoc[1]] == GameMap.END) {
                    operateMap[boxLoc[0]][boxLoc[1]] = GameMap.END;
                } else {
                    operateMap[boxLoc[0]][boxLoc[1]] = GameMap.PASS;
                }
            }
            // 对箱子下一个坐标的判断
            if (!cantMoveList.contains(operateMap[nextLocation[0]][nextLocation[1]])) {
                if (GameMap.getMap(getLevel())[nextLocation[0]][nextLocation[1]] == GameMap.END) {
                    operateMap[nextLocation[0]][nextLocation[1]] = GameMap.ENDBOX;
                } else {
                    operateMap[nextLocation[0]][nextLocation[1]] = GameMap.BOX;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 移动箱子操作
     * @param direction
     */
    private void moveBox(int direction) {
        int[] direct = direct(direction);
        int[] roleLocation = getLocation(GameMap.ROLE).get(0);
        int[] boxLocation = {roleLocation[0] + direct[0], roleLocation[1] + direct[1]};
        int[] nextBoxLocation = {boxLocation[0] + direct[0], boxLocation[1] + direct[1]};
        if (operateMap[boxLocation[0]][boxLocation[1]] == GameMap.BOX
                || operateMap[boxLocation[0]][boxLocation[1]] == GameMap.ENDBOX) {
            if (move(GameMap.BOX, direction, boxLocation, nextBoxLocation)) {
                setRecord(roleLocation, boxLocation, nextBoxLocation);
                undoFlag = true;
            }
        }
    }

    /**
     * 记录悔棋步骤
     * @param roleLocation
     * @param boxLocation
     */
    public void setRecord(int[] roleLocation, int[]... boxLocation) {
        ArrayList<int[]> list = new ArrayList<int[]>();
        // 储存角色坐标
        list.add(roleLocation);
        // 储存箱子坐标
        if (boxLocation.length != 0) {
            for (int[] i : boxLocation) {
                list.add(i);
            }
        }
        // 储存到悔棋集合
        if (backList.size() < RETRACT) {
            backList.addLast(list);
        } else {
            backList.removeFirst();
            backList.addLast(list);
        }
    }

    /**
     * 悔棋操作
     */
    public void moveBack() {
        ArrayList<int[]> backInfo = null;
        int[] roleBackLocation = null;
        int[] boxBackLocation = null;
        int[] boxCurrentLocation = null;
        if (backList.size() != 0) {
            backInfo = backList.removeLast();
            if (backInfo.size() == 1) {
                roleBackLocation = backInfo.get(0);
            } else if (backInfo.size() == 3) {
                roleBackLocation = backInfo.get(0);
                boxBackLocation = backInfo.get(1);
                boxCurrentLocation = backInfo.get(2);
            }
            int[] roleLocation = getLocation(GameMap.ROLE).get(0);
            // 撤回角色动作
            if (GameMap.getMap(getLevel())[roleLocation[0]][roleLocation[1]] == GameMap.END) {
                operateMap[roleLocation[0]][roleLocation[1]] = GameMap.END;
            } else {
                operateMap[roleLocation[0]][roleLocation[1]] = GameMap.PASS;
            }
            operateMap[roleBackLocation[0]][roleBackLocation[1]] = GameMap.ROLE;
            // 撤回箱子动作
            if (boxBackLocation != null && boxCurrentLocation != null) {
                // BOX
                if (operateMap[boxCurrentLocation[0]][boxCurrentLocation[1]] == GameMap.BOX) {
                    if (GameMap.getMap(getLevel())[boxBackLocation[0]][boxBackLocation[1]] == GameMap.END) {
                        operateMap[boxBackLocation[0]][boxBackLocation[1]] = GameMap.ENDBOX;
                    } else {
                        operateMap[boxBackLocation[0]][boxBackLocation[1]] = GameMap.BOX;
                    }
                    operateMap[boxCurrentLocation[0]][boxCurrentLocation[1]] = GameMap.PASS;
                }
                // ENDBOX
                if (operateMap[boxCurrentLocation[0]][boxCurrentLocation[1]] == GameMap.ENDBOX) {
                    if (GameMap.getMap(getLevel())[boxBackLocation[0]][boxBackLocation[1]] == GameMap.END) {
                        operateMap[boxBackLocation[0]][boxBackLocation[1]] = GameMap.ENDBOX;
                    } else {
                        operateMap[boxBackLocation[0]][boxBackLocation[1]] = GameMap.BOX;
                    }
                    operateMap[boxCurrentLocation[0]][boxCurrentLocation[1]] = GameMap.END;
                }
            }
        }
    }

    /**
     * 判断关卡是否过关
      * @return
     */
    public boolean passLevel() {
        List<int[]> endBoxList = getLocation(GameMap.ENDBOX);
        if (endList.size() == endBoxList.size()) {
            playRole = false;
        }
        return !playRole;
    }

    public void moveToUp() {
        if (playRole) {
            moveBox(UP);
            move(GameMap.ROLE, UP);
            passLevel();
        }
    }

    public void moveToDown() {
        if (playRole) {
            moveBox(DOWN);
            move(GameMap.ROLE, DOWN);
            passLevel();
        }
    }

    public void moveToLeft() {
        if (playRole) {
            moveBox(LEFT);
            move(GameMap.ROLE, LEFT);
            passLevel();
        }
    }

    public void moveToRight() {
        if (playRole) {
            moveBox(RIGHT);
            move(GameMap.ROLE, RIGHT);
            passLevel();
        }
    }
}
