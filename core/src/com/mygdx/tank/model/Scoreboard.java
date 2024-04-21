package com.mygdx.tank.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scoreboard implements Observer {
    private final List<Player> players;
    private final HashMap<String, Integer> scoreboard;

    public Scoreboard() {
        players = new ArrayList<>();
        scoreboard = new HashMap<>();
    }


    // Method to add a player to the scoreboard

    public void addPlayer(Player player) {
        players.add(player);
        String userMail = player.getUserMail();
        scoreboard.put(userMail.split("@")[0], 0);
    }

    public Integer getPlayerScore(String playerName){
        return scoreboard.get(playerName);
    }
    @Override
    public void update(Player updatePlayer) {
        // Update the scoreboard based on the updated playerScore
        for (Player player : players) {
            if (player.getPlayerName().equals(updatePlayer.getPlayerName())) {
                String userMail = updatePlayer.getUserMail();
                String userName = userMail.split("@")[0];
                Integer newScore = updatePlayer.getPlayerScoreComponent().score;

                scoreboard.put(userName, newScore);

                break;
            }
        }
    }

    public HashMap<String, Integer> getScoreboard() {
        return this.scoreboard;
    }
}
