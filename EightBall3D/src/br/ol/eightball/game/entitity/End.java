package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.game.infra.Engine;
import br.ol.eightball.model.EightBallGameModel.Mode;
import br.ol.eightball.renderer3d.core.Renderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * End class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class End extends EightBallEntity {
    
    private Font font = new Font("Arial", Font.PLAIN, 50);
    
    private String _1UP_WinMessage = "1UP WIN";
    private String _2UP_CPD_WinMessage = "?";

    private boolean _1UP_WinMessageVisible = false;
    private boolean _2UP_CPU_WinMessageVisible = false;

    private final Color p1Color = new Color(32, 32, 200, 255);
    private final Color p2Color = new Color(200, 32, 32, 255);

    public End(String name, EightBallScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void updateEnd(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    if (gameModel.getMode() == Mode._1UP_2UP) {
                        _2UP_CPD_WinMessage = "2UP WIN";
                    }
                    else if (gameModel.getMode() == Mode._1UP_CPU) {
                        _2UP_CPD_WinMessage = "CPU WIN";
                    }
                    _1UP_WinMessageVisible = gameModel.isPlayerWin(0);
                    _2UP_CPU_WinMessageVisible = gameModel.isPlayerWin(1);
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 8000) {
                        break yield;
                    }
                    scene.broadcastMessage("fadeOut");
                    instructionPointer = 2;
                case 2:
                    boolean fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    instructionPointer = 3;
                case 3:
                    scene.setState(State.TITLE);
                    break yield;
            }
        }
    }

    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!visible) {
            return;
        }
        
        if (_1UP_WinMessageVisible) {
            int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, _1UP_WinMessage)) / 2;
            scene.drawText(g, font, _1UP_WinMessage, startX, 170, p1Color);
        }
        else if (_2UP_CPU_WinMessageVisible) {
            int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, _2UP_CPD_WinMessage)) / 2;
            scene.drawText(g, font, _2UP_CPD_WinMessage, startX, 170, p2Color);
        }
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        visible = scene.state == State.END;
        if (scene.state == State.END) {
            instructionPointer = 0;
        }
    }
    
}
