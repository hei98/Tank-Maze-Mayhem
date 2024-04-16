package com.mygdx.tank.model.components.tank;

import com.mygdx.tank.model.components.Component;

public class PlayerComponent implements Component {

    public String playerName;

    public PlayerComponent(String playerName) {
        this.playerName = playerName;
    }
}
