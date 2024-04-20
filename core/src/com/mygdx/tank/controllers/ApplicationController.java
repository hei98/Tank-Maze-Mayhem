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
//        inPartyController = new InPartyController(model, new InPartyView(game), game, accountService);
        leaderboardController = new LeaderboardController(model, new LeaderboardView(game), game, accountService, game.getFirebaseInterface());
        lobbyController = new LobbyController(model, new LobbyView(game, model), game, accountService);
        mainMenuController = new MainMenuController(model, new MainMenuView(game, model), game, accountService);
        settingsController = new SettingsController(model, new SettingsView(game, model), game, accountService);
        signInController = new SignInController(model, new SignInView(game, model), game, accountService);
        signUpController = new SignUpController(model, new SignUpView(game, model), game, accountService);
        tutorialController = new TutorialController(model, new TutorialView(game, model), game, accountService);
    }

    public void switchToMainMenu() {
        game.setScreen(mainMenuController.getView());
        mainMenuController.updateModelView();
        mainMenuController.addListeners();
    }

    public void switchToSettings() {
        game.setScreen(settingsController.getView());
        settingsController.updateModelView();
        settingsController.addListeners();
    }

    public void switchToLeaderboard() {
        game.setScreen(leaderboardController.getView());
        leaderboardController.updateModelView();
        leaderboardController.addListeners();
    }

    public void switchToSignIn() {
        game.setScreen(signInController.getView());
        signInController.updateModelView();
        signInController.addListeners();
    }

    public void switchToSignUp() {
        game.setScreen(signUpController.getView());
        signUpController.updateModelView();
        signUpController.addListeners();
    }

    public void switchToTutorial() {
        game.setScreen(tutorialController.getView());
        tutorialController.updateModelView();
        tutorialController.addListeners();
    }

    public void switchToLobby() {
        game.setScreen(lobbyController.getView());
        lobbyController.updateModelView();
        lobbyController.addListeners();
    }

    public void switchToInParty(Client client) {
        inPartyController = new InPartyController(model, new InPartyView(game), game, accountService, client);
        game.setScreen(inPartyController.getView());
        inPartyController.updateModelView();
        inPartyController.addListeners();
    }
}
