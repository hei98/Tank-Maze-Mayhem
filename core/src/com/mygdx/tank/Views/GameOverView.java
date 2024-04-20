package com.mygdx.tank.Views;

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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mygdx.tank.AccountService;
import com.mygdx.tank.Constants;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.controllers.ApplicationController;
import com.mygdx.tank.model.Scoreboard;

public class GameOverView implements Screen {
    private final Constants con;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private Stage stage;
    private final Texture background;
    private final TextButton backButton;
    private final ImageButton settingsButton;
    private SpriteBatch batch;
    private Table scoreboardTable;
    private ScrollPane scrollPane;
    private final Scoreboard scoreboard;

    public GameOverView(TankMazeMayhem game, AccountService accountService, Scoreboard scoreboard) {
        this.game = game;
        this.accountService = accountService;
        this.scoreboard = scoreboard;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");

        settingsButton = new ImageButton(con.getSkin(), "settings");
        backButton = new TextButton("Back", con.getSkin(), "default");
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtonLayout();
        createHeadline();
        createLeaderboardTable();

        addListeners();

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
    }

    private void setButtonLayout() {
        backButton.setBounds(con.getCenterTB(),con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());
        settingsButton.setSize(con.getIBSize(), con.getIBSize());
        settingsButton.getImageCell().expand().fill();
        settingsButton.setPosition(con.getSWidth() - con.getIBSize() - 10, con.getSHeight() - con.getIBSize() - 10);

        stage.addActor(backButton);
        stage.addActor(settingsButton);
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Scoreboard", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF());
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }

    private void addListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToMainMenu();
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToSettings();
            }
        });
    }

    private void createLeaderboardTable() {
        scoreboardTable = new Table();
        scoreboardTable.top(); // Align the items at the top of the table
        scoreboardTable.defaults().expand().fillX();

        float orangeBoxWidth = con.getSWidth() * 0.5f;
        float orangeBoxHeight = con.getSHeight() * 0.5f;
        float orangeBoxStartY = con.getSHeight() * 0.27f;
        float tableStartY = orangeBoxStartY + (orangeBoxHeight / 2);

        scoreboardTable.setSize(orangeBoxWidth, orangeBoxHeight);
        scoreboardTable.setPosition((con.getSWidth() - orangeBoxWidth) / 2, tableStartY);

        // Create a scroll pane for the leaderboardTable
        scrollPane = new ScrollPane(scoreboardTable, con.getSkin());
        scrollPane.setSize(orangeBoxWidth, orangeBoxHeight);
        scrollPane.setPosition(scoreboardTable.getX(), con.getSHeight() - orangeBoxStartY - orangeBoxHeight);

        stage.addActor(scrollPane);

        handleLeaderboardData(scoreboard.getScoreboard());
    }

    private void handleLeaderboardData(HashMap<String, Integer> entries) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(entries.entrySet());

        // Sort the list based on the values
        Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                // Compare the values in descending order
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });

        // Populate the scoreboard table with sorted entries
        populateLeaderBoardTable(sortedEntries);
    }

    private void populateLeaderBoardTable(List<Map.Entry<String, Integer>> scoreboard) {
        scoreboardTable.clearChildren();
        float columnWidth = scrollPane.getWidth() / 2f - 10f;
        for (Map.Entry<String, Integer> entry : scoreboard) {
            String userName = entry.getKey();
            Integer score = entry.getValue();
            Label nameLabel = new Label(userName, new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));
            Label scoreLabel = new Label(String.valueOf(score), new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));

            nameLabel.setFontScale(con.getTScaleF());
            scoreLabel.setFontScale(con.getTScaleF());

            scoreboardTable.row().pad(10f).fillX();
            scoreboardTable.add(nameLabel).width(columnWidth);
            scoreboardTable.add(scoreLabel).width(columnWidth);
        }
        scoreboardTable.pack();
    }
}
