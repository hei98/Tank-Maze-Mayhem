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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Constants;
import com.mygdx.tank.controllers.SignUpController;
import com.mygdx.tank.TankMazeMayhem;

public class SignUpView implements Screen {
    private final TankMazeMayhem game;
    private final Constants con;
    private final AccountService accountService;
    private final SignUpController signUpController;
    private Stage stage;
    private SpriteBatch batch;
    private final Texture background;
    private final Skin skin;
    private final TextField emailTextField, passwordTextField, confirmPasswordTextField;
    private final TextButton signUpButton, backButton;
    private Label errorLabel;

    public SignUpView(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
        this.signUpController = new SignUpController(accountService);
        con = Constants.getInstance();
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));
        background = new Texture("Backgrounds/main-menu.JPG");

        // Create text fields and buttons
        emailTextField = new TextField("", skin);
        passwordTextField = new TextField("", skin);
        confirmPasswordTextField = new TextField("", skin);
        signUpButton = new TextButton("Sign Up", skin);
        backButton = new TextButton("Back", skin);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();

        setErrorLabel();
        setButtonsAndFields();
        addListeners();

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
        skin.dispose();
        background.dispose();
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
        emailTextField.setBounds(con.getCenterTB(), con.getSHeight() * 0.6f, con.getTBWidth(), con.getTBHeight());
        emailTextField.setMessageText("Enter Email");

        passwordTextField.setBounds(con.getCenterTB(), con.getSHeight() * (0.6f - 0.12f), con.getTBWidth(), con.getTBHeight());
        passwordTextField.setMessageText("Enter Password");
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('*');

        confirmPasswordTextField.setBounds(con.getCenterTB(), con.getSHeight() * (0.6f - 0.24f), con.getTBWidth(), con.getTBHeight());
        confirmPasswordTextField.setMessageText("Confirm Password");
        confirmPasswordTextField.setPasswordMode(true);
        confirmPasswordTextField.setPasswordCharacter('*');

        // Set font scale for email and password text fields
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(emailTextField.getStyle());
        textFieldStyle.font.getData().setScale(con.getTScaleF()); // Set the font scale
        emailTextField.setStyle(textFieldStyle);
        passwordTextField.setStyle(textFieldStyle);
        confirmPasswordTextField.setStyle(textFieldStyle);

        signUpButton.setBounds(con.getCenterTB(), con.getSHeight() * 0.2f, con.getTBWidth(), con.getTBHeight());
        signUpButton.getLabel().setFontScale(con.getTScaleF());
        backButton.setBounds(con.getCenterTB(), con.getSHeight() * 0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        // Add actors to stage
        stage.addActor(emailTextField);
        stage.addActor(passwordTextField);
        stage.addActor(confirmPasswordTextField);
        stage.addActor(signUpButton);
        stage.addActor(backButton);
    }

    private void addListeners() {
        // Add listeners to buttons
        signUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    String email = emailTextField.getText();
                    String password = passwordTextField.getText();
                    String confirmPassword = confirmPasswordTextField.getText();
                    signUpController.updateEmail(email);
                    signUpController.updatePassword(password);
                    signUpController.updateConfirmPassword(confirmPassword);
                    signUpController.onSignUpClick();
                    errorLabel.setText("");
                    game.setScreen(new MainMenuView(game, accountService));
                } catch (Exception e) {
                    errorLabel.setText("Sign up failed: " + e.getLocalizedMessage());
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SignInView(game, accountService));
            }
        });
    }

}
