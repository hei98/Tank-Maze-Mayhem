package com.mygdx.tank;

import static java.awt.Color.WHITE;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.tank.controllers.GameController;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.components.PlayerComponent;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class GameView implements ScoreObserver{
    private GameModel model;
    private SpriteBatch spriteBatch;
    private final Constants con;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private float knobPercentX, knobPercentY;
    private Touchpad touchpad;
    private Stage stage;
    private GameController controller;
    private Skin touchpadSkin, skin;
    private Texture buttonTexture;
    private ImageButton circularButton;
    private ImageButton.ImageButtonStyle buttonStyle;
    private Label scoreLabel;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;
        spriteBatch = new SpriteBatch();
        con = Constants.getInstance();
        touchpadSkin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));
        buttonTexture = new Texture(Gdx.files.internal("images/fireButton.png"));

        // Create a skin for the circular button
        skin = new Skin();
        skin.add("circleButton", buttonTexture);

        // Define the style for the circular button
        buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(buttonTexture);
        circularButton = new ImageButton(buttonStyle);

        scoreLabel = new Label("Score:" + model.getPlayerTank().getComponent(PlayerComponent.class).player.getPlayerScoreComponent().getScore(), skin);

        this.model.getPlayerScoreSystem().addObserver(this);
    }

    public void create() {
        // Can remove the first map later since we only run on android
        if (Gdx.app.getType() == ApplicationType.Desktop) {
            map = new TmxMapLoader().load("TiledMap/Map.tmx");
        } else {
            map = new TmxMapLoader().load("TiledMap/Map2.tmx");
        }

        stage = new Stage();

        // Initialize the camera with the screen's width and height
        camera = new OrthographicCamera();
        camera.setToOrtho(false, con.getSWidth(), con.getSHeight());
        camera.update();

        renderer = new OrthogonalTiledMapRenderer(map, 1);

        setButtons();
        addListeners();
        ;
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

        updateEntitySprites();

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
        model.getPlayerScoreSystem().removeObserver(this);
    }

    private void setButtons() {
        knobPercentX = 0.0f;
        knobPercentY = 0.0f;

        touchpad = new Touchpad(5f, touchpadSkin);
        touchpad.setBounds(con.getSWidth() * 0.05f, con.getSHeight() * 0.05f, con.getIBSize() * 2f, con.getIBSize() * 2f);

        circularButton.setPosition(con.getSWidth() * 0.85f, con.getSHeight() * 0.05f);
        circularButton.setSize(con.getIBSize() * 2f, con.getIBSize() * 2f);

        // Make buttons see through
        touchpad.getColor().a = 0.5f;
        circularButton.getColor().a = 0.5f;

        stage.addActor(touchpad);
        stage.addActor(circularButton);
    }

    private void addListeners() {
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                knobPercentX = touchpad.getKnobPercentX();
                knobPercentY = touchpad.getKnobPercentY();
            }

        });

        circularButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle button click event here
                controller.handleFireButton();
            }
        });
    }

    private void updateEntitySprites() {
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
    }

    @Override
    public void scoreUpdated(String playerId, int newScore) {
        if (playerId.equals(model.getPlayerTank().getComponent(PlayerComponent.class).player.getPlayerName())) {
            Gdx.app.postRunnable(() -> {
                scoreLabel.setText("Score: " + newScore);
            });
        }
    }
}
