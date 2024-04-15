package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Constants;
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.TankMazeMayhem;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.tank.model.Entity;

import java.io.IOException;

public class CreateGameScreen implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final Constants con;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private final Skin skin;
    private final TextButton backButton, startGameButton;
    private Server server;
    private Client client;

    public CreateGameScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface, AccountService accountService) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        this.accountService = accountService;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        backButton = new TextButton("Back", skin, "default");
        startGameButton = new TextButton("Start game", skin,"default");
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();
        server = new Server();
        server.start();

        try {
            server.bind(54555, 54777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.getKryo().register(String.class);

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                connection.sendTCP("En klient har koblet til!");
            }
        });

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof String) {
                    String message = (String) object;
                    System.out.println("Server mottok meldingen: " + message);
                    System.out.println("Server sender tilbake meldingen: " + "Sikt bedre!");
                    connection.sendTCP("Sikt bedre!");
                }
            }
        });

        client = new Client();
        client.start();
        client.getKryo().register(String.class);
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof String) {
                    String message = (String) object;
                    System.out.println("Klient mottok denne meldingen fra server: " + message);
                }
            }
        });
        try {
            client.connect(5000, "localhost", 54555, 54777);
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
            server.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setButtons() {
        backButton.setBounds(con.getCenterX(), con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        startGameButton.setBounds(con.getCenterX(), con.getSHeight()*0.2f, con.getTBWidth(), con.getTBHeight());
        startGameButton.getLabel().setFontScale(con.getTScaleF());

        stage.addActor(backButton);
        stage.addActor(startGameButton);
    }

    private void addListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LobbyScreen(game,firebaseInterface, accountService));
                server.close();
            }
        });
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InGameScreen(game, accountService, client));
            }
        });
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(skin.getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Game lobby", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF()*2f);
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }
}

