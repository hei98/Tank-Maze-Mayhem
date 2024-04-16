package com.mygdx.tank;

import java.util.List;

public class Player {

    private int orderOfPartyJoin;

    public Player(int orderOfPartyJoin) {
        this.orderOfPartyJoin = orderOfPartyJoin;
    }

    public int getOrderOfPartyJoin() {
        return orderOfPartyJoin;
    }

    public void setOrderOfPartyJoin(int orderOfPartyJoin) {
        this.orderOfPartyJoin = orderOfPartyJoin;
    }
}
