package com.mygdx.tank;

public interface FirebaseInterface {
    void setValue(String reference, int value, String user);
    void getValue(String reference, final FirebaseDataListener listener);
    void getLeaderboardData(FirebaseDataListener listener);
}
