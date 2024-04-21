package com.mygdx.tank.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Constants;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.controllers.ApplicationController;

public class GameCrashedView implements Screen {
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private Constants con;
    private Texture background;
    private Stage stage;
    private TextButton exitGameButton;
    private SpriteBatch batch;

    public GameCrashedView(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
    }

    @Override
    public void show() {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);

        stage = new Stage();
        batch = new SpriteBatch();
        con = Constants.getInstance();
        background = new Texture("Backgrounds/orange.png");
        exitGameButton = new TextButton("Exit Game", con.getSkin());

        Image backgroundImage = new Image(background);
        backgroundImage.setBounds(con.getSWidth() * 0.2f, con.getSHeight() * 0.1f, con.getSWidth() * 0.6f, con.getSHeight() * 0.8f);
        stage.addActor(backgroundImage);

        setButtons();
        createHeadline();
        addListeners();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Update and render the stage
        stage.act();
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
        stage.dispose();
        batch.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }

    private void setButtons() {
        exitGameButton.setSize(con.getTBWidth(), con.getTBHeight());
        exitGameButton.getLabel().setFontScale(con.getTScaleF());
        exitGameButton.setPosition(con.getCenterTB(), con.getSHeight() * 0.12f);
        exitGameButton.getColor().set(Color.BLACK);

        stage.addActor(exitGameButton);
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Server crashed", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF() * 1.5f);
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight() * 0.9f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.BLACK);
        Label label = new Label("The server on host's device has shut down. \nPlease exit to main menu.", labelStyle);

        label.setAlignment(Align.center);
        label.setY(con.getSHeight() * 0.5f);
        label.setFontScale(con.getTScaleF());
        label.setWidth(con.getSWidth());

        stage.addActor(label);
        stage.addActor(headlineLabel);
    }

    private void addListeners() {
        exitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToMainMenu();
            }
        });
    }
}

