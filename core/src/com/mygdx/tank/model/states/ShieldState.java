package com.mygdx.tank.model.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.SpeedComponent;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;
import com.mygdx.tank.model.components.tank.HealthComponent;
import com.mygdx.tank.model.components.tank.PowerupStateComponent;
import com.mygdx.tank.model.components.tank.ShootingCooldownComponent;

public class ShieldState implements PowerupState {

    @Override
    public void doAction(Entity tank) {
        PowerupStateComponent powerupStateComponent = tank.getComponent(PowerupStateComponent.class);
        if (powerupStateComponent.inPowerupMode) {
            if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                tank.getComponent(SpeedComponent.class).speed = 140;
            } else {
                tank.getComponent(SpeedComponent.class).speed = 300;
            }

            tank.getComponent(ShootingCooldownComponent.class).cooldown = 1.5f;
        }

        tank.getComponent(HealthComponent.class).setHealth(1000);
        powerupStateComponent.inPowerupMode = true;
    }

    @Override
    public String getPowerupType() {
        return "Shield";
    }
}