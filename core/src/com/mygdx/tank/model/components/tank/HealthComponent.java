package com.mygdx.tank.model.components.tank;

import com.mygdx.tank.model.components.Component;

public class HealthComponent implements Component {
    private int health;
    private final int maxHealth = 1;

    public HealthComponent() {
        this.health = maxHealth;
    }

    public void takeDamage() {
        health = 0;
    }

    public void resetHealth() {
        health = maxHealth;
    }
    public boolean isAlive() {
        return health > 0;
    }
}
