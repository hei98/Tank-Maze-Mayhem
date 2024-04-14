package com.mygdx.tank;

public interface FirebaseInterface {
    void getLeaderboardData(FirebaseDataListener listener);

    String getAvailibleGame(FirebaseDataListener listener);
}
