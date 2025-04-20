package Main;

import Entity.Enemy;
import Entity.Entity;
import Entity.Player;
import Entity.Projectile;
import map.TileManager;
import Object.SuperObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    public final int maxWorldRow = 41;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;
    UI ui;
    public TileManager tileManager;

    public Keys key = new Keys();
    Thread gameThread;


    public Player player = new Player(this, key);
    Enemy enemy = new Enemy(this, 2);
    public ArrayList<Entity> projectileList= new ArrayList<>();

    public SuperObject obj[] = new SuperObject[10];
    public AssetManager aManager;

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


        ui = new UI(this);
        tileManager = new TileManager(this, maxWorldCol, maxWorldRow);

        setupGame();
        startGameThread();
    }

    public void setupGame() {
        aManager = new AssetManager(this);
        gameState = TitleState;
        aManager.setObject();
    }

    public void startGameThread() {
        if (gameThread == null) {
            // ---------------------------------------------------------------
            // Swing Timer-based game loop (runs on EDT):
            // - Synchronizes game updates/rendering with Swing's event thread
            // - Prevents thread conflicts between key events and game state
            // - Includes error handling to prevent silent failures
            // ---------------------------------------------------------------
            Timer timer = new Timer(1000 / FPS, e -> {
                try {
                    update();
                    repaint();
                } catch (Exception ex) {
                    System.err.println("Game loop error:");
                    ex.printStackTrace();
                }
            });
            timer.setRepeats(true);
            timer.start();
        }
    }

    @Override
    public void run() {
        /* Original method to run the game loop on a separate thread switched
        to more efficient method cause of the player glitching:
         1. Calculates precise frame timing using System.nanoTime()
         2. Tries to maintain FPS via Thread.sleep()
         3. Causes thread conflicts between key events (EDT) and game updates
        //
        double drawInterval = (double) 1000000000 / FPS;
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

         */
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
            for (int i = 0; i < projectileList.size(); i++) {
                Entity e = projectileList.get(i);
                if (e != null) {
                    if (e.alive && e.life > 0) {
                        if (e instanceof Projectile) {
                            ((Projectile) e).update();
                        }
                    } else {
                        projectileList.remove(i);
                        i--;
                    }
                }
            }

            if (key.escape) {
                gameState = pauseState;
                key.escape = false; // Resetowanie flagi escape
            }



        }
        if (gameState == pauseState) {
            this.setBackground(Color.black);
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
        // Rysowanie Interfejsu Użytkownika

        if (gameState == playState) {
            tileManager.draw(g2);
            player.draw(g2);
            //  enemy.draw(g2);
            this.setBackground(Color.decode("#f8b48c"));
            for (int i = 0; i < projectileList.size(); i++) {
                Projectile p = (Projectile) projectileList.get(i);
                if (p != null && p.alive) {
                    p.draw(g2);
                }
            }
            for (SuperObject object : obj) {
                if (object != null && object.image != null) {
                    // Draw the image at its NATIVE size (128x128)
                    g2.drawImage(
                            object.image,
                            object.worldX - player.Worldx + player.screenX,
                            object.worldY - player.Worldy + player.screenY,
                            object.image.getWidth(),
                            object.image.getHeight(),
                            null
                    );
                }
            }
            ui.draw(g2);
        }else{
            ui.draw(g2);
        }

        if (key.checkoutDrawTime) {
            long drawEnd = System.nanoTime();
            long elapsedTime = drawEnd - drawStart;
            System.out.println("Draw Time: " + elapsedTime);
        }

        g2.dispose();
    }
}
