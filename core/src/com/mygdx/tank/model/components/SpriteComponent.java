package com.mygdx.tank.model.components;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {
    private Sprite sprite;

    public SpriteComponent(String path) {
        this.sprite = new Sprite(new Texture(path));
        this.sprite.setOrigin(sprite.getWidth() / 2, (sprite.getHeight() - sprite.getHeight() / 3) / 2);

    }

    public Sprite getSprite() {
        return sprite;
    }
}
