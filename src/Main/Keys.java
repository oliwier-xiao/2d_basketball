package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keys implements KeyListener {
    public boolean up, down, left, right, enter, escape, shoot;
    public boolean checkoutDrawTime;
    public boolean keyPressed = false;

    // New movement tracking
    public boolean movementStarted = false;
    public boolean movementStopped = false;
    private boolean wasMoving = false;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        boolean anyMovementBefore = up || down || left || right;

        if (keyCode == KeyEvent.VK_W) up = true;
        if (keyCode == KeyEvent.VK_S) down = true;
        if (keyCode == KeyEvent.VK_A) left = true;
        if (keyCode == KeyEvent.VK_D) right = true;
        if (keyCode == KeyEvent.VK_ENTER) enter = true;
        if (keyCode == KeyEvent.VK_ESCAPE) escape = true;
        if (keyCode == KeyEvent.VK_SPACE) shoot = true;

        // Detect movement start
        boolean anyMovementNow = up || down || left || right;
        if (!anyMovementBefore && anyMovementNow) {
            movementStarted = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        boolean anyMovementBefore = up || down || left || right;

        if (keyCode == KeyEvent.VK_W) up = false;
        if (keyCode == KeyEvent.VK_S) down = false;
        if (keyCode == KeyEvent.VK_A) left = false;
        if (keyCode == KeyEvent.VK_D) right = false;
        if (keyCode == KeyEvent.VK_ENTER) enter = false;
        if (keyCode == KeyEvent.VK_ESCAPE) escape = false;
        if (keyCode == KeyEvent.VK_SPACE) shoot = false;

        // Detect movement stop
        boolean anyMovementNow = up || down || left || right;
        if (anyMovementBefore && !anyMovementNow) {
            movementStopped = true;
        }
    }

    public void resetMovementFlags() {
        movementStarted = false;
        movementStopped = false;
    }
}