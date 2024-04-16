package com.mygdx.tank;

public interface ScoreObserver {
    void scoreUpdated(String playerId, int newScore);
}
