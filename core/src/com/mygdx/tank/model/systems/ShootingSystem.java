package com.mygdx.tank.model.systems;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.BulletFactory;
import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.bullet.ShootingCooldownComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;

public class ShootingSystem {
    private GameModel model;

    public ShootingSystem(GameModel model) {
        this.model = model;
    }

    public void shootFromTank() {
        Entity playerTank = this.model.getPlayerTank();
        if (playerTank == null) return;
        ShootingCooldownComponent shootingCooldownComponent = playerTank.getComponent(ShootingCooldownComponent.class);
        if (shootingCooldownComponent.cooldown == 0.0f) {
            PositionComponent tankPosition = playerTank.getComponent(PositionComponent.class);
            SpriteDirectionComponent spriteDirectionComponent = playerTank.getComponent(SpriteDirectionComponent.class);
            SpriteComponent tankSprite = playerTank.getComponent(SpriteComponent.class);

            if (tankPosition == null || spriteDirectionComponent == null || tankSprite == null) return;

            float knobAngle = spriteDirectionComponent.angle + 90;
            if (knobAngle > 360) knobAngle -= 360;

            float knobAngleRad = knobAngle * MathUtils.degreesToRadians;
            float directionX = MathUtils.cos(knobAngleRad);
            float directionY = MathUtils.sin(knobAngleRad);

            float bulletSpawnOffset = Math.max(tankSprite.getSprite().getWidth(), tankSprite.getSprite().getHeight()) / 2 + (Gdx.app.getType() == Application.ApplicationType.Desktop ? 30 : 60); // +5 ensures it spawns outside
            float offsetAdjustment = Gdx.app.getType() == Application.ApplicationType.Desktop ? 10 : 30;
            float bulletStartX = tankPosition.x + (knobAngle >= 270 ? offsetAdjustment : 0) + (knobAngle >= 90 && knobAngle <= 180 ? offsetAdjustment : 0) + directionX * bulletSpawnOffset;
            float bulletStartY = tankPosition.y + (knobAngle >= 90 && knobAngle <= 180 ? offsetAdjustment : 0) + directionY * bulletSpawnOffset;

            Entity bullet = BulletFactory.createBullet(bulletStartX, bulletStartY, directionX, directionY);
            model.addEntity(bullet);
            shootingCooldownComponent.cooldown = 1.5f;
        }
    }

    public void update(float deltaTime) {
        Entity playerTank = this.model.getPlayerTank();
        ShootingCooldownComponent shootingCooldownComponent = playerTank.getComponent(ShootingCooldownComponent.class);
        shootingCooldownComponent.cooldown = (shootingCooldownComponent.cooldown - deltaTime < 0) ? 0.0f : shootingCooldownComponent.cooldown - deltaTime;
    }
}
