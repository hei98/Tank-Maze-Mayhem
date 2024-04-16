package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Player;
import com.mygdx.tank.User;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PowerupFactory implements EntityFactory {
    @Override
    public Entity createEntity(Player player) {
        Entity powerup = new Entity();
        powerup.addComponent(new TypeComponent(TypeComponent.EntityType.POWERUP));

        PowerUpTypeComponent.PowerupType[] powerupTypes = PowerUpTypeComponent.PowerupType.values();
        // Get a random index
        int randomIndex = new Random().nextInt(powerupTypes.length);
        // Use the random index to select a random enum constant
        PowerUpTypeComponent.PowerupType randomPowerupType = powerupTypes[randomIndex];
        powerup.addComponent(new PowerUpTypeComponent(randomPowerupType));

        List<int[]> possibleSpawnpoints = new ArrayList<>();
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            possibleSpawnpoints.add(new int[]{220, 210});
            possibleSpawnpoints.add(new int[]{220, 420});
            possibleSpawnpoints.add(new int[]{560, 100});
            possibleSpawnpoints.add(new int[]{20, 320});
            possibleSpawnpoints.add(new int[]{740, 100});
            possibleSpawnpoints.add(new int[]{700, 330});
        } else {
            possibleSpawnpoints.add(new int[]{600, 580});
            possibleSpawnpoints.add(new int[]{600, 940});
            possibleSpawnpoints.add(new int[]{1515, 340});
            possibleSpawnpoints.add(new int[]{75, 755});
            possibleSpawnpoints.add(new int[]{2100, 310});
            possibleSpawnpoints.add(new int[]{2000, 755});
        }
        int randomSpawnpointIndex = new Random().nextInt(possibleSpawnpoints.size());
        int[] spawnPoint = possibleSpawnpoints.get(randomSpawnpointIndex);
        powerup.addComponent(new PositionComponent(spawnPoint[0], spawnPoint[1]));

        String imagePath;
        if (randomPowerupType == PowerUpTypeComponent.PowerupType.Shield) {
            imagePath = "images/ShieldPowerup.png";
        } else if (randomPowerupType == PowerUpTypeComponent.PowerupType.Minigun) {
            imagePath = "images/MachineGunPowerup.png";
        } else if (randomPowerupType == PowerUpTypeComponent.PowerupType.Speed) {
            imagePath = "images/SpeedPowerup.png";
        } else {
            imagePath = "images/ShieldPowerup.png";
        }
        powerup.addComponent(new SpriteComponent(imagePath));
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            Sprite sprite = powerup.getComponent(SpriteComponent.class).getSprite();
            sprite.setScale(2.0f);
        }
        return powerup;
    }

    public Entity createSpecificPowerup(PowerUpTypeComponent.PowerupType powerupType, float positionX, float positionY) {
        Entity powerup = new Entity();
        powerup.addComponent(new TypeComponent(TypeComponent.EntityType.POWERUP));
        powerup.addComponent(new PowerUpTypeComponent(powerupType));
        powerup.addComponent(new PositionComponent(positionX, positionY));

        String imagePath;
        if (powerupType == PowerUpTypeComponent.PowerupType.Shield) {
            System.out.println("Jeg lager et skjold!");
            imagePath = "images/ShieldPowerup.png";
        } else if (powerupType == PowerUpTypeComponent.PowerupType.Minigun) {
            System.out.println("Jeg lager en Minigun");
            imagePath = "images/MachineGunPowerup.png";
        } else if (powerupType == PowerUpTypeComponent.PowerupType.Speed) {
            System.out.println("Jeg lager en Speed");
            imagePath = "images/SpeedPowerup.png";
        } else {
            System.out.println("Jeg lager en wtf");
            imagePath = "images/ShieldPowerup.png";
        }
        powerup.addComponent(new SpriteComponent(imagePath));
        if (Gdx.app.getType() != Application.ApplicationType.Desktop) {
            Sprite sprite = powerup.getComponent(SpriteComponent.class).getSprite();
            sprite.setScale(2.0f);
        }
        return powerup;
    }
}
