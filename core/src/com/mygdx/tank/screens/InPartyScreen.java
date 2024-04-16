package com.mygdx.tank.screens;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Constants;
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.LeaderboardEntry;
import com.mygdx.tank.Player;
import com.mygdx.tank.TankMazeMayhem;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.tank.User;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.tank.*;
import com.mygdx.tank.model.components.bullet.*;
import com.mygdx.tank.model.components.powerup.*;
import com.mygdx.tank.model.states.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InPartyScreen implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final Constants con;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private final Skin skin;
    private final TextButton backButton;
    private Client client;
    private List<String> connectedPlayers = new ArrayList<>();
    private Table playersTable;
    private ScrollPane scrollPane;
    private User user;
    private boolean startGame;
    private Listener listener;

    public InPartyScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface, AccountService accountService) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        this.accountService = accountService;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        backButton = new TextButton("Back", skin, "default");
        startGame = false;
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        client = new Client();
        client.start();

        client.getKryo().register(String.class);
        client.getKryo().register(ArrayList.class);
        client.getKryo().register(HashMap.class);

        listener = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof String) {
                    String message = (String) object;
                    if (message.equals("GameStart")) {
                        startGame = true;
                    }
                } else if (object instanceof List) {
                    if (connectedPlayers.size() == 0) {
                        @SuppressWarnings("unchecked")
                        List<String> receivedPlayers = (List<String>) object;
                        connectedPlayers = receivedPlayers;
                        createPlayersTable();
                    } else {
                        @SuppressWarnings("unchecked")
                        List<String> receivedPlayers = (List<String>) object;
                        connectedPlayers = receivedPlayers;
                        populatePlayerTable(connectedPlayers);
                    }
                    user = accountService.getCurrentUser();
                    int partySize = connectedPlayers.size();
                    Player player = null;
                    switch (partySize) {
                        case 2:
                            player = new Player(2);
                            break;
                        case 3:
                            player = new Player(3);
                            break;
                        case 4:
                            player = new Player(4);
                            break;
                    }
                    user.setPlayer(player);
                }
            }
        };

        client.addListener(listener);
        try {
            client.connect(5000, "10.0.2.2", 5000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setButtons();
        createHeadline();
        addListeners();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);

        if (startGame) {
            client.removeListener(listener);
            game.setScreen(new InGameScreen(game, accountService, client));
        }

        batch.begin();
        batch.draw(background, 0, 0, con.getSWidth(), con.getSHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        batch.dispose();
        skin.dispose();
        try {
            client.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setButtons() {
        backButton.setBounds(con.getCenterX() - con.getTBWidth() / 2 - con.getTBWidth() / 5, con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        stage.addActor(backButton);
    }

    private void addListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LobbyScreen(game,firebaseInterface, accountService));
                client.close();
            }
        });
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(skin.getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Party", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF());
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }

    private void createPlayersTable() {
        playersTable = new Table();
        playersTable.top(); // Align the items at the top of the table
        playersTable.defaults().expand().fillX();

        float orangeBoxWidth = con.getSWidth() * 0.5f;
        float orangeBoxHeight = con.getSHeight() * 0.5f;
        float orangeBoxStartY = con.getSHeight() * 0.27f;
        float tableStartY = orangeBoxStartY + (orangeBoxHeight / 2);

        playersTable.setSize(orangeBoxWidth, orangeBoxHeight);
        playersTable.setPosition((con.getSWidth() - orangeBoxWidth) / 2, tableStartY);

        // Create a scroll pane for the leaderboardTable
        scrollPane = new ScrollPane(playersTable, skin);
        scrollPane.setSize(orangeBoxWidth, orangeBoxHeight);
        scrollPane.setPosition(playersTable.getX(), con.getSHeight() - orangeBoxStartY - orangeBoxHeight);

        stage.addActor(scrollPane);

        populatePlayerTable(connectedPlayers);
    }

    private void populatePlayerTable(List<String> players) {
        playersTable.clearChildren();
        float columnWidth = scrollPane.getWidth() / 2f - 10f;
        for (String player : players) {
            Label nameLabel = new Label(player, new Label.LabelStyle(skin.getFont("font"), Color.BLACK));
            Label scoreLabel = new Label("Connected", new Label.LabelStyle(skin.getFont("font"), Color.BLACK));

            nameLabel.setFontScale(con.getTScaleF());
            scoreLabel.setFontScale(con.getTScaleF());

            playersTable.row().pad(10f).fillX();
            playersTable.add(nameLabel).width(columnWidth);
            playersTable.add(scoreLabel).width(columnWidth);
        }
        playersTable.pack();
    }


}

