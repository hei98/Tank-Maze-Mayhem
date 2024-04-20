package com.mygdx.tank.model;

import com.mygdx.tank.IModel;
import com.mygdx.tank.LeaderboardEntry;
import com.mygdx.tank.Player;

import java.util.ArrayList;
import java.util.List;

public class MenuModel implements IModel {
    private boolean isLoggedIn;
    private String userEmail;
    private ArrayList<LeaderboardEntry> leaderboardEntries;
    private String errorLabel = "";
    private int tutorialPageIndex = 0;
    private List<Player> connectedPlayers = new ArrayList<>();
    private boolean startGame;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void update(boolean loggedIn, String userEmail) {
        this.isLoggedIn = loggedIn;
        this.userEmail = userEmail;
    }

    public void updateLeaderboard(ArrayList<LeaderboardEntry> leaderboardEntries) {
        this.leaderboardEntries = leaderboardEntries;
    }

    public void updateErrorLabel(String errorLabel) {
        this.errorLabel = errorLabel;
    }
    public String getErrorLabel() {
        return errorLabel;
    }

    public ArrayList<LeaderboardEntry> getLeaderboardEntries() {
        return leaderboardEntries;
    }

    public String getAccountLabel() {
        return isLoggedIn ? "User email: " + userEmail : "Not logged in";
    }

    public int getTutorialPageIndex() {
        return tutorialPageIndex;
    }

    public void setTutorialPageIndex(int index) {
        this.tutorialPageIndex = index;
    }

    public List<Player> getConnectedPlayers() {
        return connectedPlayers;
    }

    public void setConnectedPlayers(List<Player> players) {
        connectedPlayers = players;
    }
}
