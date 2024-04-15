package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.tank.TankMazeMayhem;

public class TutorialScreen implements Screen {

    private TankMazeMayhem game;
    private Stage stage;
    private SpriteBatch batch;
    private Image tutorialImage;
    private TextButton skipButton, nextButton;
    private BitmapFont largeFont;
    private TextButton.TextButtonStyle largeButtonStyle;
    private int currentPageIndex = 0;
    private final Texture[] pages = new Texture[]{
            new Texture(Gdx.files.internal("Backgrounds/StartpageTutorial.png")),
            new Texture(Gdx.files.internal("Backgrounds/projectile-tutorial.png")),
            new Texture(Gdx.files.internal("Backgrounds/projectile-tutorial2.png")),
            new Texture(Gdx.files.internal("Backgrounds/game tutorial (6).png")),
            new Texture(Gdx.files.internal("Backgrounds/game tutorial (5).png")),
            new Texture(Gdx.files.internal("Backgrounds/game tutorial (4).png")),
    };
    private Screen returnScreen;

    public TutorialScreen(TankMazeMayhem game, Screen returnScreen) {
        this.game = game;
        stage = new Stage();
        this.returnScreen = returnScreen;
        tutorialImage = new Image(pages[currentPageIndex]);
        tutorialImage.setFillParent(true);
        stage.addActor(tutorialImage); // Add to the stage for drawing

        largeFont = new BitmapFont();
        largeFont.getData().setScale(3); // Set the scale to make the text larger


        // Create a new style based on the larger font
        largeButtonStyle = new TextButton.TextButtonStyle();
        largeButtonStyle.font = largeFont;
        largeButtonStyle.fontColor = Color.BLACK;
        setupButtons();
        Gdx.input.setInputProcessor(stage);



    }




    private void setupButtons() {
        // Set up the "Skip Tutorial" button
        skipButton = new TextButton("Skip Tutorial", largeButtonStyle);
        skipButton.setPosition(20, Gdx.graphics.getHeight() - skipButton.getHeight() - 20);
        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                endTutorial();
            }
        });
        stage.addActor(skipButton);

    // Initialize the nextButton and handle its click events
    nextButton = new TextButton("Next", largeButtonStyle);
        nextButton.setPosition(Gdx.graphics.getWidth() - nextButton.getWidth() - 20, 20);
        nextButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            currentPageIndex++;
            if (currentPageIndex >= pages.length) {
                endTutorial(); // No more pages, end tutorial
            } else {
                tutorialImage.setDrawable(new TextureRegionDrawable(new TextureRegion(pages[currentPageIndex])));
            }
        }
    });
        stage.addActor(nextButton);

    // Initialize the "Back" button, placed on the left side of the screen at the bottom
    TextButton backButton = new TextButton("Back", largeButtonStyle);
    backButton.setPosition(20, 20); // Bottom left
        backButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (currentPageIndex > 0) {
                currentPageIndex--;
                tutorialImage.setDrawable(new TextureRegionDrawable(new TextureRegion(pages[currentPageIndex])));
            }
        }
    });
    stage.addActor(backButton);
}
    private void endTutorial() {
        game.setScreen(returnScreen);
    }

    @Override
    public void show() {
        //stage = new Stage();
        //batch = new SpriteBatch();




        //Gdx.input.setInputProcessor(stage);
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        for (Texture page : pages) {
            page.dispose();
        }
        if (largeFont != null) {
            largeFont.dispose();

        stage.dispose();
    }

    // Implement the other required methods of the Screen interface...
}}
