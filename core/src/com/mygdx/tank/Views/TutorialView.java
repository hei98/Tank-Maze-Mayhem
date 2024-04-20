package com.mygdx.tank.Views;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.tank.Constants;
import com.mygdx.tank.IView;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.model.MenuModel;

public class TutorialView implements Screen, IView {
    private final TankMazeMayhem game;
    private final MenuModel model;
    private final Constants con;
    private Stage stage;
    private final Image tutorialImage;
    private final TextButton skipButton, nextButton, backButton;
    private final Texture[] pages = new Texture[]{
            new Texture(Gdx.files.internal("Backgrounds/tutorial_1.JPG")),
            new Texture(Gdx.files.internal("Backgrounds/tutorial_2.JPG")),
            new Texture(Gdx.files.internal("Backgrounds/tutorial_3.png")),
            new Texture(Gdx.files.internal("Backgrounds/tutorial_4.JPG")),
            new Texture(Gdx.files.internal("Backgrounds/tutorial_5.JPG")),
            new Texture(Gdx.files.internal("Backgrounds/tutorial_6.png")),
    };

    public TutorialView(TankMazeMayhem game, MenuModel model) {
        this.game = game;
        this.model = model;

        con = Constants.getInstance();
        model.setTutorialPageIndex(0);
        tutorialImage = new Image(pages[model.getTutorialPageIndex()]);

        skipButton = new TextButton("Skip Tutorial", con.getSkin());
        nextButton = new TextButton("Next", con.getSkin());
        backButton = new TextButton("Back", con.getSkin());
    }

    @Override
    public void show() {
        stage = new Stage();

        tutorialImage.setFillParent(true);
        stage.addActor(tutorialImage);

        setupButtons();

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

    public TextButton getBackButton() {
        return this.backButton;
    }
    public TextButton getSkipButton() {
        return this.skipButton;
    }
    public TextButton getNextButton() {
        return this.nextButton;
    }

    public int getPagesLength() {
        return pages.length;
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

    @Override
    public void updateView(MenuModel model) {
        tutorialImage.setDrawable(new TextureRegionDrawable(new TextureRegion(pages[model.getTutorialPageIndex()])));
    }
}
