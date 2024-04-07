package com.mygdx.tank;

import java.util.List;
import java.util.ArrayList;

public class GameModel {
    private List<Entity> entities = new ArrayList<>();
    private MovementSystem movementSystem;
    private Entity playerTank;

    public GameModel() {
        playerTank = new Entity();
        playerTank.addComponent(new PositionComponent(0.0f, 0.0f));
        playerTank.addComponent(new SpeedComponent(200.0f));
        playerTank.addComponent(new SpriteComponent("tank_blue_2.png"));

        entities.add(playerTank);

        movementSystem = new MovementSystem(entities);

    }

    public void update(float deltaTime) {
        movementSystem.update(deltaTime);
    }

    public Entity getPlayerTank() {
        return playerTank;
    }

    public void movePlayerTank(float deltaX, float deltaY) {
        SpeedComponent speed = playerTank.getComponent(SpeedComponent.class);
        if (speed != null) {
            speed.speedX = deltaX * speed.speed;
            speed.speedY = deltaY * speed.speed;
        }
    }
}
