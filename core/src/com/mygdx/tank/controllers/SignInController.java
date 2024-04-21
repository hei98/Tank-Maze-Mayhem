package com.mygdx.tank.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.MainMenuView;
import com.mygdx.tank.Views.SettingsView;
import com.mygdx.tank.Views.SignInView;
import com.mygdx.tank.Views.SignUpView;
import com.mygdx.tank.model.MenuModel;

import java.util.ArrayList;

public class SignInController implements IController{
    private final MenuModel model;
    private final SignInView view;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private String email = "";
    private String password = "";


    public SignInController(MenuModel model, SignInView view, TankMazeMayhem game, AccountService accountService) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.accountService = accountService;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
        Gdx.app.log("email updated", email);
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void onSignInClick() throws Exception {
        try {
            accountService.signIn(email, password);
            accountService.resetInstance();
        }
        catch(Exception e){
            String error = e.getMessage();
            if (error.contains("auth credential")){
                throw new Exception("Wrong email/password");
            }
            else if(error.contains("email")){
                throw new Exception("Invalid Email");
            }
            else if(error.contains("user")){
                throw new Exception("User does not exist");
            }
            else if(error.contains("String")){
                throw new Exception("You didn't write anything, silly");
            }
            else{
                throw new Exception(e.getMessage());
            }

        }
    }

    @Override
    public void updateModelView() {
        view.updateView(model);
    }

    @Override
    public void addListeners() {
        view.getSignInButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ArrayList<String> emailAndPass = view.getEmailPass();
                updateEmail(emailAndPass.get(0));
                updatePassword(emailAndPass.get(1));
                updateModelView();
                model.updateErrorLabel("");
                try {
                    onSignInClick();
                    game.setShowTutorial(false);
                    ApplicationController.getInstance(game, accountService).switchToMainMenu();
                } catch (Exception e) {
                    // Update the model errorlabel with the error
                    model.updateErrorLabel("Login failed: " + e.getLocalizedMessage());
                    updateModelView();
                }

            }
        });
        view.getSignUpButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("SignUp", "Sign Up button clicked");
                ApplicationController.getInstance(game, accountService).switchToSignUp();
            }
        });
        view.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToMainMenu();
            }
        });
    }

    @Override
    public Screen getView() {
        return view;
    }
}
