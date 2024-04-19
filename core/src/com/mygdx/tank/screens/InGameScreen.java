package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.MusicManager;
import com.mygdx.tank.Player;
import com.mygdx.tank.controllers.GameController;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.GameView;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.model.Scoreboard;

import java.util.List;

public class InGameScreen implements Screen {

    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Client client;
    private Stage stage;
    private MusicManager musicManager;
    private final List<Player> connectedPlayers;
    private Server server;
    private GameView view;
    private GameModel model;
    private GameController controller;

    public InGameScreen(TankMazeMayhem game, AccountService accountService, Client client, List<Player> connectedPlayers) {
        this.game = game;
        this.accountService = accountService;
        this.client = client;
        this.connectedPlayers = connectedPlayers;
    }

    public InGameScreen(TankMazeMayhem game, AccountService accountService, Client client, List<Player> connectedPlayers, Server server) {
        this.game = game;
        this.accountService = accountService;
        this.client = client;
        this.connectedPlayers = connectedPlayers;
        this.server = server;
    }

    @Override
    public void show() {
        stage = new Stage();

        TextButton backButton = new TextButton("Back", game.getButtonStyle());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, accountService));
            }
        });

        Scoreboard scoreboard = new Scoreboard();


        for (Player player : connectedPlayers) {
            scoreboard.addPlayer(player);
        }

        model = new GameModel(game.getFirebaseInterface(), accountService, client, connectedPlayers, scoreboard);
        controller = new GameController(model, client);
        if (server != null) {
            view = new GameView(model, controller, game, accountService, scoreboard, server);
        } else {
            view = new GameView(model, controller, game, accountService, scoreboard, client);
        }

        stage.addActor(backButton);
        backButton.setPosition(100, 100);
        game.getMusicManager().startGameMusic();

        Gdx.input.setInputProcessor(stage);
        view.create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        float deltaTime = Gdx.graphics.getDeltaTime();
        model.update(deltaTime);
        view.render();
    }

    @Override
    public void resize(int width, int height) {
        view.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        game.getMusicManager().stopGameMusic();
        stage.dispose();
    }

    @Override
    public void dispose() {
        view.dispose();
    }

    // Other methods from the Screen interface
}
