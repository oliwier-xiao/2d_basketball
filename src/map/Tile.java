package map;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    public boolean collision =false;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}