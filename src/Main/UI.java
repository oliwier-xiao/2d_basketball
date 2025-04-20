package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.imageio.ImageIO;

public class UI {

    Game_Panel gp;
    Font arial_40, arial_80;
    BufferedImage arrowImage;

    public UI(Game_Panel gp) {
        this.gp = gp;
        arial_40 = new Font("arial", Font.PLAIN, 40);
        arial_80 = new Font("arial", Font.BOLD, 80);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Font customFont;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/Font/ThaleahFat.ttf")).deriveFont(48f);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 48); // Czcionka zapasowa
        }

        if(gp.gameState==gp.playState) {
            try {
                arrowImage = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/Objects/Arrow.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (gp.player.basketball != null) {
                float cooldownPercentage = gp.player.basketball.getCooldownPercentage();
                int barWidth = 200;
                int barHeight = 20;
                int x = (gp.ScreenWidth - barWidth) / 2;
                int y = 20;

                g2.setColor(Color.GRAY);
                g2.fillRect(x, y, barWidth, barHeight);

                g2.setColor(Color.RED);
                g2.fillRect(x, y, (int) (barWidth * cooldownPercentage), barHeight);

                g2.setColor(Color.WHITE);
                g2.drawRect(x, y, barWidth, barHeight);

                g2.setFont(new Font("Arial", Font.BOLD, 14));
                String text = "Basketball Cooldown";
                int textWidth = g2.getFontMetrics().stringWidth(text);
                g2.drawString(text, (gp.ScreenWidth - textWidth) / 2, y + barHeight + 15);
            }

            drawHoopArrows(g2);
        }


        if (gp.gameState == gp.TitleState) {
            g2.setColor(Color.white);

            // Rysowanie tytułu
            g2.setFont(customFont.deriveFont(72f)); // Większa czcionka na tytuł
            FontMetrics fmTitle = g2.getFontMetrics();
            String title = "2D_Basketball";
            int titleX = (gp.ScreenWidth - fmTitle.stringWidth(title)) / 2;
            int titleY = fmTitle.getAscent() + 40; // Pozycja tytułu od góry z lekkim marginesem
            g2.drawString(title, titleX, titleY);

            // Resetowanie czcionki dla opcji
            g2.setFont(customFont.deriveFont(48f));
            FontMetrics fmOptions = g2.getFontMetrics();

            // Opcje menu
            String[] options = {"Start the Game", "Options", "Exit"};
            int startY = titleY + 100; // Ustawienie pierwszej opcji poniżej tytułu
            int spacing = 70; // Większy odstęp między opcjami

            for (int i = 0; i < options.length; i++) {
                int x = (gp.ScreenWidth - fmOptions.stringWidth(options[i])) / 2;
                int y = startY + i * spacing; // Każda opcja przesunięta o odstęp

                g2.drawString(options[i], x, y);

                if (gp.selectedOption == i) {
                    g2.drawString(">", x - fmOptions.stringWidth(">") - 20, y);
                }
            }
        }
        if (gp.gameState == gp.pauseState) {
            g2.setFont(customFont.deriveFont(50f));
            g2.setColor(Color.white);
            g2.drawString("Pause", 100, 100);
            g2.drawString("Press Enter to Resume", 100, 160);
            g2.drawString("Press Esc to Return to Menu", 80, 220);
        }
        if (gp.gameState==gp.endState) {
            g.setColor(Color.black);
            g.fillRect(0, 0, gp.ScreenWidth, gp.ScreenHeight);

            g.setColor(Color.white);
            g.setFont(arial_80);
            g.drawString("You Won !", 100, 100);

            gp.gameThread = null;
        } else {
            g.setFont(arial_40);
            g.setColor(Color.white);
        }
    }
    private void drawHoopArrows(Graphics2D g2) {
        int playerX = gp.player.Worldx;
        int playerY = gp.player.Worldy;
        int hoopXWorld = gp.obj[0].worldX;
        int hoopYWorld = gp.obj[0].worldY;

        // Calculate a direction from player to hoop
        double deltaX = hoopXWorld - playerX;
        double deltaY = hoopYWorld - playerY;
        double angle = Math.atan2(deltaY, deltaX);

        int screenX, screenY;

        // Determine if hoop is off-screen
        boolean isOffScreen = (hoopXWorld < playerX - gp.player.screenX ||
                hoopXWorld > playerX + gp.player.screenX ||
                hoopYWorld < playerY - gp.player.screenY ||
                hoopYWorld > playerY + gp.player.screenY);

        if (isOffScreen) {
            // Calculate edge position
            double directionX = deltaX / Math.hypot(deltaX, deltaY);
            double directionY = deltaY / Math.hypot(deltaX, deltaY);

            screenX = (int) (gp.ScreenWidth / 2 + directionX * (gp.ScreenWidth / 2 - 20));
            screenY = (int) (gp.ScreenHeight / 2 + directionY * (gp.ScreenHeight / 2 - 20));
        } else {
            // Use hoop's on-screen position
            screenX = gp.obj[0].worldX - playerX + gp.player.screenX;
            screenY = gp.obj[0].worldY - playerY + gp.player.screenY;
        }

        if (arrowImage != null) {

            AffineTransform oldTransform = g2.getTransform();
            g2.translate(screenX, screenY);
            g2.rotate(angle);
            // Center the 16x16 image (-8 offset)
            g2.drawImage(arrowImage, -8, -8, null);
            g2.setTransform(oldTransform);
        } else {
            // Fallback to original arrow drawing
            int arrowX = (int) (screenX + Math.cos(angle) * 30);
            int arrowY = (int) (screenY + Math.sin(angle) * 30);
            g2.setColor(Color.YELLOW);
            g2.drawLine(screenX, screenY, arrowX, arrowY);
            g2.fillPolygon(new int[]{arrowX, arrowX - 5, arrowX + 5}, new int[]{arrowY, arrowY - 10, arrowY - 10}, 3);
        }
    }

}