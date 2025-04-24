package Object;

import Main.GameException;
import Main.Game_Panel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Heart extends SuperObject {
    public BufferedImage full, blank;

    public OBJ_Heart() {
        name = "Heart";
        loadImage();
        collision = false;
    }
    @Override
    public void loadImage() {
        try {
            full = ImageIO.read(getClass().getResourceAsStream("/Objects/heart_full.png"));
            blank = ImageIO.read(getClass().getResourceAsStream("/Objects/heart_blank.png"));

            if(full == null || blank == null) {
                throw new GameException("Heart textures not working properly: "
                        + (full != null) + ", blank: " + (blank != null) + ")");
            }

            image = full;
        } catch (IOException e) {
            throw new GameException("Failed to load heart textures", e);
        }
    }
}