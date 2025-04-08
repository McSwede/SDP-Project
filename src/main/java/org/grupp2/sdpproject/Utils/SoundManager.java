package org.grupp2.sdpproject.Utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, MediaPlayer> soundPlayers;
    private MediaPlayer currentMusic;
    private List<MediaPlayer> playlist;
    private int currentTrackIndex = 0;
    private double volume = 0.5; // Default volume (50%)
    private boolean isPaused = false;

    private SoundManager() {
        soundPlayers = new HashMap<>();
        // Initialize JavaFX Media (important if not using JavaFX application)
        try {
            // This ensures JavaFX media is initialized
            new javafx.embed.swing.JFXPanel();
        } catch (Exception e) {
            System.err.println("JavaFX media initialization failed");
            e.printStackTrace();
        }
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Loads a sound file
     * @param name Key to identify the sound
     * @param soundFile Path to the sound file (relative to resources)
     */
    public void loadSound(String name, String soundFile) {
        try {
            URL resource = getClass().getResource(soundFile);
            if (resource == null) {
                System.err.println("Sound file not found: " + soundFile);
                return;
            }

            Media media = new Media(resource.toString());
            MediaPlayer player = new MediaPlayer(media);
            soundPlayers.put(name, player);
        } catch (Exception e) {
            System.err.println("Error loading sound: " + soundFile);
            e.printStackTrace();
        }

    }

    /**
     * Plays a sound effect once
     * @param name Name of the sound to play
     */
    public void playSound(String name) {
        MediaPlayer player = soundPlayers.get(name);
        if (player != null) {
            player.stop();
            player.setVolume(volume);
            player.play();
        } else {
            System.err.println("Sound not found: " + name);
        }
    }

    /**
     * Plays background music in loop
     * @param name Name of the music to play
     */
    public void playMusic(String name) {
        stopCurrentMusic();

        currentMusic = soundPlayers.get(name);
        if (currentMusic != null) {
            currentMusic.setCycleCount(MediaPlayer.INDEFINITE);
            currentMusic.setVolume(volume);
            currentMusic.play();
        } else {
            System.err.println("Music not found: " + name);
        }
    }

    public void playAllMusic() {
        stopCurrentMusic();

        if (soundPlayers.isEmpty()) {
            System.err.println("No music loaded.");
            return;
        }

        playlist = new ArrayList<>(soundPlayers.values());
        currentTrackIndex = 0;

        playTrack(currentTrackIndex);
    }

    private void playTrack(int index) {
        if (index >= playlist.size()) {
            // Restart playlist when it ends
            currentTrackIndex = 0;
            index = 0;
        }

        currentMusic = playlist.get(index);
        currentMusic.setVolume(volume);
        currentMusic.setOnEndOfMedia(() -> {
            currentTrackIndex++;
            playTrack(currentTrackIndex);
        });

        currentMusic.play();
    }


    /**
     * Sets the global volume (0.0 to 1.0)
     * @param volume Volume level between 0 (mute) and 1 (max)
     */
    public void setGlobalVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        if (currentMusic != null) {
            currentMusic.setVolume(this.volume);
        }
    }

    /**
     * Stops the currently playing music
     */
    public void stopCurrentMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    /**
     * Pauses the current music
     */
    public void pauseCurrentMusic() {
        if (currentMusic != null) {
            isPaused = true;
            currentMusic.pause();
        }
    }

    /**
     * Resumes the paused music
     */
    public void resumeCurrentMusic() {
        if (currentMusic != null) {
            isPaused = false;
            currentMusic.play();
        }
    }

    /**
     * Cleans up resources
     */
    public void cleanup() {
        stopCurrentMusic();
        for (MediaPlayer player : soundPlayers.values()) {
            player.dispose();
        }
        soundPlayers.clear();
    }

    public boolean isPaused() {
        return isPaused;
    }
}
