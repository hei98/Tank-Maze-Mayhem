package com.mygdx.tank.controllers;

import com.badlogic.gdx.Screen;

public interface IController {
    void updateModelView();
    void addListeners();
    Screen getView();
}
