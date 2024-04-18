package com.mygdx.tank.model;

import com.mygdx.tank.Player;

public interface EntityFactory {
    Entity createEntity(Player player);
}
