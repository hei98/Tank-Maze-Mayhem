package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.tank.model.components.SpeedComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;
import com.mygdx.tank.model.systems.CollisionSystem;
import com.mygdx.tank.model.systems.MovementSystem;
import com.mygdx.tank.model.systems.PowerupSpawnSystem;
import com.mygdx.tank.model.systems.ShootingSystem;

import java.util.List;
import java.util.ArrayList;

public class GameModel {
    private List<Entity> entities;
    private MovementSystem movementSystem;
    private ShootingSystem shootingSystem;
    private CollisionSystem collisionSystem;
    private PowerupSpawnSystem powerupSpawnSystem;
    private Entity playerTank;
    private TiledMap map;
    private EntityFactory tankFactory = new TankFactory();
    private EntityFactory bulletFactory;

    public GameModel() {
        entities = new ArrayList<>();
        String mapPath = (Gdx.app.getType() == Application.ApplicationType.Desktop) ? "TiledMap/Map.tmx" : "TiledMap/Map2.tmx";
        map = new TmxMapLoader().load(mapPath);
        collisionSystem = new CollisionSystem(map, entities);
        movementSystem = new MovementSystem(entities, collisionSystem);
        shootingSystem = new ShootingSystem(this);
        powerupSpawnSystem = new PowerupSpawnSystem(this);
        playerTank = tankFactory.createEntity();
        entities.add(playerTank);
    }

    public void update(float deltaTime) {
        movementSystem.update(deltaTime);
        collisionSystem.update(deltaTime);
        shootingSystem.update(deltaTime);
        powerupSpawnSystem.update(deltaTime);
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
    public void handleShootAction() {
        shootingSystem.shootFromTank();
    }

    public void rotatePlayerTank(float knobPercentX, float knobPercentY) {
        SpriteDirectionComponent spriteDirectionComponent = playerTank.getComponent(SpriteDirectionComponent.class);

        float knobAngleRad = (float) Math.atan2(knobPercentY, knobPercentX);
        float knobAngleDeg = knobAngleRad * MathUtils.radiansToDegrees - 90;
        if (knobAngleDeg < 0) {
            knobAngleDeg += 360f;
        }

        if (knobPercentX != 0 && knobPercentY != 0) {
            spriteDirectionComponent.angle = knobAngleDeg;
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    public List<Entity> getEntities() {
        return entities;
    }
}

