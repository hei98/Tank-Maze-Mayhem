package com.mygdx.tank.model.systems;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpeedComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.components.PlayerComponent;

import java.util.ArrayList;
import java.util.List;

public class MovementSystem {
    private List<Entity> entities;
    private CollisionSystem collisionSystem;
    private Client client;
    private AccountService accountService;

    public MovementSystem(List<Entity> entities, CollisionSystem collisionSystem, Client client, AccountService accountService) {
        this.entities = entities;
        this.collisionSystem = collisionSystem;
        this.client = client;
        this.accountService = accountService;
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
                    TypeComponent typeComponent = entity.getComponent(TypeComponent.class);
                    if (typeComponent.type == TypeComponent.EntityType.TANK) {
                        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
                        if (playerComponent.playerName.equals(accountService.getCurrentUser().getPlayer().getPlayerName())) {
                            List<Object> list = new ArrayList<>();
                            list.add(accountService.getCurrentUser().getPlayer().getPlayerName());
                            list.add(position);
                            client.sendTCP(list);
                        }
                    }
                } else {
                    collisionSystem.handleWallCollision(entity, proposedDeltaX, proposedDeltaY);
                }
            }
        }
    }
}

