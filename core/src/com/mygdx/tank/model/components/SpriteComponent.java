package com.mygdx.tank.model.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {
    private final Sprite sprite;

    public SpriteComponent(String path) {
        this.sprite = new Sprite(new Texture(path));
    }

    public Sprite getSprite() {
        return sprite;
    }
}
