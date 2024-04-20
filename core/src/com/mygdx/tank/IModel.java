package com.mygdx.tank;

public interface IModel {
    boolean isLoggedIn();
    String getAccountLabel();
    void update(boolean loggedIn, String userEmail);
}
