package br.ol.eightball.model;

import br.ol.eightball.model.EightBallGameModel.State;

/**
 * Eight Ball Model Listener interface.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public interface EightBallModelListener {
    
    public void ballPocketed(BallModel ballModel);
    public void stateChanged(State newState);
    
}
