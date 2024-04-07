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
    private Entity playerTank;
    private TiledMap map;

    public GameModel() {
        playerTank = new Entity();
        playerTank.addComponent(new PositionComponent(0.0f, 0.0f));
        playerTank.addComponent(new SpeedComponent(200.0f));
        playerTank.addComponent(new SpriteComponent("tank_blue_2.png"));

        entities.add(playerTank);

        movementSystem = new MovementSystem(entities);
        map = new TmxMapLoader().load("MazeMayhemMapNew.tmx");

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

    public boolean isCollisionWithWalls(Entity entity, float deltaX, float deltaY) {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Rutelag 1");

        // Get the sprite component and position component of the entity
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);

        if (spriteComponent == null || positionComponent == null) {
            // If the entity doesn't have necessary components, consider it not colliding
            return false;
        }

        // Calculate the bounding box of the entity's sprite after applying the movement
        Rectangle nextBoundingBox = new Rectangle(
                positionComponent.x + deltaX,
                positionComponent.y + deltaY,
                spriteComponent.getSprite().getWidth(),
                spriteComponent.getSprite().getHeight()
        );

        // Iterate over each tile within the bounding box and check for collision with walls
        for (float y = nextBoundingBox.y; y < nextBoundingBox.y + nextBoundingBox.height; y += collisionLayer.getTileHeight()) {
            for (float x = nextBoundingBox.x; x < nextBoundingBox.x + nextBoundingBox.width; x += collisionLayer.getTileWidth()) {
                // Convert player coordinates to tile coordinates
                int tileX = (int) (x / collisionLayer.getTileWidth());
                int tileY = (int) (y / collisionLayer.getTileHeight());

                // Check if the tile at the current position is blocked
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(tileX, tileY);
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked")) {
                    // Collision detected with a wall
                    return true;
                }
            }
        }

        // No collision detected with any wall
        return false;
    }
}

