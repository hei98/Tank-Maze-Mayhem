package com.mygdx.tank;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.screens.MainMenuScreen;


public class LeaderboardScreen implements Screen {

    private TankMazeMayhem game;
    private AccountService accountService;
    private Stage stage;

    public LeaderboardScreen(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
    }

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

            stage.addActor(backButton);
            backButton.setPosition(100, 100);

            Gdx.input.setInputProcessor(stage);
        }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Implement this if you need to handle the game pausing
    }

    @Override
    public void resume() {
        // Implement this if you need to handle the game resuming
    }

    @Override
    public void hide() {
        stage.dispose();
        // It's often better to dispose of resources in the dispose() method rather than here
    }

    @Override
    public void dispose() {
        stage.dispose();
        // If you have other disposables, dispose of them here
    }
}
