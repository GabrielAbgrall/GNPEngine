package fr.gabrielabgrall.gnpengine;

import fr.gabrielabgrall.gnpengine.utils.Clock;
import fr.gabrielabgrall.gnpengine.utils.Vector2;
import fr.gabrielabgrall.gnpengine.gnpobjects.Camera;
import fr.gabrielabgrall.gnpengine.gnpobjects.GNPObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class GraphicsEngine extends Thread{

    protected boolean initialized = false;
    protected double fps;
    protected Camera camera;
    protected JFrame frame = new JFrame();
    protected Controller controller = new Controller(this);
    protected final List<GNPObject> GNPObjects;

    protected GraphicsEngine(double fps, List<GNPObject> GNPObjects) throws IllegalArgumentException {
        if (fps < 0) throw new IllegalArgumentException("FPS must be a positive number");

        this.fps = fps;
        this.GNPObjects = GNPObjects;
    }

    public void init() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(controller);
        frame.setUndecorated(false);

        frame.setVisible(true);

        initialized = true;
    }

    public void clean() {
        frame.dispose();
        initialized = false;
    }

    @Override
    public void run() {

        init();
        Clock clock = new Clock("GraphicsEngine");

        while(!interrupted()) {
            display();
            clock.tick(fps);
        }
    }

    protected void display() {
        if(!initialized || camera == null) return;

        BufferedImage surface = new BufferedImage(camera.getDimensions().x, camera.getDimensions().y, BufferedImage.TYPE_INT_ARGB);

        GNPObjects.forEach(gameObject -> {
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
        private final GraphicsEngine graphicsEngine;

        public Controller(GraphicsEngine graphicsEngine) {
            this.graphicsEngine = graphicsEngine;
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
            return new Dimension(graphicsEngine.camera.getDimensions().x, graphicsEngine.camera.getDimensions().y);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            graphicsEngine.camera.move(new Vector2(
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
