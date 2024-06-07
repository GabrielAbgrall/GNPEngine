package fr.gabrielabgrall.gameengine.gameobject;

import fr.gabrielabgrall.gameengine.utils.Vector2;

public class Camera extends GameObject {

    protected Vector2 dimensions;
    protected boolean resized = true;

    public Camera(Vector2 position, Vector2 dimensions) {
        super(position);
        this.dimensions = dimensions;
    }

    public Vector2 getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vector2 dimensions) {
        this.resized = true;
        this.dimensions = dimensions;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isResized() {
        return resized;
    }
}
