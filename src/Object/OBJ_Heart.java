package Object;

import Main.Game_Panel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Heart extends SuperObject {
    public BufferedImage full, blank;

    public OBJ_Heart() {
        name = "Heart";
        try {
            full = ImageIO.read(getClass().getResourceAsStream("/Objects/heart_full.png"));
            blank = ImageIO.read(getClass().getResourceAsStream("/Objects/heart_blank.png"));
            image = full; // Default image
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}