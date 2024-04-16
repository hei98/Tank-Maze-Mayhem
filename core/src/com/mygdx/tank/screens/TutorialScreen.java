package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Constants;
import com.mygdx.tank.TankMazeMayhem;

public class TutorialScreen implements Screen {
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Constants con;
    private Stage stage;
    private final Image tutorialImage;
    private final TextButton skipButton, nextButton, backButton;
    private int currentPageIndex = 0;
    private final Texture[] pages = new Texture[]{
            new Texture(Gdx.files.internal("Backgrounds/tutorial_1.JPG")),
            new Texture(Gdx.files.internal("Backgrounds/tutorial_2.JPG")),
            new Texture(Gdx.files.internal("Backgrounds/Tutorial_3.png")),
            new Texture(Gdx.files.internal("Backgrounds/tutorial_4.JPG")),
            new Texture(Gdx.files.internal("Backgrounds/tutorial_5.JPG")),
            new Texture(Gdx.files.internal("Backgrounds/Tutorial_6.png")),
    };
    private final Screen returnScreen;

    public TutorialScreen(TankMazeMayhem game, Screen returnScreen, AccountService accountService) {
        this.game = game;
        this.returnScreen = returnScreen;
        this.accountService = accountService;

        Skin skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));
        con = Constants.getInstance();
        tutorialImage = new Image(pages[currentPageIndex]);

        skipButton = new TextButton("Skip Tutorial", skin);
        nextButton = new TextButton("Next", skin);
        backButton = new TextButton("Back", skin);
    }

    private void endTutorial() {
        game.setScreen(returnScreen);
    }

    @Override
    public void show() {
        stage = new Stage();

        tutorialImage.setFillParent(true);
        stage.addActor(tutorialImage);

        setupButtons();
        addListeners();

        Gdx.input.setInputProcessor(stage);
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
        stage.dispose();
    }

    private void setupButtons() {
        skipButton.setBounds(con.getSWidth() * 0.02f, con.getSHeight() * 0.87f, con.getTBWidth() / 2, con.getTBHeight());
        skipButton.getLabel().setFontScale(con.getTScaleF());
        skipButton.getColor().set(Color.BLACK);

        nextButton.setBounds(con.getSWidth() * 0.85f, con.getSHeight() * 0.02f, con.getTBWidth() / 2, con.getTBHeight());
        nextButton.getLabel().setFontScale(con.getTScaleF());
        nextButton.getColor().set(Color.BLACK);

        backButton.setBounds(con.getSWidth() * 0.02f, con.getSHeight() * 0.02f, con.getTBWidth() / 2, con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());
        backButton.getColor().set(Color.BLACK);

        stage.addActor(skipButton);
        stage.addActor(nextButton);
        stage.addActor(backButton);
    }

    private void addListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentPageIndex > 0) {
                    currentPageIndex--;
                    tutorialImage.setDrawable(new TextureRegionDrawable(new TextureRegion(pages[currentPageIndex])));
                } else {
                    game.setScreen(new MainMenuScreen(game, accountService));
                }
            }
        });
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
        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                endTutorial();
            }
        });
        stage.addActor(skipButton);
    }
}
