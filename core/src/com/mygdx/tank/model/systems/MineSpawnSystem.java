package com.mygdx.tank.model.systems;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.EntityFactory;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.MineFactory;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;

import java.util.ArrayList;
import java.util.List;

public class MineSpawnSystem {
    public float timer;
    public boolean spawnedMine;
    private final EntityFactory mineFactory;
    private final GameModel model;
    private final AccountService accountService;
    private final Client client;

    public MineSpawnSystem(GameModel model, AccountService accountService, Client client) {
        this.timer = 0f;
        this.spawnedMine = false;
        this.mineFactory = new MineFactory();
        this.model = model;
        this.accountService = accountService;
        this.client = client;
    }



    public void update(float deltaTime) {
        if (timer == 0.0f && !spawnedMine) {
            Entity mine = mineFactory.createEntity(accountService.getCurrentUser().getPlayer());
            List<Object> list = new ArrayList<>();
            if (accountService.getCurrentUser().getPlayer().getPlayerName().equals("Player1")) {
                // only player1 can spawn a mine. After spawning it on client-side, it sends it to server
                // this is to ensure only one mine is present at a time
                TypeComponent sendMine = new TypeComponent(TypeComponent.EntityType.MINE);
                list.add(sendMine);
                list.add(accountService.getCurrentUser().getPlayer());
                model.addEntity(mine);
                client.sendTCP(list);
            }
            spawnedMine = true;
        } else if (timer > 0) {
            if (timer - deltaTime < 0) {
                timer = 0.0f;
            } else {
                timer -= deltaTime;
            }
        }
    }

    public void mineRemoved() {
        this.spawnedMine = false;
        this.timer = 5.0f;
    }
}
