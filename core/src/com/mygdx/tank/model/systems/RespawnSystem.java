package com.mygdx.tank.model.systems;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.tank.HealthComponent;

import java.util.ArrayList;
import java.util.List;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;

public class RespawnSystem {
    private List<Entity> entities;
    private AccountService accountService;
    private Client client;

    public RespawnSystem(List<Entity> entities, AccountService accountService, Client client) {
        this.entities = entities;
        this.accountService = accountService;
        this.client = client;
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            HealthComponent health = entity.getComponent(HealthComponent.class);
            if (health != null && !health.isAlive()) {
                respawn(entity);
            }
        }
    }

    public void respawn(Entity entity) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        SpriteDirectionComponent direction = entity.getComponent(SpriteDirectionComponent.class);
        if (position != null && direction != null) {
            position.resetPosition();
            direction.angle = 0;
            List<Object> list = new ArrayList<>();
            list.add(accountService.getCurrentUser().getPlayer());
            list.add(direction);
            client.sendTCP(list);
        }

        HealthComponent health = entity.getComponent(HealthComponent.class);
        if (health != null) {
            health.resetHealth();
        }
    }
}
