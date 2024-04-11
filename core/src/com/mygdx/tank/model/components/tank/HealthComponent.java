package com.mygdx.tank.model.components.tank;

import com.mygdx.tank.model.components.Component;

public class HealthComponent implements Component {
    public int currentHealth;

    public HealthComponent(int currentHealth) {
        this.currentHealth = currentHealth;
    }
}
