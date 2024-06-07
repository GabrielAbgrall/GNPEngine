package fr.gabrielabgrall.gnpengine;

import fr.gabrielabgrall.gnpengine.gameobject.GNPObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GNPEngine {

    protected final List<GNPObject> GNPObjects = new ArrayList<>();

    protected PhysicsEngine physicsEngine;
    protected GraphicsEngine graphicsEngine;

    public GNPEngine(double ups, double fps) throws IllegalArgumentException {
        this.physicsEngine = new PhysicsEngine(ups, GNPObjects);
        this.graphicsEngine = new GraphicsEngine(fps, GNPObjects);
    }

    public void start() {
        graphicsEngine.start();
        physicsEngine.start();
    }

    public void stop() {
        graphicsEngine.running = false;
        physicsEngine.running = false;
    }

    public void addGameObject(GNPObject GNPObject) {
        this.GNPObjects.add(GNPObject);
    }

    public void removeGameObject(GNPObject GNPObject) {
        this.GNPObjects.remove(GNPObject);
    }

    public void addGameObjects(Collection<GNPObject> GNPObjects) {
        GNPObjects.forEach(this::addGameObject);
    }

    public void removeGameObjects(Collection<GNPObject> GNPObjects) {
        GNPObjects.forEach(this::removeGameObject);
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public GraphicsEngine getGraphicsEngine() {
        return graphicsEngine;
    }

    public double getFps() {
        return graphicsEngine.fps;
    }

    public double getUps() {
        return physicsEngine.ups;
    }
}
