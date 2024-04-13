package com.mygdx.tank.model;

import com.mygdx.tank.model.components.PositionComponent;
import com.mygdx.tank.model.components.SpriteComponent;
import com.mygdx.tank.model.components.TypeComponent;
import com.mygdx.tank.model.components.powerup.PowerUpTypeComponent;
import java.util.Random;

public class PowerupFactory implements EntityFactory {
    @Override
    public Entity createEntity() {
        Entity powerup = new Entity();
        powerup.addComponent(new TypeComponent(TypeComponent.EntityType.POWERUP));

        PowerUpTypeComponent.PowerupType[] powerupTypes = PowerUpTypeComponent.PowerupType.values();
        // Get a random index
        int randomIndex = new Random().nextInt(powerupTypes.length);
        // Use the random index to select a random enum constant
        PowerUpTypeComponent.PowerupType randomPowerupType = powerupTypes[randomIndex];
        powerup.addComponent(new PowerUpTypeComponent(randomPowerupType));

        powerup.addComponent(new PositionComponent(200, 200));
        String imagePath;
        if (randomPowerupType == PowerUpTypeComponent.PowerupType.Shield) {
            imagePath = "images/ShieldPowerup.png";
        } else if (randomPowerupType == PowerUpTypeComponent.PowerupType.Firerate) {
            imagePath = "images/MachineGunPowerup.png";
        } else if (randomPowerupType == PowerUpTypeComponent.PowerupType.Speed) {
            imagePath = "images/SpeedPowerup.png";
        } else {
            imagePath = "images/ShieldPowerup.png";
        }
        powerup.addComponent(new SpriteComponent(imagePath));
        return powerup;
    }
}
