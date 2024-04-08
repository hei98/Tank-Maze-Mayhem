package com.mygdx.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GameController {
    private GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }

    public void update(float deltaTime) {
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        boolean inputDetected = false;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            model.shootFromTank();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            deltaY = 1.0f;
            inputDetected = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            deltaY = -1.0f;
            inputDetected = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            deltaX = -1.0f;
            inputDetected = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            deltaX = 1.0f;
            inputDetected = true;
        }

        if (inputDetected) {
            model.movePlayerTank(deltaX, deltaY);
        } else {
            model.movePlayerTank(0, 0);
        }
    }
}
