package com.mygdx.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class LeaderboardScreen implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final MenuConstants con;
    private final TankMazeMayhem game;
    private Stage stage;
    private final Texture background;
    private final TextButton backButton;
    private final ImageButton settingsButton;
    private SpriteBatch batch;
    private VerticalGroup leaderboardGroup;
    private final Skin buttonSkin;


    public LeaderboardScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        con = MenuConstants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.JPG");
        buttonSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        settingsButton = new ImageButton(buttonSkin, "settings");
        backButton = new TextButton("Back", buttonSkin, "default");
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtonLayout();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        stage.addActor(backButton);
        stage.addActor(settingsButton);

        Gdx.input.setInputProcessor(stage);

        leaderboardGroup = new VerticalGroup().space(10).pad(10).align(Align.center);
        leaderboardGroup.setFillParent(true); // Make the group center itself within the stage
        stage.addActor(leaderboardGroup);

        firebaseInterface.getLeaderboardData(new FirebaseDataListener() {
            @Override
            public void onDataReceived(Object data) {
                ArrayList<LeaderboardEntry> entries = (ArrayList<LeaderboardEntry>) data;
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {

                        leaderboardGroup.clearChildren(); //clear previous entries if needed
                        for (LeaderboardEntry entry : entries) {
                            Label label = new Label(entry.getUsername() + ": " + entry.getScore(), buttonSkin, "white");
                            leaderboardGroup.addActor(label);
                        }
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Gdx.app.log("Firebase", "Error fetching leaderboard " + errorMessage);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);

        batch.begin();
        batch.draw(background, 0, 0, con.getSWidth(), con.getSHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        batch.dispose();
    }

    private void setButtonLayout() {
        backButton.setBounds(con.getCenterX(), (float) (con.getSHeight()*0.05), con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());
        settingsButton.setSize(con.getIBSize(), con.getIBSize());
        settingsButton.getImageCell().expand().fill();
        settingsButton.setPosition(con.getSWidth() - con.getIBSize() - 10, con.getSHeight() - con.getIBSize() - 10);
    }
}
