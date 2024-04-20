package com.mygdx.tank.model.components;

public class TypeComponent implements Component {
    public enum EntityType {
        TANK, BULLET, POWERUP, MINE
    }

    public EntityType type;

    public TypeComponent(EntityType type) {
        this.type = type;
    }
}
