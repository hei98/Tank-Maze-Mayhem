package com.mygdx.tank;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.tank.model.User;

public interface AccountService {
    User getCurrentUser();
    String getCurrentUserId();
    String getCurrentUserEmail();
    boolean hasUser();
    void signIn(String email, String password) throws Exception;
    void signUp(String email, String password) throws Exception;
    void signOut();
    void deleteAccount() throws Exception;
    void registerAndroidServer(Server server);
    void registerAndroidClient(Client client);
    void resetInstance();
}
