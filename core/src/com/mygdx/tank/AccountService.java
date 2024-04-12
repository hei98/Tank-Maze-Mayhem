package com.mygdx.tank;

public interface AccountService {
    User getCurrentUser();
    String getCurrentUserId();
    String getCurrentUserEmail();
    boolean hasUser();
    void signIn(String email, String password) throws Exception;
    void signUp(String email, String password) throws Exception;
    void signOut();
    void deleteAccount() throws Exception;
}
