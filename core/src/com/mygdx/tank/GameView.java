package com.mygdx.tank;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.tank.controllers.GameController;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.Scoreboard;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.mygdx.tank.Views.GameCrashedView;
import com.mygdx.tank.Views.GameOverView;
import com.mygdx.tank.Views.InGameMenuView;

import java.util.HashMap;
import java.util.Map;

public class GameView{
    private final GameModel model;
    private SpriteBatch spriteBatch;
    private Constants con;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private float knobPercentX, knobPercentY;
    private Touchpad touchpad;
    private Stage gameStage;
    private final GameController controller;
    private ImageButton circularButton, menuButton;
    private final float countdownTime = 120;
    private float elapsedTime = 0;
    private Label countdownLabel;
    private final TankMazeMayhem game;
    private final Scoreboard scoreboard;
    private final AccountService accountService;
    private Label scoreLabel;
    private Server server;
    private Client client;
    private InGameMenuView inGameMenuScreen;
    private GameCrashedView gameCrashedScreen;
    private boolean isMenuVisible;
    private boolean gameCrashed = false;
    private String currentTime;
    private boolean renderedCrashedScreen = false;

    public GameView(GameModel model, GameController controller, TankMazeMayhem game, AccountService accountService, Scoreboard scoreboard, Client client) {
        this.model = model;
        this.controller = controller;
        this.game = game;
        this.scoreboard = scoreboard;
        this.accountService = accountService;
        this.client = client;
    }

    public GameView(GameModel model, GameController controller, TankMazeMayhem game, AccountService accountService, Scoreboard scoreboard, Server server) {
        this.model = model;
        this.controller = controller;
        this.game = game;
        this.scoreboard = scoreboard;
        this.accountService = accountService;
        this.server = server;
    }

