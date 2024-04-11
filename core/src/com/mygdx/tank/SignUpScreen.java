package com.mygdx.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class SignUpScreen implements Screen {
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final SignUpController signUpController;
    private Stage stage;
    private SpriteBatch batch;
    private Texture background;
    private Skin skin;
    private TextField emailTextField, passwordTextField, confirmPasswordTextField;
    private TextButton signUpButton, backButton;
    private Label errorLabel;

    public SignUpScreen(TankMazeMayhem game, AccountService accountService) {
        Gdx.app.log("InfoTag", "SignUpScreen entity created");
        this.game = game;
        this.accountService = accountService;
        this.signUpController = new SignUpController(accountService);
    }

    @Override
    public void show() {
        Gdx.app.log("InfoTag", "SignUpScreen show() initialized");
        batch = new SpriteBatch();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));
        background = new Texture("Backgrounds/main-menu.JPG");

        // Create text fields and buttons
        emailTextField = new TextField("", skin);
        passwordTextField = new TextField("", skin);
        confirmPasswordTextField = new TextField("", skin);
        signUpButton = new TextButton("Sign Up", skin);
        backButton = new TextButton("Back", skin);
        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        setButtonLayout();

        // Add actors to stage
        stage.addActor(emailTextField);
        stage.addActor(passwordTextField);
        stage.addActor(confirmPasswordTextField);
        stage.addActor(signUpButton);
        stage.addActor(backButton);
        stage.addActor(errorLabel);

        // Add listeners to buttons
        signUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Gdx.app.log("InfoTag", "Sign up clicked");
                    String email = emailTextField.getText();
                    String password = passwordTextField.getText();
                    String confirmPassword = confirmPasswordTextField.getText();
                    signUpController.updateEmail(email);
                    signUpController.updatePassword(password);
                    signUpController.updateConfirmPassword(confirmPassword);
                    signUpController.onSignUpClick();
                    errorLabel.setText("");
                    game.setScreen(new MainMenuScreen(game, accountService));
                } catch (Exception e) {
                    errorLabel.setText("Sign up failed: " + e.getLocalizedMessage());
                    Gdx.app.error("ErrorTag", e.getMessage());
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SignInScreen(game, accountService));
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    private void setButtonLayout() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float buttonWidth = screenWidth * 0.2f;
        float buttonHeight = screenHeight * 0.1f;

        emailTextField.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.6f, buttonWidth, buttonHeight);
        emailTextField.setMessageText("Enter Email");
        passwordTextField.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.5f, buttonWidth, buttonHeight);
        passwordTextField.setMessageText("Enter Password");
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('*');
        confirmPasswordTextField.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.4f, buttonWidth, buttonHeight);
        confirmPasswordTextField.setMessageText("Confirm Password");
        confirmPasswordTextField.setPasswordMode(true);
        confirmPasswordTextField.setPasswordCharacter('*');
        signUpButton.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.3f, buttonWidth, buttonHeight);
        backButton.setBounds(screenWidth/2 - buttonWidth/2, screenHeight*0.2f, buttonWidth, buttonHeight);
        errorLabel.setBounds(0, screenHeight/2, screenWidth, buttonHeight); // Adjust as needed
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
