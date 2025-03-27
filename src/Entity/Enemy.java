package Entity;
import java.awt.*;
import Main.Game_Panel;
import java.util.Random;

public class Enemy extends Entity {
    private Random random;
    Game_Panel g;

    public Enemy(Game_Panel g, int speed) {
        this.random = new Random();
        this.x = random.nextInt(800); // Assuming map width is 800
        this.y = random.nextInt(600); // Assuming map height is 600
        this.speed = speed;
        this.g = g;
    }

    public void update() {
        int direction = random.nextInt(4);
        switch (direction) {
            case 0 -> x += speed; // Move right
            case 1 -> x -= speed; // Move left
            case 2 -> y += speed; // Move down
            case 3 -> y -= speed; // Move up
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.red);
        g2.fillRect(x, y, g.tileSize, g.tileSize);
    }
}