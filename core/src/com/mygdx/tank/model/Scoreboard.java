package com.mygdx.tank.model;

import com.mygdx.tank.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard implements Observer {
    private List<Player> players;
    private HashMap<String, Integer> scoreboard;

    public Scoreboard() {
        players = new ArrayList<>();
        scoreboard = new HashMap<>();
    }


    // Method to add a player to the scoreboard

    public void addPlayer(Player player) {
        players.add(player);
        String userMail = player.getUserMail();
        scoreboard.put(userMail.split("@")[0], 0);
        player.addObserver(this);
    }

    @Override
    public void update(Player updatePlayer) {
        // Update the scoreboard based on the updated playerScore
        for (Player player : players) {
            if (player.getPlayerName().equals(updatePlayer.getPlayerName())) {
                String userMail = updatePlayer.getUserMail();
                String userName = userMail.split("@")[0];
                Integer newScore = updatePlayer.getPlayerScoreComponent().getScore();

                scoreboard.put(userName, newScore);

                System.out.println("Updated " + updatePlayer.getPlayerName() + "'s score to " + newScore);
                System.out.println("The scoreboard looks like this:");
                for (Map.Entry<String, Integer> entry : scoreboard.entrySet()) {
                    String playerUserName = entry.getKey();
                    Integer score = entry.getValue();

                    // Now you can use userName and score as needed
                    System.out.println("User: " + playerUserName + ", Score: " + score);
                }
                break;
            }
        }
    }

    public HashMap<String, Integer> getScoreboard() {
        return this.scoreboard;
    }
}
