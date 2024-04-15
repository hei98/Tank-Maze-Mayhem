package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.MenuConstants;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.screens.MainMenuScreen;

public class SettingsScreen implements Screen {

    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final MenuConstants con;
    private Stage stage;
    private final Texture background;
    private final TextButton backButton;
    private SpriteBatch batch;
    private final Skin skin;

    public SettingsScreen(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
        con = MenuConstants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        backButton = new TextButton("Back", skin, "default");
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtonLayout();
        createHeadline();
        createSoundControl();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, accountService));
            }
        });

        stage.addActor(backButton);

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
    }

    private void setButtonLayout() {
        backButton.setBounds(con.getCenterX(), con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(skin.getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Settings", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF()*2f);
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.83f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }

    private void createSoundControl() {
        Label.LabelStyle soundStyle = new Label.LabelStyle(skin.getFont("font"), Color.BLACK);
        Label soundLabel = new Label("Music", soundStyle);
        soundLabel.setFontScale(con.getTScaleF()* 1.5f);
        soundLabel.setX(con.getSWidth() * 0.4f);
        soundLabel.setY((con.getSHeight()*0.63f) - soundLabel.getPrefHeight());
        stage.addActor(soundLabel);

        ImageButton soundControl = new ImageButton(skin, "music");
        soundControl.setSize(con.getIBSize(), con.getIBSize());
        soundControl.getImageCell().expand().fill();
        soundControl.setPosition(con.getSWidth() * 0.6f, (con.getSHeight()*0.6f) - soundLabel.getPrefHeight());
        stage.addActor(soundControl);

    }
}
