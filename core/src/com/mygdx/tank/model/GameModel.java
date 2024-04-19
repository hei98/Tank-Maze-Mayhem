
package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Constants;
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.Player;
import com.mygdx.tank.User;
import com.mygdx.tank.model.components.PlayerComponent;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpeedComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;
import com.mygdx.tank.model.systems.CollisionSystem;
import com.mygdx.tank.model.systems.MovementSystem;
import com.mygdx.tank.model.systems.PowerupSpawnSystem;
import com.mygdx.tank.model.systems.ShootingSystem;
import com.mygdx.tank.model.systems.PlayerScoreSystem;
import com.mygdx.tank.model.systems.*;
import com.mygdx.tank.model.components.tank.HealthComponent;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class GameModel {
    private final List<Entity> entities;
    private final PlayerScoreSystem playerScoreSystem;
    private final MovementSystem movementSystem;
    private final ShootingSystem shootingSystem;
    private final CollisionSystem collisionSystem;
    private final PowerupSpawnSystem powerupSpawnSystem;
    private final RespawnSystem respawnSystem;
    private final GrantPowerupSystem grantPowerupSystem;
    private Entity playerTank;
    private final TiledMap map;
    private final EntityFactory tankFactory;
    private final Client client;
    private final List<Player> connectedPlayers;
    private final AccountService accountService;
    private final HashMap<String, Entity> playerTanks = new HashMap<>();
    private PowerUpTypeComponent.PowerupType spawnedPowerupType;
    private boolean updatedPowerupSprite;
    private Entity spawnedPowerup;
    private final Constants con;

    public GameModel(FirebaseInterface firebaseInterface, AccountService accountService, Client client, List<Player> connectedPlayers, Scoreboard scoreboard) {
        this.connectedPlayers = connectedPlayers;
        this.accountService = accountService;
        this.client = client;
        entities = new ArrayList<>();
        tankFactory = new TankFactory();
        con = Constants.getInstance();
        User user = accountService.getCurrentUser();
        for (Player player : connectedPlayers) {
            Entity tank = tankFactory.createEntity(player);
            if (player.getPlayerName().equals(user.getPlayer().getPlayerName())) {
                playerTank = tank;
            }
            entities.add(tank);
            playerTanks.put(player.getPlayerName(), tank);
        }
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof List) {
                    List<Object> list = (List<Object>) object;
                    Object firstElement = list.get(0);
                    Object secondElement = list.get(1);
                    if (secondElement instanceof PositionComponent) {
                        // update position of other playerTanks
                        Player player = (Player) firstElement;
                        Entity playerTank = getAnotherPlayersTank(player.getPlayerName());
                        PositionComponent positionComponentFromServer = (PositionComponent) secondElement;
                        PositionComponent positionComponent = playerTank.getComponent(PositionComponent.class);
                        positionComponent.x = positionComponentFromServer.x;
                        positionComponent.y = positionComponentFromServer.y;

                    } else if (secondElement instanceof SpriteDirectionComponent) {
                        // update rotation of other playerTanks
                        Player player = (Player) firstElement;
                        Entity playerTank = getAnotherPlayersTank(player.getPlayerName());
                        SpriteDirectionComponent spriteDirectionComponentFromServer = (SpriteDirectionComponent) secondElement;
                        SpriteDirectionComponent spriteDirectionComponent = playerTank.getComponent(SpriteDirectionComponent.class);
                        spriteDirectionComponent.angle = spriteDirectionComponentFromServer.angle;
                    } else if (secondElement instanceof Float && firstElement instanceof Player) {
                        // render the bullet another playerTank shot
                        Player player = (Player) firstElement;
                        float bulletStartX = (Float) list.get(1);
                        float bulletStartY = (Float) list.get(2);
                        float directionX = (Float) list.get(3);
                        float directionY = (Float) list.get(4);
                        for (Player player1 : connectedPlayers) {
                            if (player1.getPlayerName().equals(player.getPlayerName())) {
                                Entity bullet = BulletFactory.createBullet(bulletStartX, bulletStartY, directionX, directionY, player1);
                                synchronized (entities) {
                                    entities.add(bullet);
                                }
                                break;
                            }
                        }

                    } else if (firstElement instanceof PowerUpTypeComponent.PowerupType) {
                        // render the powerup that Player1 spawned in
                        PowerUpTypeComponent.PowerupType powerUpType = (PowerUpTypeComponent.PowerupType) firstElement;
                        float positionX = (Float) list.get(1);
                        float positionY = (Float) list.get(2);
                        PowerupFactory powerupFactory = new PowerupFactory();
                        Entity powerUp = powerupFactory.createSpecificPowerup(powerUpType, positionX, positionY);
                        spawnedPowerupType = powerUpType;
                        updatedPowerupSprite = false;
                        spawnedPowerup = powerUp;
                        synchronized (entities) {
                            entities.add(powerUp);
                        }
                    }
                } else if (object instanceof String) {
                    String playerName = (String) object;
                    for (Entity entity : entities)  {
                        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
                        if (playerComponent != null) {
                            if (playerComponent.player.getPlayerName().equals(playerName)) {
                                entity.markForRemoval(true);
                            }
                        }
                    }
                }
            }
        });

        String mapPath = (Gdx.app.getType() == Application.ApplicationType.Desktop) ? "TiledMap/Map.tmx" : "TiledMap/Map2.tmx";
        map = new TmxMapLoader().load(mapPath);

        playerScoreSystem = new PlayerScoreSystem(scoreboard);
        grantPowerupSystem = new GrantPowerupSystem();
        collisionSystem = new CollisionSystem(map, entities, this, grantPowerupSystem, playerScoreSystem, connectedPlayers);
        movementSystem = new MovementSystem(entities, collisionSystem, client, accountService);
        shootingSystem = new ShootingSystem(this, accountService, client);
        powerupSpawnSystem = new PowerupSpawnSystem(this, accountService, client);
        respawnSystem = new RespawnSystem(entities, accountService, client);
    }

    public void update(float deltaTime) {
        movementSystem.update(deltaTime);
        collisionSystem.update(deltaTime, connectedPlayers);
        shootingSystem.update(deltaTime); // Ensure this is being executed
        powerupSpawnSystem.update(deltaTime);
        respawnSystem.update(deltaTime);
        removeMarkedEntities();
        if (spawnedPowerupType != null && !updatedPowerupSprite) {
            String imagePath;
            if (spawnedPowerupType == PowerUpTypeComponent.PowerupType.Shield) {
                imagePath = "images/ShieldPowerup.png";
            } else if (spawnedPowerupType == PowerUpTypeComponent.PowerupType.Minigun) {
                imagePath = "images/MachineGunPowerup.png";
            } else if (spawnedPowerupType == PowerUpTypeComponent.PowerupType.Speed) {
                imagePath = "images/SpeedPowerup.png";
            } else {
                imagePath = "images/ShieldPowerup.png";
            }
            spawnedPowerup.removeComponent(SpriteComponent.class);
            spawnedPowerup.addComponent(new SpriteComponent(imagePath));
            Sprite sprite = spawnedPowerup.getComponent(SpriteComponent.class).getSprite();
            sprite.setSize(con.getIBSize() * 0.7f, con.getIBSize() * 0.7f);
            updatedPowerupSprite = true;
        }
    }

    public void removeMarkedEntities() {
        List<Entity> toKeep = new ArrayList<>();
        synchronized (entities) {
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
                } else {
                    toKeep.add(entity);
                }
            }
            entities.clear();
            entities.addAll(toKeep);
        }
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

    public HashMap<String, Entity> getPlayerTanks() {
        return this.playerTanks;
    }

    public void playerDisconnected(String playerName) {
        client.sendTCP(playerName);
    }
}


