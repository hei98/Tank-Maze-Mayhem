package com.mygdx.tank.model;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.controllers.ApplicationController;

import java.util.ArrayList;
import java.util.List;

public class MenuModel implements IModel {
    private static MenuModel instance;
    private boolean isLoggedIn;
    private String userEmail;
    private ArrayList<LeaderboardEntry> leaderboardEntries;
    private String errorLabel = "";
    private int tutorialPageIndex = 0;
    private List<Player> connectedPlayers;
    private boolean startGame;
    private Client client;

    private MenuModel() {
        leaderboardEntries = new ArrayList<>();
        connectedPlayers = new ArrayList<>();
        client = new Client();
        startGame = false;
    }

    public static synchronized MenuModel getInstance() {
        if(instance == null) {
            instance = new MenuModel();
        }
        return instance;
    }

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

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public boolean startGame() {
        return startGame;
    }
    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }
}
