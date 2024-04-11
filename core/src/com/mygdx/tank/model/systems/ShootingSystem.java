package com.mygdx.tank.model.systems;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.BulletFactory;
import com.mygdx.tank.model.components.*;

public class ShootingSystem {
    private GameModel model;

    public ShootingSystem(GameModel model) {
        this.model = model;
    }

    public void shootFromTank() {
        Entity playerTank = this.model.getPlayerTank();
        if (playerTank == null) return;

        PositionComponent tankPosition = playerTank.getComponent(PositionComponent.class);
        SpriteDirectionComponent spriteDirectionComponent = playerTank.getComponent(SpriteDirectionComponent.class);
        SpriteComponent tankSprite = playerTank.getComponent(SpriteComponent.class);

        if (tankPosition == null || spriteDirectionComponent == null || tankSprite == null) return;

        float knobAngle = spriteDirectionComponent.angle + 90;
        if (knobAngle > 360) knobAngle -= 360;

        float knobAngleRad = knobAngle * MathUtils.degreesToRadians;
        float directionX = MathUtils.cos(knobAngleRad);
        float directionY = MathUtils.sin(knobAngleRad);

        // Calculate the offset based on the dimensions of the tank sprite and the direction of fire
        float bulletSpawnOffset = Math.max(tankSprite.getSprite().getWidth(), tankSprite.getSprite().getHeight()) / 2 + 30; // +5 ensures it spawns outside
        float bulletStartX = tankPosition.x + directionX * bulletSpawnOffset;
        float bulletStartY = tankPosition.y + directionY * bulletSpawnOffset;

        Entity bullet = BulletFactory.createBullet(bulletStartX, bulletStartY, directionX, directionY);
        model.addEntity(bullet);
    }
}