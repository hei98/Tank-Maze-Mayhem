package com.mygdx.tank.model.systems;

import com.mygdx.tank.AccountService;
import com.mygdx.tank.model.Entity;
import com.mygdx.tank.model.components.PlayerComponent;
import com.mygdx.tank.model.components.PlayerScoreComponent;

public class PlayerScoreSystem {
    private AccountService accountService;

    public PlayerScoreSystem(AccountService accountService) {
        this.accountService = accountService;
    }

    public void updateScore(Entity playerBullet, Entity playerTank) {
        PlayerComponent tankPlayer = playerTank.getComponent(PlayerComponent.class);
        PlayerScoreComponent tankPlayerScoreComponent = tankPlayer.player.getPlayerScoreComponent();
        PlayerComponent bulletPlayer = playerBullet.getComponent(PlayerComponent.class);
        PlayerScoreComponent bulletPlayerScoreComponent = bulletPlayer.player.getPlayerScoreComponent();

        if (tankPlayer.player.getPlayerName().equals(bulletPlayer.player.getPlayerName()) ) {
            tankPlayerScoreComponent.subtractPoints(150);
        }
        else if (tankPlayer.player.getPlayerName().equals(accountService.getCurrentUser().getPlayer().getPlayerName())){
            tankPlayerScoreComponent.subtractPoints(50);
        }
        else if (bulletPlayer.player.getPlayerName().equals(accountService.getCurrentUser().getPlayer().getPlayerName())){
            bulletPlayerScoreComponent.addPoints(100);
        }
        System.out.println(accountService.getCurrentUserEmail() + " has a score of " + accountService.getCurrentUser().getPlayer().getPlayerScoreComponent().getScore());
    }
}
