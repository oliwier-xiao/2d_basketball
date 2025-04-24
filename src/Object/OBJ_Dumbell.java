package Object;
import Main.GameException;
import Main.Game_Panel;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Dumbell extends SuperObject {
    public OBJ_Dumbell() {
        name = "Dumbbell";
        collision = false;
        maxCooldown = 600; // 10 seconds
        loadImage();

    }

    @Override
    public void loadImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/dumbell.png"));
            if (image == null) {
                throw new GameException("Dumbbell texture not found");
            }
            solidArea = new Rectangle(16, 16, 16, 16);
        } catch (IOException e) {
            throw new GameException("Failed to load dumbbell texture", e);
        }
    }
}