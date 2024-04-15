package com.mygdx.tank.model;
import com.mygdx.tank.model.components.Component;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private static int nextId = 1;
    private int id;
    private String ownerId;
    private boolean markedForRemoval = false;
    private Map<Class<? extends Component>, Component> components;

    public Entity() {
        this.id = nextId++;
        this.components = new HashMap<>();
    }

    public int getId() {
        return id;
    }
    //methods for score-logic.
    public void setOwnerId(String ownerId){
        this.ownerId = ownerId;
    }
    public String getOwnerId(String ownerId){
        return ownerId;
    }

    public void markForRemoval(boolean mark) {
        this.markedForRemoval = mark;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }
    public void unmarkForRemoval() {
        this.markedForRemoval = false;
    }

    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        components.remove(componentClass);
    }
}
