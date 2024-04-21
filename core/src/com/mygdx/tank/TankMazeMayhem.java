package com.mygdx.tank;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.tank.Views.GameView;
import com.mygdx.tank.controllers.ApplicationController;
import com.mygdx.tank.controllers.GameController;
import com.mygdx.tank.model.GameModel;

public class TankMazeMayhem extends Game {
	private final AccountService accountService;
    private final FirebaseInterface firebaseInterface;
    private GameModel model;
    private GameView view;
    private GameController controller;
	private MusicManager musicManager;
    SpriteBatch batch;
	private boolean showTutorial;

	public TankMazeMayhem(FirebaseInterface firebaseInterface, AccountService accountService) {
		this.firebaseInterface = firebaseInterface;
		this.accountService = accountService;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		musicManager = new MusicManager();
		showTutorial = !accountService.hasUser();

		// Set the initial screen to the main menu
		ApplicationController.getInstance(this, accountService).switchToMainMenu();
	}

	@Override
	public void render() {
		super.render(); // Delegates rendering to the current screen
	}

	public BitmapFont getFont() {
		// Load and return the font you want to use for buttons
		return new BitmapFont(); // Example, replace this with your actual font loading code
	}

	// Remove?
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



	public boolean getShowTutorial() {
		return this.showTutorial;
	}

	public void setShowTutorial(boolean bool) {
		this.showTutorial = bool;
	}

	public AccountService getAccountService() {
		return accountService;
	}


	@Override
	public void dispose () {
		batch.dispose();
		musicManager.dispose();
	}
}
