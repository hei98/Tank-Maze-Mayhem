package com.mygdx.tank.model.components;

public class ShootingCooldownComponent implements Component {
    public float cooldown;

    public ShootingCooldownComponent(float cooldown) {
        this.cooldown = cooldown;
    }
}
