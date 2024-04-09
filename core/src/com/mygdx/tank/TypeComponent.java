package com.mygdx.tank;

public class TypeComponent implements Component {
    public enum EntityType {
        TANK, BULLET
    }

    public EntityType type;

    public TypeComponent(EntityType type) {
        this.type = type;
    }
}
