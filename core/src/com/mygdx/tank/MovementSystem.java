package com.mygdx.tank;

import java.util.List;

public class MovementSystem {
    private List<Entity> entities;

    public MovementSystem(List<Entity> entities) {
        this.entities = entities;
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            SpeedComponent speed = entity.getComponent(SpeedComponent.class);

            if (position != null && speed != null) {
                position.x += speed.speedX * deltaTime;
                position.y += speed.speedY * deltaTime;
            }
        }
    }

}

