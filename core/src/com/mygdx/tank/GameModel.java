package com.mygdx.tank;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;
import java.util.ArrayList;

public class GameModel {
    private List<Entity> entities = new ArrayList<>();
    private MovementSystem movementSystem;
    private ShootingSystem shootingSystem;
    private CollisionSystem collisionSystem;
    private Entity playerTank;
    private TiledMap map;

    public GameModel() {
        map = new TmxMapLoader().load("MazeMayhemMapNew.tmx");
        collisionSystem = new CollisionSystem(map, entities);

        movementSystem = new MovementSystem(entities, collisionSystem);
        shootingSystem = new ShootingSystem(this);
        playerTank = createPlayerTank();
    }

    private Entity createPlayerTank() {
        Entity tank = new Entity();
        tank.addComponent(new PositionComponent(0.0f, 0.0f));
        tank.addComponent(new SpeedComponent(200.0f));
        tank.addComponent(new SpriteComponent("tank_blue4.png"));
        tank.addComponent(new SpriteDirectionComponent(0f));
        tank.addComponent(new TypeComponent(TypeComponent.EntityType.TANK));
        entities.add(tank);
        return tank;

    }
    public void update(float deltaTime) {
        movementSystem.update(deltaTime);
        collisionSystem.update(deltaTime);
        //shootingSystem.update(deltaTime);
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

    public void rotatePlayerTank(boolean lookRight, boolean lookLeft, boolean lookUp, boolean lookDown) {
        SpriteDirectionComponent spriteDirectionComponent = playerTank.getComponent(SpriteDirectionComponent.class);
        float angle = 0;
        if (lookRight && lookUp) {
            angle = 315;
        } else if (lookRight && lookDown) {
            angle = 225;
        } else if (lookLeft && lookUp) {
            angle = 45;
        } else if (lookLeft && lookDown) {
            angle = 135;
        } else if (lookRight) {
            angle = 270;
        } else if (lookLeft) {
            angle = 90;
        } else if (lookUp) {
            angle = 0;
        } else if (lookDown) {
            angle = 180;
        }

        spriteDirectionComponent.angle = angle;
    }

    public void shootFromTank() {
        PositionComponent tankPosition = playerTank.getComponent(PositionComponent.class);
        shootingSystem.shoot(tankPosition.x, tankPosition.y, 1.0f, 0.0f); // Example direction
    }
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    public List<Entity> getEntities() {
        return entities;
    }
}

