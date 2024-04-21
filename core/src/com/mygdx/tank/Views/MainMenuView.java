package com.mygdx.tank.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.tank.Constants;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.model.MenuModel;

public class MainMenuView implements Screen, IView {
    private final TankMazeMayhem game;
    private MenuModel model;
    private Stage stage;
    private SpriteBatch batch;
    private final Texture background;
    private final Constants con;
    private final ImageButton settingsButton;
    private final TextButton multiplayerButton, leaderboardButton;
    private final TextButton loginButton;
    private Label accountLabel;
    private BitmapFont font;

    public MainMenuView(TankMazeMayhem game, MenuModel model) {
        this.game = game;
        this.model = model;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/main-menu.JPG");

        multiplayerButton = new TextButton("Multiplayer", con.getSkin(), "default");
        leaderboardButton = new TextButton("Leaderboard", con.getSkin(), "default");
        loginButton = new TextButton("", con.getSkin(), "default");
        settingsButton = new ImageButton(con.getSkin(), "settings");
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();

        setButtonsAndLabel();

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
    }

    private void setButtonsAndLabel() {
        // Set up all the buttons, scale them according to the screen and
        multiplayerButton.setBounds(con.getCenterTB(), con.getSHeight() * 0.6f, con.getTBWidth(), con.getTBHeight());
        multiplayerButton.getLabel().setFontScale(con.getTScaleF());

        leaderboardButton.setBounds(con.getCenterTB(), con.getSHeight() * 0.45f, con.getTBWidth(), con.getTBHeight());
        leaderboardButton.getLabel().setFontScale(con.getTScaleF());

        loginButton.setBounds(con.getCenterTB(), con.getSHeight() * 0.3f, con.getTBWidth(), con.getTBHeight());
        loginButton.getLabel().setFontScale(con.getTScaleF());

        settingsButton.setSize(con.getIBSize(), con.getIBSize());
        settingsButton.getImageCell().expand().fill();
        settingsButton.setPosition(con.getSWidth() - con.getIBSize() - 10, con.getSHeight() - con.getIBSize() - 10);

        // Create Label for the user that is logged in
        font = new BitmapFont();
        accountLabel = new Label("", new Label.LabelStyle(font, Color.BLACK));
        accountLabel.setPosition(con.getSWidth()*0.01f, con.getSHeight() * 0.98f); // Placed in top left corner
        accountLabel.setFontScale(con.getTScaleF());
        stage.addActor(accountLabel);

        // Add buttons to the stage
        stage.addActor(multiplayerButton);
        stage.addActor(settingsButton);
        stage.addActor(leaderboardButton);
        stage.addActor(loginButton);
    }

    public TextButton getMultiplayerButton() {
        return this.multiplayerButton;
    }

    public TextButton getLoginButton() {
        return this.loginButton;
    }

    public TextButton getLeaderboardButton() {
        return this.leaderboardButton;
    }

    public ImageButton getSettingsButton() {
        return this.settingsButton;
    }

    // Update accountlabel
    public void updateAccountLabel() {
        accountLabel.setText(model.getAccountLabel());
        setLogginButton(model.isLoggedIn());
    }

    public void setLogginButton(boolean isLoggedIn) {
        // login/logout according to user status
        if (isLoggedIn){
            loginButton.setText("Log Out");
        }
        else{
            loginButton.setText("Create user/login");
        }
    }

    public void updateMultiplayerButton() {
        if(model.isLoggedIn()) {
            multiplayerButton.getColor().set(Color.BLACK);
        }
    }

    @Override
    public void updateView(MenuModel model) {
        this.model = model;
        updateAccountLabel();
        if (model.isLoggedIn()) {
            multiplayerButton.setDisabled(true);
            multiplayerButton.getColor().set(con.getSkin().get("default", TextButton.TextButtonStyle.class).fontColor);
        } else {
            multiplayerButton.setDisabled(false);
            multiplayerButton.getColor().set(Color.DARK_GRAY);
        }
    }

}