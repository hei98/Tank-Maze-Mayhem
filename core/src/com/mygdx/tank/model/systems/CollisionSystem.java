package com.mygdx.tank.model.systems;

import com.badlogic.gdx.maps.tiled.TiledMap;
import java.util.List;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.components.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import java.util.List;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.bullet.BounceComponent;
import com.mygdx.tank.model.components.bullet.CollisionSideComponent;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpeedComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.components.bullet.CollisionSide;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;
import com.mygdx.tank.model.components.tank.HealthComponent;
import com.mygdx.tank.model.components.tank.PowerupStateComponent;
import com.mygdx.tank.model.states.MinigunState;
import com.mygdx.tank.model.states.NormalState;
import com.mygdx.tank.model.states.PowerupState;
import com.mygdx.tank.model.states.ShieldState;
import com.mygdx.tank.model.states.SpeedState;

public class CollisionSystem {
    private TiledMap map;
    private List<Entity> entities;
    private GameModel model;

    public CollisionSystem(TiledMap map, List<Entity> entities, GameModel model) {
        this.map = map;
        this.entities = entities;
        this.model = model;
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            SpeedComponent speed = entity.getComponent(SpeedComponent.class);
            if (speed != null) {
                float deltaX = speed.speedX * deltaTime;
                float deltaY = speed.speedY * deltaTime;
                if (isCollisionWithWalls(entity, deltaX, deltaY)) {
                    handleWallCollision(entity, deltaX, deltaY);
                }
            }
        }
        processEntityCollisions();

