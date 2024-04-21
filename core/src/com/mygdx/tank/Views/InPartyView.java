package com.mygdx.tank.Views;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.tank.Constants;
import com.mygdx.tank.model.Player;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.model.MenuModel;

import java.io.IOException;
import java.util.List;

public class InPartyView implements Screen, IView {
    private final Constants con;
    private final TankMazeMayhem game;
    private MenuModel model;
    private final Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private final TextButton backButton;
    private Table playersTable;
    private ScrollPane scrollPane;

    public InPartyView(TankMazeMayhem game, MenuModel model) {
        this.game = game;
        this.model = model;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");

        backButton = new TextButton("Back", con.getSkin(), "default");
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtons();
        createHeadline();
        createPlayersTable();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);

        if (model.startGame()) {
            game.setScreen(new InGameView(game, game.getAccountService(), model.getClient(), model.getConnectedPlayers(), model));
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
        try {
            model.getClient().dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TextButton getBackButton() {
        return this.backButton;
    }


    private void setButtons() {
        backButton.setBounds(con.getCenterTB() - con.getTBWidth() / 2 - con.getTBWidth() / 5, con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        stage.addActor(backButton);
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Party", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF());
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label label = new Label("Waiting for party leader to start game", labelStyle);

        label.setAlignment(Align.center);
        label.setY(con.getSHeight() * 0.20f);
        label.setFontScale(con.getTScaleF());
        label.setWidth(con.getSWidth());

        stage.addActor(headlineLabel);
        stage.addActor(label);
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
        scrollPane = new ScrollPane(playersTable, con.getSkin());
        scrollPane.setSize(orangeBoxWidth, orangeBoxHeight);
        scrollPane.setPosition(playersTable.getX(), con.getSHeight() - orangeBoxStartY - orangeBoxHeight);

        stage.addActor(scrollPane);
    }

    private void populatePlayerTable(List<Player> players) {
        playersTable.clearChildren();
        float columnWidth = scrollPane.getWidth() / 2f - 10f;
        for (Player player : players) {
            String userMail = player.getUserMail();
            String displayName = userMail.split("@")[0];
            Label nameLabel = new Label(displayName, new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));
            Label scoreLabel = new Label("Connected", new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));

            nameLabel.setFontScale(con.getTScaleF());
            scoreLabel.setFontScale(con.getTScaleF());

            playersTable.row().pad(10f).fillX();
            playersTable.add(nameLabel).width(columnWidth);
            playersTable.add(scoreLabel).width(columnWidth);
        }
        playersTable.pack();
    }


    @Override
    public void updateView(MenuModel model) {
        populatePlayerTable(model.getConnectedPlayers());
    }
}

