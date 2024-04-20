package com.mygdx.tank.controllers;

import com.mygdx.tank.AccountService;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.MainMenuView;
import com.mygdx.tank.Views.SettingsView;
import com.mygdx.tank.model.MenuScreens.MenuModel;

public class ApplicationController {
    private static ApplicationController instance;
    private TankMazeMayhem game;
    private AccountService accountService;
    private MenuModel model;

    private MainMenuController mainMenuController;
    private SettingsController settingsController;
    private LeaderboardController leaderboardController;
    private LobbyController lobbyController;
    private SignInController signInController;
    private SignUpController signUpController;
    private TutuorialController tutuorialController;

    private ApplicationController(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
        this.model = new MenuModel();

        initializeControllers();
    }

    public static synchronized ApplicationController getInstance(TankMazeMayhem game, AccountService accountService) {
        if(instance == null) {
            new ApplicationController(game, accountService);
        }
        return instance;
    }

    private void initializeControllers() {
        mainMenuController = new MainMenuController(model, new MainMenuView(game, model), game, accountService);
        settingsController = new SettingsController();
        leaderboardController = new LeaderboardController();
        lobbyController = new LobbyController();
        signInController = new SignInController(accountService);
        signUpController = new SignUpController(accountService);
        tutuorialController = new TutuorialController();
    }

    public void switchToMainMenu() {
        game.setScreen(mainMenuController.getView());
        mainMenuController.updateModelView();
        mainMenuController.addListeners();
    }

    public void switchToSettings() {
        game.setScreen(settingsController.getView());
        settingsController.updateView();
        settingsController.addListeners();
    }

    public void switchToLeaderboard() {
        game.setScreen(leaderboardController.getView());
        leaderboardController.updateView();
        leaderboardController.addListeners();
    }

    public void switchToSignIn() {
        game.setScreen(signInController.getView());
        signInController.updateView();
        signInController.addListeners();
    }

    public void switchToSignUp() {
        game.setScreen(signUpController.getView());
        signUpController.updateView();
        signUpController.addListeners();
    }

    public void switchToTutorial() {
        game.setScreen(tutuorialController.getView());
        tutuorialController.updateView();
        tutuorialController.addListeners();
    }

    public void switchToLobby() {
        game.setScreen(lobbyController.getView());
        lobbyController.updateView();
        lobbyController.addListeners();
    }
}
