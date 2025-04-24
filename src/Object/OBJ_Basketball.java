package Object;

import Entity.Projectile;
import Main.GameException;
import Main.Game_Panel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Rectangle;

public class OBJ_Basketball extends Projectile {
    Game_Panel gp;
    public int cooldownDuration = 300; // 5 seconds at 60 FPS
    public int cooldownCounter = 0;

    public OBJ_Basketball(Game_Panel gp) {
        super(gp);
        this.gp = gp;

        name = "Basketball";
        speed = 6;
        maxLife = 100;
        life = maxLife;
        attack = 0;
        alive = false;

        getImage();

    }

    @Override
    public void update() {
        super.update(); // Call Projectile's update() for basic movement
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = switch(direction) {
            case "up" -> up1;
            case "down" -> down1;
            case "left" -> left1;
            case "right" -> right1;
            default -> up1;
        };

        int screenX = Worldx - gp.player.Worldx + gp.player.screenX;
        int screenY = Worldy - gp.player.Worldy + gp.player.screenY;
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }


    private void getImage() {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
            if(originalImage == null) {
                throw new GameException("Basketball projectile texture not found");
            }

            int scaledSize = gp.tileSize;
            BufferedImage scaledImage = new BufferedImage(scaledSize, scaledSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(originalImage.getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();

            up1 = down1 = left1 = right1 = scaledImage;
        } catch (IOException e) {
            throw new GameException("Failed to process basketball texture", e);
        }
    }

    public void updateCooldown() {
        if(cooldownCounter > 0) cooldownCounter--;
    }

    public boolean isReady() {
        return cooldownCounter <= 0;
    }

    public void startCooldown() {
        cooldownCounter = cooldownDuration;
    }

    public float getCooldownPercentage() {
        return (float) cooldownCounter / cooldownDuration;
    }
}