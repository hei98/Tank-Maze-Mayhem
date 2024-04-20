package com.mygdx.tank.model.systems;

import com.badlogic.gdx.Gdx;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;
import com.mygdx.tank.model.components.tank.PowerupStateComponent;
import com.mygdx.tank.model.states.GodmodeState;
import com.mygdx.tank.model.states.MinigunState;
import com.mygdx.tank.model.states.PowerupState;
import com.mygdx.tank.model.states.ShieldState;
import com.mygdx.tank.model.states.SpeedState;

public class GrantPowerupSystem {

    public GrantPowerupSystem() {

    }


    public void givePlayertankPowerup(Entity tank, Entity powerup) {
        powerup.markForRemoval(true);
        Gdx.app.log("InfoTag", "Powerup:" + powerup);
        PowerupStateComponent powerupStateComponent = tank.getComponent(PowerupStateComponent.class);
        if (powerup.getComponent(PowerUpTypeComponent.class).powerupType == PowerUpTypeComponent.PowerupType.Shield) {
            powerupStateComponent.setState( new ShieldState());
        } else if (powerup.getComponent(PowerUpTypeComponent.class).powerupType == PowerUpTypeComponent.PowerupType.Speed) {
            powerupStateComponent.setState( new SpeedState());
        } else if (powerup.getComponent(PowerUpTypeComponent.class).powerupType == PowerUpTypeComponent.PowerupType.Minigun) {
            powerupStateComponent.setState( new MinigunState());
        } else if (powerup.getComponent(PowerUpTypeComponent.class).powerupType == PowerUpTypeComponent.PowerupType.Godmode) {
            powerupStateComponent.setState( new GodmodeState());
        }
        Gdx.app.log("InfoTag", "Powerup granted:" + powerupStateComponent.getState());
        PowerupState powerupState =  powerupStateComponent.getState();
        powerupState.doAction(tank);
    }
}
