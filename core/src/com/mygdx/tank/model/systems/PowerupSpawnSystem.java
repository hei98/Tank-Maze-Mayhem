package com.mygdx.tank.model.systems;

import com.mygdx.tank.AccountService;
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
    private AccountService accountService;

    public PowerupSpawnSystem(GameModel model, AccountService accountService) {
        this.timer = 10.0f;
        this.spawnedPowerup = false;
        this.powerupFactory = new PowerupFactory();
        this.model = model;
        this.accountService = accountService;
    }

    public void update(float deltaTime) {
        if (timer == 0.0f && !spawnedPowerup) {
            Entity powerUp = powerupFactory.createEntity(accountService.getCurrentUser().getPlayer());
            model.addEntity(powerUp);
            spawnedPowerup = true;
        } else if (timer > 0) {
            if (timer - deltaTime < 0) {
                timer = 0.0f;
            } else {
                timer -= deltaTime;
            }
        }
    }

    public void powerupRemoved() {
        this.spawnedPowerup = false;
        this.timer = 10.0f;
    }
}
