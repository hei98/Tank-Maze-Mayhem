package com.mygdx.tank;

import com.mygdx.tank.model.Observer;
import com.mygdx.tank.model.components.PlayerScoreComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {

    private String playerName;
    private String userMail;
    private PlayerScoreComponent playerScore;

    public Player(String playerName, String userMail) {
        this.playerName = playerName;
        this.userMail = userMail;
        this.playerScore = new PlayerScoreComponent();
    }

    public Player() {}

    public String getPlayerName() {
        return playerName;
    }

    public String getUserMail() {
        return this.userMail;
    }

    public PlayerScoreComponent getPlayerScoreComponent(){return playerScore;}

    public void setPlayerName(String newPlayername) {
        this.playerName = newPlayername;
    }
}
