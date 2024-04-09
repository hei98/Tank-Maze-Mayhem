package com.mygdx.tank;
import com.badlogic.gdx.maps.tiled.TiledMap;
import java.util.List;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

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

        if (spriteComponent == null || positionComponent == null) {
            return false;
        }

        Rectangle nextBoundingBox = new Rectangle(
                positionComponent.x + deltaX,
                positionComponent.y + deltaY,
                spriteComponent.getSprite().getWidth(),
                spriteComponent.getSprite().getHeight()
        );

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Rutelag 1");
        for (int y = (int) nextBoundingBox.y; y < nextBoundingBox.y + nextBoundingBox.height; y++) {
            for (int x = (int) nextBoundingBox.x; x < nextBoundingBox.x + nextBoundingBox.width; x++) {
                int tileX = Math.floorDiv(x, (int) collisionLayer.getTileWidth());
                int tileY = Math.floorDiv(y, (int) collisionLayer.getTileHeight());
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked")) {
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
                    if (bounce != null && bounce.bounces < bounce.maxBounces) {

                        speed.speedX *= -1;
                        speed.speedY *= -1;
                        bounce.bounces++;

                        entity.getComponent(PositionComponent.class).x -= deltaX;
                        entity.getComponent(PositionComponent.class).y -= deltaY;
                    }
                    else {

                    }
                    break;
            }
        }
    }
}
