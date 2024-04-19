package fr.gabrielabgrall.swingengine.demo.maze;

import fr.gabrielabgrall.swingengine.gameobject.GameObject;
import fr.gabrielabgrall.swingengine.utils.Debug;
import fr.gabrielabgrall.swingengine.utils.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class AStar extends GameObject {

    static class Node {

        protected Vector2 pos;
        protected boolean walkable;
        protected double totalCost;
        protected double costFromStart;
        protected double costToDest;
        protected Node parent;
        protected Color color;
        protected boolean closed;

        Node(Vector2 pos, boolean walkable) {
            this.pos = pos;
            this.walkable = walkable;
            this.totalCost = 0;
            this.costFromStart = 0;
            this.costToDest = 0;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Node)) return false;
            if(obj == this) return true;
            return this.pos.equals(((Node) obj).pos);
        }
    }

    private final Maze maze;
    private final Map<Vector2, Node> nodeMap;
    private final int size;
    private final int cellSize;

    private List<Vector2> path;
    private final Queue<Node> updated = new ConcurrentLinkedQueue<>();
    private Node end;

    public AStar(Maze maze) {
        Debug.log("AStar | Initializing AStar");
        this.nodeMap = new HashMap<>();
        this.size = maze.getSize();
        this.cellSize = maze.getCellSize();
        this.path = new ArrayList<>();
        this.maze = maze;
        this.mesh = new BufferedImage(
                size * cellSize,
                size * cellSize,
                BufferedImage.TYPE_4BYTE_ABGR
        );
    }

    public void generateNodeMap() {
        Debug.log("AStar | Generating node map");
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Vector2 pos = new Vector2(x, y);
                nodeMap.put(pos, new Node(pos, maze.getAt(pos).isWalkable()));
            }
        }
    }

    public void generatePath(Vector2 startPos, Vector2 endPos) {
        Debug.log("AStar | Generating path");

        Node start = nodeMap.get(startPos);
        end = nodeMap.get(endPos);

        List<Node> open = new ArrayList<>();

        open.add(start);
        updated.add(start);
        updated.add(end);
        start.color = Color.RED;
        end.color = Color.RED;

        long totalTime = 0;
        long displayTime = 0;
        long sortTime = 0;
        long neighborsTime = 0;
        AtomicLong closedCheckTime = new AtomicLong();
        AtomicLong openCheckTime = new AtomicLong();
        AtomicLong neighborUpdateTime = new AtomicLong();
        AtomicLong openAddTime = new AtomicLong();

        int loopCount = 0;
        while (!open.isEmpty()) {
            long startTime = System.currentTimeMillis();

            if(Debug.shouldDebug(2) && loopCount%100==0) {
                if(Debug.shouldDebug(4)) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ignored){

                    }
                }
            }
            loopCount++;

            displayTime += System.currentTimeMillis() - startTime;

            long sortStart = System.currentTimeMillis();

            Node current = open.stream().min(Comparator.comparingDouble(n -> n.totalCost)).get();

            open.remove(current);
            current.closed = true;
            updated.add(current);
            if(!current.equals(start) && !current.equals(end)) current.color = Color.LIGHT_GRAY;

            sortTime += System.currentTimeMillis() - sortStart;

            if(current == end) {
                nodeToPath(current);
                totalTime += System.currentTimeMillis() - startTime;
                Debug.log("AStar | TotalTime: " + totalTime + "ms; DisplayTime: " + displayTime + "ms; SortTime: " + sortTime + "ms; NeighborsTime: " + neighborsTime + "ms; ClosedCheckTime: " + closedCheckTime.get() + "ms; OpenCheckTime: " + openCheckTime.get() + "ms; NeighborUpdateTime: " + neighborUpdateTime.get() + "ms; OpenAddTime: " + openAddTime.get() + "ms");
                return;
            }

            long neighborsStart = System.currentTimeMillis();

            neighbors(current).forEach(neighbor -> {
                long closedCheckStart = System.currentTimeMillis();
                if(neighbor.walkable && !neighbor.closed) {
                    closedCheckTime.addAndGet(System.currentTimeMillis() - closedCheckStart);

                    double neighborCost = current.costFromStart + 1;

                    long openCheckStart = System.currentTimeMillis();
                    boolean isOpen = open.contains(neighbor);
                    openCheckTime.addAndGet(System.currentTimeMillis() - openCheckStart);

                    if(neighborCost < neighbor.costFromStart || !isOpen) {

                        long neighborUpdateStart = System.currentTimeMillis();
                        neighbor.costFromStart = neighborCost;
                        neighbor.costToDest = Math.sqrt(
                                Math.pow(end.pos.x - neighbor.pos.x, 2) + Math.pow(end.pos.y - neighbor.pos.y, 2)
                        );
                        neighbor.totalCost = neighbor.costFromStart + neighbor.costToDest;
                        neighbor.parent = current;

                        neighborUpdateTime.addAndGet(System.currentTimeMillis() - neighborUpdateStart);

                        if(!isOpen) {
                            long openAddStart = System.currentTimeMillis();
                            open.add(neighbor);
                            updated.add(neighbor);
                            if(!neighbor.equals(end)) neighbor.color = Color.BLUE;
                            openAddTime.addAndGet(System.currentTimeMillis() - openAddStart);
                        }
                    }
                }
            });

            neighborsTime += System.currentTimeMillis() - neighborsStart;

            totalTime += System.currentTimeMillis() - startTime;
        }
        path = new ArrayList<>();
    }

    private void nodeToPath(Node node) {
        path = new ArrayList<>();

        Node current = node;
        while(current.parent != null){
            path.add(current.pos);
            updated.add(current);
            if(!current.equals(end)) current.color = Color.GREEN;
            current = current.parent;
        }
        path.add(current.pos);
    }

    public List<Vector2> getPath() {
        return path;
    }

    private List<Node> neighbors(Node current){
        List<Node> temp = new ArrayList<>();
        List<Node> neighbours = new ArrayList<>();

        temp.add(nodeMap.get(current.pos.add(new Vector2(1, 0))));
        temp.add(nodeMap.get(current.pos.add(new Vector2(0, 1))));
        temp.add(nodeMap.get(current.pos.add(new Vector2(-1, 0))));
        temp.add(nodeMap.get(current.pos.add(new Vector2(0, -1))));
        temp.add(nodeMap.get(current.pos.add(new Vector2(1, 0))));
        temp.add(nodeMap.get(current.pos.add(new Vector2(0, 1))));
        temp.add(nodeMap.get(current.pos.add(new Vector2(-1, 0))));
        temp.add(nodeMap.get(current.pos.add(new Vector2(0, -1))));

        temp.forEach(t -> {
            if (t.pos.x >= 0 && t.pos.x < size && t.pos.y >= 0 && t.pos.y < size) neighbours.add(t);
        });

        return neighbours;
    }

    @Override
    public void updateMesh() {
        while(!updated.isEmpty()) {
            Node n = updated.poll();
            Graphics2D g2d = (Graphics2D) mesh.getGraphics();
            g2d.setColor(n.color);
            g2d.fillRect(n.pos.x * cellSize, n.pos.y * cellSize, cellSize, cellSize);
        }
    }
}
