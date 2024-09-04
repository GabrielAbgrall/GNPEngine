package fr.gabrielabgrall.gnpengine.utils;

import java.util.Objects;

/**
 * Vector2 est une classe utilitaire pour effectuer des calculs de position et de déplacement pour le GNPEngine.
 */
public class Vector2 {

    public int x, y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this(0, 0);
    }

    public Vector2 add(Vector2 other) {
        Vector2 v = new Vector2();
        v.x = this.x + other.x;
        v.y = this.y + other.y;
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Vector2 v)) return false;
        return this.x == v.x && this.y == v.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
