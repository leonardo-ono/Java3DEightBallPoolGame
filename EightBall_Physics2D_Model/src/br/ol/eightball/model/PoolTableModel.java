package br.ol.eightball.model;

import br.ol.eightball.physics2d.Physics2D;
import java.awt.Rectangle;

/**
 * Pool table class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class PoolTableModel {
    
    public static Rectangle insideTable = new Rectangle(-186, -116, 372, 232);
    public static Rectangle movableArea = new Rectangle(-186 - 20, -116 - 20, 372 + 40, 232 + 40);
    
    public PoolTableModel(EightBallGameModel model) {
        createWalls(model.getPhysics2D());
    }
    
    private void createWalls(Physics2D physics2D) {
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -184, -91, -184, 91)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -211, -110, -184, -91)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -190, -134, -211, -110)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -170, -107, -190, -134)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -16, -107, -170, -107)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -19, -132, -16, -107)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 10, -132, -19, -132)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 7, -107, 10, -132)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 163, -107, 7, -107)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 185, -133, 163, -107)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 206, -109, 185, -133)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 184, -92, 206, -109)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 184, 92, 184, -92)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 209, 112, 184, 92)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 185, 136, 209, 112)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 165, 107, 185, 136)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 12, 107, 165, 107)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, 15, 132, 12, 107)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -14, 132, 15, 132)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -11, 107, -14, 132)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -165, 107, -11, 107)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -189, 134, -165, 107)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -209, 109, -189, 134)); 
        physics2D.getRigidBodies().add(new TableWallModel(physics2D, -184, 91, -209, 109)); 
    }
    
}
