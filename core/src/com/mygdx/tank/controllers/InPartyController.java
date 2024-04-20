package com.mygdx.tank.controllers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Player;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.User;
import com.mygdx.tank.Views.InGameView;
import com.mygdx.tank.Views.InPartyView;
import com.mygdx.tank.model.MenuModel;

import java.util.List;

public class InPartyController implements IController{
    private final MenuModel model;
    private final InPartyView view;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Client client;
    private Listener listener;
    private User user;

    public InPartyController(MenuModel model, InPartyView view, TankMazeMayhem game, AccountService accountService, Client client) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.client = client;
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
                ApplicationController.getInstance(game, accountService).switchToLobby();
                client.close();
            }
        });

        listener = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof String) {
                    String message = (String) object;
                    if (message.equals("GameStart")) {
                        startGame();
                    }
                } else if (object instanceof List) {
                    if (model.getConnectedPlayers().isEmpty()) {
                        @SuppressWarnings("unchecked")
                        List<Player> receivedPlayers = (List<Player>) object;
                        model.setConnectedPlayers(receivedPlayers);
                        updateModelView();

                        user = accountService.getCurrentUser();
                        int partySize = model.getConnectedPlayers().size();
                        Player player = null;
                        switch (partySize) {
                            case 2:
                                player = new Player("Player2", user.getUserMail());
                                break;
                            case 3:
                                player = new Player("Player3", user.getUserMail());
                                break;
                            case 4:
                                player = new Player("Player4", user.getUserMail());
                                break;
                        }
                        user.setPlayer(player);
                    } else {
                        @SuppressWarnings("unchecked")
                        List<Player> receivedPlayers = (List<Player>) object;
                        model.setConnectedPlayers(receivedPlayers);
                        updateModelView();
                    }
                } else if (object instanceof Player) {
                    Player player = (Player) object;
                    user = accountService.getCurrentUser();
                    user.getPlayer().setPlayerName(player.getPlayerName());
                }
            }
        };

        client.addListener(listener);
        client.sendTCP(new Player("Player1", accountService.getCurrentUser().getUserMail())); // need to create a random Player-object, the correct one is sent back
    }

    @Override
    public Screen getView() {
        return view;
    }

    private void startGame() {
        client.removeListener(listener);
        game.setScreen(new InGameView(game, accountService, client, model.getConnectedPlayers(), model));
    }
}
