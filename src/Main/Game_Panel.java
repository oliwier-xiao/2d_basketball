package Main;

import Entity.Player;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Game_Panel extends JPanel implements Runnable {

    final int originalTileSize= 16; // 16x16 pikseli dla obiektu
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int ScreenWidth=tileSize * maxScreenCol;
    final int ScreenHeight=tileSize * maxScreenRow;

    int FPS=60;

    public Keys key = new Keys();
    Thread gameThread;
    Player player = new Player(this,key);

    //GAME STATES
    public int gameState;
    public int TitleState=0;
    public final int playState=1;
    public final int pauseState=2;
    public int selectedOption;

    public Game_Panel() {
        this.setPreferredSize(new Dimension(ScreenWidth,ScreenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(key);
        this.setFocusable(true);
        selectedOption=0;
        startGameThread();
        setupGame();
    }
    public void setupGame(){
        gameState= TitleState;
    }
    public void startGameThread() {
        if(gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        double drawInterval= 1000000000 /FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if (remainingTime > 0) {
                    Thread.sleep((long) remainingTime);
                    nextDrawTime+=drawInterval;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (gameState == TitleState) {
            if (!key.keyPressed) {
                if (key.up) {
                    selectedOption = (selectedOption + 2) % 3;
                    key.keyPressed = true;
                }
                if (key.down) {
                    selectedOption = (selectedOption + 1) % 3;
                    key.keyPressed = true;
                }
            }
            if (!key.up && !key.down) {
                key.keyPressed = false;
            }
            if (key.enter) {
                if (selectedOption == 0) {
                    gameState = playState;
                } else if (selectedOption == 1) {
                    // Handle options
                } else if (selectedOption == 2) {
                    System.exit(0);
                }
            }
        }
        if(gameState==playState) {
            player.update();
        }
       if(gameState==pauseState){
           //nothing
       }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Debugowanie rysowania
        long drawStart=0;
        if(key.checkoutDrawTime==true){
            drawStart=System.nanoTime();
        }

        if (gameState == TitleState) {
            g2.setColor(Color.white);
            Font customFont = null;
            try {
                customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/Font/ThaleahFat.ttf")).deriveFont(48f);
            } catch (Exception e) {
                e.printStackTrace();
                customFont = new Font("Arial", Font.BOLD, 48); // Czcionka zapasowa
            }

            // Rysowanie tytułu
            g2.setFont(customFont.deriveFont(72f)); // Większa czcionka na tytuł
            FontMetrics fmTitle = g2.getFontMetrics();
            String title = "2D_Basketball";
            int titleX = (ScreenWidth - fmTitle.stringWidth(title)) / 2;
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
                int x = (ScreenWidth - fmOptions.stringWidth(options[i])) / 2;
                int y = startY + i * spacing; // Każda opcja przesunięta o odstęp

                g2.drawString(options[i], x, y);


                if (selectedOption == i) {
                    g2.drawString(">", x - fmOptions.stringWidth(">") - 20, y);
                }
            }
        }

        if(gameState==playState){
            player.draw(g2);
        }
        if(gameState==pauseState){
            g2.setColor(Color.white);
            g2.drawString("Pause", 100, 100);
        }
        if (key.checkoutDrawTime == true) {
            long drawEnd = System.nanoTime();
            long elapsedTime = drawEnd - drawStart;
        }

        g2.dispose();
    }
}