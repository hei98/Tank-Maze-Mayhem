package com.mygdx.tank;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {
    private Sprite sprite;

    public SpriteComponent(String path) {
        this.sprite = new Sprite(new Texture(path));
        this.sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
