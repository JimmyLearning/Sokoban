package com.demo.game;

import com.demo.game.frame.GameFrame;

/**
 * 游戏主程序入口，默认从第1关开始
 *
 * @author Jimmy
 *
 * 游戏操作：
 * 1. 上下左右键移动人物
 * 2. N键重开当前关卡
 * 3. B键悔棋,默认可悔棋10步
 */
public class StartGame {
    public static void main(String[] args) {
        // 空参从第一关开始
        GameFrame gameFrame = new GameFrame();
        // 带参从第N关开始，如第2关
        // GameFrame gameFrame = new GameFrame(2);
        gameFrame.start();
    }
}
