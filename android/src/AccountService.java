
public interface AccountService {
    User getCurrentUser();
    String getCurrentUserId();
    boolean hasUser();
    void signIn(String email, String password) throws Exception;
    void signUp(String email, String password) throws Exception;
    void signOut();
    void deleteAccount() throws Exception;
}
