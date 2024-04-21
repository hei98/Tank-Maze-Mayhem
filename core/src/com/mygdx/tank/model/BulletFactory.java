package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.mygdx.tank.Constants;
import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.bullet.BounceComponent;
import com.mygdx.tank.model.components.bullet.CollisionSide;
import com.mygdx.tank.model.components.bullet.CollisionSideComponent;

public class BulletFactory {
    public static Entity createBullet(float startX, float startY, float directionX, float directionY, Player player) {
        Constants con = Constants.getInstance();
        Entity bullet = new Entity();
        bullet.addComponent(new PositionComponent(startX, startY));
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            bullet.addComponent(new SpeedComponent(1.0f, directionX * 300.0f, directionY * 300.0f));
        } else {
            bullet.addComponent(new SpeedComponent(1.0f, directionX * 400.0f, directionY * 400.0f));
        }

        SpriteComponent spriteComponent = new SpriteComponent("images/nybullet1.png");
        bullet.addComponent(spriteComponent);
        spriteComponent.getSprite().setSize(con.getSHeight() * 0.015f, con.getSHeight() * 0.015f);
        bullet.addComponent(new BounceComponent());
        bullet.addComponent(new CollisionSideComponent(CollisionSide.NONE));
        bullet.addComponent(new TypeComponent(TypeComponent.EntityType.BULLET));
        bullet.addComponent(new PlayerComponent(player));
        return bullet;
    }
}
