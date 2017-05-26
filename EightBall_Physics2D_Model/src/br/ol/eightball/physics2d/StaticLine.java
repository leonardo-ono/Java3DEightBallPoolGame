package br.ol.eightball.physics2d;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Static line class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class StaticLine extends Shape {

    private final Vec2 p1 = new Vec2();
    private final Vec2 p2 = new Vec2();

    public StaticLine(double px1, double py1, double px2, double py2) {
        p1.set(px1, py1);
        p2.set(px2, py2);
    }

    public Vec2 getP1() {
        return p1;
    }

    public Vec2 getP2() {
        return p2;
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
    }
    
}
