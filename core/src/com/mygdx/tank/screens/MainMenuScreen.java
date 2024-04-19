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
import com.mygdx.tank.Constants;
import com.mygdx.tank.TankMazeMayhem;

public class MainMenuScreen implements Screen {
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private Stage stage;
    private SpriteBatch batch;
    private final Texture background;
    private final Skin skin;
    private final Constants con;
    private final ImageButton settingsButton;
    private final TextButton multiplayerButton, leaderboardButton;
    private final TextButton loginButton;
    private Label accountLabel;
    private BitmapFont font;

    public MainMenuScreen(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/main-menu.JPG");
        this.accountService = accountService;
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        multiplayerButton = new TextButton("Multiplayer", skin, "default");
        leaderboardButton = new TextButton("Leaderboard", skin, "default");
        loginButton = new TextButton("", skin, "default");
        settingsButton = new ImageButton(skin, "settings");
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();

        setButtons();
        addListeners();


        //Display the username if logged in
        font = new BitmapFont();
        accountLabel = new Label("", new Label.LabelStyle(font, Color.BLACK));
        accountLabel.setPosition(con.getSWidth()*0.01f, con.getSHeight() * 0.98f);
        accountLabel.setFontScale(con.getTScaleF());
        stage.addActor(accountLabel);
        updateAccountLabel();
        isLoggedIn();

        // Set input processor
        Gdx.input.setInputProcessor(stage);

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
        skin.dispose();
    }

    private void setButtons() {
        multiplayerButton.setBounds(con.getCenterTB(), con.getSHeight() * 0.6f, con.getTBWidth(), con.getTBHeight());
        multiplayerButton.getLabel().setFontScale(con.getTScaleF());
        if(!accountService.hasUser()){
            multiplayerButton.getColor().set(Color.DARK_GRAY);
        }

        leaderboardButton.setBounds(con.getCenterTB(), con.getSHeight() * 0.45f, con.getTBWidth(), con.getTBHeight());
        leaderboardButton.getLabel().setFontScale(con.getTScaleF());

        loginButton.setBounds(con.getCenterTB(), con.getSHeight() * 0.3f, con.getTBWidth(), con.getTBHeight());
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
                if (accountService.hasUser() && game.getShowTutorial()){
                    game.setShowTutorial(false);
                    game.setScreen(new TutorialScreen(game, new LobbyScreen(game, game.getFirebaseInterface(), accountService), accountService));
                } else if (accountService.hasUser()) {
                    game.setScreen(new LobbyScreen(game, game.getFirebaseInterface(), accountService));
                }
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

    //Check if user logged in, and display ID
    private void updateAccountLabel() {
        if (accountService.hasUser()) {
            String user = accountService.getCurrentUserEmail();
            accountLabel.setText("User email: " + user);
        } else {
            accountLabel.setText("Not logged in");
        }
    }

    private void isLoggedIn() {
        // login/logout according to user status
        if (accountService.hasUser()){
            loginButton.setText("Log Out");
        }
        else{
            loginButton.setText("Create user/login");
        }
    }
}