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
            accountService.signUp(email, password);
            accountService.signIn(email, password);
    }
}
