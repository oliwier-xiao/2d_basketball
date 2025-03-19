package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keys implements KeyListener {
    public boolean up, down, left, right;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_W) {
            up = true;
            System.out.println("W");
        }
        if(keyCode == KeyEvent.VK_S) {
            down = true;
            System.out.println("S");
        }
        if(keyCode == KeyEvent.VK_A) {
            left = true;
            System.out.println("A");
        }
        if(keyCode == KeyEvent.VK_D) {
            right = true;
            System.out.println("D");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode == KeyEvent.VK_W) {
            up = false;
        }
        if(keyCode == KeyEvent.VK_S) {
            down = false;

        }
        if(keyCode == KeyEvent.VK_A) {
            left = false;

        }
        if(keyCode == KeyEvent.VK_D) {
            right = false;

        }
    }
}
