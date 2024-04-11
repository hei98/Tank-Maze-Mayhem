package com.mygdx.tank;

import com.badlogic.gdx.Gdx;

public class SignInController{
    private final AccountService accountService;
    private String email = "";
    private String password = "";


    public SignInController(AccountService accountService) {
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
        Gdx.app.log("InfoTag", "controller involved.");
        accountService.signIn(email, password);
    }

    public void onSignUpClick() {
        //Navigation to sign-up
    }
}
