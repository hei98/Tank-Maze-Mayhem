package com.mygdx.tank;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameView {
    private GameModel model;
    private SpriteBatch spriteBatch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private float unitScale = 1/ 20f;

    public GameView(GameModel model) {
        this.model = model;
        spriteBatch = new SpriteBatch();
    }

    public void create() {
        map = new TmxMapLoader().load("MazeMayhemMapNew.tmx");

        int mapWidthInPixels = 800;
        int mapHeightInPixels = 400;


        // Initialize the camera with the screen's width and height
        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidthInPixels * unitScale, mapHeightInPixels * unitScale);
        camera.update();

        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();

        spriteBatch.begin();
        for (Entity entity : model.getEntities()) {
            SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
            SpriteDirectionComponent spriteDirectionComponent = entity.getComponent(SpriteDirectionComponent.class);
            if (spriteComponent != null && positionComponent != null) {
                Sprite sprite = spriteComponent.getSprite();
                sprite.setPosition(positionComponent.x, positionComponent.y);
                sprite.setRotation(spriteDirectionComponent.angle);
                sprite.draw(spriteBatch);
            }
        }
        spriteBatch.end();
    }

    public void resize(int width, int height) {
        // Calculate the aspect ratio of the screen
        float aspectRatio = (float) width / (float) height;
        int mapWidthInPixels = 800;
        int mapHeightInPixels = 400;

        // Set the camera's viewport width to match the screen width
        camera.viewportWidth = aspectRatio * mapHeightInPixels * unitScale;

        // Set the camera's viewport height to match the screen height
        camera.viewportHeight = mapHeightInPixels * unitScale;

        // Update the camera
        camera.update();
    }
    public void dispose() {
        spriteBatch.dispose();
        map.dispose();
        renderer.dispose();
    }
}
