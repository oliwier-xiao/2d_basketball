package Object;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class SuperObject { // Add "abstract" keyword
    public BufferedImage image;
    public int worldX;
    public int worldY;
    public String name;
    public boolean collision = false;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public int spawnCooldown;
    public int maxCooldown = 600; // 10 seconds at 60 FPS
    public int timeActive;
    public boolean active = true;

    public abstract void loadImage();
}