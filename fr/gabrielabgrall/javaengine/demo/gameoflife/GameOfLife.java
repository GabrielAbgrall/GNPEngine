package fr.gabrielabgrall.javaengine.demo.gameoflife;

import fr.gabrielabgrall.javaengine.JavaEngine;
import fr.gabrielabgrall.javaengine.gameobject.Camera;
import fr.gabrielabgrall.javaengine.gameobject.GameObject;
import fr.gabrielabgrall.javaengine.utils.Debug;
import fr.gabrielabgrall.javaengine.utils.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameOfLife extends GameObject{

    protected final Vector2 gridSize;
    protected final int cellSize;
    protected final int alivePercentage;
    protected final boolean[][] rules = {
            {false, false, false, true, false, false, false, false, false},
            {false, false, true, true, false, false, false, false, false}
    };

    protected Map<Vector2, Cell> grid = new HashMap<>();

    public GameOfLife(Vector2 gridSize, int cellSize, int alivePercentage) {
        this.gridSize = gridSize;
        this.cellSize = cellSize;
        this.alivePercentage = alivePercentage;
        this.mesh = new BufferedImage(gridSize.x*cellSize, gridSize.y*cellSize, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public void init() {
        for (int x = 0; x < gridSize.x; x++) {
            for (int y = 0; y < gridSize.y; y++) {
                Vector2 pos = new Vector2(x, y);
                Cell cell = new Cell(pos, new Random().nextInt(100) <= alivePercentage);
                grid.put(pos, cell);
            }
        }
    }

    @Override
    public void update(long elapsed) {
        Map<Vector2, Cell> updated = new HashMap<>();
        grid.forEach((pos, cell) -> {
            updated.put(pos, new Cell(
                    pos,
                    rules[cell.isAlive()?1:0][neighbours(pos)]
            ));
        });
        grid = updated;
    }

    protected int neighbours(Vector2 pos) {
        int result = 0;

        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if(x!= 0 || y!=0){
                    Vector2 dpos = new Vector2();
                    dpos.x = ((pos.x + x) % gridSize.x + gridSize.x) % gridSize.x;
                    dpos.y = ((pos.y + y) % gridSize.y + gridSize.y) % gridSize.y;
                    if(grid.get(dpos).isAlive()) result++;
                }
            }
        }

        return result;
    }

    @Override
    public void updateMesh() {
        grid.forEach((pos, cell) -> {
            Graphics2D g2d = (Graphics2D) mesh.getGraphics();
            g2d.setColor(cell.isAlive()?Color.WHITE:Color.BLACK);
            g2d.fillRect(pos.x* cellSize, pos.y* cellSize, cellSize, cellSize);
        });
    }

    public static void main(String[] args) throws IllegalArgumentException {
        Debug.log("Main | Starting");
        Debug.setLevel(1);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        JavaEngine engine = new JavaEngine(20, 60);
        engine.getGraphicsEngine().setCamera(new Camera(
                new Vector2(0, 0),
                new Vector2(width, height)
        ));

        GameOfLife gameOfLife = new GameOfLife(
                new Vector2(250, 250),
                10,
                50
        );

        gameOfLife.init();

        engine.addGameObject(gameOfLife);

        engine.start();

        Debug.log("Main | Done");
    }

    protected static class Cell {

        private boolean alive;
        private Vector2 position;

        public Cell(Vector2 position, boolean alive) {
            this.position = position;
            this.alive = alive;
        }

        public Vector2 getPosition() {
            return position;
        }

        public void setPosition(Vector2 position) {
            this.position = position;
        }

        public boolean isAlive() {
            return alive;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }
    }
}
