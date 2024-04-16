package com.mygdx.tank;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.User;

public class AccountServiceImpl implements AccountService {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private User userInstance;

    @Override
    public User getCurrentUser() {
        if (userInstance == null) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                userInstance = new User(currentUser.getUid());
            }
        }
        return userInstance;
    }

    @Override
    public String getCurrentUserId() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }

    public String getCurrentUserEmail() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null ? currentUser.getEmail() : null;
    }

    @Override
    public boolean hasUser() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public void signIn(String email, String password) throws Exception {
        Tasks.await(firebaseAuth.signInWithEmailAndPassword(email, password));
    }

    @Override
    public void signUp(String email, String password) throws Exception {
        Tasks.await(firebaseAuth.createUserWithEmailAndPassword(email, password));
    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }

    @Override
    public void deleteAccount() throws Exception {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Tasks.await(user.delete());
        }
    }

}
