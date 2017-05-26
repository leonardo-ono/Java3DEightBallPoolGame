package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.infra.Engine;
import br.ol.eightball.renderer3d.core.Renderer;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Processing class.
 * 
 * Shows when ball is still moving or AI is processing
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Processing extends EightBallEntity {
    
    private final AffineTransform rotation = new AffineTransform();
    private static BufferedImage image;
    
    private double angle;
    private double angle30Degrees = Math.toRadians(30);
    
    private double pivotX;
    private double pivotY;
    
    public Processing(String name, EightBallScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        image = ImageIO.read(getClass().getResourceAsStream("/res/processing.png"));
        pivotX = Engine.SCREEN_WIDTH - 17;
        pivotY = Engine.SCREEN_HEIGHT - 30;
    }

    @Override
    public void updatePlaying1(Renderer renderer) {
        updateRotating();
    }
    
    @Override
    public void updatePlaying2(Renderer renderer) {
        updateRotating();
    }
    
    private void updateRotating() {
        setVisible(ai.isProcessing() || gameModel.isBallsMoving());
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 50) {
                        break yield;
                    }
                    angle -= angle30Degrees;
                    instructionPointer = 0;
                    break yield;
            }
        }

        rotation.setToIdentity();
        rotation.translate(pivotX, pivotY);
        rotation.rotate(angle);
        rotation.translate(-8, -8);
    }
    
    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!visible) {
            return;
        }
        g.drawImage(image, rotation, null);
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        visible = false;
        instructionPointer = 0;
    }

}
