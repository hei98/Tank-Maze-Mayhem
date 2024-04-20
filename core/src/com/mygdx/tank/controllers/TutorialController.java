package com.mygdx.tank.controllers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.TutorialView;
import com.mygdx.tank.model.MenuModel;

public class TutorialController implements IController{
    private final MenuModel model;
    private final TutorialView view;
    private final TankMazeMayhem game;
    private final AccountService accountService;

    public TutorialController(MenuModel model, TutorialView view, TankMazeMayhem game, AccountService accountService) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.accountService = accountService;
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
                if (view.getCurrentPageIndex() > 0) {
                    view.setCurrentPageIndex(view.getCurrentPageIndex() - 1);
                    updateModelView();
                } else {
                    ApplicationController.getInstance(game, accountService).switchToMainMenu();
                }
            }
        });
        view.getNextButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.setCurrentPageIndex(view.getCurrentPageIndex()+1);
                if (view.getCurrentPageIndex() >= view.getPagesLength()) {
                    endTutorial(); // No more pages, end tutorial
                } else {
                    updateModelView();
                }
            }
        });
        view.getSkipButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                endTutorial();
            }
        });
    }

    private void endTutorial() {
        ApplicationController.getInstance(game, accountService).switchToLobby();
    }

    @Override
    public Screen getView() {
        return view;
    }
}
