package fr.gabrielabgrall.engine;

import fr.gabrielabgrall.engine.gameobject.GameObject;
import fr.gabrielabgrall.engine.utils.Debug;

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
        long last = 0;
        int count = 0;
        while(running) {
            long start = System.currentTimeMillis();
            update();
            try {
                long elapsed = System.currentTimeMillis() - start;
                Thread.sleep((long)(1000/ups) - elapsed);
            } catch (Exception ignored) {
            }
            last += System.currentTimeMillis() - start;
            count++;
            if(last/1000 >= 1) {
                Debug.log("PhysicsEngine | " + count + " ups", 2);
                last = 0;
                count = 0;
            }
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
