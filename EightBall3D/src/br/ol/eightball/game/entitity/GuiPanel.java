package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.model.EightBallGameModel.Mode;
import br.ol.eightball.renderer3d.core.Renderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * GuiPanel class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class GuiPanel extends EightBallEntity {
    
    private final Rectangle rectangle = new Rectangle();
    private static final Color backgroundColor = new Color(0, 0, 0, 150);
    
    public GuiPanel(String name, EightBallScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        setVisible(false);
        rectangle.setBounds(40, 50, 340, 150);
    }

    @Override
    public void updateGameStartOptions(Renderer renderer) {
        transform.setIdentity();
    }

    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!isVisible()) {
            return;
        }
        g.setColor(backgroundColor);
        g.fill(rectangle);
        scene.drawRect(g, rectangle.x, rectangle.y, rectangle.width, rectangle.height, Color.BLACK);
        Font font = EightBallScene.DEFAULT_FONT;
        scene.drawText(g, font, "GAME OPTIONS:", 40, 44, Color.DARK_GRAY);
        scene.drawText(g, font, " MODE:", 50, 90, Color.DARK_GRAY);
        scene.drawText(g, font, "BREAK:", 50, 130, Color.DARK_GRAY);
        if (gameModel.getMode() == Mode._1UP_CPU) {
            scene.drawText(g, font, "LEVEL:", 50, 170, Color.DARK_GRAY);
        }
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        setVisible(scene.getState() == State.GAME_START_OPTIONS
            || scene.getState() == State.GAME_START_PREPARATIONS);
    }

    public void hideGui() {
        setVisible(false);
    }
    
}
