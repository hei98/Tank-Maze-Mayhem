package com.mygdx.tank.model.components.bullet;

import com.mygdx.tank.model.components.Component;

public class ShootingCooldownComponent implements Component {
    public float cooldown;

    public ShootingCooldownComponent(float cooldown) {
        this.cooldown = cooldown;
    }
}
