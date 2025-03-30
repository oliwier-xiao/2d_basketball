package Main;

import Entity.Enemy;
import Entity.Player;
import map.TileManager;

import javax.swing.*;
import java.awt.*;

public class Game_Panel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 pikseli dla obiektu
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int ScreenWidth = tileSize * maxScreenCol; // 768 pikseli
    public final int ScreenHeight = tileSize * maxScreenRow; // 576 pikseli

    //WORLD SETTINGS
    public final int maxWorldCol = 57;
    public final int maxWorldRow = 57;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;
    UI ui;
    public TileManager tileManager;

    public Keys key = new Keys();
    Thread gameThread;
    public Player player = new Player(this, key);
    Enemy enemy = new Enemy(this, 2);

    // GAME STATES
    public int gameState;
    public int TitleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int endState = 3;
    public int selectedOption;

    public Game_Panel() {
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(key);
        this.setFocusable(true);
        selectedOption = 0;
        startGameThread();
        setupGame();

        ui = new UI(this);
        tileManager = new TileManager(this, maxWorldCol, maxWorldRow);
    }

    public void setupGame() {
        gameState = TitleState;
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if (remainingTime > 0) {
                    Thread.sleep((long) remainingTime);
                    nextDrawTime += drawInterval;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (gameState == TitleState) {
            if (!key.keyPressed) {
                if (key.up) {
                    selectedOption = (selectedOption + 2) % 3;
                    key.keyPressed = true;
                }
                if (key.down) {
                    selectedOption = (selectedOption + 1) % 3;
                    key.keyPressed = true;
                }
            }
            if (!key.up && !key.down) {
                key.keyPressed = false;
            }
            if (key.enter) {
                if (selectedOption == 0) {
                    gameState = playState;
                } else if (selectedOption == 1) {
                    // Handle options
                } else if (selectedOption == 2) {
                    System.exit(0);
                }
                key.enter = false; // Resetowanie flagi enter
            }
        }
        if (gameState == playState) {
            player.update();
            enemy.update();
            if (key.escape) {
                gameState = pauseState;
                key.escape = false; // Resetowanie flagi escape
            }
        }
        if (gameState == pauseState) {
            if (key.enter) {
                gameState = playState;
                key.enter = false; // Resetowanie flagi enter
            }
            if (key.escape) {
                gameState = TitleState;
                key.escape = false; // Resetowanie flagi enter
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Debugowanie rysowania
        long drawStart = 0;
        if (key.checkoutDrawTime) {
            drawStart = System.nanoTime();
        }

         // Rysowanie mapy
        ui.draw(g2); // Rysowanie Interfejsu Użytkownika

        if (gameState == playState) {
            tileManager.draw(g2);
            player.draw(g2);
          //  enemy.draw(g2);
            this.setOpaque(false); // Ustawienie przezroczystości tła
        }

        if (key.checkoutDrawTime) {
            long drawEnd = System.nanoTime();
            long elapsedTime = drawEnd - drawStart;
            System.out.println("Draw Time: " + elapsedTime);
        }

        g2.dispose();
    }
}