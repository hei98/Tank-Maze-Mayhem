package com.mygdx.tank;

public class ShootingSystem {
    private GameModel model;

    public ShootingSystem(GameModel model) {
        this.model = model;
    }

    public void shoot(float startX, float startY, float directionX, float directionY) {
        System.out.println("Shooting bullet");
        Entity bullet = new Entity();
        bullet.addComponent(new PositionComponent(startX, startY));
        bullet.addComponent(new SpeedComponent(1.0f, directionX * 300.0f, directionY * 300.0f));
        bullet.addComponent(new SpriteComponent("hvitbullet.png"));
        model.addEntity(bullet);
    }
}
