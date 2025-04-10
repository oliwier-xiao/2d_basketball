package Entity;

import Main.Game_Panel;

import java.awt.image.BufferedImage;

public class Entity {

    Game_Panel gp;
    public int Worldx;
    public int Worldy;
    public int projectileX;
    public int projectileY;
    public int currentworldX;
    public int currentworldY;

    // Pictures
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    // Sprite Animation
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // Attributes
    public String name;
    public int speed;
    public Projectile projectile;
    public int attack; // 0 - no attack, 1 - attack
    public int maxLife;
    public int life;


    // Items
    public int hasball = 0; // 0 - no ball, 1 - has ball
    public boolean alive;


    public Entity(Game_Panel gp) {
        this.gp = gp;
    }


}