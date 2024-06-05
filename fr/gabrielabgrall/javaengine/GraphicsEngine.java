package fr.gabrielabgrall.javaengine;

import fr.gabrielabgrall.javaengine.utils.Vector2;
import fr.gabrielabgrall.javaengine.gameobject.Camera;
import fr.gabrielabgrall.javaengine.gameobject.GameObject;
import fr.gabrielabgrall.javaengine.utils.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class GraphicsEngine extends Thread{

    public static GraphicsEngine instance;
    protected boolean initialized = false;
    protected boolean running = true;
    protected double fps;
    protected Camera camera;
    protected JFrame frame = new JFrame();
    protected Controller controller = new Controller();
    protected final List<GameObject> gameObjects;

    protected GraphicsEngine(double fps, List<GameObject> gameObjects) throws IllegalArgumentException {
        if (fps < 0) throw new IllegalArgumentException("FPS must be a positive number");

        this.fps = fps;
        this.gameObjects = gameObjects;
    }

    public void init() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(controller);
        frame.setUndecorated(true);

        frame.setVisible(true);

        initialized = true;
    }

    @Override
    public void run() {
        instance = this;

        long last = 0;
        int count = 0;

        init();

        while(running) {
            long start = System.currentTimeMillis();
            display();
            try {
                Thread.sleep((long)(1000/fps) - (System.currentTimeMillis() - start));
            } catch (Exception ignored) {
            }
            last += System.currentTimeMillis() - start;
            count++;
            if(last/1000 >= 1) {
                Debug.log("PhysicsEngine | " + count + " fps", 2);
                last = 0;
                count = 0;
            }
        }
    }

    protected void display() {
        if(!initialized || camera == null) return;

        BufferedImage surface = new BufferedImage(camera.getDimensions().x, camera.getDimensions().y, BufferedImage.TYPE_INT_ARGB);

        gameObjects.forEach(gameObject -> {
            if(!gameObject.isHidden()) {
                gameObject.updateMesh();
                surface.getGraphics().drawImage(
                        gameObject.getMesh(),
                        gameObject.getPosition().x - camera.getPosition().x,
                        gameObject.getPosition().y - camera.getPosition().y,
                        null
                );
            }
        });

        controller.updateSurface(surface);

        if(camera.isResized()) {
            frame.pack();
            camera.setResized(false);
        }

        controller.repaint();
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public static class Controller extends JPanel implements MouseMotionListener {

        private BufferedImage surface;
        private Vector2 lastMousePos;

        public Controller() {
            addMouseMotionListener(this);
        }

        public void updateSurface(BufferedImage surface) {
            this.surface = surface;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(surface, 0, 0, null);
        }

        @Override
        public Dimension getPreferredSize() {
            if(surface == null) return new Dimension(0, 0);
            return new Dimension(surface.getWidth(), surface.getHeight());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            instance.camera.move(new Vector2(
                    lastMousePos.x - e.getX(),
                    lastMousePos.y - e.getY()
            ));
            lastMousePos = new Vector2(e.getX(), e.getY());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            lastMousePos = new Vector2(e.getX(), e.getY());
        }
    }
}
