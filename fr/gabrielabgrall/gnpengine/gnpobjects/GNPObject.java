package fr.gabrielabgrall.gnpengine.gnpobjects;

import fr.gabrielabgrall.gnpengine.utils.Vector2;

import java.awt.*;

/**
 * GNPObject est l'objet de base gérée par le GNPEngine.
 * 
 * La création de nouvelles classes filles en redéfinissant les méthodes update et updateMesh permettent de créer le comportement des objets.
 * Il doit être renseigné dans le GNPEngine via sa méthode addGNPObject.
 */
public class GNPObject {

    protected Vector2 position;
    protected Image mesh;
    protected boolean hidden;

    public GNPObject(Vector2 position, Image mesh) {
        this.position = position;
        this.mesh = mesh;
        this.hidden = false;
    }

    public GNPObject(Vector2 position) {this(position, null);}

    public GNPObject() {this(new Vector2(0, 0), null);}

    public void update(double deltaTime) {
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
