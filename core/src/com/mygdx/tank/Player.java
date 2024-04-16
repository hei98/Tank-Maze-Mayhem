package com.mygdx.tank;

import com.mygdx.tank.model.components.PlayerScoreComponent;

import java.util.List;

public class Player {

    private String playerName;
    private String userId; // samme som sin User! Hentet fra firebase
    private PlayerScoreComponent playerScore;



    public Player(String playerName, String userId) {
        this.playerName = playerName;
        this.userId = userId;
        this.playerScore = new PlayerScoreComponent();
    }

    public Player() {}

    public String getPlayerName() {
        return playerName;
    }
    public PlayerScoreComponent getPlayerScoreComponent(){return playerScore;}

    public String getUserId() {
        return this.userId;
    }

    public void setPlayerName(String newPlayername) {
        this.playerName = newPlayername;
    }
}
