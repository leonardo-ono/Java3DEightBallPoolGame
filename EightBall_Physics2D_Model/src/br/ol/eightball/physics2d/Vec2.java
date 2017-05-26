package br.ol.eightball.physics2d;

/**
 * Vec2 class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Vec2 {
    
    private double x, y;

    public Vec2() {
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
        
        if (Double.isNaN(x) || Double.isNaN(y)) {
            System.out.println("");
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        if (Double.isNaN(x) || Double.isNaN(y)) {
            System.out.println("");
        }
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        if (Double.isNaN(x) || Double.isNaN(y)) {
            System.out.println("");
        }
    }

    public void scale(double d) {
        this.x *= d;
        this.y *= d;
    }
    
    public void add(Vec2 v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void sub(Vec2 v) {
        this.x -= v.x;
        this.y -= v.y;
    }
    
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }
    
    public void normalize() {
        double length = getLength();
        if (length == 0) {
            return;
        }
        scale(1 / length);
    }

    public double dot(Vec2 v) {
        return x * v.x + y * v.y;
    }

    public void setPerp() {
        set(-y, x);
    }
    
    public Vec2 perp() {
        return new Vec2(-y, x);
    }
    
    private static final Vec2 auxPerpVec = new Vec2();
    
    public double perpDot(Vec2 v) {
        auxPerpVec.set(this);
        auxPerpVec.setPerp();
        return auxPerpVec.dot(v);
    }
    
    public double cross(Vec2 v) {
        return x * v.y - y * v.x;
    }

    @Override
    public String toString() {
        return "Vec2{" + "x=" + x + ", y=" + y + '}';
    }

}
