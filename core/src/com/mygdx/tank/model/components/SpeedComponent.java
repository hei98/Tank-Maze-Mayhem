package com.mygdx.tank.model.components;

public class SpeedComponent implements Component {
    public float speed;
    public float speedX;
    public float speedY;

    public SpeedComponent(float speed, float speedX, float speedY) {
        this.speed = speed;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public SpeedComponent(float speed) {
        this(speed, 0.0f, 0.0f);
    }
}
