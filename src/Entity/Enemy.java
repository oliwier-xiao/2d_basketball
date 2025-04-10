package Entity;
import java.awt.*;
import Main.Game_Panel;
import java.util.Random;

public class Enemy extends Entity {
    private Random random;
    Game_Panel g;

    public Enemy(Game_Panel gp, int speed) {
        super(gp);
        this.random = new Random();
        this.Worldx = random.nextInt(800); // Assuming map width is 800
        this.Worldy = random.nextInt(600); // Assuming map height is 600
        this.speed = speed;
        this.g = gp;
    }

    public void update() {
        int direction = random.nextInt(4);
        switch (direction) {
            case 0 -> Worldx += speed; // Move right
            case 1 -> Worldx -= speed; // Move left
            case 2 -> Worldy += speed; // Move down
            case 3 -> Worldy -= speed; // Move up
        }
    }

//    public void draw(Graphics2D g2) {
//        g2.setColor(Color.red);
//        g2.fillRect(Worldx, Worldy, g.tileSize, g.tileSize);
//    }
}