package com.mygdx.tank.model.components.powerup;

import com.mygdx.tank.model.components.Component;

public class PowerUpTypeComponent implements Component {
    public PowerupType powerupType;

    public enum PowerupType {
        Shield, Speed, Minigun, Godmode
    }

    public PowerUpTypeComponent(PowerupType powerupType) {
        this.powerupType = powerupType;
    }
}
