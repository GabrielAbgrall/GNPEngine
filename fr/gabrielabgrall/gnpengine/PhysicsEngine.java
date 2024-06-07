package fr.gabrielabgrall.gnpengine;

import fr.gabrielabgrall.gnpengine.gnpobjects.GNPObject;
import fr.gabrielabgrall.gnpengine.utils.Clock;

import java.util.List;

public class PhysicsEngine extends Thread {

    protected double ups;
    protected double deltaTime;
    private final List<GNPObject> GNPObjects;

    protected PhysicsEngine(double ups, List<GNPObject> GNPObjects) throws IllegalArgumentException {
        if(ups < 0) throw new IllegalArgumentException("UPS must be a positive number");

        this.ups = ups;
        this.GNPObjects = GNPObjects;
    }

    @Override
    public void run() {
        Clock clock = new Clock("PhysicsEngine");
        while(!interrupted()) {
            update();
            clock.tick(ups);
            deltaTime = clock.getDeltaTime();
        }
    }

    public void clean() {
    }

    protected void update() {
        GNPObjects.forEach(gameObject -> {
            gameObject.update(deltaTime);
        });
    };

    public void setUps(float ups) {
        this.ups = ups;
    }

    public double getUps() {
        return ups;
    }
}
