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

public class TankMazeMayhem extends Game {
	private final AccountService accountService;
    private final FirebaseInterface firebaseInterface;
    private GameModel model;
    private GameView view;
    private GameController controller;
	private Music backgroundMusic, gameMusic;
	private FPSLogger fpsLogger;
    SpriteBatch batch;

	public TankMazeMayhem(FirebaseInterface firebaseInterface, AccountService accountService) {
		this.firebaseInterface = firebaseInterface;
		this.accountService = accountService;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/09 - Fortunate Son.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.play();

		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/44 End Credits.mp3"));
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

	public void muteMusic(boolean mute) {
		if (backgroundMusic != null) {
			if (mute) {
				backgroundMusic.stop();
			} else {
				backgroundMusic.play();
			}
		}
	}

	public boolean isMusicPlaying() {
		return backgroundMusic != null && backgroundMusic.isPlaying();
	}
	public void startGameMusic() {
		if (backgroundMusic.isPlaying()) {
			backgroundMusic.stop();
		}
		gameMusic.setLooping(true);
		gameMusic.play();
	}

	public void stopGameMusic() {
		if (gameMusic.isPlaying()) {
			gameMusic.stop();
		}
		backgroundMusic.play(); // Resume background music
	}


	@Override
	public void dispose () {
		batch.dispose();
		backgroundMusic.dispose();
		gameMusic.dispose();
	}
}
