package com.mygdx.tank.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.tank.Constants;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.model.MenuModel;

import java.util.ArrayList;

public class SignInView implements Screen, IView {
    private final TankMazeMayhem game;
    private final MenuModel model;
    private final Constants con;
    private Stage stage;
    private SpriteBatch batch;
    private final Texture background;
    private final TextField emailTextField, passwordTextField;
    private final TextButton signInButton, signUpButton, backButton;
    private Label errorLabel;


    public SignInView(TankMazeMayhem game, MenuModel model) {
        this.game = game;
        this.model = model;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/main-menu.JPG");

        //Create TextFields and buttons
        emailTextField = new TextField("", con.getSkin());
        passwordTextField = new TextField("", con.getSkin());
        signInButton = new TextButton("Sign In", con.getSkin());
        signUpButton = new TextButton("Sign Up", con.getSkin());
        backButton = new TextButton("Back", con.getSkin());
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();

        setErrorLabel();
        setButtonsAndFields();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, con.getSWidth(), con.getSHeight());
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        background.dispose();
    }

    public TextButton getBackButton() {
        return this.backButton;
    }
    public TextButton getSignInButton() {
        return this.signInButton;
    }
    public TextButton getSignUpButton() {
        return this.signUpButton;
    }
    public ArrayList<String> getEmailPass() {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(emailTextField.getText());
        temp.add(passwordTextField.getText());
        return temp;
    }

    private void setErrorLabel() {
        //errorLabel
        Texture errorBackground = new Texture("Backgrounds/orange.png");
        TextureRegionDrawable errorBackgroundDrawable = new TextureRegionDrawable(new TextureRegion(errorBackground));
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        labelStyle.background = errorBackgroundDrawable;
        errorLabel = new Label("", labelStyle);

        errorLabel.setWrap(true);
        errorLabel.setAlignment(Align.center);
        errorLabel.setBounds(con.getCenterTB(), con.getSHeight() * (0.6f + 0.12f), con.getTBWidth(), con.getTBHeight());
        errorLabel.setFontScale(con.getTScaleF());

        stage.addActor(errorLabel);
    }

    private void setButtonsAndFields() {
        // Set bounds and font scale for buttons and TextFields, and set message for TextFields
        emailTextField.setBounds(con.getCenterTB(), con.getSHeight() * (0.6f), con.getTBWidth(), con.getTBHeight());
        emailTextField.setMessageText("Email");

        passwordTextField.setBounds(con.getCenterTB(), con.getSHeight() * (0.6f - 0.12f), con.getTBWidth(), con.getTBHeight());
        passwordTextField.setMessageText("Password");
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('*');

        // Set font scale for email and password text fields
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(emailTextField.getStyle());
        textFieldStyle.font.getData().setScale(con.getTScaleF()); // Set the font scale
        emailTextField.setStyle(textFieldStyle);
        passwordTextField.setStyle(textFieldStyle);

        signInButton.setBounds(con.getCenterTB(), con.getSHeight() * (0.6f - 0.24f), con.getTBWidth(), con.getTBHeight());
        signInButton.getLabel().setFontScale(con.getTScaleF());
        signUpButton.setBounds(con.getCenterTB(), con.getSHeight()*0.2f, con.getTBWidth(), con.getTBHeight());
        signUpButton.getLabel().setFontScale(con.getTScaleF());

        backButton.setBounds(con.getCenterTB(), con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        // Add actors to the stage
        stage.addActor(emailTextField);
        stage.addActor(passwordTextField);
        stage.addActor(signInButton);
        stage.addActor(signUpButton);
        stage.addActor(backButton);
    }

    @Override
    public void updateView(MenuModel model) {
        if (errorLabel != null) {
            errorLabel.setText(model.getErrorLabel());
        }
    }
}
