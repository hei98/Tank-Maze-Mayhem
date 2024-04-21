package com.mygdx.tank.controllers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.TankMazeMayhem;
import com.mygdx.tank.Views.SignUpView;
import com.mygdx.tank.model.MenuModel;

import java.util.ArrayList;

public class SignUpController implements IController {
    private final MenuModel model;
    private final SignUpView view;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private String email = "";
    private String password = "";
    private String confirmPassword = "";

    public SignUpController(MenuModel model, SignUpView view, TankMazeMayhem game, AccountService accountService) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.accountService = accountService;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateConfirmPassword(String newConfirmPassword) {
        this.confirmPassword = newConfirmPassword;
    }

    public void onSignUpClick() throws Exception {
        try {
            if (!password.equals(confirmPassword)){
                throw new Exception("passwords doesn't match");
            }
            accountService.signUp(email, password);
            accountService.signIn(email, password);
        }
        catch(Exception e){
            String error = e.getMessage();
            if (error.contains("String")){
                throw new Exception("Empty fields");
            }
            if (error.contains("formatted")){
                throw new Exception("Email badly formatted");
            }
            if (error.contains("email")){
                throw new Exception("Email already in use. Forgotten password? oh well, make a new user.");
            }
            if (error.contains("password is invalid")){
                throw new Exception("Password has to be at least 6 characters");
            }
            if (error.contains("match")){
                throw new Exception("Passwords doesn't match");
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
        // Add listeners to buttons
        view.getSignUpButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    ArrayList<String> emailPass = view.getEmailPass();
                    updateEmail(emailPass.get(0));
                    updatePassword(emailPass.get(1));
                    updateConfirmPassword(emailPass.get(2));
                    onSignUpClick();
                    model.updateErrorLabel("");
//                    updateModelView();
                    ApplicationController.getInstance(game, accountService).switchToMainMenu();
                } catch (Exception e) {
                    model.updateErrorLabel("Sign up failed: " + e.getLocalizedMessage());
                    updateModelView();
                }
            }
        });

        view.getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ApplicationController.getInstance(game, accountService).switchToSignIn();
            }
        });
    }

    @Override
    public Screen getView() {
        return view;
    }
}
