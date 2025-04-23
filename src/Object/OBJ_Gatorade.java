package Object;
import Main.Game_Panel;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Gatorade extends SuperObject {
    public OBJ_Gatorade() {
        name = "Gatorade";
        collision = false;
        maxCooldown = 600; // 10 seconds

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/gatorade.png"));
            solidArea = new Rectangle(16, 16, 16, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}