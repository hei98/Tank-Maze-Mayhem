package com.mygdx.tank;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class TankMazeMayhem extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private float unitScale = 1/ 20f;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");

		// Load the .tmx map
		map = new TmxMapLoader().load("MazeMayhemMapNew.tmx");

		int mapWidthInPixels = 800;
		int mapHeightInPixels = 400;


		// Initialize the camera with the screen's width and height
		camera = new OrthographicCamera();
		camera.setToOrtho(false, mapWidthInPixels * unitScale, mapHeightInPixels * unitScale);
		camera.update();

		renderer = new OrthogonalTiledMapRenderer(map, unitScale);
	}
	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);


		// update the camera each frame
		camera.update();
		renderer.setView(camera);
		renderer.render();


		batch.begin();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// Update the camera's viewport size
		camera.viewportWidth = width * unitScale;
		camera.viewportHeight = height * unitScale;
		camera.update();
	}
	@Override
	public void dispose () {
		batch.dispose();
		map.dispose();
		renderer.dispose();

	}
}
