package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.renderer3d.core.Renderer;
import br.ol.eightball.renderer3d.parser.wavefront.WavefrontParser;

/**
 * PoolTable class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class PoolTable extends EightBallEntity {

    public PoolTable(String name, EightBallScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/table.obj", 100);
        setVisible(false);
    }
    
    @Override
    public void update(Renderer renderer) {
        transform.setIdentity();
        //transform.translate(0, -400, -10000);
        //transform.rotateX(Time.delta * 0.00000000005);
        //transform.rotateY(Time.delta * 0.00000000004);
        //transform.rotateZ(Time.delta * 0.00000000006);
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        setVisible(scene.getState() == EightBallScene.State.TITLE
            || scene.getState() == EightBallScene.State.GAME_START_OPTIONS
            || scene.getState() == EightBallScene.State.GAME_START_PREPARATIONS
            || scene.getState() == EightBallScene.State.PLAYING_1
            || scene.getState() == EightBallScene.State.PLAYING_2
            || scene.getState() == EightBallScene.State.END);
    }
    
}
