package com.mygdx.tank;

public interface FirebaseInterface {

    void getLeaderboardData(FirebaseDataListener listener);
    void updateLeaderboard(String userName, int score);

}
