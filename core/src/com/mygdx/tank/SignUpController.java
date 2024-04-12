package com.mygdx.tank;

public class SignUpController {
    private final AccountService accountService;
    private String email = "";
    private String password = "";
    private String confirmPassword = "";

    public SignUpController(AccountService accountService) {
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
}
