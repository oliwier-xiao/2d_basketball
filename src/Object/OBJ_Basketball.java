package Object;

import Entity.Projectile;
import Main.Game_Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Basketball extends Projectile {
    Game_Panel gp;
    public static final int COOLDOWN_TIME = 300; // 5 seconds at 60 FPS (60*5=300)
    public int cooldownCounter = 0;

    public OBJ_Basketball(Game_Panel gp) {
        super(gp);
        this.gp = gp;

        name = "Basketball";
        speed = 5;
        maxLife = 100;
        life = maxLife;
        attack = 0;
        alive = false;

        getImage();
    }

    public void getImage() {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
            int scaledSize = gp.tileSize;
            BufferedImage scaledImage = new BufferedImage(scaledSize, scaledSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(originalImage.getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();
            up1 = down1 = left1 = right1 = scaledImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCooldown() {
        if (cooldownCounter > 0) {
            cooldownCounter--;
        }
    }

    public boolean isReady() {
        return cooldownCounter <= 0;
    }

    public void startCooldown() {
        cooldownCounter = COOLDOWN_TIME;
    }

    public float getCooldownPercentage() {
        return (float) cooldownCounter / COOLDOWN_TIME;
    }
}