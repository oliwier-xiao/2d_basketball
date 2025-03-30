package Entity;

import Main.Game_Panel;
import Main.Keys;
import java.awt.*;

public class Player extends Entity {
    Game_Panel gp;
    Keys key;

    public final int screenX;
    public final int screenY;
    public Player(Game_Panel gp, Keys key) {
        this.gp = gp;
        this.key = key;

        screenX = gp.ScreenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.ScreenHeight / 2 - (gp.tileSize / 2);
        setDefaultValues();
    }

    public void setDefaultValues() {
        Worldx = gp.tileSize * 28;
        Worldy = gp.tileSize * 28;
        speed = 4;
    }

    public void update() {
        if (key.up) {
            if (!gp.tileManager.checkCollision(Worldx, Worldy - speed)) {
                Worldy -= speed;
            }
        } else if (key.down) {
            if (!gp.tileManager.checkCollision(Worldx, Worldy + speed)) {
                Worldy += speed;
            }
        } else if (key.left) {
            if (!gp.tileManager.checkCollision(Worldx - speed, Worldy)) {
                Worldx -= speed;
            }
        } else if (key.right) {
            if (!gp.tileManager.checkCollision(Worldx + speed, Worldy)) {
                Worldx += speed;
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
    }
}