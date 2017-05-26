package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.infra.Mouse;
import br.ol.eightball.game.infra.Time;
import br.ol.eightball.math.MathUtil;
import br.ol.eightball.renderer3d.core.Renderer;
import br.ol.eightball.renderer3d.core.Transform;

/**
 * Camera class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Camera extends EightBallEntity {
    
    public double targetAngleX;
    public double targetAngleY;

    public double angleX;
    public double angleY;

    public double mouseAngleX;
    public double mouseAngleY;

    public double mousePressedX;
    public double mousePressedY;
    public double mousePressedAngleX;
    public double mousePressedAngleY;
    
    public double vp;
    
    public boolean mouseEnabled = false;
    
    public Transform invertTransform = new Transform();
    
    public Camera(String name, EightBallScene scene) {
        super(name, scene);
    }
        
    @Override
    public void init() throws Exception {
        transform.setIdentity();
    }


    @Override
    public void updateOlPresents(Renderer renderer) {
        transform.setIdentity();
        transform.translate(0, 0, -600);
    }

    private void updateAutoRotating() {
        mouseAngleX = -0.6;
        mouseAngleY += Time.delta * 0.00000000025;
        transform.setIdentity();
        transform.translate(0, 0, -500);
        transform.rotateX(mouseAngleX);
        transform.rotateY(-mouseAngleY);
    }
    
    @Override
    public void updateTitle(Renderer renderer) {
        updateAutoRotating();
    }

    @Override
    public void updateGameStartOptions(Renderer renderer) {
        updateAutoRotating();
    }

    @Override
    public void updateGameStartPreparations(Renderer renderer) {
        // dragCameraRotation();
    }

    @Override
    public void updatePlaying1(Renderer renderer) {
        dragCameraRotation();
    }
    
    @Override
    public void updatePlaying2(Renderer renderer) {
        dragCameraRotation();
    }
    
    // change camera view using mouse dragging with right button
    private void dragCameraRotation() {
        if (Mouse.pressed2 && !Mouse.pressedConsumed2) {
            Mouse.pressedConsumed2 = true;
            mousePressedX = Mouse.x;
            mousePressedY = Mouse.y;
            mousePressedAngleX = mouseAngleX;
            mousePressedAngleY = mouseAngleY;
            // System.out.println("---> pressed1 ... " + mouseAngleX + ", " + mouseAngleY);
        }
        else if (Mouse.pressed2) {
            double difX = (Mouse.x - mousePressedX);
            double difY = (Mouse.y - mousePressedY);
            targetAngleX = mousePressedAngleX + Math.toRadians(difY * 0.5);
            targetAngleY = mousePressedAngleY + Math.toRadians(difX * 0.5);
            // System.out.println("---> dragging ... " + difX + ", " + difY + " mouse: " + Mouse.x + ", " + Mouse.y);
        }
        
        mouseAngleX = mouseAngleX + (targetAngleX - mouseAngleX) * 0.1;
        mouseAngleY = mouseAngleY + (targetAngleY - mouseAngleY) * 0.1;
        
        mouseAngleX = MathUtil.clamp(mouseAngleX, -0.8388, -0.2978);
        
        transform.setIdentity();
        transform.translate(0, 0, -500);
        transform.rotateX(mouseAngleX);
        transform.rotateY(-mouseAngleY);
        
        // System.out.println("mouseAngleX = " + mouseAngleX + " mouseAngleY = " + mouseAngleY);
    }

    @Override
    public void updateEnd(Renderer renderer) {
    }
    
    public Transform getInvertTransform() {
        invertTransform.setIdentity();
        invertTransform.rotateY(mouseAngleY);
        invertTransform.rotateX(-mouseAngleX);
        invertTransform.translate(0, 0, 500);
        return invertTransform;
    }
    
    public void setTargetAngle(double ax, double ay, double vp) {
        targetAngleX = ax;
        targetAngleY = ay;
        this.vp = vp;
    }
    
    public boolean isRotationToTargetAngleFinished() {
        return Math.abs(targetAngleX + mouseAngleX - angleX) < 0.01 
                && Math.abs(targetAngleY + mouseAngleY - angleY) < 0.01;
    }
    
    public void resetCameraAngle() {
        targetAngleX = mouseAngleX = -0.60056;
        targetAngleY = mouseAngleY = 0.72384;
        dragCameraRotation();
    }
    
}
