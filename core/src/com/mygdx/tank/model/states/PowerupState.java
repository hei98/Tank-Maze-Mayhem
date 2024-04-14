package com.mygdx.tank.model.states;

import com.mygdx.tank.model.Entity;

public interface PowerupState {
    void doAction(Entity tank);
    String getPowerupType();
}
