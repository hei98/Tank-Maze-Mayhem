package com.mygdx.tank;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.tank.controllers.GameController;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.SpriteDirectionComponent;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class GameView {
    private GameModel model;
    private SpriteBatch spriteBatch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private float mapWidthInPixels, mapHeightInPixels;
    private float knobPercentX, knobPercentY;
    private Touchpad touchpad;
    private Stage stage;
    private GameController controller;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
        spriteBatch = new SpriteBatch();
    }

    public void create() {
        if (Gdx.app.getType() == ApplicationType.Desktop) {
            mapWidthInPixels = 800;
            mapHeightInPixels = 480;
            map = new TmxMapLoader().load("TiledMap/Map.tmx");
        } else {
            mapWidthInPixels = 2220;
            mapHeightInPixels = 1080;
            map = new TmxMapLoader().load("TiledMap/Map2.tmx");
        }

        stage = new Stage();

        // Initialize the camera with the screen's width and height
        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidthInPixels, mapHeightInPixels);
        camera.update();

        renderer = new OrthogonalTiledMapRenderer(map, 1);

        Skin touchpadSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));
        knobPercentX = 0.0f;
        knobPercentY = 0.0f;

        touchpad = new Touchpad(5, touchpadSkin);
        if (Gdx.app.getType() == ApplicationType.Desktop) {
            touchpad.setBounds(30, 30, 150, 150);
        } else {
            touchpad.setBounds(100, 100, 300, 300);
        }
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                knobPercentX = touchpad.getKnobPercentX();
                knobPercentY = touchpad.getKnobPercentY();
            }

        });
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();

        controller.handleTouchpadInput(knobPercentX, knobPercentY);

        // Start batch processing
        spriteBatch.begin();

        for (Entity entity : model.getEntities()) {
            // Attempt to retrieve both the Sprite and Position components for the entity
            SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);

            // Only proceed if both Sprite and Position components are present
            if (spriteComponent != null && positionComponent != null) {
                // Get the sprite from the component
                Sprite sprite = spriteComponent.getSprite();

                // Set the sprite's position based on the entity's position
                sprite.setPosition(positionComponent.x, positionComponent.y);

                // Attempt to retrieve the SpriteDirectionComponent, if it exists
                SpriteDirectionComponent spriteDirectionComponent = entity.getComponent(SpriteDirectionComponent.class);

                // If a SpriteDirectionComponent is present, apply its angle to the sprite's rotation
                if (spriteDirectionComponent != null) {
                    sprite.setRotation(spriteDirectionComponent.angle);
                } else {
                    // If no direction component, reset rotation to default or simply do not rotate.
                    // This line can be omitted if you want to keep the sprite's current rotation,
                    // or if your sprites do not require resetting rotation.
                    sprite.setRotation(0);
                }

                // Draw the sprite with its set position (and rotation, if applicable)
                sprite.draw(spriteBatch);
            }
        }

        // End batch processing
        spriteBatch.end();

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        // Calculate the aspect ratio of the screen

    }
    public void dispose() {
        spriteBatch.dispose();
        map.dispose();
        renderer.dispose();
        stage.dispose();
    }
}
