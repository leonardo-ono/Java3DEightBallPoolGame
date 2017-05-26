package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.renderer3d.core.Renderer;

/**
 * Initializer class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Initializer extends EightBallEntity {

    public Initializer(String name, EightBallScene scene) {
        super(name, scene);
    }

    @Override
    public void updateInitializing(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    scene.broadcastMessage("fadeOut");
                    instructionPointer = 2;
                case 2:
                    boolean fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    scene.setState(State.OL_PRESENTS);
                    break yield;
            }
        }
    }
    
}
