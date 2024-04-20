package com.mygdx.tank.model;

import com.mygdx.tank.Constants;
import com.mygdx.tank.Player;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.TypeComponent;

public class MineFactory implements EntityFactory{
    @Override
    public Entity createEntity(Player player) {
        Constants con = Constants.getInstance();
        Entity mine = new Entity();
        mine.addComponent(new TypeComponent(TypeComponent.EntityType.MINE));
        float x = con.getSWidth() * 0.4f;
        float y = con.getSHeight() * 0.3f;
        mine.addComponent(new PositionComponent(x,y));
        mine.addComponent(new SpriteComponent("images/Landmine.png"));
        mine.getComponent(SpriteComponent.class).getSprite().setSize(con.getIBSize() * 0.7f, con.getIBSize() * 0.7f);
        return mine;
    }
}
