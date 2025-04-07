// src/Entity/Player.java
package Entity;

import Main.Game_Panel;
import Main.Keys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    Game_Panel gp;
    Keys key;

    public int screenX;
    public int screenY;
    public int hasball = 1;

    public int scale = 2; // Współczynnik skalowania

    public Player(Game_Panel gp, Keys key) {
        this.gp = gp;
        this.key = key;
        setDefaultValues();
        getPlayerImage();
        direction = "right";
    }

    public void setDefaultValues() {
        Worldx = gp.tileSize * 10;
        Worldy = gp.tileSize * 10;
        speed = 4;
        screenX = gp.ScreenWidth / 2 - gp.tileSize / 2;
        screenY = gp.ScreenHeight / 2 - gp.tileSize / 2;
    }

    public void getPlayerImage() {
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
            e.printStackTrace();
        }
    }

    public void update() {
        if (key.up) {
            direction = "up";
            Worldy -= speed;
        } else if (key.down) {
            direction = "down";
            Worldy += speed;
        } else if (key.left) {
            direction = "left";
            Worldx -= speed;
        } else if (key.right) {
            direction = "right";
            Worldx += speed;
        }

        spriteCounter++;
        if(spriteCounter>12){
            if(spriteNum == 1){
                spriteNum = 2;
            } else if (spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }


        // Sprawdzenie granic mapy
        if (Worldx < 0) {
            Worldx = 0;
        } else if (Worldx > gp.worldWidth - gp.tileSize) {
            Worldx = gp.worldWidth - gp.tileSize;
        }

        if (Worldy < 0) {
            Worldy = 0;
        } else if (Worldy > gp.worldHeight - gp.tileSize) {
            Worldy = gp.worldHeight - gp.tileSize;
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
}