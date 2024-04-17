package com.mygdx.tank.model.systems;

import com.mygdx.tank.AccountService;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.Scoreboard;
import com.mygdx.tank.model.components.PlayerComponent;
import com.mygdx.tank.model.components.PlayerScoreComponent;

import java.util.Map;

public class PlayerScoreSystem {
    private AccountService accountService;
    private Scoreboard scoreboard;

    public PlayerScoreSystem(AccountService accountService, Scoreboard scoreboard) {
        this.accountService = accountService;
        this.scoreboard = scoreboard;
    }

    public void updateScore(Entity playerBullet, Entity playerTank) {
        PlayerComponent tankPlayer = playerTank.getComponent(PlayerComponent.class);
        PlayerScoreComponent tankPlayerScoreComponent = tankPlayer.player.getPlayerScoreComponent();
        PlayerComponent bulletPlayer = playerBullet.getComponent(PlayerComponent.class);
        PlayerScoreComponent bulletPlayerScoreComponent = bulletPlayer.player.getPlayerScoreComponent();

        System.out.println(bulletPlayer.player.getPlayerName() + " skøyt " + tankPlayer.player.getPlayerName());

        if (tankPlayer.player.getPlayerName().equals(bulletPlayer.player.getPlayerName()) ) {
            System.out.println("Fjerner 150 poeng fra " + tankPlayer.player.getUserMail());
            if (tankPlayerScoreComponent.score - 150 < 0) {
                tankPlayerScoreComponent.score = 0;
            } else {
                tankPlayerScoreComponent.score -= 150;
            }
            System.out.println(tankPlayer.player.getUserMail() + " nye poeng er " + tankPlayerScoreComponent.score);
            scoreboard.update(tankPlayer.player);
        } else {
            System.out.println("Fjerner 50 poeng fra " + tankPlayer.player.getUserMail());
            if (tankPlayerScoreComponent.score - 50 < 0) {
                tankPlayerScoreComponent.score = 0;
            } else {
                tankPlayerScoreComponent.score -= 50;
            }
            System.out.println(tankPlayer.player.getUserMail() + " nye poeng er " + tankPlayerScoreComponent.score);
            scoreboard.update(tankPlayer.player);

            System.out.println("Legger til 100 poeng til " + bulletPlayer.player.getUserMail());
            bulletPlayerScoreComponent.score += 100;
            System.out.println(bulletPlayer.player.getUserMail() + " nye poeng er " + bulletPlayerScoreComponent.score);
            scoreboard.update(bulletPlayer.player);
        }

        System.out.println(scoreboard.getScoreboard());
    }
}