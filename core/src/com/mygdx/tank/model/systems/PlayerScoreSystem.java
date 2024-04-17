package com.mygdx.tank.model.systems;

import com.mygdx.tank.AccountService;
import com.mygdx.tank.Player;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.Scoreboard;
import com.mygdx.tank.model.components.PlayerComponent;
import com.mygdx.tank.model.components.PlayerScoreComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerScoreSystem {
    private AccountService accountService;
    private Scoreboard scoreboard;

    public PlayerScoreSystem(AccountService accountService, Scoreboard scoreboard) {
        this.accountService = accountService;
        this.scoreboard = scoreboard;
    }

    public void updateScore(Entity playerBullet, Entity playerTank, List<Player> connectedPlayers) {
        Player tankPlayer = new Player();
        Player bulletPlayer = new Player();
        for (Player player : connectedPlayers) {
            if (player.getPlayerName().equals(playerTank.getComponent(PlayerComponent.class).player.getPlayerName())) {
                tankPlayer = player;
            }
            if (player.getPlayerName().equals(playerBullet.getComponent(PlayerComponent.class).player.getPlayerName())) {
                bulletPlayer = player;
            }
        }

        PlayerScoreComponent tankPlayerScoreComponent = tankPlayer.getPlayerScoreComponent();
        PlayerScoreComponent bulletPlayerScoreComponent = bulletPlayer.getPlayerScoreComponent();

        System.out.println(bulletPlayer.getPlayerName() + " skøyt " + tankPlayer.getPlayerName());

        if (tankPlayer.getPlayerName().equals(bulletPlayer.getPlayerName()) ) {
            System.out.println("Fjerner 150 poeng fra " + tankPlayer.getUserMail());
            if (tankPlayerScoreComponent.score - 150 < 0) {
                tankPlayerScoreComponent.score = 0;
            } else {
                tankPlayerScoreComponent.score -= 150;
            }
            System.out.println(tankPlayer.getUserMail() + " nye poeng er " + tankPlayerScoreComponent.score);
            scoreboard.update(tankPlayer);
        } else {
            System.out.println("Fjerner 50 poeng fra " + tankPlayer.getUserMail());
            if (tankPlayerScoreComponent.score - 50 < 0) {
                tankPlayerScoreComponent.score = 0;
            } else {
                tankPlayerScoreComponent.score -= 50;
            }
            System.out.println(tankPlayer.getUserMail() + " nye poeng er " + tankPlayerScoreComponent.score);
            scoreboard.update(tankPlayer);

            System.out.println("Legger til 100 poeng til " + bulletPlayer.getUserMail());
            bulletPlayerScoreComponent.score += 100;
            System.out.println(bulletPlayer.getUserMail() + " nye poeng er " + bulletPlayerScoreComponent.score);
            scoreboard.update(bulletPlayer);
        }

        System.out.println(scoreboard.getScoreboard());
    }
}