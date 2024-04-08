

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

    public void onSignUpClick() {
        try {
            if (!password.equals(confirmPassword)) {
                throw new Exception("Passwords do not match");
            }
            accountService.signUp(email, password);
            //Navigate to main menu
        } catch (Exception e) {
            // Handle sign-up failure (error?)
        }
    }
}
