package fr.gabrielabgrall.engine;

import fr.gabrielabgrall.engine.gameobject.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Engine {

    protected final List<GameObject> gameObjects = new ArrayList<>();

    protected PhysicsEngine physicsEngine;
    protected GraphicsEngine graphicsEngine;

    public Engine(double ups, double fps) throws IllegalArgumentException {
        this.physicsEngine = new PhysicsEngine(ups, gameObjects);
        this.graphicsEngine = new GraphicsEngine(fps, gameObjects);
    }

    public void start() {
        graphicsEngine.start();
        physicsEngine.start();
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }

    public void addGameObjects(Collection<GameObject> gameObjects) {
        gameObjects.forEach(this::addGameObject);
    }

    public void removeGameObjects(Collection<GameObject> gameObjects) {
        gameObjects.forEach(this::removeGameObject);
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