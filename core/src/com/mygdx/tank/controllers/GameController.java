package com.mygdx.tank.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.tank.model.GameModel;

public class GameController {
    private final GameModel model;
    private final Client client;

    public GameController(GameModel model, Client client) {
        this.model = model;
        this.client = client;
    }

    public void handleTouchpadInput(float knobPercentX, float knobPercentY) {
        model.movePlayerTank(knobPercentX, knobPercentY);
        model.rotatePlayerTank(knobPercentX, knobPercentY);
    }

    public void handleFireButton() {
        model.handleShootAction();
        client.sendTCP("Jeg skyter!");
    }
}

