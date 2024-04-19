package fr.gabrielabgrall.swingengine;

public class Engine {

    PhysicsEngine physicsEngine;
    RenderingEngine renderingEngine;

    public Engine(double ups, double fps) {
        this.physicsEngine = new PhysicsEngine(ups);
        this.renderingEngine = new RenderingEngine(fps);
    }

    public void start() {
        renderingEngine.start();
        physicsEngine.start();
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public RenderingEngine getRenderingEngine() {
        return renderingEngine;
    }
}
