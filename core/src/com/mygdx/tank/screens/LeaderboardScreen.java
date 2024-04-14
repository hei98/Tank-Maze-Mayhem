package com.mygdx.tank.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.mygdx.tank.FirebaseDataListener;
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.LeaderboardEntry;
import com.mygdx.tank.MenuConstants;
import com.mygdx.tank.TankMazeMayhem;

public class LeaderboardScreen implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final MenuConstants con;
    private final TankMazeMayhem game;
    private Stage stage;
    private final Texture background;
    private final TextButton backButton;
    private final ImageButton settingsButton;
    private SpriteBatch batch;
    private final Skin buttonSkin;
    private Table leaderboardTable;
    private ScrollPane scrollPane;

    public LeaderboardScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        con = MenuConstants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");
        buttonSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        settingsButton = new ImageButton(buttonSkin, "settings");
        backButton = new TextButton("Back", buttonSkin, "default");
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtonLayout();
        createHeadline();
        createLeaderboardTable();

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
        stage.getViewport().update(width, height, true);
        createHeadline();
        createLeaderboardTable();
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
        buttonSkin.dispose();
    }

    private void setButtonLayout() {
        backButton.setBounds(con.getCenterX(), (float) (con.getSHeight()*0.05), con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());
        settingsButton.setSize(con.getIBSize(), con.getIBSize());
        settingsButton.getImageCell().expand().fill();
        settingsButton.setPosition(con.getSWidth() - con.getIBSize() - 10, con.getSHeight() - con.getIBSize() - 10);
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(buttonSkin.getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Leaderboard", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF());
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }

    private void createLeaderboardTable() {
        leaderboardTable = new Table();
        leaderboardTable.top(); // Align the items at the top of the table
        leaderboardTable.defaults().expand().fillX();

        //Assume the orange box starts 10% from the top and 5% from the button
        float orangeBoxWidth = con.getSWidth() * 0.5f;
        float orangeBoxHeight = con.getSHeight() * 0.5f;
        float orangeBoxStartY = con.getSHeight() * 0.27f;
        float tableStartY = orangeBoxStartY + (orangeBoxHeight / 2);

        leaderboardTable.setSize(orangeBoxWidth, orangeBoxHeight);
        leaderboardTable.setPosition((con.getSWidth() - orangeBoxWidth) / 2, tableStartY);

        // Create a scroll pane for the leaderboardTable
        scrollPane = new ScrollPane(leaderboardTable, buttonSkin);
        scrollPane.setSize(orangeBoxWidth, orangeBoxHeight);
        scrollPane.setPosition(leaderboardTable.getX(), con.getSHeight() - orangeBoxStartY - orangeBoxHeight);

        stage.addActor(scrollPane);

        getLeaderboardData();
    }

    private void getLeaderboardData() {
        firebaseInterface.getLeaderboardData(new FirebaseDataListener() {
            @Override
            public void onDataReceived(Object data) {
                ArrayList<LeaderboardEntry> entries = (ArrayList<LeaderboardEntry>) data;
                Gdx.app.postRunnable(() -> {
                    Collections.sort(entries, new Comparator<LeaderboardEntry>() {
                        @Override
                        public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
                            return Integer.compare(o2.getScore(), o1.getScore());
                        }
                    });
                    leaderboardTable.clearChildren();
                    float columnWidth = scrollPane.getWidth() / 2 - 10;
                    for (LeaderboardEntry entry : entries) {
                        Label nameLabel = new Label(entry.getUsername(), new Label.LabelStyle(buttonSkin.getFont("font"), Color.BLACK));
                        Label scoreLabel = new Label(String.valueOf(entry.getScore()), new Label.LabelStyle(buttonSkin.getFont("font"), Color.BLACK));

                        nameLabel.setFontScale(con.getTScaleF());
                        scoreLabel.setFontScale(con.getTScaleF());

                        leaderboardTable.row().pad(10).fillX();
                        leaderboardTable.add(nameLabel).width(columnWidth);
                        leaderboardTable.add(scoreLabel).width(columnWidth);
                    }
                    leaderboardTable.pack();
                });
            }

            @Override
            public void onError(String errorMessage) {
                Gdx.app.log("Firebase", "Error fetching leaderboard " + errorMessage);
            }
        });
    }
}
