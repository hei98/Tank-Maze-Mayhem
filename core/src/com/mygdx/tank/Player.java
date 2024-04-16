package com.mygdx.tank;

import java.util.List;

public class Player {

    private String playerName;
    private String userId; // samme som sin User! Hentet fra firebase

    public Player(String playerName, String userId) {
        this.playerName = playerName;
        this.userId = userId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getUserId() {
        return this.userId;
    }
}
