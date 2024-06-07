package fr.gabrielabgrall.javaengine;

import fr.gabrielabgrall.javaengine.gameobject.GameObject;
import fr.gabrielabgrall.javaengine.utils.Clock;
import fr.gabrielabgrall.javaengine.utils.Debug;

import java.util.List;

public class PhysicsEngine extends Thread {

    protected boolean running = true;
    protected double ups;
    protected long elapsed;
    private final List<GameObject> gameObjects;

    protected PhysicsEngine(double ups, List<GameObject> gameObjects) throws IllegalArgumentException {
        if(ups < 0) throw new IllegalArgumentException("UPS must be a positive number");

        this.ups = ups;
        this.gameObjects = gameObjects;
    }

    @Override
    public void run() {
        Clock clock = new Clock("PhysicsEngine");
        while(running) {
            update();
            clock.tick(ups);
        }
    }

    protected void update() {
        gameObjects.forEach(gameObject -> {
            gameObject.update(elapsed);
        });
    };

    public void setUps(float ups) {
        this.ups = ups;
    }

    public double getUps() {
        return ups;
    }
}
