package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Constants;
import com.mygdx.tank.IView;
import com.mygdx.tank.MusicManager;
import com.mygdx.tank.MusicMemento;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.model.MenuScreens.MenuModel;

public class SettingsView implements Screen, IView {

    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Constants con;
    private Stage stage;
    private final Texture background;
    private final TextButton backButton;
    private ImageButton soundControl;
    private SelectBox<String> musicSelect;
    private final ImageButton soundControlButton;
    private SpriteBatch batch;
    private Boolean isPlaying;
    private final MusicManager musicManager;
    private final MusicMemento initialMusicSettings;

    public SettingsView(TankMazeMayhem game, AccountService accountService) {
        this.game = game;
        this.accountService = accountService;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");
        backButton = new TextButton("Back", con.getSkin(), "default");
        soundControlButton = new ImageButton(con.getSkin(), "music");
        musicManager = game.getMusicManager();
        initialMusicSettings = musicManager.saveToMemento();
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtonLayout();
        createHeadline();
        createSoundControl();
        createMusicSelection();
        addUndoButton();

        stage.addActor(backButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);

        batch.begin();
        batch.draw(background, 0, 0, con.getSWidth(), con.getSHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public TextButton getBackButton() {
        return this.backButton;
    }

    public ImageButton getSoundControlButton() {
        return this.soundControlButton;
    }

    private void setButtonLayout() {
        backButton.setBounds(con.getCenterTB(), con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Settings", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF()*2f);
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.83f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }

    private void createMusicSelection() {
        Label.LabelStyle selectionStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK);
        Label selectionLabel = new Label("Select Music", selectionStyle);
        selectionLabel.setFontScale(con.getTScaleF()* 1.5f);
        selectionLabel.setX(con.getSWidth() * 0.4f);
        selectionLabel.setY((con.getSHeight()*0.5f) - selectionLabel.getPrefHeight());
        stage.addActor(selectionLabel);

        String[] trackNames = new String[]{"To Victory", "Heart of Courage", "Fever Dream", "Fortunate Son"};
        musicSelect = new SelectBox<>(con.getSkin());
        musicSelect.setItems(trackNames);
        musicSelect.setSelected(musicManager.getCurrentTrackName());
        musicSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicManager.changeTrack(musicSelect.getSelected());
            }
        });
        musicSelect.setSize(con.getTBWidth()*0.75f, con.getTBHeight()*0.75f);
        musicSelect.setPosition(con.getSWidth() * 0.6f, con.getSHeight() * 0.47f - selectionLabel.getPrefHeight());
        stage.addActor(musicSelect);
    }

    private void createSoundControl() {
        Label.LabelStyle soundStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK);
        Label soundLabel = new Label("Mute/unmute", soundStyle);
        soundLabel.setFontScale(con.getTScaleF()* 1.5f);
        soundLabel.setX(con.getSWidth() * 0.4f);
        soundLabel.setY((con.getSHeight()*0.63f) - soundLabel.getPrefHeight());
        stage.addActor(soundLabel);

        soundControl = new ImageButton(con.getSkin(), "music");
        soundControlButton.setSize(con.getIBSize(), con.getIBSize());
        soundControlButton.getImageCell().expand().fill();
        soundControlButton.setPosition(con.getSWidth() * 0.6f, (con.getSHeight()*0.6f) - soundLabel.getPrefHeight());
        if(musicManager.isMenuMusicPlaying()){
            soundControlButton.toggle(); //For visual correctness
        }

        soundControl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                musicManager.muteMenuMusic(musicManager.isMenuMusicPlaying());
            }
        });

        stage.addActor(soundControlButton);

    }

    private void addUndoButton(){
        TextButton undoButton = new TextButton("undo", con.getSkin());
        undoButton.setBounds(con.getSWidth()*0.6f, con.getSHeight()*0.2f, con.getTBWidth()*0.5f, con.getTBHeight()*0.5f);
        undoButton.getLabel().setFontScale(con.getTScaleF());

        undoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(initialMusicSettings.isPlaying() != musicManager.isMenuMusicPlaying()){
                    soundControl.toggle(); //For visual correctness
                }
                musicManager.restoreFromMemento(initialMusicSettings);
                musicSelect.setSelected(musicManager.getCurrentTrackName());


            }
        });
        stage.addActor(undoButton);
    }
}
