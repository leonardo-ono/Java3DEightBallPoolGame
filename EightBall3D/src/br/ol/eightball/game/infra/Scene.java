package br.ol.eightball.game.infra;

import java.awt.Graphics2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import br.ol.eightball.renderer3d.core.Renderer;

/**
 * Scene abstract class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public abstract class Scene {

    public Engine engine;
    public Entity camera;
    public List<Entity> entities = new ArrayList<Entity>();

    public Scene() {
    }

    public Engine getEngine() {
        return engine;
    }

    public Entity getCamera() {
        return camera;
    }

    public void setCamera(Entity camera) {
        this.camera = camera;
    }

    public void init() throws Exception {
        camera.init();
        for (Entity entity : entities) {
            entity.init();
        }
    }
    
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void updatePhysics() {
    }
        
    public void update(Renderer r) {
        camera.update(r);
        r.setViewTranform(camera.transform);
        
        for (Entity entity : entities) {
            entity.update(r);
        }
    }
    
    public void draw(Renderer r) {
        for (Entity entity : entities) {
            entity.preDraw(r);
            entity.draw(r);
        }
    }

    public void draw(Renderer renderer, Graphics2D g) {
        for (Entity entity : entities) {
            entity.draw(renderer, g);
        }
    }
    
    public void broadcastMessage(String message) {
        for (Entity entity : entities) {
            try {
                Method method = entity.getClass().getMethod(message);
                if (method != null) {
                    method.invoke(entity);
                }
            } catch (Exception ex) {
            }
        }
    }

}
