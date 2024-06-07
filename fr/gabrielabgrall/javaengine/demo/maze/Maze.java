package fr.gabrielabgrall.javaengine.demo.maze;

import fr.gabrielabgrall.javaengine.gameobject.Camera;
import fr.gabrielabgrall.javaengine.JavaEngine;
import fr.gabrielabgrall.javaengine.gameobject.GameObject;
import fr.gabrielabgrall.javaengine.utils.Debug;
import fr.gabrielabgrall.javaengine.utils.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Maze extends GameObject {

    public static class Node{
        protected Vector2 pos;
        protected boolean walkable;
        protected boolean closed;

        Node(Vector2 pos, boolean walkable) {
            this.pos = pos;
            this.walkable = walkable;
        }

        public boolean isWalkable() {
            return walkable;
        }
    }

    protected int cellSize;
    protected int size;
    private final Map<Vector2, Node> nodes = new HashMap<>();
    private final Queue<Node> updated = new ConcurrentLinkedQueue<>();

    public Maze(int size, int cellSize) throws IllegalArgumentException {
        if (size % 2 == 0) throw new IllegalArgumentException("size must be an odd number");
        this.size = size;
        this.cellSize = cellSize;
        this.mesh = new BufferedImage(
                size* this.cellSize,
                size* this.cellSize,
                BufferedImage.TYPE_4BYTE_ABGR
        );

        Debug.log("Maze | Size: " + size + "x" + size + " (" + (int)Math.pow(size, 2) + " nodes)");

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Node node = new Node(new Vector2(x, y), false);
                nodes.put(node.pos, node);
                updated.add(node);
            }
        }
    }

    /**
     * Generates the Maze using the following process :
     * 1. Pick a random cell
     * 2. Make it walkable
     * 3. Pick a random cell next to any other walkable cell
     * 4. Make it walkable and open the wall to the desired cell
     * 5. Repeat from step 3 until all cells are walkable
     */
    public void generate() {

        Debug.log("Maze | Generating maze");
        long start = System.currentTimeMillis();

        int totalCell = (int)Math.pow(size/2,  2);
        int cellCount = 0;
        int percentage = 0;

        Stack<Node> open = new Stack<>();

        open.add(nodes.get(new Vector2(
                new Random().nextInt(size/2)*2 + 1,
                new Random().nextInt(size/2)*2 + 1
        )));

        while (!open.isEmpty()) {
            int currentPercentage = (int) (cellCount / (double)totalCell * 100);
            if(currentPercentage > percentage) {
                percentage = currentPercentage;
                Debug.log("Maze | " + percentage + "%", 2);
            }

            Node current = open.peek();
            current.walkable = true;
            updated.add(current);

            List<Node> unvisitedNeighbours = new ArrayList<>();
            getNeighbours(current).forEach(n -> {
                if (!open.contains(n) && !n.closed) unvisitedNeighbours.add(n);
            });

            if (unvisitedNeighbours.isEmpty()) {
                open.pop();
                current.closed = true;
                cellCount++;
                continue;
            }

            Collections.shuffle(unvisitedNeighbours);
            Node next = unvisitedNeighbours.getFirst();
            open.add(next);

            Node wall = nodes.get(new Vector2(
                    current.pos.x + (next.pos.x - current.pos.x)/2,
                    current.pos.y + (next.pos.y - current.pos.y)/2
            ));
            wall.walkable = true;
            updated.add(wall);
        }
        long end = System.currentTimeMillis();
        Debug.log("Maze | Done generating in " + (end - start) + "ms");
    }

    /**
     * Get all possible neighbours of the target cell
     *
     * @param node: Position of the cell
     * @return neighbours
     */
    private List<Node> getNeighbours(Node node) {
        List<Vector2> temp = new ArrayList<>();
        List<Node> neighbours = new ArrayList<>();

        temp.add(new Vector2(node.pos.x - 2, node.pos.y));
        temp.add(new Vector2(node.pos.x + 2, node.pos.y));
        temp.add(new Vector2(node.pos.x, node.pos.y - 2));
        temp.add(new Vector2(node.pos.x, node.pos.y + 2));

        temp.forEach(t -> {
            if (t.x >= 0 && t.x < size && t.y >= 0 && t.y < size) neighbours.add(nodes.get(t));
        });

        return neighbours;
    }

    /**
     * Return ce cell at the desired position
     *
     * @param pos: Position of the desired cell
     * @return cell content
     */
    public Node getAt(Vector2 pos) {
        return nodes.get(pos);
    }

    /**
     * Return the size of the Maze
     *
     * @return size
     */
    public int getSize() {
        return size;
    }

    public int getCellSize() {
        return cellSize;
    }

    /**
     * Generates a Surface from the current maze state and return it
     *
     * @return surface
     */
    @Override
    public void updateMesh() {
        while(!updated.isEmpty()){
            Node n = updated.poll();
            Graphics2D g2d = (Graphics2D) mesh.getGraphics();
            g2d.setColor(n.walkable?Color.WHITE:Color.BLACK);
            g2d.fillRect(n.pos.x* cellSize, n.pos.y* cellSize, cellSize, cellSize);
        }
    }

    public static void main(String[] args) throws IllegalArgumentException, InterruptedException {
        Debug.log("Main | Starting");
        Debug.setLevel(1);

        int size = 999;
        double fps = 60;

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        JavaEngine engine = new JavaEngine(0, fps);
        engine.getGraphicsEngine().setCamera(new Camera(
                new Vector2(0, 0),
                new Vector2(width, height)
        ));

        Maze maze = new Maze(size, 2);
        AStar aStar = new AStar(maze);

        engine.addGameObject(maze);
        engine.addGameObject(aStar);

        engine.start();

        maze.generate();
        aStar.generateNodeMap();
        aStar.generatePath(
                new Vector2(1, 1),
                new Vector2(maze.getSize() - 2, maze.getSize() - 2)
        );

        Debug.log("Main | Done");
    }
}
