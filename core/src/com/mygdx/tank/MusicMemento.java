package com.mygdx.tank;


public class MusicMemento {
    private final String trackName;
    private final boolean isPlaying;

    public MusicMemento(String trackName, boolean isPlaying) {
        this.trackName = trackName;
        this.isPlaying = isPlaying;
    }

    public String getTrack() {
        return trackName;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
