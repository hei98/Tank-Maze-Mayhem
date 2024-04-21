package com.mygdx.tank.controllers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.MainMenuView;
import com.mygdx.tank.model.MenuModel;

public class MainMenuController implements IController{
    private MenuModel model;
    private MainMenuView view;
    private TankMazeMayhem game;
    private AccountService accountService;

    public MainMenuController(MenuModel model, MainMenuView view, TankMazeMayhem game, AccountService accountService) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.accountService = accountService;
    }

    public Screen getView() {
        return view;
    }

    public void updateModelView() {
        // Check if the user is logged in from accountservice
        boolean isLoggedIn = accountService.hasUser();
        String userEmail = isLoggedIn ? accountService.getCurrentUserEmail() : null;
        //Update the model
        model.update(isLoggedIn, userEmail);

        // Update the view
        view.updateView(model);
    }

    public void addListeners() {
        // Add listeners to buttons
        view.getMultiplayerButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (accountService.hasUser() && game.getShowTutorial()){
                    game.setShowTutorial(false);
//                    Gdx.app.log("");
                    ApplicationController.getInstance(game, accountService).switchToTutorial();
//                    game.setScreen(new TutorialView(game, new LobbyView(game, game.getFirebaseInterface(), accountService), accountService));
                } else if (accountService.hasUser()) {
                    ApplicationController.getInstance(game, accountService).switchToLobby();
//                    game.setScreen(new LobbyView(game, game.getFirebaseInterface(), accountService));
                }
            }
        });
        view.getSettingsButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToSettings();
//                game.setScreen(new SettingsView(game, accountService));
            }
        });
        view.getLoginButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (accountService.hasUser()){
                    accountService.signOut();
                    ApplicationController.getInstance(game, accountService).switchToMainMenu();
//                    game.setScreen( new MainMenuView(game, model));
                }
                else{
                    ApplicationController.getInstance(game, accountService).switchToSignIn();
//                    game.setScreen(new SignInView(game, accountService));
                }
            }
        });
        view.getLeaderboardButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToLeaderboard();
//                game.setScreen(new LeaderboardView(game, game.getFirebaseInterface(), accountService));
            }
        });
    }
}
