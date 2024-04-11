package com.mygdx.tank.model.systems;

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

            if (tankPosition == null || spriteDirectionComponent == null) return;

            float knobAngle = spriteDirectionComponent.angle + 90;
            if (knobAngle > 360) knobAngle -= 360;

            float knobAngleRad = knobAngle * MathUtils.degreesToRadians;
            float directionX = MathUtils.cos(knobAngleRad);
            float directionY = MathUtils.sin(knobAngleRad);

            Entity bullet = BulletFactory.createBullet(tankPosition.x, tankPosition.y, directionX, directionY);
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
