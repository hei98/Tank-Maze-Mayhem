package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private Skin buttonSkin;
    private TextButton backButton, startGameButton;

    public LobbyScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface, AccountService accountService) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        this.accountService = accountService;
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

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, accountService));
            }
        });
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InGameScreen(game, accountService));
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
}
