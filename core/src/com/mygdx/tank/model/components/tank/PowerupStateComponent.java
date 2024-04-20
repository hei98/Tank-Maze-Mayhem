package com.mygdx.tank.model.components.tank;

import com.mygdx.tank.model.components.Component;
import com.mygdx.tank.model.states.PowerupState;

public class PowerupStateComponent implements Component {
    private PowerupState state;
    private String prevState;
    public boolean inPowerupMode;

    public float timer;

    public PowerupStateComponent(PowerupState state) {
        this.state = state;
        inPowerupMode = false;
        timer = 8f;
        prevState = state.getPowerupType();
    }

    public PowerupState getState() {
        return this.state;
    }

    public void setState(PowerupState state) {
        this.state = state;
    }
    public void setPrevState(String prevState) {
        this.prevState = prevState;
    }
    public String getPrevState() {
        return this.prevState;
    }
}
