package com.mygdx.tank;

import com.mygdx.tank.model.components.KillDeathComponent;
import com.mygdx.tank.model.components.PlayerScoreComponent;


public class Player {

    private String playerName;
    private String userMail;
    private PlayerScoreComponent playerScore;
    private KillDeathComponent killDeathComponent;

    public Player(String playerName, String userMail) {
        this.playerName = playerName;
        this.userMail = userMail;
        this.playerScore = new PlayerScoreComponent();
        this.killDeathComponent = new KillDeathComponent();
    }

    public Player() {}

    public String getPlayerName() {
        return playerName;
    }

    public String getUserMail() {
        return this.userMail;
    }

    public PlayerScoreComponent getPlayerScoreComponent(){return playerScore;}
    public KillDeathComponent getKillDeathComponent(){return killDeathComponent;}

    public void setPlayerName(String newPlayername) {
        this.playerName = newPlayername;
    }
}