    public void create() {
        // Can remove the first map later since we only run on android
        if (Gdx.app.getType() == ApplicationType.Desktop) {
            map = new TmxMapLoader().load("TiledMap/Map.tmx");
        } else {
            map = new TmxMapLoader().load("TiledMap/Map2.tmx");
        }

        gameStage = new Stage();
        spriteBatch = new SpriteBatch();
        con = Constants.getInstance();
        Texture buttonTexture = new Texture(Gdx.files.internal("images/fireButton.png"));

        // Create a skin for the circular button
        Skin circularButtonSkin = new Skin();
        circularButtonSkin.add("circleButton", buttonTexture);

        // Define the buttons and style
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        menuButton = new ImageButton(con.getSkin(), "settings");
        buttonStyle.up = new TextureRegionDrawable(buttonTexture);
        circularButton = new ImageButton(buttonStyle);

        BitmapFont font = new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        scoreLabel = new Label("Score: 0", labelStyle);

        gameCrashedScreen = new GameCrashedView(game, accountService);

        // Initiate in game menu screen and ensure it is hidden
        if (client != null) {
            inGameMenuScreen = new InGameMenuView(game, accountService, scoreboard, this, client, model);
            client.addListener(new Listener() {
                @Override
                public void disconnected(Connection connection) {
                    gameCrashed = true;
                }
            });
        } else {
            inGameMenuScreen = new InGameMenuView(game, accountService, scoreboard, this, server);
        }

        isMenuVisible = false;

        countdownLabel = new Label("", labelStyle);
        countdownLabel.setPosition(con.getSWidth() * 0.5f, con.getSHeight() * 0.95f);
        countdownLabel.setFontScale(con.getTScaleF());
        gameStage.addActor(countdownLabel);

        // Initialize the camera with the screen's width and height
        camera = new OrthographicCamera();
        camera.setToOrtho(false, con.getSWidth(), con.getSHeight());
        camera.update();

        // Tried to scale the map to fit all screens, but the map will only fit certain screens
        float mapWidth = map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class);
        float mapHeight = map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);
        float scaleFactorX = con.getSWidth() / mapWidth;
        float scaleFactorY = con.getSHeight() / mapHeight;
        float scaleFactor = Math.min(scaleFactorX, scaleFactorY);

        renderer = new OrthogonalTiledMapRenderer(map, scaleFactor);

        setButtonsLabelTouchPad();
        addListeners();

        Gdx.input.setInputProcessor(gameStage);
    }

    public void render() {
        renderGame();
        if (isMenuVisible) {
            renderMenuScreen();
        } else if (gameCrashed) {
            if (!renderedCrashedScreen) {
                gameCrashedScreen.show();
                renderedCrashedScreen = true;
            }
            gameCrashedScreen.render(Gdx.graphics.getDeltaTime());
        }
    }



    public void resize(int width, int height) {
        // Calculate the aspect ratio of the screen
    }

    public void dispose() {
        spriteBatch.dispose();
        map.dispose();
        renderer.dispose();
        gameStage.dispose();
        inGameMenuScreen.dispose();
    }

    private void setButtonsLabelTouchPad() {
        knobPercentX = 0.0f;
        knobPercentY = 0.0f;

        touchpad = new Touchpad(5f, con.getSkin());
        touchpad.setBounds(con.getSWidth() * 0.05f, con.getSHeight() * 0.05f, con.getIBSize() * 2f, con.getIBSize() * 2f);

        circularButton.setPosition(con.getSWidth() * 0.85f, con.getSHeight() * 0.05f);
        circularButton.setSize(con.getIBSize() * 2f, con.getIBSize() * 2f);

        menuButton.setSize(con.getIBSize(), con.getIBSize());
        menuButton.getImageCell().expand().fill();
        menuButton.setPosition(con.getSWidth() * 0.01f, con.getSHeight() * 0.98f - menuButton.getHeight());

        // Make buttons see through
        touchpad.getColor().a = 0.5f;
        circularButton.getColor().a = 0.5f;
        menuButton.getColor().a = 0.5f;

        //scoreLabel
        scoreLabel.setPosition(con.getSWidth()*0.9f, Gdx.graphics.getHeight()*0.9f);
        scoreLabel.setFontScale(con.getTScaleF());


        gameStage.addActor(touchpad);
        gameStage.addActor(circularButton);
        gameStage.addActor(scoreLabel);
        gameStage.addActor(menuButton);
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
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle button click event here
                inGameMenuScreen.show();
                isMenuVisible = true;
            }
        });
    }

    private void updateEntitySprites() {
        synchronized (model.getEntities()) {
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
    }

    public void renderGame() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsedTime += Gdx.graphics.getDeltaTime();

        camera.update();
        renderer.setView(camera);
        renderer.render();

        controller.handleTouchpadInput(knobPercentX, knobPercentY);

        //Player Score
        String playerName = accountService.getCurrentUserEmail().split("@")[0];
        scoreLabel.setText("Score:" + scoreboard.getPlayerScore(playerName));
        float remainingTime = countdownTime - elapsedTime;
        int minutes = (int) (remainingTime / 60);
        int seconds = (int) (remainingTime % 60);

        if (remainingTime < 0 && !gameCrashed) {
            HashMap<String, Integer> scores = scoreboard.getScoreboard();
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                String userName = entry.getKey();
                Integer score = entry.getValue();
                game.getFirebaseInterface().updateLeaderboard(userName, score);
            }
            if (server != null) {
                server.close();
            }
            game.setScreen(new GameOverView(game, accountService, scoreboard));
        }

        // Start batch processing
        spriteBatch.begin();

        updateEntitySprites();
        String timerText = String.format("%02d:%02d", minutes, seconds);
        if (!gameCrashed) {
            currentTime = timerText;
        }
        countdownLabel.setText(currentTime);

        // End batch processing
        spriteBatch.end();

        gameStage.act();
        gameStage.draw();
    }

    private void renderMenuScreen() {
        // Render the in menu screen
        inGameMenuScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void toggleMenu() {
        isMenuVisible = !isMenuVisible;
        if (isMenuVisible) {
            inGameMenuScreen.show();
        } else {
            inGameMenuScreen.hide();
            Gdx.input.setInputProcessor(gameStage); // Set the input processor back to gameStage
        }
    }
}
