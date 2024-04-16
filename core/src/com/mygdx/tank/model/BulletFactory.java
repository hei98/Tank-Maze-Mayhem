package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.bullet.BounceComponent;
import com.mygdx.tank.model.components.bullet.CollisionSide;
import com.mygdx.tank.model.components.bullet.CollisionSideComponent;

public class BulletFactory {
    public static Entity createBullet(float startX, float startY, float directionX, float directionY, String playerName) {
        Entity bullet = new Entity();
        bullet.addComponent(new PositionComponent(startX, startY));
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            bullet.addComponent(new SpeedComponent(1.0f, directionX * 300.0f, directionY * 300.0f));
        } else {
            bullet.addComponent(new SpeedComponent(1.0f, directionX * 400.0f, directionY * 400.0f));
        }

        bullet.addComponent(new SpriteComponent("images/nybullet1.png"));
        bullet.addComponent(new BounceComponent());
        bullet.addComponent(new CollisionSideComponent(CollisionSide.NONE));
        bullet.addComponent(new TypeComponent(TypeComponent.EntityType.BULLET));
        bullet.addComponent(new PlayerComponent(playerName));
        return bullet;
    }
}
