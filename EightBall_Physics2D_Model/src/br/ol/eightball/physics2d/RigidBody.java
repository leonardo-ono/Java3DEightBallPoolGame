package br.ol.eightball.physics2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Rigid body class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class RigidBody {
    
    private final Physics2D physics2D;
    private final Vec2 position = new Vec2();
    private boolean visible = true;
    private Color color = Color.RED;
    private double mass;
    private final Vec2 force = new Vec2();
    private final Vec2 acceleration = new Vec2();
    private final Vec2 velocity = new Vec2();
    private final Vec2 ds = new Vec2();
    private Shape shape;
    private final Vec2 auxVec = new Vec2();
    
    
    public RigidBody(Physics2D physics2D, Shape shape) {
        this.physics2D = physics2D;
        this.shape = shape;
    }

    public Physics2D getPhysics2D() {
        return physics2D;
    }

    public Vec2 getPosition() {
        return position;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
    
    public void addForce(Vec2 force) {
        this.force.add(force);
    }
    
    public Vec2 getForce() {
        return force;
    }

    public void applyImpulse(Vec2 impulse) {
        auxVec.set(impulse);
        auxVec.scale(1 / mass);
        velocity.add(auxVec);
    }
    
    public Vec2 getAcceleration() {
        return acceleration;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public Shape getShape() {
        return shape;
    }

    public void updateVelocity() {
        double dt = Physics2DTime.getDelta() / 1000000000.0;
        force.add(physics2D.getGravity());
        // linear motion
        acceleration.set(force);
        acceleration.scale(dt / mass);
        velocity.add(acceleration);
        velocity.scale(0.99);
    }
    
    public void update() {
        double dt = Physics2DTime.getDelta() / 1000000000.0;
        ds.set(velocity);
        ds.scale(dt);
        position.add(ds);
        
        // clear net force and torque
        force.set(0, 0);
    }

    public void draw(Graphics2D g) {
        if (shape != null) {
            AffineTransform at = g.getTransform();
            g.setColor(color);
            g.translate(position.getX(), position.getY());
            shape.draw(g);
            g.setTransform(at);
        }
    }
    
}
