

public class SignInController{
    private final AccountService accountService;
    private String email = "";
    private String password = "";


    public SignInController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void onSignInClick() {
        try {
            accountService.signIn(email, password);
            //Navigate to main menu
        } catch (Exception e) {
            // Handle sign-in failure (error message?)
        }
    }

    public void onSignUpClick() {
        //Navigation to sign-up
    }
}
