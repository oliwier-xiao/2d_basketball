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
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/basket_hoop.png"));


            solidArea = new Rectangle(
                    16,
                    48,
                    96,
                    16
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Dummy values
        maxCooldown = 0;
        spawnCooldown = 0;
    }
}