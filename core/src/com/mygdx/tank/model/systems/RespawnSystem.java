package com.mygdx.tank.model.systems;

import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.tank.HealthComponent;
import java.util.List;
import com.mygdx.tank.model.components.PositionComponent;

public class RespawnSystem {
    private List<Entity> entities;

    public RespawnSystem(List<Entity> entities) {
        this.entities = entities;
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
        if (position != null) {
            position.resetPosition();
        }

        HealthComponent health = entity.getComponent(HealthComponent.class);
        if (health != null) {
            health.resetHealth();
        }
    }
}
