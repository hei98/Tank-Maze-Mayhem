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
import com.mygdx.tank.TankMazeMayhem;

public class LobbyScreen implements Screen {

    private TankMazeMayhem game;
    private SpriteBatch batch;
    private Stage stage;
    private Skin buttonSkin;
    private Texture background;
    private TextButton backButton, startGameButton;

    public LobbyScreen(TankMazeMayhem game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();
        buttonSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));
        background = new Texture("Backgrounds/main-menu_blank.JPG");

        backButton = new TextButton("Back", buttonSkin, "default");
        startGameButton = new TextButton("Start game", buttonSkin, "default");
        setButtonLayout();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InGameScreen(game));
            }
        });

        stage.addActor(backButton);
        stage.addActor(startGameButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    private void setButtonLayout() {
        float buttonWidth = 200;
        float buttonHeight = 50;

        backButton.setBounds(20, 20, buttonWidth, buttonHeight);
        startGameButton.setBounds(Gdx.graphics.getWidth() - startGameButton.getWidth() * 2 - 20, 20, buttonWidth, buttonHeight);
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
        buttonSkin.dispose();
    }
}
