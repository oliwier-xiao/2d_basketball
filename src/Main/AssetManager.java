package Main;

import Object.OBJ_Basket_Hoop;

import java.util.Random;
import java.util.Arrays;

public class AssetManager {
    Game_Panel gp;
    Random rand = new Random();

    public AssetManager(Game_Panel gp) {
        this.gp = gp;
    }

    public void setObject() {
        Arrays.fill(gp.obj, null);
        gp.obj[0] = new OBJ_Basket_Hoop();
        OBJ_Basket_Hoop hoop = (OBJ_Basket_Hoop) gp.obj[0];

        boolean collisionFound;
        do {
            collisionFound = false;

            // Calculate buffer zones (5% of world size)
            int bufferX = (int) (gp.worldWidth * 0.05);
            int bufferY = (int) (gp.worldHeight * 0.05);

            // Calculate max valid positions for X and Y
            int maxValidX = gp.worldWidth - bufferX - hoop.image.getWidth();
            int maxValidY = gp.worldHeight - bufferY - hoop.image.getHeight();

            // Generate random positions within the valid range
            hoop.worldX = bufferX + rand.nextInt(maxValidX - bufferX + 1); // +1 to include maxValidX
            hoop.worldY = bufferY + rand.nextInt(maxValidY - bufferY + 1);

            // Check collisions for tiles under the hoop
            int startCol = hoop.worldX / gp.tileSize;
            int startRow = hoop.worldY / gp.tileSize;
            int endCol = (hoop.worldX + hoop.image.getWidth()) / gp.tileSize;
            int endRow = (hoop.worldY + hoop.image.getHeight()) / gp.tileSize;

            for (int col = startCol; col <= endCol; col++) {
                for (int row = startRow; row <= endRow; row++) {
                    if (gp.tileManager.checkCollision(col * gp.tileSize, row * gp.tileSize)) {
                        collisionFound = true;
                        break;
                    }
                }
                if (collisionFound) break;
            }
        } while (collisionFound);

        // Debug log
        System.out.println("Hoop spawned at: " + hoop.worldX + ", " + hoop.worldY);
    }
}