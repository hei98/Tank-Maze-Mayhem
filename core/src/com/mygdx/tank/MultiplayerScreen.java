package com.mygdx.tank;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MultiplayerScreen implements Screen {

    private final TankMazeMayhem game;
    private final AccountService accountService;
    private Stage stage;

    public MultiplayerScreen(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
    }

    private GameView view;
    private GameModel model;
    private GameController controller;


    @Override
    public void show() {
        stage = new Stage();

        TextButton backButton = new TextButton("Back", game.getButtonStyle());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, accountService));
            }
        });

        model = new GameModel();
        controller = new GameController(model);
        view = new GameView(model);

        stage.addActor(backButton);
        backButton.setPosition(100, 100);

        Gdx.input.setInputProcessor(stage);
        view.create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        float deltaTime = Gdx.graphics.getDeltaTime();
        controller.update(deltaTime);
        model.update(deltaTime);
        view.render();
    }

    @Override
    public void resize(int width, int height) {
        view.resize(width, height);
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

    // Other methods from the Screen interface
}
