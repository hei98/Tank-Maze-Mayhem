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
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.MenuConstants;
import com.mygdx.tank.TankMazeMayhem;

public class LobbyScreen implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final MenuConstants con;
    private final TankMazeMayhem game;
    private final Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private final Skin buttonSkin;
    private final TextButton backButton, startGameButton;

    public LobbyScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        con = MenuConstants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");
        buttonSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        backButton = new TextButton("Back", buttonSkin, "default");
        startGameButton = new TextButton("Start game", buttonSkin,"default");
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtonLayout();
        createHeadline();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InGameScreen(game));
            }
        });

        stage.addActor(backButton);
        stage.addActor(startGameButton);

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
        buttonSkin.dispose();
    }

    private void setButtonLayout() {
        backButton.setBounds((con.getSWidth() - con.getTBWidth()) * 0.1f, (float) (con.getSHeight()*0.05), con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        startGameButton.setBounds((con.getSWidth() - con.getTBWidth()) * 0.9f, (float) (con.getSHeight()*0.05), con.getTBWidth(), con.getTBHeight());
        startGameButton.getLabel().setFontScale(con.getTScaleF());
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(buttonSkin.getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Game lobby", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF()*2f);
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }

    private void SetGame() {
        // Get game with at least one spot available and is not running

        // Reserve a spot for the current user

        // Set the game here
    }

//    private void displayPlayersForGame(ArrayList<User> users) {
//        // Update the list of the users in currently in the game
//        for (User user : users) {
//
//        }
//    }
}
