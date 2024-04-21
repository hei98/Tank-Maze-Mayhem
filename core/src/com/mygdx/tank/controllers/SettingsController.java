package com.mygdx.tank.controllers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.MusicManager;
import com.mygdx.tank.MusicMemento;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.SettingsView;
import com.mygdx.tank.model.MenuModel;

public class SettingsController implements IController{
    private final MenuModel model;
    private final SettingsView view;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final MusicManager musicManager;
    private final MusicMemento initialMusicSettings;

    public SettingsController(MenuModel model, SettingsView view, TankMazeMayhem game, AccountService accountService) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.accountService = accountService;
        musicManager = game.getMusicManager();
        initialMusicSettings = musicManager.saveToMemento();
    }

    @Override
    public void updateModelView() {
        view.updateView(model);
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
                musicManager.muteMenuMusic(musicManager.isMenuMusicPlaying());
            }
        });
        view.getUndoButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(initialMusicSettings.isPlaying() != musicManager.isMenuMusicPlaying()){
                    updateModelView(); // for visual correctness
                }
                musicManager.restoreFromMemento(initialMusicSettings);
                view.getMusicSelect().setSelected(musicManager.getCurrentTrackName());
            }
        });
        view.getMusicSelect().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicManager.changeTrack(view.getMusicSelect().getSelected());
            }
        });
    }

    @Override
    public Screen getView() {
        return view;
    }
}
