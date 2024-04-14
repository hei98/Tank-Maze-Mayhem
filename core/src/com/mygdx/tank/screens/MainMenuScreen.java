package com.mygdx.tank.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.MenuConstants;
import com.mygdx.tank.TankMazeMayhem;

public class MainMenuScreen implements Screen {
    private final TankMazeMayhem game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture background;
    private Skin buttonSkin;
    private final MenuConstants con;
    private TextButton multiplayerButton, leaderboardButton, loginButton;
    private ImageButton settingsButton;

    public MainMenuScreen(TankMazeMayhem game) {
        this.game = game;
        con = MenuConstants.getInstance();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        background = new Texture("Backgrounds/main-menu.JPG");
        //Load the skin and atlas for the buttons
        buttonSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        //Create the buttons with the new style
        multiplayerButton = new TextButton("Multiplayer", buttonSkin, "menu");
        leaderboardButton = new TextButton("Leaderboard", buttonSkin, "default");
        loginButton = new TextButton("Create User/Login", buttonSkin, "default");
        settingsButton = new ImageButton(buttonSkin, "settings");

        setButtonLayout();


        // Add click listeners to buttons
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LobbyScreen(game, game.getFirebaseInterface()));
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game, game.getFirebaseInterface()));
            }
        });
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game, game.getFirebaseInterface()));
            }
        });

        // Add buttons to the stage
        stage.addActor(multiplayerButton);
        stage.addActor(settingsButton);
        stage.addActor(leaderboardButton);
        stage.addActor(loginButton);

        // Set input processor
        Gdx.input.setInputProcessor(stage);
    }

    private void setButtonLayout() {
        float buttonX = con.getCenterX();
        float button1Y = con.getSHeight() * 0.6f;
        float button2Y = con.getSHeight() * 0.45f;
        float button3Y = con.getSHeight() * 0.3f;

        multiplayerButton.setBounds(buttonX, button1Y, con.getTBWidth(), con.getTBHeight());
        leaderboardButton.setBounds(buttonX, button2Y, con.getTBWidth(), con.getTBHeight());
        loginButton.setBounds(buttonX, button3Y, con.getTBWidth(), con.getTBHeight());

        settingsButton.setSize(con.getIBSize(), con.getIBSize());
        settingsButton.getImageCell().expand().fill();
        settingsButton.setPosition(con.getSWidth() - con.getIBSize() - 10, con.getSHeight() - con.getIBSize() - 10);

        multiplayerButton.getLabel().setFontScale(con.getTScaleF());
        leaderboardButton.getLabel().setFontScale(con.getTScaleF());
        loginButton.getLabel().setFontScale(con.getTScaleF());
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);

        // Draw background
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw stage
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        // Dispose stage and resources when not in use
        stage.dispose();
    }

    @Override
    public void pause() {
        // No specific pause behavior needed for the main menu
    }

    @Override
    public void resume() {
        // No specific pause behavior needed for the main menu
    }

    @Override
    public void dispose() {
        stage.dispose();
        buttonSkin.dispose();
    }
}