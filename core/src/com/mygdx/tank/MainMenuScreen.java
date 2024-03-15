package com.mygdx.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
    private TankMazeMayhem game;
    private Stage stage;
    private SpriteBatch batch;

    public MainMenuScreen(TankMazeMayhem game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();

        // Create button style
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = game.getFont(); // You need to define your font
        TextButton leaderboardButton = new TextButton("Leaderboard", buttonStyle);
        TextButton singleplayerButton = new TextButton("Singleplayer", buttonStyle);
        TextButton multiplayerButton = new TextButton("Multiplayer", buttonStyle);
        TextButton settingsButton = new TextButton("Settings", buttonStyle);


        // Add click listeners to buttons
        singleplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SingleplayerScreen(game));
            }
        });
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MultiplayerScreen(game));
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Replace with the appropriate action to show the leaderboard
                //game.setScreen(new LeaderboardScreen(game));
            }
        });

        // Add buttons to the stage
        stage.addActor(singleplayerButton);
        stage.addActor(multiplayerButton);
        stage.addActor(settingsButton);
        stage.addActor(leaderboardButton);

        // Set button positions
        singleplayerButton.setPosition(100, 400);
        multiplayerButton.setPosition(100, 300);
        settingsButton.setPosition(100, 200);
        leaderboardButton.setPosition(100,100);

        // Set input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        batch.begin();
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
        // Dispose of any resources held by this screen
        stage.dispose();
    }

    // Other methods from the Screen interface
}