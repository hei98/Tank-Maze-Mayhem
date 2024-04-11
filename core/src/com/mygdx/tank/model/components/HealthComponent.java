package com.mygdx.tank.model.components;

public class HealthComponent implements Component {
    private int health;

    public HealthComponent() {
        this.health = 1;
    }

    public void takeDamage() {
        health = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
