package com.mygdx.tank;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.tank.controllers.GameController;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.screens.MainMenuScreen;

import java.util.HashMap;

public class TankMazeMayhem extends Game {
	private final AccountService accountService;
    private final FirebaseInterface firebaseInterface;
    private GameModel model;
    private GameView view;
    private GameController controller;
	private MusicManager musicManager;
	private FPSLogger fpsLogger;
    SpriteBatch batch;

	public TankMazeMayhem(FirebaseInterface firebaseInterface, AccountService accountService) {
		this.firebaseInterface = firebaseInterface;
		this.accountService = accountService;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		musicManager = new MusicManager();
		// Set the initial screen to the main menu
		setScreen(new MainMenuScreen(this, accountService));
		fpsLogger = new FPSLogger();
	}

	@Override
	public void render() {
		super.render(); // Delegates rendering to the current screen
		fpsLogger.log();
	}

	public BitmapFont getFont() {
		// Load and return the font you want to use for buttons
		return new BitmapFont(); // Example, replace this with your actual font loading code
	}

	public TextButton.TextButtonStyle getButtonStyle() {
		TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
		buttonStyle.font = getFont(); // Ensure to define getFont() method as mentioned earlier
		return buttonStyle;
	}

	public FirebaseInterface getFirebaseInterface() {
		return firebaseInterface;
	}

	//Music handling


	public MusicManager getMusicManager(){
		return musicManager;
	}




	@Override
	public void dispose () {
		batch.dispose();
		musicManager.dispose();
	}
}
