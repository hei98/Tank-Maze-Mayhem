package com.mygdx.tank;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Constants {
    private static Constants instance;
    private final Skin skin;
    // Screen dimensions
    private static final float BASE_SCREEN_WIDTH = 800f;
    private static final float BASE_SCREEN_HEIGHT = 480f;

    // Scaling factors
    private static final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private static final float WIDTH_SCALE_FACTOR = SCREEN_WIDTH / BASE_SCREEN_WIDTH;
    private static final float HEIGHT_SCALE_FACTOR = SCREEN_HEIGHT / BASE_SCREEN_HEIGHT;

    // Button sizes
    private static final float TEXT_BUTTON_WIDTH = 200f * WIDTH_SCALE_FACTOR;
    private static final float TEXT_BUTTON_HEIGHT = 50f * HEIGHT_SCALE_FACTOR;
    private static final float IMAGE_BUTTON_SIZE = (SCREEN_HEIGHT * 0.1f);
    private static final float TEXT_SCALE_FACTOR = (TEXT_BUTTON_HEIGHT * 0.7f) / 32f;
    private static final float TANK_WIDTH = (35f * 0.5f) * WIDTH_SCALE_FACTOR;
    private static final float TANK_HEIGHT = (53f * 0.5f) * WIDTH_SCALE_FACTOR;

    // Other
    private static final float CENTER_TB = (SCREEN_WIDTH - TEXT_BUTTON_WIDTH) / 2;

    private Constants() {
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));
    }

    public static synchronized Constants getInstance() {
        if(instance == null){
            instance = new Constants();
        }
        return instance;
    }

    public float getCenterTB() { return CENTER_TB; }

    public float getSWidth() { return SCREEN_WIDTH; }

    public float getSHeight() { return SCREEN_HEIGHT; }

    public float getTBHeight() {
        return TEXT_BUTTON_HEIGHT;
    }

    public float getTBWidth() {
        return TEXT_BUTTON_WIDTH;
    }

    public float getTScaleF() { return TEXT_SCALE_FACTOR; }

    public float getIBSize() { return IMAGE_BUTTON_SIZE; }
    public float getTankWidth() { return TANK_WIDTH; }
    public float getTankHeight() { return TANK_HEIGHT; }
    public Skin getSkin() { return skin; }
}
