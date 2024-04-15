package com.mygdx.tank.model.systems;

public class PlayerScoreSystem { //Maybe a component instead?
    private int score;
    private final String playerId;

    public PlayerScoreSystem(String playerId){
        this.playerId = playerId;
        this.score = 0;
    }

    public void addPoints(int points){
        score += points;
    }

    public void subtractPoints(int points){
        score -= points;
    }

    public String getPlayerId(){
        return playerId;
    }

    public int getScore(){
        return score;
    }
    //Set score for updating score from server
    public void setScore(int score){
        this.score = score;
    }
}
