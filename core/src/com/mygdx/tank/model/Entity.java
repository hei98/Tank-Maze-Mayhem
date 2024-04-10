package com.mygdx.tank.model;
import com.mygdx.tank.model.components.Component;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private static int nextId = 1;
    private int id;
    private Map<Class<? extends Component>, Component> components;

    public Entity() {
        this.id = nextId++;
        this.components = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }
}
