package Object;

import Entity.Projectile;
import Entity.Entity;
import Main.Game_Panel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Basketball extends Projectile {
    Game_Panel gp;

    public OBJ_Basketball(Game_Panel gp) {
        super(gp);
        this.gp = gp;

        name = "Basketball";
        speed = 5;
        maxLife = 100;
        life = maxLife;
        attack = 0;
        alive=false;
        getImage();
    }
    public void getImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Objects/basketball.png"));
        } catch (IOException e) {
            System.err.println("BŁĄD: Nie można załadować obrazka piłki!");
            e.printStackTrace();
        }
    }
}