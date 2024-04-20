package com.mygdx.tank.model;

import com.mygdx.tank.IModel;
import com.mygdx.tank.LeaderboardEntry;

import java.util.ArrayList;

public class MenuModel implements IModel {
    private boolean isLoggedIn;
    private String userEmail;
    private ArrayList<LeaderboardEntry> leaderboardEntries;
    private String errorLabel = "";

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
}
