package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.model.BallModel;
import br.ol.eightball.renderer3d.core.Renderer;
import br.ol.eightball.renderer3d.parser.wavefront.WavefrontParser;

/**
 * Ball3DShadow class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Ball3DShadow extends EightBallEntity {
    
    private int id;
    private BallModel ball2D; // 2D physics ball
    
    public Ball3DShadow(int id, EightBallScene scene) {
        super("ball_shadow_" + id, scene);
        this.id = id;
        ball2D = gameModel.getBall(id);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/ball_shadow.obj", 85);
        setVisible(false);
    }

    @Override
    public void update(Renderer renderer) {
        setVisible(ball2D.isVisible() && scene.getState() != EightBallScene.State.INITIALIZING && scene.getState() != EightBallScene.State.OL_PRESENTS);

        transform.setIdentity();
        transform.translate(ball2D.getPosition().getX(), 11, ball2D.getPosition().getY());
    }

    @Override
    public void preDraw(Renderer renderer) {
        renderer.setBackfaceCullingEnabled(true);
        renderer.setShader(EightBallScene.shadowShader);
        renderer.setMatrixMode(Renderer.MatrixMode.MODEL);
        renderer.setModelTransform(transform);
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        setVisible(scene.getState() == State.TITLE
            || scene.getState() == State.GAME_START_OPTIONS
            || scene.getState() == State.GAME_START_PREPARATIONS
            || scene.getState() == State.PLAYING_1
            || scene.getState() == State.PLAYING_2
            || scene.getState() == State.END);
    }
    
}
