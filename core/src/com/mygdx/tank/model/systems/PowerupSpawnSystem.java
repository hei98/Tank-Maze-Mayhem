package com.mygdx.tank.model.systems;

import com.mygdx.tank.model.BulletFactory;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.EntityFactory;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.PowerupFactory;

public class PowerupSpawnSystem {
    public float timer;
    public boolean spawnedPowerup;
    private EntityFactory powerupFactory;
    private GameModel model;

    public PowerupSpawnSystem(GameModel model) {
        this.timer = 10.0f;
        this.spawnedPowerup = false;
        this.powerupFactory = new PowerupFactory();
        this.model = model;
    }

    public void update(float deltaTime) {
        if (timer == 0.0f && !spawnedPowerup) {
            Entity powerUp = powerupFactory.createEntity();
            model.addEntity(powerUp);
            spawnedPowerup = true;
            timer = 10.0f;
        } else if (timer > 0) {
            if (timer - deltaTime < 0) {
                timer = 0.0f;
            } else {
                timer -= deltaTime;
            }
        }
    }


}
