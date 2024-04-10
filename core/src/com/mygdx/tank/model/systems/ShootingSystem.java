package com.mygdx.tank.model.systems;

import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.GameModel;
import com.mygdx.tank.model.components.BounceComponent;
import com.mygdx.tank.model.components.CollisionSide;
import com.mygdx.tank.model.components.CollisionSideComponent;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpeedComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.TypeComponent;

public class ShootingSystem {
    private GameModel model;

    public ShootingSystem(GameModel model) {
        this.model = model;
    }

    public void shoot(float startX, float startY, float directionX, float directionY) {
        System.out.println("Shooting bullet");
        Entity bullet = new Entity();
        bullet.addComponent(new PositionComponent(startX, startY));
        bullet.addComponent(new SpeedComponent(1.0f, directionX * 300.0f, directionY * 300.0f));
        bullet.addComponent(new SpriteComponent("images/hvitbullet.png"));
        bullet.addComponent(new BounceComponent());
        bullet.addComponent(new CollisionSideComponent(CollisionSide.NONE));
        bullet.addComponent(new TypeComponent(TypeComponent.EntityType.BULLET));
        model.addEntity(bullet);
    }
}
