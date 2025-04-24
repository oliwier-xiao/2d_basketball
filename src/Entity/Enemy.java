package Entity;

import Main.GameException;
import Main.Game_Panel;
import Object.OBJ_Basket_Hoop;
import Object.SuperObject;

import java.awt.Rectangle;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


public class Enemy extends Entity {
    private Random random = new Random();
    private int actionLockCounter = 0;
    private int targetX, targetY;
    private boolean hasPath = false;
    public Rectangle solidArea;

    public Enemy(Game_Panel gp) {
        super(gp);
        setDefaultValues();
        getEnemyImage();
    }

    private void setDefaultValues() {
        speed = 2;
        maxLife = 3;
        life = maxLife;
        solidArea = new Rectangle(8, 16, 32, 32);
        spawnNearHoop();
    }

    private void spawnNearHoop() {
        OBJ_Basket_Hoop hoop = null;
        for(SuperObject obj : gp.obj) {
            if(obj instanceof OBJ_Basket_Hoop) {
                hoop = (OBJ_Basket_Hoop)obj;
                break;
            }
        }
        if(hoop == null) {
            throw new GameException("Cannot spawn enemy - no basketball hoop exists in the game");
        }

        if(hoop != null) {
            Worldx = hoop.worldX + random.nextInt(400) - 200;
            Worldy = hoop.worldY + random.nextInt(400) - 200;

            Worldx = Math.max(0, Math.min(Worldx, gp.worldWidth - gp.tileSize));
            Worldy = Math.max(0, Math.min(Worldy, gp.worldHeight - gp.tileSize));

            System.out.println("[ENEMY SPAWN] At: " + Worldx + ", " + Worldy +
                    " | Hoop was at: " + hoop.worldX + ", " + hoop.worldY);
        } else {
            System.out.println("[ENEMY SPAWN ERROR] No hoop found!");
        }
    }

    public void getEnemyImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/Enemy/enemy_up1.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Enemy/enemy_down1.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Enemy/enemy_left1.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Enemy/enemy_right1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Update speed based on score
        speed = 2 + (gp.score / 5);

        // Change direction every 2 seconds (120 frames)
        actionLockCounter++;
        if(actionLockCounter >= 120 || !hasPath) {
            getNewDirection();
            actionLockCounter = 0;
            hasPath = true;
        }

        // Check collision before moving
        if(!checkCollision(Worldx + targetX, Worldy + targetY)) {
            Worldx += targetX;
            Worldy += targetY;
        } else {
            hasPath = false;
        }
    }

    private void getNewDirection() {
        // Get direction towards player
        if(random.nextInt(100) < 30) {
            int playerCenterX = gp.player.Worldx + (gp.player.gp.tileSize/2);
            int playerCenterY = gp.player.Worldy + (gp.player.gp.tileSize/2);

            int dx = playerCenterX - (Worldx + (gp.tileSize/2));
            int dy = playerCenterY - (Worldy + (gp.tileSize/2));

            double distance = Math.sqrt(dx*dx + dy*dy);

            if(distance != 0) {
                targetX = (int)((dx/distance) * speed);
                targetY = (int)((dy/distance) * speed);
            }
        } else {
            // Random direction
            targetX = (random.nextInt(3) - 1) * speed; //
            targetY = (random.nextInt(3) - 1) * speed;
        }
    }

    private boolean checkCollision(int newX, int newY) {
        // Check tile collision
        int left = newX + solidArea.x;
        int right = left + solidArea.width;
        int top = newY + solidArea.y;
        int bottom = top + solidArea.height;

        int leftCol = left / gp.tileSize;
        int rightCol = right / gp.tileSize;
        int topRow = top / gp.tileSize;
        int bottomRow = bottom / gp.tileSize;

        for(int col = leftCol; col <= rightCol; col++) {
            for(int row = topRow; row <= bottomRow; row++) {
                if(gp.tileManager.checkCollision(col * gp.tileSize, row * gp.tileSize)) {
                    return true;
                }
            }
        }

        // Check hoop collision
        for(SuperObject obj : gp.obj) {
            if(obj instanceof OBJ_Basket_Hoop && obj.collision) {
                Rectangle hoopBox = new Rectangle(
                        obj.worldX + obj.solidArea.x,
                        obj.worldY + obj.solidArea.y,
                        obj.solidArea.width,
                        obj.solidArea.height
                );

                Rectangle enemyBox = new Rectangle(
                        newX + solidArea.x,
                        newY + solidArea.y,
                        solidArea.width,
                        solidArea.height
                );

                if(hoopBox.intersects(enemyBox)) return true;
            }
        }

        return false;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        String dir = calculateDirection();

        switch(dir) {
            case "up": image = up1; break;
            case "down": image = down1; break;
            case "left": image = left1; break;
            case "right": image = right1; break;
        }

        // Convert world position to screen position
        int screenX = Worldx - gp.player.Worldx + gp.player.screenX;
        int screenY = Worldy - gp.player.Worldy + gp.player.screenY;

        // Draw with scaling (same as player)
        int scaledSize = gp.tileSize * 2; // Match player's scale if needed
        g2.drawImage(image, screenX, screenY, scaledSize, scaledSize, null);
    }

    private String calculateDirection() {
        if(Math.abs(targetX) > Math.abs(targetY)) {
            return targetX > 0 ? "right" : "left";
        } else {
            return targetY > 0 ? "down" : "up";
        }
    }
}