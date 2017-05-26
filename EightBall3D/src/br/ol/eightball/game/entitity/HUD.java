package br.ol.eightball.game.entitity;

import br.ol.eightball.game.BallIconDrawer;
import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.game.infra.Engine;
import static br.ol.eightball.model.EightBallGameModel.MAX_IMPULSE_LENGTH;
import br.ol.eightball.model.EightBallGameModel.Mode;
import br.ol.eightball.renderer3d.core.Renderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * HUD class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class HUD extends EightBallEntity {
    
    private final Font font = new Font("Arial", Font.PLAIN, 30);
    private final Font font2 = new Font("Arial", Font.PLAIN, 14);
    
    private Color p1Color = new Color(32, 32, 200, 255);
    private Color p2Color = new Color(200, 32, 32, 255);
    private boolean p1Visible, p2Visible;
    
    private String label1UP = "1UP";
    private String label2UP = "";
    
    public HUD(String name, EightBallScene scene) {
        super(name, scene);
    }

    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!visible) {
            return;
        }

        String playerTarget = gameModel.getPlayerTarget(0);
        int startX = 20;
        scene.drawText(g, font2, playerTarget, startX, 70, p1Color);
        if (p1Visible) {
            startX = 20;
            scene.drawText(g, font, label1UP, startX, 50, p1Color);
        }

        playerTarget = gameModel.getPlayerTarget(1);
        startX = Engine.SCREEN_WIDTH - scene.getTextWidth(g, font2, playerTarget) - 20;
        scene.drawText(g, font2, playerTarget, startX, 70, p2Color);
        startX = Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, label2UP) - 20;
        if (p2Visible) {
            scene.drawText(g, font, label2UP, startX, 50, p2Color);
        }
        drawIconBalls(g, 0, scene.getTextWidth(g, font, label1UP) + 30);
        drawIconBalls(g, 1, startX - 118);
        drawShotForce(g);
    }
    
    private void drawShotForce(Graphics2D g) {
        if (gameModel.getImpulseLength() > 0) {
            scene.drawRect(g, 10, 278, (int) (MAX_IMPULSE_LENGTH * 0.5) + 4, 13, Color.BLACK);
            g.setColor(Color.BLUE);
            g.fillRect(13, 280, (int) (gameModel.getImpulseLength() * 0.5), 10);
        }        
    }
    
    private void drawIconBalls(Graphics2D g, int player, int offsetX) {
        int playerTarget = gameModel.getPlayerTargetInt(player);
        int startN = playerTarget == 1 ? 1 : 9;
                
        for (int n = startN; n < startN + 4; n++) {
            int n2 = playerTarget != 0 ? n : 17;
            int bx = offsetX + 22 * (n - startN + 1);
            BallIconDrawer.drawBall(g, n2, bx, 20);
            if (gameModel.isBallPocketed(n2)) {
                BallIconDrawer.drawPocketed(g, bx, 20);
            }
        }
        for (int n = startN + 4; n < startN + 7; n++) {
            int n2 = playerTarget != 0 ? n : 17;
            int bx = offsetX + 22 * (n - startN - 3) + 8;
            BallIconDrawer.drawBall(g, n2, bx, 40);
            if (gameModel.isBallPocketed(n2)) {
                BallIconDrawer.drawPocketed(g, bx, 40);
            }
        }
    }

    @Override
    public void updatePlaying1(Renderer renderer) {
        p1Visible = (int) (System.nanoTime() * 0.0000000025) % 2 == 0;
        p2Visible = true;
    }

    @Override
    public void updatePlaying2(Renderer renderer) {
        p1Visible = true;
        p2Visible = (int) (System.nanoTime() * 0.0000000025) % 2 == 0;
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        if (scene.getState() == State.END) {
            setVisible(false);
        }
    }

    public void showHUD() {
        label2UP = gameModel.getMode() == Mode._1UP_CPU ? "CPU" : "2UP";
        setVisible(true);
        p1Visible = p2Visible = true;
    }
    
}
