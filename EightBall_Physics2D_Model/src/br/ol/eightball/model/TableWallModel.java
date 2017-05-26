package br.ol.eightball.model;

import br.ol.eightball.physics2d.Physics2D;
import br.ol.eightball.physics2d.RigidBody;
import br.ol.eightball.physics2d.StaticLine;

/**
 * Table wall class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class TableWallModel extends RigidBody {
    
    private double x1, y1, x2, y2;
    private final StaticLine staticLine;
    
    public TableWallModel(Physics2D scene, double x1, double y1, double x2, double y2) {
        super(scene, new StaticLine(x1, y1, x2, y2));
        getVelocity().set(0, 0);
        getPosition().set(0, 0);
        setMass(99999999);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        staticLine = (StaticLine) getShape();
    }

    @Override
    public void update() {
        super.update();
        // ensure stay fixed in same position
        staticLine.getP1().set(x1, y1);
        staticLine.getP2().set(x2, y2);
    }

}
