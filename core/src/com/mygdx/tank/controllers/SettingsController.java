package com.mygdx.tank.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.MainMenuView;
import com.mygdx.tank.Views.SettingsView;
import com.mygdx.tank.model.MenuScreens.MenuModel;

import java.lang.reflect.Constructor;

public class SettingsController implements IController{
    private MenuModel model;
    private SettingsView view;
    private TankMazeMayhem game;
    private AccountService accountService;

    public SettingsController(MenuModel model, SettingsView view, TankMazeMayhem game, AccountService accountService) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.accountService = accountService;
    }

    @Override
    public void updateModelView() {

    }

    @Override
    public void addListeners() {
        view.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToMainMenu();
//                game.setScreen(new MainMenuView(game, accountService));
            }
        });
        view.getSoundControlButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("InfoTag", "MuteButton pressed");
                boolean isPlaying = game.isMusicPlaying();
                Gdx.app.log("InfoTag", "isPlaying?" + isPlaying);
                game.muteMusic(isPlaying);
            }
        });
    }

    @Override
    public Screen getView() {
        return view;
    }
}
