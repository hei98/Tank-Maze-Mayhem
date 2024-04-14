package com.mygdx.tank.model.components.bullet;

import com.mygdx.tank.model.components.Component;
import com.mygdx.tank.model.components.bullet.CollisionSide;

public class CollisionSideComponent implements Component {
    public CollisionSide side;

    public CollisionSideComponent(CollisionSide side) {
        this.side = side;
    }
}
