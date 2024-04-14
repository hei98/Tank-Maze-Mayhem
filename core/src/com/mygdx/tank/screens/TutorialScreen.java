package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
    private int currentPageIndex = 0;
    private final String[] pageImages = {"backgrounds/StartpageTutorial.png", "backgrounds/projectile-tutorial.png", "backgrounds/projectile-tutorial2.png"};
    private final Texture[] pages = new Texture[pageImages.length];
    private Screen returnScreen;

    public TutorialScreen(TankMazeMayhem game,Screen returnScreen) {
        this.game = game;
        this.returnScreen = returnScreen;

        for (int i = 0; i < pageImages.length; i++) {
            pages[i] = new Texture(Gdx.files.internal(pageImages[i]));
        }

/*


        tutorialImage = new Image(pages[currentPageIndex]);
        tutorialImage.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Fill the screen
        stage.addActor(tutorialImage); // Add to the stage for drawing

        setupButtons();
        stage.addActor(skipButton);
        stage.addActor(nextButton);
/**/

    }




    private void setupButtons() {
        // Set up the "Skip Tutorial" button
        skipButton = new TextButton("Skip Tutorial", game.getButtonStyle());
        skipButton.setPosition(20, Gdx.graphics.getHeight() - skipButton.getHeight() - 20);
        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                endTutorial(); // Use this method to handle ending the tutorial
            }
        });

        // Set up the "Next" button
        nextButton = new TextButton("Next", game.getButtonStyle());
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
    }
    private void endTutorial() {
        game.setScreen(returnScreen);
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();




        Gdx.input.setInputProcessor(stage);
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        batch.begin();
        batch.draw(pages[0],0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.end();


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

        stage.dispose();
    }

    // Implement the other required methods of the Screen interface...
}
