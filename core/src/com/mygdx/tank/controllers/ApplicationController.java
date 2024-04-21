package com.mygdx.tank.controllers;

import com.esotericsoftware.kryonet.Client;

import com.mygdx.tank.AccountService;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.InPartyView;
import com.mygdx.tank.Views.LeaderboardView;
import com.mygdx.tank.Views.LobbyView;
import com.mygdx.tank.Views.MainMenuView;
import com.mygdx.tank.Views.SettingsView;
import com.mygdx.tank.Views.SignInView;
import com.mygdx.tank.Views.SignUpView;
import com.mygdx.tank.Views.TutorialView;
import com.mygdx.tank.model.MenuModel;

public class ApplicationController {
    private static ApplicationController instance;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final MenuModel model;

    private InPartyController inPartyController;
    private LeaderboardController leaderboardController;
    private LobbyController lobbyController;
    private MainMenuController mainMenuController;
    private SettingsController settingsController;
    private SignInController signInController;
    private SignUpController signUpController;
    private TutorialController tutorialController;

    private ApplicationController(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
        this.model = MenuModel.getInstance();
    }

    public static synchronized ApplicationController getInstance(TankMazeMayhem game, AccountService accountService) {
        if(instance == null) {
            instance = new ApplicationController(game, accountService);
        }
        return instance;
    }

    public void switchToMainMenu() {
        mainMenuController = new MainMenuController(model, new MainMenuView(game, model), game, accountService);
        game.setScreen(mainMenuController.getView());
        mainMenuController.addListeners();
        mainMenuController.updateModelView();
    }

    public void switchToSettings() {
        settingsController = new SettingsController(model, new SettingsView(game, model), game, accountService);
        game.setScreen(settingsController.getView());
        settingsController.addListeners();
    }

    public void switchToLeaderboard() {
        leaderboardController = new LeaderboardController(model, new LeaderboardView(game), game, accountService, game.getFirebaseInterface());
        game.setScreen(leaderboardController.getView());
        leaderboardController.updateModelView();
        leaderboardController.addListeners();
    }

    public void switchToSignIn() {
        signInController = new SignInController(model, new SignInView(game, model), game, accountService);
        game.setScreen(signInController.getView());
        signInController.addListeners();
    }

    public void switchToSignUp() {
        signUpController = new SignUpController(model, new SignUpView(game, model), game, accountService);
        game.setScreen(signUpController.getView());
        signUpController.addListeners();
    }

    public void switchToTutorial() {
        tutorialController = new TutorialController(model, new TutorialView(game, model), game, accountService);
        game.setScreen(tutorialController.getView());
        tutorialController.addListeners();
        tutorialController.updateModelView();
    }

    public void switchToLobby() {
        lobbyController = new LobbyController(model, new LobbyView(game, model), game, accountService);
        game.setScreen(lobbyController.getView());
        lobbyController.addListeners();
    }

    public void switchToInParty(Client client) {
        inPartyController = new InPartyController(model, new InPartyView(game, model), game, accountService, client);
        game.setScreen(inPartyController.getView());
        inPartyController.addListeners();
    }
}
