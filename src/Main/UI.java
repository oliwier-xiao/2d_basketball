package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class UI {

    Game_Panel gp;
    Font arial_40, arial_80;
    BufferedImage keyImage;

    public UI(Game_Panel gp) {
        this.gp = gp;
        arial_40 = new Font("arial", Font.PLAIN, 40);
        arial_80 = new Font("arial", Font.BOLD, 80);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/Font/ThaleahFat.ttf")).deriveFont(48f);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 48); // Czcionka zapasowa
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

}