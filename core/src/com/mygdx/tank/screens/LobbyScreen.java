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
import com.mygdx.tank.AccountService;
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.MenuConstants;
import com.mygdx.tank.TankMazeMayhem;

public class LobbyScreen implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final MenuConstants con;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private final Skin buttonSkin;
    private final TextButton backButton, createGameButton, joinGameButton;

    public LobbyScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface, AccountService accountService) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        this.accountService = accountService;
        con = MenuConstants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");
        buttonSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        backButton = new TextButton("Back", buttonSkin, "default");
        createGameButton = new TextButton("Create game", buttonSkin,"default");
        joinGameButton = new TextButton("Join game", buttonSkin,"default");
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtons();
        createHeadline();
        addListeners();


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

    private void setButtons() {
        backButton.setBounds(con.getCenterX(), (float) (con.getSHeight()*0.05f), con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        createGameButton.setBounds(con.getCenterX(), (float) (con.getSHeight()*0.5f), con.getTBWidth(), con.getTBHeight());
        createGameButton.getLabel().setFontScale(con.getTScaleF());

        joinGameButton.setBounds(con.getCenterX(), (float) (con.getSHeight()*0.3f), con.getTBWidth(), con.getTBHeight());
        joinGameButton.getLabel().setFontScale(con.getTScaleF());

        stage.addActor(backButton);
        stage.addActor(createGameButton);
        stage.addActor(joinGameButton);
    }

    private void addListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, accountService));
            }
        });
        createGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InGameScreen(game, accountService));
            }
        });
        joinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InGameScreen(game, accountService));
            }
        });
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

}
