package com.mygdx.tank;

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

public class SignInScreen implements Screen {
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final SignInController signInController;
    private Stage stage;
    private SpriteBatch batch;
    private Texture background;
    private Skin skin;
    private TextField emailTextField, passwordTextField;
    private TextButton signInButton, signUpButton, backButton;
    private Label errorLabel;


    public SignInScreen(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
        this.signInController = new SignInController(accountService);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));
        background = new Texture("Backgrounds/main-menu.JPG");

        //errorLabel
        Texture errorBackground = new Texture("Backgrounds/orange.png");
        TextureRegionDrawable errorBackgroundDrawable = new TextureRegionDrawable(new TextureRegion(errorBackground));
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        labelStyle.background = errorBackgroundDrawable;
        errorLabel = new Label("", labelStyle);
        errorLabel.setWrap(true);
        errorLabel.setAlignment(Align.center);


        //Create textfields and buttons
        emailTextField = new TextField("", skin);
        passwordTextField = new TextField("", skin);
        signInButton = new TextButton("Sign In", skin);
        signUpButton = new TextButton("Sign Up", skin);
        backButton = new TextButton("Back", skin);

        setButtonLayout();

        stage.addActor(emailTextField);
        stage.addActor(passwordTextField);
        stage.addActor(signInButton);
        stage.addActor(signUpButton);
        stage.addActor(backButton);
        stage.addActor(errorLabel);


        signInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String email = emailTextField.getText();
                String password = passwordTextField.getText();
                signInController.updateEmail(email);
                signInController.updatePassword(password);
                errorLabel.setText("");
                try {
                    signInController.onSignInClick();
                    game.setScreen(new MainMenuScreen(game, accountService));
                } catch (Exception e) {
                    errorLabel.setText("Login failed: " + e.getLocalizedMessage()); // Display the error message
                    Gdx.app.error("InfoTag", e.getMessage());
                }

            }
        });


        signUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("InfoTag", "signup clicked");
                game.setScreen(new SignUpScreen(game, accountService));
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game, accountService));
            }
        });



        Gdx.input.setInputProcessor(stage);
    }

    private void setButtonLayout() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float buttonWidth = screenWidth * 0.2f; // 20% of screen width
        float buttonHeight = screenHeight * 0.1f; // 10% of screen height

        emailTextField.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.6f, buttonWidth, buttonHeight);
        emailTextField.setMessageText("Email");
        passwordTextField.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.5f, buttonWidth, buttonHeight);
        passwordTextField.setMessageText("Password");
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('*');
        signInButton.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.4f, buttonWidth, buttonHeight);
        signUpButton.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.3f, buttonWidth, buttonHeight);
        backButton.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.1f, buttonWidth, buttonHeight);
        errorLabel.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.2f, buttonWidth, buttonHeight/3 );
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        skin.dispose();
        background.dispose();
    }
}
