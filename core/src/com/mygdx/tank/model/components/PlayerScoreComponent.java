package com.mygdx.tank.model.components;


public class PlayerScoreComponent implements Component {
    public int score;

    public PlayerScoreComponent(){
        this.score = 0;
    }

    public int getScore(){
        return score;
    }

    public void addPoints (int points){
        score += points;
    }

    public void subtractPoints(int points){
        if (score - points < 0) {
            score = 0;
        } else {
            score -= points;
        }

    }
}