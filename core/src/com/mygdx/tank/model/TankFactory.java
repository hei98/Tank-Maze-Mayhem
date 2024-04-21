package com.mygdx.tank.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.tank.Constants;
import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.PlayerComponent;
import com.mygdx.tank.model.components.tank.ShootingCooldownComponent;
import com.mygdx.tank.model.components.tank.HealthComponent;
import com.mygdx.tank.model.components.tank.PowerupStateComponent;
import com.mygdx.tank.model.components.tank.SpriteDirectionComponent;
import com.mygdx.tank.model.states.NormalState;

public class TankFactory implements EntityFactory {


    public Entity createEntity(Player player) {
        Constants con = Constants.getInstance();
        Entity tank = new Entity();

        tank.addComponent(new SpeedComponent(300));

        switch (player.getPlayerName()) {
            case "Player1":
                tank.addComponent(new PositionComponent(0.0f, 0.0f));
                tank.addComponent(new SpriteComponent("images/tank_blue.png"));
                break;
            case "Player2": {
                Sprite tempSprite = new Sprite(new Texture("images/tank_orange.png"));
                tank.addComponent(new PositionComponent(Constants.getInstance().getSWidth() - tempSprite.getWidth(), 0.0f));
                tank.addComponent(new SpriteComponent("images/tank_orange.png"));
                break;
            }
            case "Player3": {
                Sprite tempSprite = new Sprite(new Texture("images/tank_green.png"));
                tank.addComponent(new PositionComponent(Constants.getInstance().getSWidth() - tempSprite.getWidth(), Constants.getInstance().getSHeight() - tempSprite.getHeight()));
                tank.addComponent(new SpriteComponent("images/tank_green.png"));
                break;
            }
            case "Player4": {
                Sprite tempSprite = new Sprite(new Texture("images/tank_grey.png"));
                tank.addComponent(new PositionComponent(0, Constants.getInstance().getSHeight() - tempSprite.getHeight()));
                tank.addComponent(new SpriteComponent("images/tank_grey.png"));
                break;
            }
        }

        Sprite sprite = tank.getComponent(SpriteComponent.class).getSprite();
        sprite.setSize(con.getTankWidth(), con.getTankHeight());
        sprite.setOrigin(con.getTankWidth() / 2, (sprite.getHeight() - sprite.getHeight() / 3) / 2);
        tank.addComponent(new SpriteDirectionComponent(0f));

        tank.addComponent(new HealthComponent());
        tank.addComponent(new TypeComponent(TypeComponent.EntityType.TANK));
        tank.addComponent(new ShootingCooldownComponent(1.5f));
        tank.addComponent(new PowerupStateComponent(new NormalState()));
        tank.addComponent(new PlayerComponent(player));

        return tank;
    }
}
