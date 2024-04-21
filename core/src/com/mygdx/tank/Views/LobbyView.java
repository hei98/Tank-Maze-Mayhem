package com.mygdx.tank.Views;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.mygdx.tank.Constants;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.model.MenuModel;

public class LobbyView implements Screen, IView {
    private final Constants con;
    private final TankMazeMayhem game;
    private MenuModel model;
    private final Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private final TextButton backButton, createPartyButton, joinPartyButton;
    private final TextField IPaddressField, portField;
    private Label errorLabel;

    public LobbyView(TankMazeMayhem game, MenuModel model) {
        this.game = game;
        this.model = model;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");

        backButton = new TextButton("Back", con.getSkin(), "default");
        createPartyButton = new TextButton("Create party", con.getSkin(),"default");
        joinPartyButton = new TextButton("Join party", con.getSkin(),"default");

        IPaddressField = new TextField("", con.getSkin());
        portField = new TextField("", con.getSkin());
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        setButtons();
        setFields();
        createHeadline();
        setErrorLabel();

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
        background.dispose();
        batch.dispose();
    }

    public TextButton getBackButton() {
        return this.backButton;
    }
    public TextButton getCreatePartyButton() {
        return this.createPartyButton;
    }
    public TextButton getJoinPartyButton() {
        return this.joinPartyButton;
    }
    public String getIPAddress() {
        return IPaddressField.getText();
    }

    public String getPort() {
        return portField.getText();
    }

    private void setButtons() {
        backButton.setBounds(con.getCenterTB() - con.getTBWidth() / 2 - con.getTBWidth() / 5, con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        createPartyButton.setBounds(con.getCenterTB() + con.getTBWidth() / 2 + con.getTBWidth() / 5, con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        createPartyButton.getLabel().setFontScale(con.getTScaleF());

        joinPartyButton.setBounds(con.getCenterTB(), con.getSHeight()*0.35f, con.getTBWidth(), con.getTBHeight());
        joinPartyButton.getLabel().setFontScale(con.getTScaleF());

        stage.addActor(backButton);
        stage.addActor(createPartyButton);
        stage.addActor(joinPartyButton);
    }

    private void setFields() {
        IPaddressField.setBounds(con.getCenterTB() - con.getTBWidth() * 0.2f, con.getSHeight() * 0.5f, con.getTBWidth() * 0.8f, con.getTBHeight());
        IPaddressField.setText("10.0.2.2");


        portField.setBounds(con.getCenterTB() + con.getTBWidth() * 0.75f, con.getSHeight() * 0.5f, con.getTBWidth() * 0.4f, con.getTBHeight());
        portField.setText("5000");

        // Set font scale for email and password text fields
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(IPaddressField.getStyle());
        textFieldStyle.font.getData().setScale(con.getTScaleF()); // Set the font scale
        IPaddressField.setStyle(textFieldStyle);
        portField.setStyle(textFieldStyle);

        stage.addActor(IPaddressField);
        stage.addActor(portField);
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Multiplayer", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF()*2f);
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }

    private void setErrorLabel() {
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.BLACK);

        errorLabel = new Label(model.getErrorLabel(), labelStyle);

        errorLabel.setWrap(true);
        errorLabel.setAlignment(Align.center);
        errorLabel.setBounds(con.getCenterTB(), con.getSHeight() * 0.20f, con.getTBWidth(), con.getTBHeight());
        errorLabel.setFontScale(con.getTScaleF());

        stage.addActor(errorLabel);
    }

    @Override
    public void updateView(MenuModel model) {
        errorLabel.setText(model.getErrorLabel());
    }
}
