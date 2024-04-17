package com.mygdx.tank.model.systems;

import com.badlogic.gdx.Gdx;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.ScoreObserver;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.PlayerComponent;
import com.mygdx.tank.model.components.PlayerScoreComponent;

import java.util.ArrayList;
import java.util.List;

public class PlayerScoreSystem {
    private AccountService accountService;
    private List<ScoreObserver> observers = new ArrayList<>();

    public PlayerScoreSystem(AccountService accountService) {
        this.accountService = accountService;
    }
    public void addObserver(ScoreObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ScoreObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String playerId, int newScore) {
        for (ScoreObserver observer : observers) {
            observer.scoreUpdated(playerId, newScore);
        }
    }


    public void updateScore(Entity playerBullet, Entity playerTank) {
        PlayerComponent tankPlayer = playerTank.getComponent(PlayerComponent.class);
        PlayerScoreComponent tankPlayerScoreComponent = tankPlayer.player.getPlayerScoreComponent();
        PlayerComponent bulletPlayer = playerBullet.getComponent(PlayerComponent.class);
        PlayerScoreComponent bulletPlayerScoreComponent = bulletPlayer.player.getPlayerScoreComponent();

        if (tankPlayer.player.getPlayerName().equals(bulletPlayer.player.getPlayerName()) ) {
            tankPlayerScoreComponent.subtractPoints(150);
            notifyObservers(tankPlayer.player.getPlayerName(), tankPlayerScoreComponent.getScore());
        }
        else if (tankPlayer.player.getPlayerName().equals(accountService.getCurrentUser().getPlayer().getPlayerName())){
            tankPlayerScoreComponent.subtractPoints(50);
            notifyObservers(tankPlayer.player.getPlayerName(), tankPlayerScoreComponent.getScore());
        }
        else if (bulletPlayer.player.getPlayerName().equals(accountService.getCurrentUser().getPlayer().getPlayerName())){
            bulletPlayerScoreComponent.addPoints(100);
            notifyObservers(bulletPlayer.player.getPlayerName(), bulletPlayerScoreComponent.getScore());
        }
        Gdx.app.log("InfoTag", accountService.getCurrentUserEmail() + " has a score of " + accountService.getCurrentUser().getPlayer().getPlayerScoreComponent().getScore());
    }
}
