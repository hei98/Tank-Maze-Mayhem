package com.mygdx.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LeaderboardScreen implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final TankMazeMayhem game;
    private Stage stage;
    private Texture background;
    private TextButton backButton;
    private ImageButton settingsButton;
    private SpriteBatch batch;
    private Skin buttonSkin;
    final float BASE_SCREEN_WIDTH, BASE_SCREEN_HEIGHT;
    float screenWidth, screenHeight, widthScaleFactor, heightScaleFactor, centerX, buttonWidth, buttonHeight, settingsButtonSize, textScaleFactor, settingsImageSize;

    public LeaderboardScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        background = new Texture("Backgrounds/Leaderboard2.JPG");
        buttonSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        // Define your reference (base) screen width and height
        BASE_SCREEN_WIDTH = 800f;
        BASE_SCREEN_HEIGHT = 480f;

        // Get the current screen width and height
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // Calculate the scale factor
        widthScaleFactor = screenWidth / BASE_SCREEN_WIDTH;
        heightScaleFactor = screenHeight / BASE_SCREEN_HEIGHT;

        settingsButton = new ImageButton(buttonSkin, "settings");
        backButton = new TextButton("Back", buttonSkin, "default");
        buttonWidth = 200 * widthScaleFactor;
        buttonHeight = 50 * heightScaleFactor;
        centerX = (screenWidth - buttonWidth) / 2;
        settingsButtonSize = (screenHeight * 0.1f);
        textScaleFactor = 3f;

    }

    private void setButtonLayout() {
        backButton.setBounds(centerX, (float) (screenHeight*0.05), buttonWidth, buttonHeight);
        backButton.getLabel().setFontScale(textScaleFactor);
        settingsButton.setSize(settingsButtonSize, settingsButtonSize);
        settingsButton.getImageCell().expand().fill();
        settingsButton.setPosition(screenWidth - settingsButtonSize - 10, screenHeight - settingsButtonSize - 10);
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtonLayout();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        stage.addActor(backButton);
        stage.addActor(settingsButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, screenWidth, screenHeight);
        batch.end();

        stage.act(delta);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        batch.dispose();
    }
}
