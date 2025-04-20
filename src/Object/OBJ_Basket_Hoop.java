package Object;

import Main.Game_Panel;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Basket_Hoop extends SuperObject {
    public OBJ_Basket_Hoop() {
        name = "Basket Hoop";
        collision = true;

        try {
            // Load the 128x128 image (no scaling!)
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/basket_hoop.png"));

            // Collision box (adjust as needed)
            solidArea = new Rectangle(32, 64, 64, 32); // Example: Center-aligned
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}