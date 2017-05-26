package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.math.Mat4;
import br.ol.eightball.math.Quaternion;
import br.ol.eightball.math.Vec4;
import br.ol.eightball.model.BallModel;
import br.ol.eightball.renderer3d.core.Image;
import br.ol.eightball.renderer3d.core.Renderer;
import br.ol.eightball.renderer3d.parser.wavefront.Obj;
import br.ol.eightball.renderer3d.parser.wavefront.WavefrontParser;

/**
 * Ball3D class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Ball3D extends EightBallEntity {
    
    private int id;
    private BallModel ball2D; // 2D physics ball
    
    public Mat4 rotationMatrix = new Mat4();
    public Quaternion accumulatedRotation = new Quaternion(0, new Vec4(1, 0, 0, 0));
    
    public Vec4 rotationDirection = new Vec4(1, 0, 0, 0);
    public Quaternion rotation = new Quaternion(Math.toRadians(1), rotationDirection);
    
    public Ball3D(int id, EightBallScene scene) {
        super("ball_" + id, scene);
        this.id = id;
        ball2D = gameModel.getBall(id);
        rotationDirection.set(10 * Math.random() - 5, 10 * Math.random() - 5, 10 * Math.random() - 5, 0);
        rotationDirection.normalize();
        accumulatedRotation.set(360 * Math.random(), rotationDirection);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/ball.obj", 80);
        setTexture("/res/" + id + ".gif");
        setVisible(false);
    }

    private void setTexture(String textureResource) {
        for (Obj obj : mesh) {
            obj.material.map_kd = new Image(textureResource);
        }
    }
        
    @Override
    public void update(Renderer renderer) {
        setVisible(ball2D.isVisible() && scene.getState() != EightBallScene.State.INITIALIZING && scene.getState() != EightBallScene.State.OL_PRESENTS);

        transform.setIdentity();
        transform.translate(ball2D.getPosition().getX(), 9, ball2D.getPosition().getY());
        
        rotationDirection.set(ball2D.getVelocity().getY(), 0.01, -ball2D.getVelocity().getX(), 0);
        rotationDirection.normalize();
        
        double angle = ball2D.getVelocity().getLength() * 0.003;
        
        rotation.set(angle, rotationDirection);
        rotation.multiply(accumulatedRotation, accumulatedRotation);
        
        accumulatedRotation.convertToMatrix(rotationMatrix);
        transform.getMatrix().multiply(rotationMatrix);
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        setVisible((scene.getState() == State.TITLE
            || scene.getState() == State.GAME_START_OPTIONS
            || scene.getState() == State.GAME_START_PREPARATIONS
            || scene.getState() == State.PLAYING_1
            || scene.getState() == State.PLAYING_2
            || scene.getState() == State.END) && !gameModel.isBallPocketed(id));
    }
    
}
