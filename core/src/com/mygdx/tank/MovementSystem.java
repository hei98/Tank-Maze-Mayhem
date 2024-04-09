package com.mygdx.tank;

import java.util.List;

public class MovementSystem {
    private List<Entity> entities;
    private CollisionSystem collisionSystem;

    public MovementSystem(List<Entity> entities, CollisionSystem collisionSystem) {
        this.entities = entities;
        this.collisionSystem = collisionSystem;
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            SpeedComponent speed = entity.getComponent(SpeedComponent.class);

            if (position != null && speed != null) {
                float proposedDeltaX = speed.speedX * deltaTime;
                float proposedDeltaY = speed.speedY * deltaTime;

                if (!collisionSystem.isCollisionWithWalls(entity, proposedDeltaX, proposedDeltaY)) {
                    // No collision apply movement
                    position.x += proposedDeltaX;
                    position.y += proposedDeltaY;
                } else {
                    collisionSystem.handleCollision(entity, proposedDeltaX, proposedDeltaY);
                }
            }
        }
    }
}

