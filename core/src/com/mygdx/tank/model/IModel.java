package com.mygdx.tank.model;

public interface IModel {
    boolean isLoggedIn();
    String getAccountLabel();
    void update(boolean loggedIn, String userEmail);
}
