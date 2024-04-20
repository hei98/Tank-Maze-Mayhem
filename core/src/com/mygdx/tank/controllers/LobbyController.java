package com.mygdx.tank.controllers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Player;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.CreateGameView;
import com.mygdx.tank.Views.LobbyView;
import com.mygdx.tank.model.MenuModel;
import com.mygdx.tank.model.components.PlayerScoreComponent;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LobbyController implements IController{
    private final MenuModel model;
    private final LobbyView view;
    private final TankMazeMayhem game;
    private final AccountService accountService;

    public LobbyController(MenuModel model, LobbyView view, TankMazeMayhem game, AccountService accountService) {
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
                ApplicationController.getInstance(game, accountService).switchToMainMenu();
            }
        });
        view.getCreatePartyButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreateGameView(game, game.getFirebaseInterface(), accountService, model));
            }
        });
        view.getJoinPartyButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Client client = new Client();
                client.start();

                client.getKryo().register(String.class);
                client.getKryo().register(ArrayList.class);
                client.getKryo().register(HashMap.class);
                client.getKryo().register(PositionComponent.class);
                client.getKryo().register(SpriteDirectionComponent.class);
                client.getKryo().register(Float.class);
                client.getKryo().register(Player.class);
                client.getKryo().register(PowerUpTypeComponent.class);
                client.getKryo().register(PowerUpTypeComponent.PowerupType.class);
                client.getKryo().register(PlayerScoreComponent.class);

                String IPaddress = view.getIPAddress();
                int port = Integer.parseInt(view.getPort());
                try {
                    client.connect(2500, IPaddress, port);
                    ApplicationController.getInstance(game, accountService).switchToInParty(client);
                } catch (IOException e) {
                    model.updateErrorLabel("Unable to connect to: /" + IPaddress + ":" + port); // Display the error message
                    updateModelView();
                }
            }
        });
    }

    @Override
    public Screen getView() {
        return view;
    }
}
