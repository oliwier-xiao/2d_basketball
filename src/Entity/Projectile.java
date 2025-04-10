package Entity;

import Main.Game_Panel;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Projectile extends Entity {

    Entity user;
    public Projectile(Game_Panel gp) {
        super(gp);
    }


    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.alive = alive;
        this.name = user.name;
        this.Worldx = worldX;
        this.Worldy = worldY;
        this.direction = direction;
        this.life=this.maxLife;
        this.user=user;
    }

    public void update() {
       switch(direction) {
            case "up":
                Worldy -= speed;
                break;
            case "down":
                Worldy += speed;
                break;
            case "left":
                Worldx -= speed;
                break;
            case "right":
                Worldx += speed;
                break;
        }
        life--;
       if (life <= 0) {
              alive = false;
       }
        }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = up1;
                break;
            case "down":
                image = down1;
                break;
            case "left":
                image = left1;
                break;
            case "right":
                image = right1;
                break;
        }

        int screenX = Worldx - gp.player.Worldx + gp.player.screenX;
        int screenY = Worldy - gp.player.Worldy + gp.player.screenY;
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

    }

}