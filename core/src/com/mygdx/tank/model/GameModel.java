package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpeedComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.SpriteDirectionComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.systems.CollisionSystem;
import com.mygdx.tank.model.systems.MovementSystem;
import com.mygdx.tank.model.systems.ShootingSystem;

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
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            map = new TmxMapLoader().load("TiledMap/Map.tmx");
        } else {
            map = new TmxMapLoader().load("TiledMap/Map2.tmx");
        }

        collisionSystem = new CollisionSystem(map, entities);

        movementSystem = new MovementSystem(entities, collisionSystem);
        shootingSystem = new ShootingSystem(this);
        playerTank = createPlayerTank();
    }

    private Entity createPlayerTank() {
        Entity tank = new Entity();
        tank.addComponent(new PositionComponent(0.0f, 0.0f));
        tank.addComponent(new SpeedComponent(200.0f));
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            tank.addComponent(new SpriteComponent("images/tank_blue4.png"));
        } else {
            tank.addComponent(new SpriteComponent("images/tank_blue5.png"));
        }

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

    public void rotatePlayerTank(float knobPercentageX, float knobPercentageY) {
        SpriteDirectionComponent spriteDirectionComponent = playerTank.getComponent(SpriteDirectionComponent.class);

        float knobAngleRad = (float) Math.atan2(knobPercentageY, knobPercentageX);
        float knobAngleDeg = knobAngleRad * MathUtils.radiansToDegrees - 90;
        if (knobAngleDeg < 0) {
            knobAngleDeg += 360f;
        }

        if (knobPercentageX != 0 && knobPercentageY != 0) {
            spriteDirectionComponent.angle = knobAngleDeg;
        }
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

