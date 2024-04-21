package com.mygdx.tank.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import com.mygdx.tank.model.LeaderboardEntry;
import com.mygdx.tank.Constants;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.model.MenuModel;

public class LeaderboardView implements Screen, IView {
    private final Constants con;
    private final TankMazeMayhem game;
    private Stage stage;
    private final Texture background;
    private final TextButton backButton;
    private final ImageButton settingsButton;
    private SpriteBatch batch;
    private Table leaderboardTable;
    private ScrollPane scrollPane;

    public LeaderboardView(TankMazeMayhem game) {
        this.game = game;
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

    @Override
    public void updateView(MenuModel model) {
        populateLeaderBoardTable(model.getLeaderboardEntries());
    }

    public TextButton getBackButton() {
        return this.backButton;
    }

    public ImageButton getSettingsButton() {
        return this.settingsButton;
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
        Label headlineLabel = new Label("Leaderboard (Top 5)", headlineStyle);
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

        float orangeBoxWidth = con.getSWidth() * 0.5f;
        float orangeBoxHeight = con.getSHeight() * 0.5f;
        float orangeBoxStartY = con.getSHeight() * 0.27f;
        float tableStartY = orangeBoxStartY + (orangeBoxHeight / 2);

        leaderboardTable.setSize(orangeBoxWidth, orangeBoxHeight);
        leaderboardTable.setPosition((con.getSWidth() - orangeBoxWidth) / 2, tableStartY);

        // Create a scroll pane for the leaderboardTable
        scrollPane = new ScrollPane(leaderboardTable, con.getSkin());
        scrollPane.setSize(orangeBoxWidth, orangeBoxHeight);
        scrollPane.setPosition(leaderboardTable.getX(), con.getSHeight() - orangeBoxStartY - orangeBoxHeight);

        stage.addActor(scrollPane);
    }

    private void populateLeaderBoardTable(ArrayList<LeaderboardEntry> entries) {
        leaderboardTable.clearChildren();
        float columnWidth = scrollPane.getWidth() / 2f - 10f;
        for (LeaderboardEntry entry : entries) {
            System.out.println(entry.getUsername());
            System.out.println(entry.getScore());
            Label nameLabel = new Label(entry.getUsername(), new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));
            Label scoreLabel = new Label(String.valueOf(entry.getScore()), new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));

            nameLabel.setFontScale(con.getTScaleF());
            scoreLabel.setFontScale(con.getTScaleF());

            leaderboardTable.row().pad(10f).fillX();
            leaderboardTable.add(nameLabel).width(columnWidth);
            leaderboardTable.add(scoreLabel).width(columnWidth);
        }
        leaderboardTable.pack();
    }
}
