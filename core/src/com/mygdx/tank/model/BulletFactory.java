package com.mygdx.tank.model;

import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.bullet.BounceComponent;
import com.mygdx.tank.model.components.bullet.CollisionSide;
import com.mygdx.tank.model.components.bullet.CollisionSideComponent;

public class BulletFactory {
    public static Entity createBullet(float startX, float startY, float directionX, float directionY) {
        Entity bullet = new Entity();
        bullet.addComponent(new PositionComponent(startX, startY));
        bullet.addComponent(new SpeedComponent(1.0f, directionX * 300.0f, directionY * 300.0f));
        bullet.addComponent(new SpriteComponent("images/hvitbullet.png"));
        bullet.addComponent(new BounceComponent());
        bullet.addComponent(new CollisionSideComponent(CollisionSide.NONE));
        bullet.addComponent(new TypeComponent(TypeComponent.EntityType.BULLET));
        return bullet;
    }
}
