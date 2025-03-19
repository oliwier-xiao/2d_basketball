package Main;

import Entity.Player;

import javax.swing.JPanel;
import java.awt.*;

public class Game_Panel extends JPanel implements Runnable {

    final int originalTileSize= 16; // 16x16 pikseli dla obiektu
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int ScreenWidth=tileSize * maxScreenCol;
    final int ScreenHeight=tileSize * maxScreenRow;

    int FPS=60;

    public Keys key = new Keys();
    Thread gameThread;
    Player player = new Player(this,key);
    int playerX= 100;
    int playerY=100;
    int playerSpeed=4;

    public Game_Panel() {
        this.setPreferredSize(new Dimension(ScreenWidth,ScreenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(key);
        this.setFocusable(true);
        startGameThread();
    }

    public void startGameThread() {
        if(gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        double drawInterval= 1000000000 /FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if (remainingTime > 0) {
                    Thread.sleep((long) remainingTime);
                    nextDrawTime+=drawInterval;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        player.draw(g2);

        g2.dispose();
    }
}