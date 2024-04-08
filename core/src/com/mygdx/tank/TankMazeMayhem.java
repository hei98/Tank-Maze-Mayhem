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


		// Initializing the camera with the screen's width and height
		camera = new OrthographicCamera();
		camera.setToOrtho(false, mapWidthInPixels * unitScale, mapHeightInPixels * unitScale);
		camera.update();

		renderer = new OrthogonalTiledMapRenderer(map, unitScale);
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
		// Calculate the aspect ratio of the screen
		float screenAspect = (float) width / (float) height;

		// Map width and height in tiles
		int mapWidthInTiles = 40;
		int mapHeightInTiles = 20;

		// Tile size in pixels
		float tileSize = 20f;

		// Convert the map size to world units (assuming 1 unit = 1 tile)
		float mapWidthInUnits = mapWidthInTiles;
		float mapHeightInUnits = mapHeightInTiles;

		// Calculate the scale needed to fit the map on screen
		float scale = Math.max(mapWidthInUnits / screenAspect, mapHeightInUnits);

		// Applying the scale to get the viewport dimensions that fit the map to the screen
		camera.viewportWidth = scale * screenAspect;
		camera.viewportHeight = scale;

		// Updating the camera position to center the map
		camera.position.set(mapWidthInUnits / 2, mapHeightInUnits / 2, 0);

		// Update the camera to apply changes
		camera.update();

		// Updating the renderer's view
		renderer.setView(camera);
	}

	@Override
	public void dispose () {
		batch.dispose();
		map.dispose();
		renderer.dispose();

	}
}
