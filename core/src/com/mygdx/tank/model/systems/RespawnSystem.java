package com.mygdx.tank.model.systems;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.PlayerComponent;
import com.mygdx.tank.model.components.tank.HealthComponent;

import java.util.ArrayList;
import java.util.List;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.tank.PowerupStateComponent;
import com.mygdx.tank.model.states.InvulnerabilityState;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;
import com.mygdx.tank.model.states.NormalState;

public class RespawnSystem {
    private final List<Entity> entities;
    private final AccountService accountService;
    private final Client client;

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
            entity.getComponent(PowerupStateComponent.class).setState(new NormalState());
            position.resetPosition();
            direction.angle = 0;
        }

        HealthComponent health = entity.getComponent(HealthComponent.class);
        if (health != null) {
            health.resetHealth(); // Reset health
        }

        PowerupStateComponent stateComponent = entity.getComponent(PowerupStateComponent.class);
        if (stateComponent != null) {
            InvulnerabilityState invulnerabilityState = new InvulnerabilityState();
            stateComponent.setState(invulnerabilityState);
            invulnerabilityState.doAction(entity);
        }
    }
}
