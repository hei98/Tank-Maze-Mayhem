package com.mygdx.tank;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.AccountServiceImpl;


public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		AccountService accountService = new AccountServiceImpl();
		initialize(new TankMazeMayhem(new FirebaseController(), accountService), config);
	}
}
