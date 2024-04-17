package com.mygdx.tank;

public class User {
    private final String userMail;
    private Player player;

    public User(String userMail) {
        this.userMail = userMail;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getUserMail() {
        return userMail;
    }
}
