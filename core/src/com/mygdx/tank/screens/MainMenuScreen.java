package com.mygdx.tank.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.MenuConstants;
import com.mygdx.tank.TankMazeMayhem;

public class MainMenuScreen implements Screen {
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private Stage stage;
    private SpriteBatch batch;
    private final Texture background;
    private final Skin buttonSkin;
    private final MenuConstants con;
    private final ImageButton settingsButton;
    private TextButton multiplayerButton, leaderboardButton, loginButton;
    private ImageButton settingsButton;
    private Label accountLabel;
    private BitmapFont font;

    public MainMenuScreen(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        con = MenuConstants.getInstance();
        background = new Texture("Backgrounds/main-menu.JPG");
        this.accountService = accountService;
        buttonSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        multiplayerButton = new TextButton("Multiplayer", buttonSkin, "default");
        leaderboardButton = new TextButton("Leaderboard", buttonSkin, "default");
        loginButton = new TextButton("Create User/Login", buttonSkin, "default");
        settingsButton = new ImageButton(buttonSkin, "settings");
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();

        //Display the username if logged in
        font = new BitmapFont();
        accountLabel = new Label("", new Label.LabelStyle(font, Color.BLACK));
        accountLabel.setPosition(10, Gdx.graphics.getHeight() - 20);
        stage.addActor(accountLabel);
        updateAccountLabel();
    }


    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();

        // login/logout according to user status
        if (accountService.hasUser()){
            loginButton = new TextButton("Log out", buttonSkin, "default");
        }
        else{
            loginButton = new TextButton("Create User/Login", buttonSkin, "default");
        }

        setButtons();
        addListeners();

        // Set input processor
        Gdx.input.setInputProcessor(stage);
    }
     
    //Check if user logged in, and display ID
    private void updateAccountLabel() {
        if (accountService.hasUser()) {
            String user = accountService.getCurrentUserEmail();
            accountLabel.setText("User email: " + user);
        } else {
            accountLabel.setText("Not logged in");
        }
    }


    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 0, 0, 1);

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
        stage.dispose();
        buttonSkin.dispose();
    }

    private void setButtons() {
        multiplayerButton.setBounds(con.getCenterX(), con.getSHeight() * 0.6f, con.getTBWidth(), con.getTBHeight());
        multiplayerButton.getLabel().setFontScale(con.getTScaleF());

        leaderboardButton.setBounds(con.getCenterX(), con.getSHeight() * 0.45f, con.getTBWidth(), con.getTBHeight());
        leaderboardButton.getLabel().setFontScale(con.getTScaleF());

        loginButton.setBounds(con.getCenterX(), con.getSHeight() * 0.3f, con.getTBWidth(), con.getTBHeight());
        loginButton.getLabel().setFontScale(con.getTScaleF());

        settingsButton.setSize(con.getIBSize(), con.getIBSize());
        settingsButton.getImageCell().expand().fill();
        settingsButton.setPosition(con.getSWidth() - con.getIBSize() - 10, con.getSHeight() - con.getIBSize() - 10);

        // Add buttons to the stage
        stage.addActor(multiplayerButton);
        stage.addActor(settingsButton);
        stage.addActor(leaderboardButton);
        stage.addActor(loginButton);
    }

    private void addListeners() {
        // Add click listeners to buttons
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LobbyScreen(game, game.getFirebaseInterface(), accountService));
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
                if (accountService.hasUser()){
                    accountService.signOut();
                    game.setScreen( new MainMenuScreen(game, accountService));
                }
                else{
                    game.setScreen(new SignInScreen(game, accountService));
                }
            }
        });
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game, game.getFirebaseInterface(), accountService));
            }
        });
    }
}