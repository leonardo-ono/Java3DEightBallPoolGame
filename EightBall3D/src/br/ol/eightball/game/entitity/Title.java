package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.game.infra.Engine;
import br.ol.eightball.game.infra.Mouse;
import br.ol.eightball.game.infra.Time;
import br.ol.eightball.renderer3d.core.Renderer;
import br.ol.eightball.renderer3d.parser.wavefront.WavefrontParser;
import br.ol.eightball.renderer3d.shader.GouraudShaderWithTexture;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Title class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Title extends EightBallEntity {

    private final String programmedByOL = "Programmed by O.L. (c) 2017";
    private final String clickWithMouseToStart = "- Click with mouse to start -";
    private boolean clickWithMouseToStartVisible = false;
    private boolean programmedByOLVisible = false;
    private double titleY;
    private double titleAngleY;
    private double a;
    
    public Title(String name, EightBallScene scene) {
        super(name, scene);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/title.obj", 1200);
        setVisible(false);
    }
    
    @Override
    public void updateTitle(Renderer renderer) {
        transform.setIdentity();
        transform.translate(0, titleY, 0);
        transform.rotateZ(Math.cos(a) * 0.10);
        transform.rotateY(Math.sin(a) * 0.075 + titleAngleY);
        transform.rotateX(Math.cos(a) * 0.05 + Math.toRadians(-90));
        a += Time.delta * 0.000000002;
        //transform.rotateZ(Time.delta * 0.00000000006);
        
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    scene.broadcastMessage("fadeIn");
                    instructionPointer = 1;
                case 1:
                    boolean fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    gameModel.shot(1, 0, 300);
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2;
                case 2:
                    if (System.currentTimeMillis() - waitTime < 2000) {
                        break yield;
                    }
                    instructionPointer = 3;
                case 3:
                    double difTitleY = 900 - titleY; 
                    double difTitleAngleY = 0 - titleAngleY; 
                    titleY = titleY + difTitleY * Time.delta * 0.000000002;
                    titleAngleY = titleAngleY + difTitleAngleY * Time.delta * 0.000000002;
                    if (Math.abs(difTitleY) < 5) {
                        titleY = 900;
                        titleAngleY = 0;
                        instructionPointer = 4;
                    }
                    break yield;
                case 4:
                    clickWithMouseToStartVisible = (int) (System.nanoTime() * 0.0000000025) % 2 == 0;
                    if (Mouse.pressed1 && !Mouse.pressedConsumed1) {
                        Mouse.pressedConsumed1 = true;
                        clickWithMouseToStartVisible = false;
                        instructionPointer = 5;
                    }
                    break yield;
                case 5:
                    //titleY = titleY + Time.delta * 0.00001;
                    //if (titleY > 5000) {
                    //    titleY = 5000;
                        instructionPointer = 6;
                    //}
                    //break yield;
                case 6:
                    scene.setState(State.GAME_START_OPTIONS);
                    break yield;
            }
        }
    }

    @Override
    public void updateGameStartPreparations(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    scene.broadcastMessage("fadeOut");
                    instructionPointer = 1;
                case 1:
                    boolean fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    programmedByOLVisible = false;
                    gameModel.restart();
                    ((Camera) scene.camera).resetCameraAngle();
                    scene.broadcastMessage("hideGui");
                    scene.broadcastMessage("showHUD");
                    scene.broadcastMessage("fadeIn");
                    instructionPointer = 2;
                case 2:
                    fadeEffectFinished = scene.getProperty("fadeEffectFinished", Boolean.class);
                    if (!fadeEffectFinished) {
                        break yield;
                    }
                    if (gameModel.getBreakPlayer() == 0) {
                        scene.setState(State.PLAYING_1);
                    }
                    else {
                        scene.setState(State.PLAYING_2);
                    }
                    break yield;
            }
        }        
    }
    
    @Override
    public void preDraw(Renderer renderer) {
        if (!isVisible() 
                || scene.state == State.GAME_START_OPTIONS 
                || scene.state == State.GAME_START_PREPARATIONS) {
            GouraudShaderWithTexture.minIntensity = 0.5;
            return;
        }
        super.preDraw(renderer);
        renderer.getDepthBuffer().clear();
        renderer.getViewTranform().setIdentity();
        renderer.getViewTranform().translate(0, 0, -4000);
        renderer.getViewTranform().rotateY(-0.25);
        renderer.getViewTranform().rotateX(0.5);
        GouraudShaderWithTexture.minIntensity = 0.9;
    }

    @Override
    public void draw(Renderer renderer) {
        if (!isVisible() 
                || scene.state == State.GAME_START_OPTIONS 
                || scene.state == State.GAME_START_PREPARATIONS) {
            GouraudShaderWithTexture.minIntensity = 0.5;
            return;
        }
        super.draw(renderer);
    }
    
    @Override
    public void draw(Renderer renderer, Graphics2D g) {
        if (!isVisible()) {
            GouraudShaderWithTexture.minIntensity = 0.5;
            return;
        }
        Font font = EightBallScene.DEFAULT_FONT;
        if (clickWithMouseToStartVisible) {
            int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, clickWithMouseToStart)) / 2;
            scene.drawText(g, font, clickWithMouseToStart, startX, 230, Color.BLACK);
        }
        int startX = (Engine.SCREEN_WIDTH - scene.getTextWidth(g, font, programmedByOL)) / 2;
        if (programmedByOLVisible) {
            scene.drawText(g, font, programmedByOL, startX, 290, Color.BLACK);
        }
        GouraudShaderWithTexture.minIntensity = 0.5;
    }

    // broadcast messages

    @Override
    public void stateChanged() {
        setVisible(scene.state == State.TITLE
            || scene.state == State.GAME_START_OPTIONS
            || scene.state == State.GAME_START_PREPARATIONS);
        if (scene.state == State.TITLE) {
            instructionPointer = 0;
            titleY = 5000;
            titleAngleY = Math.toRadians(90);
            clickWithMouseToStartVisible = false;
            programmedByOLVisible = true;
            gameModel.restart();
        }
        else if (scene.state == State.GAME_START_PREPARATIONS) {
            instructionPointer = 0;
        }
    }
    
}
