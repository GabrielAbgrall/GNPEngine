package fr.gabrielabgrall.swingengine;

import fr.gabrielabgrall.swingengine.gameobject.GameObject;
import fr.gabrielabgrall.swingengine.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine extends Thread {

    protected boolean running = true;
    protected double ups;
    protected long elapsed;
    private final List<GameObject> gameObjects = new ArrayList<>();

    protected PhysicsEngine(double ups) {
        this.ups = ups;
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

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }
}
