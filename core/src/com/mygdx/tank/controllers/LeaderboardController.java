package com.mygdx.tank.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.FirebaseDataListener;
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.model.LeaderboardEntry;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.LeaderboardView;
import com.mygdx.tank.model.MenuModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardController implements IController{
    private final FirebaseInterface firebaseInterface;
    private final MenuModel model;
    private final LeaderboardView view;
    private final TankMazeMayhem game;
    private final AccountService accountService;

    public LeaderboardController(MenuModel model, LeaderboardView view, TankMazeMayhem game, AccountService accountService, FirebaseInterface firebaseInterface) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.accountService = accountService;
        this.firebaseInterface = firebaseInterface;
    }

    @Override
    public void updateModelView() {
        // Get leaderboard entries and update the model
        fetchLeaderboardData();
    }

    @Override
    public void addListeners() {
        view.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToMainMenu();
            }
        });
        view.getSettingsButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToSettings();
            }
        });
    }

    @Override
    public Screen getView() {
        return view;
    }

    private void fetchLeaderboardData() {
        firebaseInterface.getLeaderboardData(new FirebaseDataListener() {
            @Override
            public void onDataReceived(Object data) {
                handleLeaderboardData((ArrayList<LeaderboardEntry>) data);
            }

            @Override
            public void onError(String errorMessage) {
                Gdx.app.log("Firebase", "Error fetching leaderboard " + errorMessage);
            }
        });
    }

    private void handleLeaderboardData(ArrayList<LeaderboardEntry> entries) {
        Gdx.app.postRunnable(() -> {
            Collections.sort(entries, new Comparator<LeaderboardEntry>() {
                @Override
                public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
                    return Integer.compare(o2.getScore(), o1.getScore());
                }
            });
            ArrayList<LeaderboardEntry> topFiveEntries = new ArrayList<>(entries.subList(0, Math.min(entries.size(), 5)));
            model.updateLeaderboard(topFiveEntries);
            if (model.getLeaderboardEntries() != null && !model.getLeaderboardEntries().isEmpty()) {
                view.updateView(model);
            } else {
                Gdx.app.log("Error", "Leaderboard data is empty or not initialized");
            }
        });
    }
}
