package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.bullet.ShootingCooldownComponent;
import com.mygdx.tank.model.components.tank.HealthComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;

public class TankFactory implements EntityFactory {
    public Entity createEntity() {
        Entity tank = new Entity();
        tank.addComponent(new PositionComponent(0.0f, 0.0f));
        tank.addComponent(new SpeedComponent(200));
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            tank.addComponent(new SpriteComponent("images/tank_blue4.png"));
        } else {
            tank.addComponent(new SpriteComponent("images/tank_blue5.png"));
        }
        tank.addComponent(new SpriteDirectionComponent(0f));
        tank.addComponent(new HealthComponent());
        tank.addComponent(new TypeComponent(TypeComponent.EntityType.TANK));
        tank.addComponent(new ShootingCooldownComponent(1.5f));
        return tank;
    }
}
