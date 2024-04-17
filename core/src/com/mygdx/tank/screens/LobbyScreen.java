package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.Constants;
import com.mygdx.tank.Player;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.model.Scoreboard;
import com.mygdx.tank.model.components.PlayerScoreComponent;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import java.io.IOException;

public class LobbyScreen implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final Constants con;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private final Skin skin;
    private final TextButton backButton, createPartyButton, joinPartyButton;
    private final TextField IPaddressField, portField;
    private Label errorLabel;

    public LobbyScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface, AccountService accountService) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        this.accountService = accountService;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        backButton = new TextButton("Back", skin, "default");
        createPartyButton = new TextButton("Create party", skin,"default");
        joinPartyButton = new TextButton("Join party", skin,"default");

        IPaddressField = new TextField("", skin);
        portField = new TextField("", skin);

    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtons();
        setFields();
        createHeadline();
        addListeners();
        setErrorLabel();

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
    }

    private void setButtons() {
        backButton.setBounds(con.getCenterTB() - con.getTBWidth() / 2 - con.getTBWidth() / 5, con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        createPartyButton.setBounds(con.getCenterTB() + con.getTBWidth() / 2 + con.getTBWidth() / 5, con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        createPartyButton.getLabel().setFontScale(con.getTScaleF());

        joinPartyButton.setBounds(con.getCenterTB(), con.getSHeight()*0.35f, con.getTBWidth(), con.getTBHeight());
        joinPartyButton.getLabel().setFontScale(con.getTScaleF());

        stage.addActor(backButton);
        stage.addActor(createPartyButton);
        stage.addActor(joinPartyButton);
    }

    private void setFields() {
        IPaddressField.setBounds(con.getCenterTB() - con.getTBWidth() * 0.2f, con.getSHeight() * 0.5f, con.getTBWidth() * 0.8f, con.getTBHeight());
        IPaddressField.setText("10.0.2.2");

        portField.setBounds(con.getCenterTB() + con.getTBWidth() * 0.75f, con.getSHeight() * 0.5f, con.getTBWidth() * 0.4f, con.getTBHeight());
        portField.setText("5000");

        // Set font scale for email and password text fields
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(IPaddressField.getStyle());
        textFieldStyle.font.getData().setScale(con.getTScaleF()); // Set the font scale
        IPaddressField.setStyle(textFieldStyle);
        portField.setStyle(textFieldStyle);

        stage.addActor(IPaddressField);
        stage.addActor(portField);
    }

    private void addListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, accountService));
            }
        });
        createPartyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreateGameScreen(game, firebaseInterface, accountService));
            }
        });
        joinPartyButton.addListener(new ClickListener() {
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
                client.getKryo().register(Scoreboard.class);
                client.getKryo().register(Object.class);

                String IPaddress = IPaddressField.getText();
                int port = Integer.parseInt(portField.getText());
                try {
                    client.connect(2500, IPaddress, port);
                    game.setScreen(new InPartyScreen(game, firebaseInterface, accountService, client));
                } catch (IOException e) {
                    errorLabel.setText("Unable to connect to: /" + IPaddress + ":" + port); // Display the error message
                }
            }
        });
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(skin.getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Multiplayer", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF()*2f);
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }

    private void setErrorLabel() {
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.BLACK);

        errorLabel = new Label("", labelStyle);

        errorLabel.setWrap(true);
        errorLabel.setAlignment(Align.center);
        errorLabel.setBounds(con.getCenterTB(), con.getSHeight() * 0.20f, con.getTBWidth(), con.getTBHeight());
        errorLabel.setFontScale(con.getTScaleF());

        stage.addActor(errorLabel);
    }
}
