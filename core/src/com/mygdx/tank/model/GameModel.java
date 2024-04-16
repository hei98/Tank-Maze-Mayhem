
package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.Player;
import com.mygdx.tank.User;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpeedComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.components.PlayerComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;
import com.mygdx.tank.model.systems.CollisionSystem;
import com.mygdx.tank.model.systems.MovementSystem;
import com.mygdx.tank.model.systems.PowerupSpawnSystem;
import com.mygdx.tank.model.systems.ShootingSystem;
import com.mygdx.tank.model.systems.*;
import com.mygdx.tank.model.components.tank.HealthComponent;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class GameModel {
    private List<Entity> entities;
    private MovementSystem movementSystem;
    private ShootingSystem shootingSystem;
    private CollisionSystem collisionSystem;
    private PowerupSpawnSystem powerupSpawnSystem;
    private RespawnSystem respawnSystem;
    private GrantPowerupSystem grantPowerupSystem;
    private Entity playerTank;
    private TiledMap map;
    private EntityFactory tankFactory;
    private Client client;
    private List<Player> connectedPlayers;
    private AccountService accountService;
    private HashMap<String, Entity> playerTanks = new HashMap<>();

    public GameModel(FirebaseInterface firebaseInterface, AccountService accountService, Client client, List<Player> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
        this.accountService = accountService;
        this.client = client;
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof List) {
                    List<Object> list = (List<Object>) object;
                    Object firstElement = list.get(0);
                    Object secondElement = list.get(1);
                    if (secondElement instanceof PositionComponent) {
                        Player player = (Player) firstElement;
                        Entity playerTank = getAnotherPlayersTank(player.getPlayerName());
                        PositionComponent positionComponentFromServer = (PositionComponent) secondElement;
                        PositionComponent positionComponent = playerTank.getComponent(PositionComponent.class);
                        positionComponent.x = positionComponentFromServer.x;
                        positionComponent.y = positionComponentFromServer.y;

                    } else if (secondElement instanceof SpriteDirectionComponent) {
                        Player player = (Player) firstElement;
                        Entity playerTank = getAnotherPlayersTank(player.getPlayerName());
                        SpriteDirectionComponent spriteDirectionComponentFromServer = (SpriteDirectionComponent) secondElement;
                        SpriteDirectionComponent spriteDirectionComponent = playerTank.getComponent(SpriteDirectionComponent.class);
                        spriteDirectionComponent.angle = spriteDirectionComponentFromServer.angle;
                    } else if (secondElement instanceof Float && firstElement instanceof Player) {
                        Player player = (Player) firstElement;
                        float bulletStartX = (Float) list.get(1);
                        float bulletStartY = (Float) list.get(2);
                        float directionX = (Float) list.get(3);
                        float directionY = (Float) list.get(4);
                        Entity bullet = BulletFactory.createBullet(bulletStartX, bulletStartY, directionX, directionY, player);
                        entities.add(bullet);
                    }
                }
            }
        });

        entities = new ArrayList<>();
        tankFactory = new TankFactory();
        String mapPath = (Gdx.app.getType() == Application.ApplicationType.Desktop) ? "TiledMap/Map.tmx" : "TiledMap/Map2.tmx";
        map = new TmxMapLoader().load(mapPath);
        User user = accountService.getCurrentUser();

        grantPowerupSystem = new GrantPowerupSystem();
        collisionSystem = new CollisionSystem(map, entities, this, grantPowerupSystem);
        movementSystem = new MovementSystem(entities, collisionSystem, client, accountService);
        shootingSystem = new ShootingSystem(this, accountService, client);
        powerupSpawnSystem = new PowerupSpawnSystem(this, accountService);
        respawnSystem = new RespawnSystem(entities);

        for (Player player : connectedPlayers) {
            Entity tank = tankFactory.createEntity(player);
            if (player.getPlayerName().equals(user.getPlayer().getPlayerName())) {
                playerTank = tank;
            }
            entities.add(tank);
            playerTanks.put(player.getPlayerName(), tank);
        }
    }

    public void update(float deltaTime) {
        movementSystem.update(deltaTime);
        collisionSystem.update(deltaTime);
        shootingSystem.update(deltaTime);
        powerupSpawnSystem.update(deltaTime);
        respawnSystem.update(deltaTime);
        removeMarkedEntities();
    }

    public void removeMarkedEntities() {
        List<Entity> toKeep = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.isMarkedForRemoval()) {
                TypeComponent typeComponent = entity.getComponent(TypeComponent.class);
                if (typeComponent != null) {
                    switch (typeComponent.type) {
                        case TANK:
                            HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
                            if (healthComponent != null && !healthComponent.isAlive()) {
                                respawnSystem.respawn(entity);
                                continue;
                            }
                            break;
                        case POWERUP:
                            powerupSpawnSystem.powerupRemoved();
                            break;
                    }
                }
                System.out.println("Removing entity: " + entity);
            } else {
                toKeep.add(entity);
            }
        }
        entities.clear();
        entities.addAll(toKeep);
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
            List<Object> list = new ArrayList<>();
            list.add(accountService.getCurrentUser().getPlayer());
            list.add(spriteDirectionComponent);
            client.sendTCP(list);
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    public List<Entity> getEntities() {
        return entities;
    }

    public Entity getAnotherPlayersTank(String playerName) {
        return playerTanks.get(playerName);
    }
}


