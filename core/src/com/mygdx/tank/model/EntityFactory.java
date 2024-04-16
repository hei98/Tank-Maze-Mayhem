package com.mygdx.tank.model;

import com.mygdx.tank.AccountService;
import com.mygdx.tank.model.Entity;

public interface EntityFactory {
    Entity createEntity(AccountService accountService);
}
