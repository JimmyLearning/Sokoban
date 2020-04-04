package com.demo.game.frame;

import com.demo.game.constant.GameMap;
import com.demo.game.service.OperateMapAndRole;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 游戏图形化界面类
 * 生成游戏GUI，并监听玩家操作
 *
 * @author Jimmy
 * @version 1.0
 */
public class GameFrame extends JFrame {
    private static final long serialVersionUID = 2365547596656493524L;

    private OperateMapAndRole mapAndRole;

    /**
     * picture
     */
    private BufferedImage pic[] = null;

    /**
     * title icon
     */
    private BufferedImage titleIcon = null;

    /*
     * show tips
     */
    static {
        JOptionPane.showMessageDialog(null, "1. 上下左右键移动\n2. N键重开当前关卡，共" + GameMap.getTotalLevel() + "关\n3. B键悔棋，可悔棋"
                + OperateMapAndRole.RETRACT + "步", "游戏操作", JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * load resources
     */ {
        pic = new BufferedImage[7];
        for (int i = 0; i < pic.length; i++) {
            try {
                URL ico = GameFrame.class.getResource("/images/ico.png");
                titleIcon = ImageIO.read(ico.openStream());
                URL url = GameFrame.class.getResource("/images/pic" + i + ".png");
                InputStream stream = url.openStream();
                pic[i] = ImageIO.read(stream);
            } catch (IOException e) {
                throw new RuntimeException("未读取到图片资源");
            }
        }
    }

    /**
     * start from lv.1
     */
    public GameFrame() {
        this(1);
    }

    /**
     * start from lv.X
     *
     * @param level
     */
    public GameFrame(int level) {
        super("推箱");
        mapAndRole = new OperateMapAndRole(level);
        setSize(mapAndRole.getMapWidth() * 30, mapAndRole.getMapHeight() * 30);
        initFrame();
        setFrameCenter(this);
        this.setIconImage(titleIcon);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * add a key listener
     */
    private void initFrame() {
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    mapAndRole.moveToUp();
                    repaint();
                    pass();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    mapAndRole.moveToDown();
                    repaint();
                    pass();
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    mapAndRole.moveToLeft();
                    repaint();
                    pass();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    mapAndRole.moveToRight();
                    repaint();
                    pass();
                }
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    reStart();
                }
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    mapAndRole.moveBack();
                    repaint();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }
        });
    }

    // 根据游戏是否过关来操作图形界面
    private void pass() {
        if (mapAndRole.passLevel()) {
            JOptionPane.showMessageDialog(this, "第" + mapAndRole.getLevel() + "关过关！");
            this.dispose();
            int lv = this.mapAndRole.getLevel();
            if (lv < GameMap.getTotalLevel()) {
                new GameFrame(this.mapAndRole.getLevel() + 1).start();
            } else if (lv == GameMap.getTotalLevel()) {
                JOptionPane.showMessageDialog(this, "所有关卡全部过关！");
                System.exit(0);
            }
        }
    }

    /**
     * start paint
     */
    public void start() {
        this.repaint();
    }

    private void reStart() {
        this.dispose();
        new GameFrame(this.mapAndRole.getLevel()).start();
    }

    @Override
    public void paint(Graphics g) {
        // super.paint(g);
        int[][] currentMap = mapAndRole.getCurrentMap();
        for (int i = 0; i < currentMap.length; i++) {
            for (int j = 0; j < currentMap[0].length; j++) {
                g.drawImage(pic[currentMap[i][j]], i * 30, j * 30, this);
            }
        }
    }

    // 图形界面居中显示
    private void setFrameCenter(JFrame f) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        double screenWidth = d.getWidth();
        double screenHeight = d.getHeight();
        int frameWidth = f.getWidth();
        int frameHeight = f.getHeight();

        int pointWidth = (int) (screenWidth - frameWidth) / 2;
        int pointHeight = (int) (screenHeight - frameHeight) / 2;

        f.setLocation(pointWidth, pointHeight);
    }
}
