package com.mygdx.tank;

import java.util.List;

public class MovementSystem {
    private List<Entity> entities;
    private GameModel model; // Add reference to GameModel to access the collision detection method

    public MovementSystem(List<Entity> entities, GameModel model) {
        this.entities = entities;
        this.model = model;
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            SpeedComponent speed = entity.getComponent(SpeedComponent.class);

            if (position != null && speed != null) {
                // Predict the next position based on current speed
                float nextX = position.x + speed.speedX * deltaTime;
                float nextY = position.y + speed.speedY * deltaTime;

                // Check if moving to the next position would cause a collision
                if (!model.isCollisionWithWalls(entity, speed.speedX * deltaTime, speed.speedY * deltaTime)) {
                    // If no collision, update the position
                    position.x = nextX;
                    position.y = nextY;
                } else {
                    // Handle collision (e.g., stop movement, adjust speed, etc.)
                    speed.speedX = 0;
                    speed.speedY = 0;
                    // Optionally, adjust position or speed based on the type of collision
                }
            }
        }
    }
}
