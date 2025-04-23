package Object;
import Main.Game_Panel;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Dumbell extends SuperObject {
    public OBJ_Dumbell() {
        name = "Dumbbell";
        collision = false;
        maxCooldown = 600; // 10 seconds

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/dumbell.png"));
            solidArea = new Rectangle(16, 16, 16, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}