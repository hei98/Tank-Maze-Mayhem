package com.mygdx.tank.model.components.tank;

import com.mygdx.tank.model.components.Component;

public class HealthComponent implements Component {
    private int health;

    public HealthComponent() {
        this.health = 1;
    }

    public void takeDamage() {
        health -= 1;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
