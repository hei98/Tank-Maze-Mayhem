package com.mygdx.tank.model;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.tank.Constants;
import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PowerupFactory implements EntityFactory {
    @Override
    public Entity createEntity() {
        Constants con = Constants.getInstance();
        Entity powerup = new Entity();
        powerup.addComponent(new TypeComponent(TypeComponent.EntityType.POWERUP));

        PowerUpTypeComponent.PowerupType[] powerupTypes = PowerUpTypeComponent.PowerupType.values();
        // Get a random index
        int randomIndex = new Random().nextInt(powerupTypes.length);
        // Use the random index to select a random enum constant
        PowerUpTypeComponent.PowerupType randomPowerupType = powerupTypes[randomIndex];
        powerup.addComponent(new PowerUpTypeComponent(randomPowerupType));

        List<float[]> possibleSpawnpoints = new ArrayList<>();
        possibleSpawnpoints.add(new float[]{0.05f, 0.3f});
        possibleSpawnpoints.add(new float[]{0.9f, 0.45f});
        possibleSpawnpoints.add(new float[]{0.25f, 0.5f});
        possibleSpawnpoints.add(new float[]{0.25f, 0.9f});
        possibleSpawnpoints.add(new float[]{0.6f, 0.4f});
        possibleSpawnpoints.add(new float[]{0.48f, 0.05f});
        possibleSpawnpoints.add(new float[]{0.71f, 0.3f});

        float[] spawnPoint = possibleSpawnpoints.get(new Random().nextInt(possibleSpawnpoints.size()));
        float x = spawnPoint[0] * con.getSWidth();
        float y = spawnPoint[1] * con.getSHeight();
        powerup.addComponent(new PositionComponent(x, y));

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
        Sprite sprite = powerup.getComponent(SpriteComponent.class).getSprite();
        sprite.setSize(con.getIBSize() * 0.7f, con.getIBSize() * 0.7f);
        return powerup;
    }
}
