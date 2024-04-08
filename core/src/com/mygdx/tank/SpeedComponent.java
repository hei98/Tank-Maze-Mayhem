package com.mygdx.tank;

public class SpeedComponent implements Component {
    public float speed;
    public float speedX;
    public float speedY;

    // Constructor
    public SpeedComponent(float speed) {
        this.speed = speed;
        this.speedX = 0.0f;
        this.speedY = 0.0f;
    }
}
