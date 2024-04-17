package com.mygdx.tank;

public class Player {

    private String playerName;
    private String userMail;

    public Player(String playerName, String userMail) {
        this.playerName = playerName;
        this.userMail = userMail;
    }

    public Player() {}

    public String getPlayerName() {
        return playerName;
    }

    public String getUserMail() {
        return this.userMail;
    }

    public void setPlayerName(String newPlayername) {
        this.playerName = newPlayername;
    }
}
