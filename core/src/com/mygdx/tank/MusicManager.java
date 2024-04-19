package com.mygdx.tank;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class MusicManager {
    private HashMap<String, Music> tracks;
    private Music currentTrack;
    private Music gameMusic;
    private String currentTrackName;
    private boolean isMenuMusicPlaying = true;
    private boolean isGameMusicPlaying = true;

    public MusicManager() {
        initializeTracks();
    }

    private void initializeTracks() {
        tracks = new HashMap<>();
        tracks.put("To Victory", Gdx.audio.newMusic(Gdx.files.internal("Music/To Victory.mp3")));
        tracks.put("Heart of Courage", Gdx.audio.newMusic(Gdx.files.internal("Music/Heart of Courage.mp3")));
        tracks.put("Fever Dream", Gdx.audio.newMusic(Gdx.files.internal("Music/Fever Dream.mp3")));
        tracks.put("Fortunate Son", Gdx.audio.newMusic(Gdx.files.internal("Music/09 - Fortunate Son.mp3")));

        currentTrackName = "To Victory";
        currentTrack = tracks.get("To Victory");
        currentTrack.setLooping(true);
        currentTrack.play();

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/44 End Credits.mp3"));

    }

    public MusicMemento saveToMemento() {
        return new MusicMemento(currentTrackName, isMenuMusicPlaying);
    }

    public void restoreFromMemento(MusicMemento memento) {
        if (!currentTrackName.equals(memento.getTrack())){
            changeTrack(memento.getTrack());
        }
        if (memento.isPlaying() != this.isMenuMusicPlaying) {
            muteMenuMusic(!memento.isPlaying());
        }
    }

    public void changeTrack(String track) {
        if (currentTrack != null && currentTrack.isPlaying()) {
            currentTrack.stop();
        }
        currentTrack = tracks.get(track);
        currentTrackName = track;
        if (isMenuMusicPlaying && currentTrack != null) {
            currentTrack.setLooping(true);
            currentTrack.play();
        }
    }

    public void muteMenuMusic(boolean playing) {
        if (currentTrack != null) {
            if (playing) {
                isMenuMusicPlaying = false;
                currentTrack.pause();
            } else {
                isMenuMusicPlaying = true;
                currentTrack.play();
            }
        }
    }

    public void muteGameMusic(boolean playing) {
        if (gameMusic != null) {
            if (playing) {
                isGameMusicPlaying = false;
                gameMusic.pause();
            } else {
                isGameMusicPlaying = true;
                gameMusic.play();
            }
        }
    }

    public void startGameMusic() {
        if (currentTrack.isPlaying()) {
            currentTrack.stop();
        }
        gameMusic.setLooping(true);
        gameMusic.play();
    }

    public void stopGameMusic() {
        if (gameMusic.isPlaying()) {
            gameMusic.stop();
        }
        currentTrack.play(); // Resume background music
    }

    public boolean isMenuMusicPlaying() {
        return currentTrack != null && isMenuMusicPlaying;
    }

    public boolean isGameMusicPlaying(){
        return isGameMusicPlaying;
    }

    public String getCurrentTrackName(){return currentTrackName;}

    public void dispose() {
        for (Music track : tracks.values()) {
            track.dispose();
        }
    }

}

