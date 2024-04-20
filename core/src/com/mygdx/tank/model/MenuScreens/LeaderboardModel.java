package com.mygdx.tank.model.MenuScreens;

import com.mygdx.tank.IModel;

public class LeaderboardModel implements IModel {
    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public String getAccountLabel() {
        return null;
    }

    @Override
    public void setLoggedIn(boolean loggedIn, String userEmail) {

    }
}
