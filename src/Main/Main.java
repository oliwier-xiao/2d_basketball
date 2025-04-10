package Main;

import javax.swing.*; // klasy i komponenty interfejsu GUI

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Basketball_2D");

        Game_Panel game_panel = new Game_Panel();
        frame.add(game_panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}