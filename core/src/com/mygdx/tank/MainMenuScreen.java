package com.mygdx.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.w3c.dom.Text;

public class MainMenuScreen implements Screen {
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private Stage stage;
    private SpriteBatch batch;
    private Texture background;
    private Skin buttonSkin;
    private TextButton multiplayerButton, leaderboardButton, loginButton;
    private ImageButton settingsButton;
    private Label accountLabel;

    public MainMenuScreen(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        background = new Texture("Backgrounds/main-menu.JPG");



        //Load the skin and atlas for the buttons
        buttonSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        //Create the buttons with the new style
        multiplayerButton = new TextButton("Multiplayer", buttonSkin, "menu");
        leaderboardButton = new TextButton("Leaderboard", buttonSkin, "default");
        loginButton = new TextButton("Create User/Login", buttonSkin, "default");
        settingsButton = new ImageButton(buttonSkin, "settings");

        setButtonLayout();


        // Add click listeners to buttons
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MultiplayerScreen(game, accountService));
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, accountService));
            }
        });
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SignInScreen(game, accountService));
            }
        });
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game, accountService));
            }
        });

        // Add buttons to the stage
        stage.addActor(multiplayerButton);
        stage.addActor(settingsButton);
        stage.addActor(leaderboardButton);
        stage.addActor(loginButton);

        // Set input processor
        Gdx.input.setInputProcessor(stage);
    }

    private void setButtonLayout() {
        float buttonWidth = 200;
        float buttonHeight = 50;
        float centerX = (Gdx.graphics.getWidth() - buttonWidth) / 2;

        multiplayerButton.setBounds(centerX, 300, buttonWidth, buttonHeight);
        leaderboardButton.setBounds(centerX, 200, buttonWidth, buttonHeight);
        loginButton.setBounds(centerX, 100, buttonWidth, buttonHeight);
        settingsButton.setBounds(Gdx.graphics.getWidth() - settingsButton.getWidth() - 10, Gdx.graphics.getHeight() - settingsButton.getHeight() - 10, 50, 50);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw stage
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        // Dispose stage and resources when not in use
        stage.dispose();
    }

    @Override
    public void pause() {
        // No specific pause behavior needed for the main menu
    }

    @Override
    public void resume() {
        // No specific pause behavior needed for the main menu
    }

    @Override
    public void dispose() {
        // Dispose of any resources held by this screen
        stage.dispose();
        buttonSkin.dispose();
    }

    // Other methods from the Screen interface
}