        Entity playerTank = model.getPlayerTank();
        PowerupStateComponent powerupStateComponent = playerTank.getComponent(PowerupStateComponent.class);
        if (powerupStateComponent.inPowerupMode) {
            powerupStateComponent.timer = (powerupStateComponent.timer - deltaTime < 0) ? 0.0f : powerupStateComponent.timer - deltaTime;
            if (powerupStateComponent.timer == 0.0f) {
                powerupStateComponent.setState(new NormalState());
                powerupStateComponent.getState().doAction(playerTank);
            }
        }

    }
    private Rectangle getBoundingBox(Entity entity) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        SpriteComponent sprite = entity.getComponent(SpriteComponent.class);
        return new Rectangle(position.x, position.y, sprite.getSprite().getWidth(), sprite.getSprite().getHeight());
    }
    private boolean doEntitiesOverlap(Entity e1, Entity e2) {
        Rectangle r1 = getBoundingBox(e1);
        Rectangle r2 = getBoundingBox(e2);
        return r1.overlaps(r2);
    }
    private void processEntityCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);
                if (doEntitiesOverlap(e1, e2)) {
                    handleEntityCollision(e1, e2);
                }
            }
        }
    }

    private void handleEntityCollision(Entity e1, Entity e2) {
        TypeComponent type1 = e1.getComponent(TypeComponent.class);
        TypeComponent type2 = e2.getComponent(TypeComponent.class);

        if (type1.type == TypeComponent.EntityType.BULLET && type2.type == TypeComponent.EntityType.TANK) {
            markBulletForRemovalAndDamageTank(e1, e2);
            System.out.println("Entities collided: " + e1 + " with " + e2);
        } else if (type2.type == TypeComponent.EntityType.BULLET && type1.type == TypeComponent.EntityType.TANK) {
            markBulletForRemovalAndDamageTank(e2, e1);
            System.out.println("Entities collided: " + e1 + " with " + e2);
        } else if (type1.type == TypeComponent.EntityType.POWERUP && type2.type == TypeComponent.EntityType.TANK) {
            givePlayertankPowerup(e2, e1);
        } else if (type2.type == TypeComponent.EntityType.POWERUP && type1.type == TypeComponent.EntityType.TANK) {
            givePlayertankPowerup(e1, e2);
        }
    }


    private void markBulletForRemovalAndDamageTank(Entity bullet, Entity tank) {
        bullet.markForRemoval(true);

        HealthComponent health = tank.getComponent(HealthComponent.class);
        if (health != null) {
            health.takeDamage();
            if (!health.isAlive()) {
                tank.markForRemoval(true);
            }
        }
    }

    private void givePlayertankPowerup(Entity tank, Entity powerup) {
        // this code could be moved to an entirely new PowerupPickupSystem
        powerup.markForRemoval(true);

        PowerupStateComponent powerupStateComponent = tank.getComponent(PowerupStateComponent.class);
        if (powerup.getComponent(PowerUpTypeComponent.class).powerupType == PowerUpTypeComponent.PowerupType.Shield) {
            powerupStateComponent.setState( new ShieldState());
        } else if (powerup.getComponent(PowerUpTypeComponent.class).powerupType == PowerUpTypeComponent.PowerupType.Speed) {
            powerupStateComponent.setState( new SpeedState());
        } else if (powerup.getComponent(PowerUpTypeComponent.class).powerupType == PowerUpTypeComponent.PowerupType.Minigun) {
            powerupStateComponent.setState( new MinigunState());
        }

        PowerupState powerupState =  powerupStateComponent.getState();
        powerupState.doAction(tank);
    }

    public boolean isCollisionWithWalls(Entity entity, float deltaX, float deltaY) {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        TypeComponent typeComponent = entity.getComponent(TypeComponent.class);

        if (spriteComponent == null || positionComponent == null) {
            return false;
        }

        float heightModifier = (typeComponent != null && typeComponent.type == TypeComponent.EntityType.TANK) ? -spriteComponent.getSprite().getHeight() / 3 : 0;

        Rectangle nextBoundingBox = new Rectangle(
                positionComponent.x + deltaX,
                positionComponent.y + deltaY,
                spriteComponent.getSprite().getWidth(),
                spriteComponent.getSprite().getHeight() + heightModifier
        );

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Walls");
        for (int y = (int) nextBoundingBox.y; y < nextBoundingBox.y + nextBoundingBox.height; y++) {
            for (int x = (int) nextBoundingBox.x; x < nextBoundingBox.x + nextBoundingBox.width; x++) {
                int tileX = Math.floorDiv(x, (int) collisionLayer.getTileWidth());
                int tileY = Math.floorDiv(y, (int) collisionLayer.getTileHeight());
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked")) {
                    if (typeComponent != null && typeComponent.type == TypeComponent.EntityType.BULLET) {
                        // if the entity is a bullet, find out which side it hit the wall on
                        Rectangle tileBoundingBox = new Rectangle(tileX * collisionLayer.getTileWidth(), tileY * collisionLayer.getTileHeight(), collisionLayer.getTileWidth(), collisionLayer.getTileHeight());
                        CollisionSide side = getCollisionSide(nextBoundingBox, tileBoundingBox);
                        CollisionSideComponent collisionSideComponent = entity.getComponent(CollisionSideComponent.class);

                        // update the collisionSideComponent with the side the bullet collided with
                        collisionSideComponent.side = side;
                    }
                    return true; // Collision detected
                }
            }
        }
        return false; // No collision detected
    }

    public void handleWallCollision(Entity entity, float deltaX, float deltaY) {
        TypeComponent typeComponent = entity.getComponent(TypeComponent.class);
        SpeedComponent speed = entity.getComponent(SpeedComponent.class);

        if (typeComponent != null && speed != null) {
            switch (typeComponent.type) {
                case TANK:
                    speed.speedX = 0;
                    speed.speedY = 0;
                    break;

                case BULLET:
                    BounceComponent bounce = entity.getComponent(BounceComponent.class);
                    CollisionSideComponent collisionSideComponent = entity.getComponent(CollisionSideComponent.class);
                    if (bounce != null && bounce.bounces < bounce.maxBounces) {
                        // change direction of bullet depending on which side it hit the wall
                        if (collisionSideComponent.side != CollisionSide.NONE) {
                            if (collisionSideComponent.side == CollisionSide.RIGHT || collisionSideComponent.side == CollisionSide.LEFT) {
                                speed.speedX *= -1;
                            } else if (collisionSideComponent.side == CollisionSide.TOP || collisionSideComponent.side == CollisionSide.BOTTOM) {
                                speed.speedY *= -1;
                            }
                        }

                        bounce.bounces++;

                        entity.getComponent(PositionComponent.class).x -= deltaX;
                        entity.getComponent(PositionComponent.class).y -= deltaY;
                    }
                    else {
                        entity.markForRemoval(true);

                    }
                    break;
            }
        }
    }

    // method to find out which side the bullet collided with on a wall
    public CollisionSide getCollisionSide(Rectangle bulletBoundingBox, Rectangle tileBoundingBox) {
        float bulletCenterX = bulletBoundingBox.x + bulletBoundingBox.width / 2;
        float bulletCenterY = bulletBoundingBox.y + bulletBoundingBox.height / 2;
        float tileCenterX = tileBoundingBox.x + tileBoundingBox.width / 2;
        float tileCenterY = tileBoundingBox.y + tileBoundingBox.height / 2;
        float dx = bulletCenterX - tileCenterX;
        float dy = bulletCenterY - tileCenterY;
        float width = (bulletBoundingBox.width + tileBoundingBox.width) / 2;
        float height = (bulletBoundingBox.height + tileBoundingBox.height) / 2;

        float crossWidth = width * dy;
        float crossHeight = height * dx;

        if (Math.abs(dx) <= width && Math.abs(dy) <= height) {
            if (crossWidth > crossHeight) {
                if (crossWidth > -crossHeight) {
                    return CollisionSide.BOTTOM;
                } else {
                    return CollisionSide.LEFT;
                }
            } else {
                if (crossWidth > -crossHeight) {
                    return CollisionSide.RIGHT;
                } else {
                    return CollisionSide.TOP;
                }
            }
        }

        return null;
    }
}

