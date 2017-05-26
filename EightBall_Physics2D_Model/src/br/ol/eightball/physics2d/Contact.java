package br.ol.eightball.physics2d;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * (Collision) Contact class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Contact {

    private RigidBody rba;
    private RigidBody rbb;
    private double penetration;
    private final Vec2 position = new Vec2();
    private final Vec2 normal = new Vec2();
    private final Vec2 auxVec = new Vec2();

    public Contact(RigidBody rba, RigidBody rbb) {
        this.rba = rba;
        this.rbb = rbb;
    }

    public Contact(RigidBody rba, RigidBody rbb, Vec2 position, Vec2 normal, double penetration) {
        this.rba = rba;
        this.rbb = rbb;
        this.position.set(position);
        this.normal.set(normal);
        this.penetration = penetration;
    }

    void setRba(RigidBody rba) {
        this.rba = rba;
    }

    public RigidBody getRba() {
        return rba;
    }

    void setRbb(RigidBody rbb) {
        this.rbb = rbb;
    }

    public RigidBody getRbb() {
        return rbb;
    }

    public double getPenetration() {
        return penetration;
    }

    public void setPenetration(double penetration) {
        this.penetration = penetration;
    }

    public Vec2 getPosition() {
        return position;
    }

    public Vec2 getNormal() {
        return normal;
    }
    
    public void resolveCollision() {
        auxVec.set(rba.getVelocity());
        auxVec.sub(rbb.getVelocity());
        if (auxVec.dot(normal) >= 0) {
            return;
        }
        double restitution = 0.95; // hardcoded
        double j = -(1 + restitution) * auxVec.dot(normal);
        double totalMass = 1 / rba.getMass() + 1 / rbb.getMass();
        j /= totalMass;
        auxVec.set(normal);
        auxVec.scale(j);
        rba.applyImpulse(auxVec);
        
        auxVec.set(normal);
        auxVec.scale(-j);
        rbb.applyImpulse(auxVec);
    }
    
    public void correctPosition() {
        double totalMass = rba.getMass() + rba.getMass();
        double r1 = rbb.getMass() / totalMass;
        double r2 = rba.getMass() / totalMass;
        auxVec.set(normal);
        auxVec.scale(penetration * r1 * 0.2);
        rba.getPosition().add(auxVec);
        auxVec.set(normal);
        auxVec.scale(-penetration * r2 * 0.2);
        rbb.getPosition().add(auxVec);
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillOval((int) (position.getX() - 2), (int) (position.getY() - 2), 4, 4);
        double sx = position.getX() + normal.getX() * penetration / 2;
        double sy = position.getY() + normal.getY() * penetration / 2;
        double ex = position.getX() - normal.getX() * penetration / 2;
        double ey = position.getY() - normal.getY() * penetration / 2;
        g.drawLine((int) sx, (int) sy, (int) ex, (int) ey);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Contact other = (Contact) obj;
        if (this.rba == other.rba && this.rbb == other.rbb) {
            return true;
        }
        else if (this.rbb == other.rba && this.rba == other.rbb) {
            return true;
        }
        return false;
    }
    
}
