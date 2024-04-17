package com.mygdx.tank.model.components.bullet;

import com.mygdx.tank.model.components.Component;

public class CollisionSideComponent implements Component {
    public CollisionSide side;

    public CollisionSideComponent(CollisionSide side) {
        this.side = side;
    }
}
