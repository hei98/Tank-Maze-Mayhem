package com.mygdx.tank.model;

import com.mygdx.tank.User;
import com.mygdx.tank.model.Entity;

public interface EntityFactory {
    Entity createEntity(int player);
}
