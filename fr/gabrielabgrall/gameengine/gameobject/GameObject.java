package fr.gabrielabgrall.gameengine.gameobject;

import fr.gabrielabgrall.gameengine.utils.Vector2;

import java.awt.*;

public class GameObject {

    protected Vector2 position;
    protected Image mesh;
    protected boolean hidden;

    public GameObject(Vector2 position, Image mesh) {
        this.position = position;
        this.mesh = mesh;
        this.hidden = false;
    }

    public GameObject(Vector2 position) {this(position, null);}

    public GameObject() {this(new Vector2(0, 0), null);}

    public void update(long elapsed) {
    }

    public void updateMesh(){
    }

    public Image getMesh() {
        return mesh;
    }

    public void setMesh(Image mesh) {
        this.mesh = mesh;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void move(Vector2 movement) {
        this.position = this.position.add(movement);
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }
}
