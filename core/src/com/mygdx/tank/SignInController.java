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
        try {
            accountService.signIn(email, password);
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

}
