package Entity;

import Main.GameException;
import Main.Game_Panel;
import Main.Keys;
import Object.OBJ_Basketball;
import Object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    public Game_Panel gp;
    Keys key;

    public OBJ_Basketball basketball;
    public int screenX, screenY;
    public int scale = 2; // Współczynnik skalowania

    public Player(Game_Panel gp, Keys key) {
        super(gp);
        basketball = new OBJ_Basketball(gp);
        this.gp = gp;
        this.key = key;
        setDefaultValues();
        getImage();
        direction = "right";
    }

    public void setDefaultValues() {
        Worldx = gp.tileSize * 10;
        Worldy = gp.tileSize * 10;
        speed = 10;
        life=3;
        screenX = gp.ScreenWidth / 2 - gp.tileSize / 2;
        screenY = gp.ScreenHeight / 2 - gp.tileSize / 2;
        hasball = 1; // Domyślnie gracz ma piłkę
        projectile = new OBJ_Basketball(gp);
        damageCooldown=0;
    }

    public void getImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/Player/playerball_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Player/playerball_up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Player/playerball_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Player/playerball_down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Player/playerball_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Player/playerball_left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Player/playerball_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Player/playerball_right2.png"));
        } catch (IOException e) {
            throw new GameException("Failed to load player textures", e);
        }
    }

    public void update() {
        handleMovement();
        updateSprite();
        checkMapBoundaries();
        if (basketball != null) {
            basketball.updateCooldown();
        }

        if (gp.key.shoot) {
            shootProjectile();
            gp.key.shoot = false;
        }

        if(damageCooldown > 0) damageCooldown--;
    }

    private void handleMovement() {
        int newWorldx = Worldx;
        int newWorldy = Worldy;

        if (key.up) {
            newWorldy -= speed;
        } else if (key.down) {
            newWorldy += speed;
        } else if (key.left) {
            newWorldx -= speed;
        } else if (key.right) {
            newWorldx += speed;
        }

        // Check collision at the new position
        if (!checkCollision(newWorldx, newWorldy)) {
            Worldx = newWorldx;
            Worldy = newWorldy;
            // Update direction after confirming movement
            if (key.up) direction = "up";
            else if (key.down) direction = "down";
            else if (key.left) direction = "left";
            else if (key.right) direction = "right";
        }
    }

    private boolean checkCollision(int newWorldx, int newWorldy) {
        int scaledSize = gp.tileSize * scale;

        // Player's projected hitbox
        Rectangle playerHitbox = new Rectangle(
                newWorldx,
                newWorldy,
                scaledSize,
                scaledSize
        );

        // Check tile collisions
        int startCol = newWorldx / gp.tileSize;
        int startRow = newWorldy / gp.tileSize;
        int endCol = (newWorldx + scaledSize) / gp.tileSize;
        int endRow = (newWorldy + scaledSize) / gp.tileSize;

        for (int col = startCol; col <= endCol; col++) {
            for (int row = startRow; row <= endRow; row++) {
                if (gp.tileManager.checkCollision(col * gp.tileSize, row * gp.tileSize)) {
                    return true;
                }
            }
        }

        // Check object collisions
        for (SuperObject obj : gp.obj) {
            if (obj != null && obj.collision) {
                // Object's collision area in world coordinates
                Rectangle objectHitbox = new Rectangle(
                        obj.worldX + obj.solidArea.x,
                        obj.worldY + obj.solidArea.y,
                        obj.solidArea.width,
                        obj.solidArea.height
                );

                if (playerHitbox.intersects(objectHitbox)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void updateSprite() {
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    private void checkMapBoundaries() {
        Worldx = Math.max(0, Math.min(Worldx, gp.worldWidth - gp.tileSize));
        Worldy = Math.max(0, Math.min(Worldy, gp.worldHeight - gp.tileSize));
    }

    private void shootProjectile() {
        if (basketball != null && basketball.isReady()) {
            projectileX = Worldx + (gp.tileSize / 2) - 12;
            projectileY = Worldy + (gp.tileSize / 2) - 12;
            Projectile newProjectile = new OBJ_Basketball(gp); // Create a new object
            newProjectile.set(projectileX, projectileY, direction, true, this);
            gp.projectileList.add(newProjectile);
            basketball.startCooldown();

            if (gp.projectileList.size() > 10) {
                gp.projectileList.remove(0);
            }

        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        if (hasball == 1) {
            switch (direction) {
                case "up" -> {
                    if (spriteNum == 1) {
                        image = up1;
                    } else if (spriteNum == 2) {
                        image = up2;
                    }
                }
                case "down" -> {
                    if (spriteNum == 1) {
                        image = down1;
                    } else if (spriteNum == 2) {
                        image = down2;
                    }
                }
                case "left" -> {
                    if (spriteNum == 1) {
                        image = left1;
                    } else if (spriteNum == 2) {
                        image = left2;
                    }
                }
                case "right" -> {
                    if (spriteNum == 1) {
                        image = right1;
                    } else if (spriteNum == 2) {
                        image = right2;
                    }
                }
            }
            int scaledSize = gp.tileSize * scale;
            g2.drawImage(image, screenX, screenY, scaledSize, scaledSize, null);
        }


    }
    public void checkEnemyCollision() {
        Rectangle playerHitbox = new Rectangle(
                Worldx, Worldy,
                gp.tileSize * scale,
                gp.tileSize * scale
        );

        for(Enemy enemy : gp.enemyList) {
            Rectangle enemyHitbox = new Rectangle(
                    enemy.Worldx + enemy.solidArea.x,
                    enemy.Worldy + enemy.solidArea.y,
                    enemy.solidArea.width,
                    enemy.solidArea.height
            );

            if(playerHitbox.intersects(enemyHitbox) && damageCooldown <= 0) {
                life--;
                damageCooldown = 60;
                if(life <= 0) {
                    gp.gameState = gp.endState;
                }
                break;
            }
        }
    }
}