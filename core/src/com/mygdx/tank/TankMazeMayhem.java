package com.mygdx.tank;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TankMazeMayhem extends Game {
    private FirebaseInterface firebaseInterface;
    private GameModel model;
    private GameView view;
    private GameController controller;
    SpriteBatch batch;

	public TankMazeMayhem(FirebaseInterface firebaseInterface) {
		this.firebaseInterface = firebaseInterface;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		// Set the initial screen to the main menu
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render(); // Delegates rendering to the current screen
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
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
