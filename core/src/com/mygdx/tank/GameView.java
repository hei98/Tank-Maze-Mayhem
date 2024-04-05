package com.mygdx.tank;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameView {
    private GameModel model;
    private SpriteBatch spriteBatch;
    private Texture tankTexture = new Texture("tank_blue_2.png");

    public GameView(GameModel model) {
        this.model = model;
        spriteBatch = new SpriteBatch();
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        Entity playerTank = model.getPlayerTank();
        SpriteComponent spriteComponent = playerTank.getComponent(SpriteComponent.class);
        PositionComponent positionComponent = playerTank.getComponent(PositionComponent.class);
        if (spriteComponent != null && positionComponent != null) {
            spriteBatch.draw(spriteComponent.getSprite(), positionComponent.x, positionComponent.y);
        }
        spriteBatch.end();
    }
    public void dispose() {
        spriteBatch.dispose();
    }
}
