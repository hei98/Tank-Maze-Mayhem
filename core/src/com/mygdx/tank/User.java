package com.mygdx.tank;

public class User {
    private String id;
    private Player player;

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void removePlayer() {
        this.player = null;
    }
}
