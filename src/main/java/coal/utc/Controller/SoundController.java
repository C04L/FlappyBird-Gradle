package coal.utc.Controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundController {
    private Map<String, MediaPlayer> sounds;
    private MediaPlayer backgroundMusic;

    private boolean soundEnabled = true;
    private double soundVolume = 0.7;
    public static SoundController instance = new SoundController();

    public SoundController() {
        sounds = new HashMap<String, MediaPlayer>();
        initializeSounds();
    }

    public static SoundController getInstance() {
        if (instance == null) {
            instance = new SoundController();
        }
        return instance;
    }

    private void initializeSounds() {
        try {
            URL jumpSource = getClass().getResource("/Assets/sounds/hop.m4a");
            if (jumpSource != null) {
                sounds.put("jump", new MediaPlayer(new Media(jumpSource.toString())));
            }

            URL backgroundSource = getClass().getResource("/Assets/sounds/background.mp3");
            if (backgroundSource != null) {
                backgroundMusic = new MediaPlayer(new Media(backgroundSource.toString()));
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundMusic.setVolume(0.7);
            }
        } catch (Exception e) {
            System.err.println("Error loading sound files: " + e.getMessage());
        }
    }

    public void playSound(String soundName) {
         if (!soundEnabled) return;

        MediaPlayer player = sounds.get(soundName);
        player.setVolume(0.5);
        if (player != null) {
            player.stop();
            player.play();
        }
    }

    public void playBackgroundMusic() {
        if (!soundEnabled) return;

        if (backgroundMusic != null) {
            backgroundMusic.setVolume(0.7);
            backgroundMusic.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }


    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void toggleSound() {
        soundEnabled = !soundEnabled;
    }
}
