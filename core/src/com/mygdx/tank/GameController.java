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
        boolean lookLeft = false;
        boolean lookRight = false;
        boolean lookUp = false;
        boolean lookDown = false;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            deltaY = 1.0f;
            inputDetected = true;
            lookUp = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            deltaY = -1.0f;
            inputDetected = true;
            lookDown = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            deltaX = -1.0f;
            inputDetected = true;
            lookLeft = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            deltaX = 1.0f;
            inputDetected = true;
            lookRight = true;
        }

        if (inputDetected) {
            model.rotatePlayerTank(lookRight, lookLeft, lookUp, lookDown);
            if (!model.isCollisionWithWalls(model.getPlayerTank(), deltaX, deltaY)) {
                model.movePlayerTank(deltaX, deltaY);
            }
        } else {
            model.movePlayerTank(0, 0);
            lookRight = false;
            lookLeft = false;
            lookUp = false;
            lookDown = false;
        }
    }
}
