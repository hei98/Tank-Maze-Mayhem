package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.tank.ShootingCooldownComponent;
import com.mygdx.tank.model.components.tank.HealthComponent;
import com.mygdx.tank.model.components.tank.PowerupStateComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;
import com.mygdx.tank.model.states.NormalState;

public class TankFactory implements EntityFactory {


    public Entity createEntity() {
        Entity tank = new Entity();
        tank.addComponent(new PositionComponent(0.0f, 0.0f));
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            tank.addComponent(new SpriteComponent("images/tank_blue4.png"));
            tank.addComponent(new SpeedComponent(140));
        } else {
            tank.addComponent(new SpriteComponent("images/tank_blue5.png"));
            tank.addComponent(new SpeedComponent(300));
        }
        Sprite sprite = tank.getComponent(SpriteComponent.class).getSprite();
        sprite.setOrigin(sprite.getWidth() / 2, (sprite.getHeight() - sprite.getHeight() / 3) / 2);
        tank.addComponent(new SpriteDirectionComponent(0f));
        tank.addComponent(new HealthComponent());
        tank.addComponent(new TypeComponent(TypeComponent.EntityType.TANK));
        tank.addComponent(new ShootingCooldownComponent(1.5f));
        tank.addComponent(new PowerupStateComponent(new NormalState()));
        return tank;
    }
}
