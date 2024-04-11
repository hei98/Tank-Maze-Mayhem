package com.mygdx.tank.model.systems;
import com.badlogic.gdx.maps.tiled.TiledMap;
import java.util.List;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.BounceComponent;
import com.mygdx.tank.model.components.CollisionSideComponent;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpeedComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.components.CollisionSide;

public class CollisionSystem {
    private TiledMap map;
    private List<Entity> entities;

    public CollisionSystem(TiledMap map, List<Entity> entities) {
        this.map = map;
        this.entities = entities;
    }

    public void update(float deltaTime) {
        for (Entity entity : entities) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            SpeedComponent speed = entity.getComponent(SpeedComponent.class);
            TypeComponent type = entity.getComponent(TypeComponent.class);

            if (position != null && speed != null && type != null) {
                float deltaX = speed.speedX * deltaTime;
                float deltaY = speed.speedY * deltaTime;

                if (isCollisionWithWalls(entity, deltaX, deltaY)) {
                    handleCollision(entity, deltaX, deltaY);
                } else {
                    // If no collision, update position
                    position.x += deltaX;
                    position.y += deltaY;
                }
            }
        }
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

    public void handleCollision(Entity entity, float deltaX, float deltaY) {
        // Determine the type of the entity
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
