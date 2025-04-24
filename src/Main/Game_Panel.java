package Main;

import Entity.Enemy;
import Entity.Entity;
import Entity.Player;
import Entity.Projectile;
import map.TileManager;
import Object.SuperObject;
import Object.OBJ_Dumbell;
import Object.OBJ_Gatorade;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import javax.sound.sampled.Clip;

public class Game_Panel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 pixels for an object
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int ScreenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int ScreenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
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
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Enemy> enemyList = new ArrayList<>();

    public SuperObject obj[] = new SuperObject[10];
    public AssetManager aManager;

    //Sound
    public Sound sound = new Sound();

    // GAME STATES
    public int gameState;
    public int TitleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int endState = 3;
    public int selectedOption;

    // SCORING
    public int score = 0;

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
        sound.loadSounds();
    }

    public void startGameThread() {
        if (gameThread != null && gameThread.isAlive()) {
            throw new GameException("Game thread already running");
        }
        gameThread = new Thread(() -> {
            final double drawInterval = 1_000_000_000.0 / FPS; // Nanoseconds per frame
            double nextDrawTime = System.nanoTime() + drawInterval;

            while (gameThread != null) {
                try {
                    update();
                    repaint();

                    double remainingTime = nextDrawTime - System.nanoTime();
                    if (remainingTime > 0) {
                        Thread.sleep((long) (remainingTime / 1_000_000)); // Convert to milliseconds
                    }
                    nextDrawTime += drawInterval;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        gameThread.start();
    }




    @Override
    public void run() {
        // Original method replaced with a Swing Timer-based game loop
    }

    public void update() {
        if (gameState == TitleState) {
            sound.playTheme();
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
                    sound.stopTheme();
                } else if (selectedOption == 1) {
                    // Handle options
                } else if (selectedOption == 2) {
                    System.exit(0);
                }
                key.enter = false;
            }
        }
        if (gameState == playState) {
            if (key.movementStarted) {
                sound.playDribble();
            }
            if (key.movementStopped) {
                sound.stopDribble();
            }
            key.resetMovementFlags();

            player.update();
            player.checkEnemyCollision();
            updateEnemies();
            for (int i = 0; i < projectileList.size(); i++) {
                Entity e = projectileList.get(i);
                if (e != null) {
                    if (e.alive && e.life > 0) {
                        if (e instanceof Projectile) {
                            ((Projectile) e).update();

                            // Check collision with hoop
                            if (checkProjectileHoopCollision(e)) {
                                score++;
                                aManager.respawnHoop();
                                e.alive = false;
                                sound.playSwish();
                            }

                            // Check collision with enemies
                            for (Enemy enemy : enemyList) {
                                Rectangle projectileRect = new Rectangle(
                                        e.Worldx, e.Worldy,
                                        this.tileSize, this.tileSize
                                );
                                Rectangle enemyRect = new Rectangle(
                                        enemy.Worldx + enemy.solidArea.x,
                                        enemy.Worldy + enemy.solidArea.y,
                                        enemy.solidArea.width,
                                        enemy.solidArea.height
                                );

                                if (projectileRect.intersects(enemyRect)) {
                                    score = Math.max(0, score - 1);
                                    e.alive = false;
                                    break;
                                }
                            }
                        }
                    } else {
                        projectileList.remove(i);
                        i--;
                    }
                }
            }

            if (key.escape) {
                gameState = pauseState;
                key.escape = false;
            }

            updateObjects();
            manageObjectCount();
        }
        if (gameState == pauseState) {
            this.setBackground(Color.black);
            if (key.enter) {
                gameState = playState;
                key.enter = false;
            }
            if (key.escape) {
                gameState = TitleState;
                key.escape = false;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        long drawStart = 0;
        if (key.checkoutDrawTime) {
            drawStart = System.nanoTime();
        }

        if (gameState == playState) {
            tileManager.draw(g2);
            player.draw(g2);
            this.setBackground(Color.decode("#f8b48c"));
            for (int i = 0; i < projectileList.size(); i++) {
                Projectile p = (Projectile) projectileList.get(i);
                if (p != null && p.alive) {
                    p.draw(g2);
                }
            }
            for (Enemy enemy : enemyList) {
                enemy.draw(g2);
            }
            for (SuperObject object : obj) {
                if (object != null && object.active && object.image != null) {
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
            for (int i = 0; i < obj.length; i++) {
                SuperObject object = obj[i];
                if (object != null && object.active) {
                    if (object.name.equals("Gatorade") && checkPlayerObjectCollision(object)) {
                        player.speed++;
                        aManager.respawnObject(i);
                    } else if (object.name.equals("Dumbbell") && checkPlayerObjectCollision(object)) {
                        player.basketball.cooldownDuration = (int) Math.max(60,
                                player.basketball.cooldownDuration * 0.8);
                        aManager.respawnObject(i);
                    }
                }
            }
        }

        if (key.checkoutDrawTime) {
            long drawEnd = System.nanoTime();
            long elapsedTime = drawEnd - drawStart;
            System.out.println("Draw Time: " + elapsedTime);
        }
        ui.draw(g2);
        g2.dispose();
    }

    public boolean checkProjectileHoopCollision(Entity projectile) {
        if (obj[0] == null) return false;

        SuperObject hoop = obj[0];

        int pLeft = projectile.Worldx;
        int pRight = pLeft + this.tileSize;
        int pTop = projectile.Worldy;
        int pBottom = pTop + this.tileSize;

        int rimLeft = hoop.worldX + 32;
        int rimRight = rimLeft + 64;
        int rimTop = hoop.worldY + 48;
        int rimBottom = rimTop + 16;

        return pLeft < rimRight &&
                pRight > rimLeft &&
                pTop < rimBottom &&
                pBottom > rimTop;
    }

    public boolean checkPlayerObjectCollision(SuperObject object) {
        Rectangle playerRect = new Rectangle(
                player.Worldx,
                player.Worldy,
                this.tileSize * player.scale,
                this.tileSize * player.scale
        );

        Rectangle objectRect = new Rectangle(
                object.worldX + object.solidArea.x,
                object.worldY + object.solidArea.y,
                object.solidArea.width,
                object.solidArea.height
        );

        return playerRect.intersects(objectRect);
    }

    private void updateObjects() {
        for (int i = 0; i < obj.length; i++) {
            SuperObject object = obj[i];
            if (object != null) {
                if (object.active) {
                    object.timeActive--;
                    if (object.timeActive <= 0) {
                        aManager.respawnObject(i);
                    }
                } else {
                    object.spawnCooldown--;
                    if (object.spawnCooldown <= 0) {
                        aManager.placeObject(object);
                    }
                }
            }
        }
    }

    private void manageObjectCount() {
        if(score < 0) {
            throw new GameException("Negative score detected: " + score);
        }
        int targetCount = Math.min(5, 1 + (score / 5));

        for (int i = 1; i <= 5; i++) {
            if (i <= targetCount) {
                if (obj[i] == null) {
                    obj[i] = new OBJ_Gatorade();
                    aManager.placeObject(obj[i]);
                }
            } else {
                if (i < obj.length) obj[i] = null;
            }
        }

        for (int i = 6; i <= 9; i++) {
            if (i <= 6 + targetCount) {
                if (obj[i] == null) {
                    obj[i] = new OBJ_Dumbell();
                    aManager.placeObject(obj[i]);
                }
            } else {
                if (i < obj.length) obj[i] = null;
            }
        }
    }

    private void updateEnemies() {
        int targetEnemies = Math.min(10, 1 + (score / 3));

        while (enemyList.size() < targetEnemies) {
            Enemy newEnemy = new Enemy(this);
            enemyList.add(newEnemy);
            System.out.println("[ENEMY COUNT] Total enemies: " + enemyList.size());
        }

        while (enemyList.size() > targetEnemies) {
            enemyList.remove(0);
        }

        for (Enemy enemy : new ArrayList<>(enemyList)) {
            if (enemy.life <= 0) {
                enemyList.remove(enemy);
            } else {
                enemy.update();
            }
        }
    }
}