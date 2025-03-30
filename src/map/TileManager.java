package map;

import Main.Game_Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TileManager extends JPanel {
    private static final int TILE_SIZE = 48; // Rozmiar kafelka w pikselach
    private BufferedImage[][] tiles;
    private Tile[][] tileObjects;
    private int maxWorldCol;
    private int maxWorldRow;
    private Game_Panel gp;

    public TileManager(Game_Panel gp, int maxWorldCol, int maxWorldRow) {
        this.gp = gp;
        this.maxWorldCol = maxWorldCol;
        this.maxWorldRow = maxWorldRow;
        tiles = new BufferedImage[maxWorldCol][maxWorldRow];
        tileObjects = new Tile[maxWorldCol][maxWorldRow];
        loadTiles();
    }

    private void loadTiles() {
        for (int y = 0; y < maxWorldRow; y++) {
            for (int x = 0; x < maxWorldCol; x++) {
                try {
                    String path = WorldMap.getTilePath(x, y);
                    tiles[y][x] = ImageIO.read(new File(path));
                    tileObjects[y][x] = new Tile();
                    tileObjects[y][x].setImage(tiles[y][x]);
                    if (x == 0 || y == 0 || x == maxWorldCol - 1 || y == maxWorldRow - 1) {
                        tileObjects[y][x].collision = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        super.paintComponent(g2);
        for (int y = 0; y < maxWorldRow; y++) {
            for (int x = 0; x < maxWorldCol; x++) {
                if (tiles[y][x] != null) {
                    int worldX = x * TILE_SIZE;
                    int worldY = y * TILE_SIZE;
                    int screenX = worldX - gp.player.Worldx + gp.player.screenX;
                    int screenY = worldY - gp.player.Worldy + gp.player.screenY;
                    if(worldX + gp.tileSize > gp.player.Worldx - gp.player.screenX &&
                            worldX - gp.tileSize < gp.player.Worldx + gp.player.screenX + gp.tileSize &&
                            worldY + gp.tileSize > gp.player.Worldy - gp.player.screenY &&
                            worldY - gp.tileSize < gp.player.Worldy + gp.player.screenY + gp.tileSize) {
                        g2.drawImage(tiles[y][x], screenX, screenY, TILE_SIZE, TILE_SIZE, null);
                    }
                }
            }
        }
    }

    public boolean checkCollision(int worldX, int worldY) {
        int col = worldX / TILE_SIZE;
        int row = worldY / TILE_SIZE;
        if (col >= 0 && col < maxWorldCol && row >= 0 && row < maxWorldRow) {
            return tileObjects[row][col].collision;
        }
        return false;
    }
}