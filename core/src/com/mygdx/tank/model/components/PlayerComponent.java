package com.mygdx.tank.model.components;

import com.mygdx.tank.Player;
import com.mygdx.tank.model.components.Component;

public class PlayerComponent implements Component {

    public Player player;

    public PlayerComponent(Player player) {
        this.player = player;
    }
}
