package com.mygdx.tank.model.components.tank;

import com.mygdx.tank.model.components.Component;

public class SpriteDirectionComponent implements Component {
    public float angle;

    public SpriteDirectionComponent(float angle) {
        this.angle = angle;
    }

    public SpriteDirectionComponent() {}
}
