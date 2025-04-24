package Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    private Clip themeClip;
    private Clip dribbleClip;
    private Clip swishClip;
    private boolean isThemePlaying = false;

    public void stopDribble() {
        if (dribbleClip != null && dribbleClip.isRunning()) {
            dribbleClip.stop();
        }
    }
    public void loadSounds() {
        try {

            themeClip = loadClip("/Sound/theme.wav");

            dribbleClip = loadClip("/Sound/dribble.wav");

            swishClip = loadClip("/Sound/swish.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Clip loadClip(String path) {
        try {
            URL url = getClass().getResource(path);
            if(url == null) {
                throw new GameException("Sound file not found: " + path);
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            throw new GameException("Failed to load audio clip: " + path, e);
        }
    }

    public void playTheme() {
        if (!isThemePlaying && themeClip != null) {
            themeClip.loop(Clip.LOOP_CONTINUOUSLY);
            isThemePlaying = true;
        }
    }

    public void stopTheme() {
        if (themeClip != null && themeClip.isRunning()) {
            themeClip.stop();
            themeClip.setFramePosition(0);
            isThemePlaying = false;
        }
    }

    public void playDribble() {
        playSound(dribbleClip);
    }

    public void playSwish() {
        playSound(swishClip);
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }
}