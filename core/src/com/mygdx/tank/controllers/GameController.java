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
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        boolean inputDetected = false;

        boolean lookLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean lookRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean lookUp = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean lookDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            model.shootFromTank();
        }

        if (lookUp) {
            deltaY = 1.0f;
            inputDetected = true;
        }
        if (lookDown) {
            deltaY = -1.0f;
            inputDetected = true;
        }
        if (lookLeft) {
            deltaX = -1.0f;
            inputDetected = true;
        }
        if (lookRight) {
            deltaX = 1.0f;
            inputDetected = true;
        }

        if (inputDetected) {
            model.rotatePlayerTank(lookRight, lookLeft, lookUp, lookDown);
            model.movePlayerTank(deltaX, deltaY);
        }

        else {
            model.movePlayerTank(0, 0);

        }
    }
}

