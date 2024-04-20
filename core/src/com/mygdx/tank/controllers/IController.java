package com.mygdx.tank.controllers;

import com.badlogic.gdx.Screen;
import com.mygdx.tank.IView;

public interface IController {
    void updateModelView();
    void addListeners();
    Screen getView();
}
