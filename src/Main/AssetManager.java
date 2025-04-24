package Main;

import Object.OBJ_Basket_Hoop;
import Object.SuperObject;
import Object.OBJ_Gatorade;
import Object.OBJ_Dumbell;
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
        placeObject(gp.obj[0]);


        gp.obj[1] = new OBJ_Gatorade();
        gp.obj[6] = new OBJ_Dumbell();

        for (int i = 1; i < gp.obj.length; i++) { // Skip index 0
            if (gp.obj[i] != null) placeObject(gp.obj[i]);
        }
    }

    void placeObject(SuperObject obj) {
        int maxAttempts = 100;
        int attempts = 0;
        boolean collisionFound;
        do {
            collisionFound = false;

            int bufferX = (int) (gp.worldWidth * 0.05);
            int bufferY = (int) (gp.worldHeight * 0.05);

            int maxValidX = gp.worldWidth - bufferX - obj.image.getWidth();
            int maxValidY = gp.worldHeight - bufferY - obj.image.getHeight();

            obj.worldX = bufferX + rand.nextInt(maxValidX - bufferX + 1);
            obj.worldY = bufferY + rand.nextInt(maxValidY - bufferY + 1);

            // Check tile collisions
            int startCol = obj.worldX / gp.tileSize;
            int startRow = obj.worldY / gp.tileSize;
            int endCol = (obj.worldX + obj.image.getWidth()) / gp.tileSize;
            int endRow = (obj.worldY + obj.image.getHeight()) / gp.tileSize;
            attempts++;
            if(attempts > maxAttempts) {
                throw new GameException("Failed to place object after " + maxAttempts + " attempts");
            }
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
        obj.timeActive = Math.max(60, 300 - (gp.score * 10));
        obj.spawnCooldown = 0;
        obj.active = true;

        System.out.println("[SPAWN] " + obj.name + " at: " + obj.worldX + ", " + obj.worldY);
    }

    public void respawnObject(int index) {
        if (index == 0) return;

        SuperObject obj = gp.obj[index];
        if (obj != null) {
            obj.active = false;
            obj.spawnCooldown = obj.maxCooldown;
        }
    }
    public void respawnHoop() {
        // Only handle index 0 (hoop)
        SuperObject hoop = gp.obj[0];
        if (hoop != null) {
            placeObject(hoop); // Reuse placement logic
            System.out.println("Hoop respawned at: " + hoop.worldX + ", " + hoop.worldY);
        }
    }
}