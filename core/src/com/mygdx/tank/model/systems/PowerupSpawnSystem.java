package com.mygdx.tank.model.systems;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.model.BulletFactory;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.EntityFactory;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.PowerupFactory;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;

import java.util.ArrayList;
import java.util.List;

public class PowerupSpawnSystem {
    public float timer;
    public boolean spawnedPowerup;
    private EntityFactory powerupFactory;
    private GameModel model;
    private AccountService accountService;
    private Client client;

    public PowerupSpawnSystem(GameModel model, AccountService accountService, Client client) {
        this.timer = 10.0f;
        this.spawnedPowerup = false;
        this.powerupFactory = new PowerupFactory();
        this.model = model;
        this.accountService = accountService;
        this.client = client;
    }

    public void update(float deltaTime) {
        if (timer == 0.0f && !spawnedPowerup) {
            Entity powerUp = powerupFactory.createEntity(accountService.getCurrentUser().getPlayer());
            List<Object> list = new ArrayList<>();
            PowerUpTypeComponent powerUpType = powerUp.getComponent(PowerUpTypeComponent.class);
            PositionComponent position = powerUp.getComponent(PositionComponent.class);
            if (accountService.getCurrentUser().getPlayer().getPlayerName().equals("Player1")) {
                // only player1 can spawn a powerup. After spawning it on client-side, it sends it to server
                // this is to ensure only one powerup is present at a time
                list.add(powerUpType.powerupType);
                list.add(position.x);
                list.add(position.y);
                model.addEntity(powerUp);
                client.sendTCP(list);
            }
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
