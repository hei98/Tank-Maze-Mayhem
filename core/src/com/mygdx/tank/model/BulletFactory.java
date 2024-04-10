package com.mygdx.tank.model;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.tank.model.components.*;

public class BulletFactory {
    public static Entity createBullet(float startX, float startY, float directionX, float directionY) {
        Entity bullet = new Entity();
        bullet.addComponent(new PositionComponent(startX, startY));
        bullet.addComponent(new SpeedComponent(300, directionX, directionY));
        bullet.addComponent(new SpriteComponent("images/hvitbullet.png"));
        bullet.addComponent(new BounceComponent()); // Assuming 3 bounces before destruction
        bullet.addComponent(new TypeComponent(TypeComponent.EntityType.BULLET));
        return bullet;
    }
}
