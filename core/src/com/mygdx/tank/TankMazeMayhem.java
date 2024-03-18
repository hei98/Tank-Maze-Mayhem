package com.mygdx.tank;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class TankMazeMayhem extends ApplicationAdapter {
	private GameModel model;
	private GameView view;
	private GameController controller;

	@Override
	public void create() {
		model = new GameModel();
		view = new GameView(model);
		controller = new GameController(model);

	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		controller.update(deltaTime);
		model.update(deltaTime);
		view.render();
	}
}

