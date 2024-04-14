package com.mygdx.tank;

import com.badlogic.gdx.Gdx;

public class MenuConstants {
    private static MenuConstants instance;
    // Screen dimensions
    private static final float BASE_SCREEN_WIDTH = 800f;
    private static final float BASE_SCREEN_HEIGHT = 480f;

    // Scaling factors
    private static final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private static final float WIDTH_SCALE_FACTOR = SCREEN_WIDTH / BASE_SCREEN_WIDTH;
    private static final float HEIGHT_SCALE_FACTOR = SCREEN_HEIGHT / BASE_SCREEN_HEIGHT;

    // Button sizes
    private static final float TEXT_BUTTON_WIDTH = 200 * WIDTH_SCALE_FACTOR;
    private static final float TEXT_BUTTON_HEIGHT = 50 * HEIGHT_SCALE_FACTOR;
    private static final float IMAGE_BUTTON_SIZE = (SCREEN_HEIGHT * 0.1f);
    private static final float TEXT_SCALE_FACTOR = (TEXT_BUTTON_HEIGHT * 0.7f) / 32;

    // Other
    private static final float CENTER_X = (SCREEN_WIDTH - TEXT_BUTTON_WIDTH) / 2;

    private MenuConstants() {

    }

    public static synchronized MenuConstants getInstance() {
        if(instance == null){
            instance = new MenuConstants();
        }
        return instance;
    }

    public float getCenterX() { return CENTER_X; }

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
}
