package Entity;

import Main.Game_Panel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
// Abstract class can't have its own instance of objects
public abstract class Entity {

    Game_Panel gp;
    public int Worldx;
    public int Worldy;
    public int projectileX;
    public int projectileY;
    public int currentworldX;
    public int currentworldY;

    // Pictures
    public BufferedImage up1;
    public BufferedImage up2;
    public BufferedImage down1;
    public BufferedImage down2;
    public BufferedImage left1;
    public BufferedImage left2;
    public BufferedImage right1;
    public BufferedImage right2;
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
    public int damageCooldown = 0;

    // Items
    public int hasball = 0; // 0 - no ball, 1 - has ball
    public boolean alive;

    public Entity(Game_Panel gp) {
        this.gp = gp;
    }

    // Abstract methods
    public abstract void update();
    public abstract void draw(Graphics2D g2);
}