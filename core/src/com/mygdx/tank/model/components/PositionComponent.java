package com.mygdx.tank.model.components;

public class PositionComponent implements Component {
    public float x, y;
    public final float originalX, originalY;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
    }

    public PositionComponent() {
        this.originalX = x;
        this.originalY = y;
    }

    public void resetPosition() {
        this.x = originalX;
        this.y = originalY;
    }
}