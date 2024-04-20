package com.mygdx.tank.model.MenuScreens;

import com.mygdx.tank.IModel;

public class MenuModel implements IModel {
    private boolean isLoggedIn;
    private String userEmail;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void update(boolean loggedIn, String userEmail) {
        this.isLoggedIn = loggedIn;
        this.userEmail = userEmail;
    }

    public String getAccountLabel() {
        return isLoggedIn ? "User email: " + userEmail : "Not logged in";
    }
}
