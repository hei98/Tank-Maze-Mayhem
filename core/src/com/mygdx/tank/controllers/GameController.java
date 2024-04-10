package com.mygdx.tank.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.tank.model.GameModel;

public class GameController {
    private GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }

    public void update(float deltaTime) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            model.shootFromTank();
        }
    }

    public void handleTouchpadInput(float knobPercentX, float knobPercentY) {
        model.movePlayerTank(knobPercentX, knobPercentY);
        model.rotatePlayerTank(knobPercentX, knobPercentY);
    }
}

