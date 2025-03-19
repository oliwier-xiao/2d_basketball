package Entity;

import Main.Game_Panel;
import Main.Keys;

import java.awt.*;

public class Player extends Entity {

    Game_Panel g;
    Keys key;
    public Player(Game_Panel g, Keys key) {
        this.g = g;
        this.key = g.key;
        setDefaultValues();
    }

    public void setDefaultValues (){
        x = 100;
        y = 100;
        speed = 4;
    }
    public void update() {
        if (key.up) {
            y -= speed;
        }
        if (key.down) {
            y += speed;
        }
        if (key.left) {
            x -= speed;
        }
        if (key.right) {
            x += speed;
        }
    }
    public void draw(Graphics2D g2) {

        g2.setColor(Color.white);
        g2.fillRect(x,y, g.tileSize, g.tileSize);
    }
}
