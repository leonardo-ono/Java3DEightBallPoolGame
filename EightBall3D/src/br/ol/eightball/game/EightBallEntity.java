package br.ol.eightball.game;

import br.ol.eightball.game.infra.Entity;
import br.ol.eightball.model.EightBallAI;
import br.ol.eightball.model.EightBallGameModel;
import br.ol.eightball.renderer3d.core.Renderer;

/**
 * EightBallEntity class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class EightBallEntity extends Entity<EightBallScene> {
    
    public int instructionPointer;
    public long waitTime;
    public EightBallGameModel gameModel;
    public EightBallAI ai;
    
    public EightBallEntity(String name, EightBallScene scene) {
        super(name, scene);
        this.gameModel = scene.getGameModel();
        this.ai = scene.getAI();
    }

    @Override
    public void update(Renderer renderer) {
        update2DPhysics();
        preUpdate(renderer);
        switch(scene.state) {
            case INITIALIZING: updateInitializing(renderer); break;
            case OL_PRESENTS: updateOlPresents(renderer); break;
            case TITLE: updateTitle(renderer); break;
            case GAME_START_OPTIONS: updateGameStartOptions(renderer); break;
            case GAME_START_PREPARATIONS: updateGameStartPreparations(renderer); break;
            case PLAYING_1: updatePlaying1(renderer); break;
            case PLAYING_2: updatePlaying2(renderer); break;
            case END: updateEnd(renderer); break;
        }
        posUpdate(renderer);
    }
    
    public void update2DPhysics() {
    }
    
    public void preUpdate(Renderer renderer) {
    }

    public void updateInitializing(Renderer renderer) {
    }

    public void updateOlPresents(Renderer renderer) {
    }

    public void updateTitle(Renderer renderer) {
    }

    public void updateGameStartOptions(Renderer renderer) {
    }

    public void updateGameStartPreparations(Renderer renderer) {
    }
    
    public void updatePlaying1(Renderer renderer) {
    }

    public void updatePlaying2(Renderer renderer) {
    }

    public void updateEnd(Renderer renderer) {
    }

    public void posUpdate(Renderer renderer) {
    }
    
    // broadcast messages
    
    public void stateChanged() {
    }


}
