package com.mygdx.tank;

import android.content.res.AssetManager;
import com.badlogic.gdx.backends.android.AndroidFileHandle;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountServiceImpl implements AccountService {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private User userInstance;

    @Override
    public User getCurrentUser() {
        if (userInstance == null) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                userInstance = new User(currentUser.getEmail());
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

    @Override
    public void registerAndroidServer(Server server) {
        server.getKryo().register(AndroidFileHandle.class);
        server.getKryo().register(AssetManager.class);
    }
    @Override
    public void registerAndroidClient(Client client) {
        client.getKryo().register(AndroidFileHandle.class);
        client.getKryo().register(AssetManager.class);
    }

}
