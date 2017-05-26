package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.game.infra.Mouse;
import br.ol.eightball.model.EightBallAI.Level;
import br.ol.eightball.model.EightBallGameModel.Mode;
import br.ol.eightball.renderer3d.core.Renderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * GuiButton class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class GuiButton extends EightBallEntity {
    
    // 1 = 1UP vs 2UP   2 = 1UP vs CPU
    // 3 = 1UP   4 = (2UP or CPU)
    // 5 = EASY   6 = NORMAL   7 = HARD
    // 8 = PLAY!
    private final int id;
    
    private final String[] labels = {
        "1UP vs 2UP", "1UP vs CPU",
        "1UP", "2UP", 
        "EASY", "NORMAL", "HARD",
        "PLAY!", "CPU"
    };
    
    private final Point[] positions = {
        new Point(120, 90), new Point(220, 90),
        new Point(120, 130), new Point(220, 130),
        new Point(120, 170), new Point(220, 170), new Point(320, 170),
        new Point(190, 240)
    };

    private final boolean[] mustBlinkWhenSelected = {
        false, false,
        false, false, 
        false, false, false,
        true
    };
    
    private String text;
    private final int x;
    private final int y;
    private final Rectangle rectangle = new Rectangle();
    
    private boolean selected;
    private boolean isOnMouseOver;
    private boolean blinkBorder;
    private final boolean blinkWhenSelected;
    private boolean enabled = true;
    
    private static final Color onMouseOverColor = new Color(255, 255, 255, 150);
    
    public GuiButton(String name, EightBallScene scene, int id) {
        super(name, scene);
        this.id = id;
        this.text = labels[id - 1];
        this.x = positions[id - 1].x;
        this.y = positions[id - 1].y;
        this.blinkWhenSelected = mustBlinkWhenSelected[id - 1];
    }

    @Override
    public void init() throws Exception {
        setVisible(false);
    }

    @Override
    public void updateGameStartOptions(Renderer renderer) {
        transform.setIdentity();
        isOnMouseOver = rectangle.contains(Mouse.sx, Mouse.sy);
        
        blinkBorder = (int) (System.nanoTime() * 0.0000000025) % 2 == 0;
        blinkBorder = !blinkWhenSelected ? true : blinkBorder;
        
        if (isOnMouseOver && Mouse.pressed1 && !Mouse.pressedConsumed1) {
            Mouse.pressedConsumed1 = true;
            switch (id) {
                case 1: gameModel.setMode(Mode._1UP_2UP); break;
                case 2: gameModel.setMode(Mode._1UP_CPU); break;
                case 3: gameModel.setBreakPlayer(0); break;
                case 4: gameModel.setBreakPlayer(1); break;
                case 5: ai.setLevel(Level.EASY); break;
                case 6: ai.setLevel(Level.NORMAL); break;
                case 7: ai.setLevel(Level.HARD); break;
                case 8: 
                    scene.setState(State.GAME_START_PREPARATIONS);
                    break;
            }
        }
        
        switch (id) {
            case 1: selected = gameModel.getMode() == Mode._1UP_2UP; break;
            case 2: selected = gameModel.getMode() == Mode._1UP_CPU; break;
            case 3: selected = gameModel.getBreakPlayer() == 0; break;
            case 4: 
                selected = gameModel.getBreakPlayer() == 1; 
                text = gameModel.getMode() == Mode._1UP_2UP ? labels[3] : labels[8];
                break;
            case 5: selected = ai.getLevel() == Level.EASY; break;
            case 6: selected = ai.getLevel() == Level.NORMAL; break;
            case 7: selected = ai.getLevel() == Level.HARD; break;
            case 8: selected = true; break;
        }
        
        if (id > 4 && id < 8) {
            enabled = gameModel.getMode() == Mode._1UP_CPU;
        }
    }

    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        Font font = EightBallScene.DEFAULT_FONT;
        int textWidth = scene.getTextWidth(g, font, text);
        int textHeight = g.getFontMetrics(font).getHeight();
        rectangle.setBounds(x - 10, y - textHeight - 2, textWidth + 20, textHeight + 10);
        if (!isVisible() || !enabled) {
            return;
        }
        if (isOnMouseOver) {
            g.setColor(onMouseOverColor);
            g.fill(rectangle);
        }
        scene.drawText(g, font, text, x, y, Color.BLACK);
        if (selected && blinkBorder) {
            scene.drawRect(g, rectangle.x, rectangle.y, rectangle.width, rectangle.height, Color.BLACK);
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
