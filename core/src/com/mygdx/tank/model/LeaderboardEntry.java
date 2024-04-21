package com.mygdx.tank.model;

public class LeaderboardEntry {
    private String key;
    private String username;
    private int score;

    public LeaderboardEntry() {}

    public LeaderboardEntry(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
