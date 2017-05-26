package br.ol.eightball.game.entitity;

import br.ol.eightball.game.EightBallEntity;
import br.ol.eightball.game.EightBallScene;
import br.ol.eightball.game.EightBallScene.State;
import br.ol.eightball.game.infra.Mouse;
import br.ol.eightball.game.infra.Time;
import br.ol.eightball.math.Line;
import br.ol.eightball.math.Plane;
import br.ol.eightball.math.Vec4;
import br.ol.eightball.model.EightBallGameModel;
import br.ol.eightball.model.EightBallGameModel.Mode;
import br.ol.eightball.model.PoolTableModel;
import br.ol.eightball.physics2d.Physics2DTime;
import br.ol.eightball.physics2d.Vec2;
import br.ol.eightball.renderer3d.core.Renderer;
import br.ol.eightball.renderer3d.parser.wavefront.WavefrontParser;

/**
 * Cursor class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Cursor extends EightBallEntity {
    
    private final Plane plane = new Plane();
    private final Line ray = new Line();
    private final Vec4 intersectionPoint = new Vec4();
    private final Camera camera;

    private final Vec2 impulse = new Vec2();
    private final Vec2 mouseEyeSpacePosition = new Vec2();
    private final Vec2 mouseCorrectedPosition = new Vec2();
    
    private double angle;
    private double scale;
    
    
    
    public Cursor(String name, EightBallScene scene, Camera camera) {
        super(name, scene);
        this.camera = camera;
        setVisible(true);
    }

    @Override
    public void init() throws Exception {
        mesh = WavefrontParser.load("/res/cursor.obj", 120);
        setVisible(false);
    }
    
    private void updateCursorPosition(Renderer renderer) {
        // set plane and line in eye-space
        plane.set(0, 1, 0, 0, 0, 0);
        renderer.getViewTranform().getMatrix().multiply(plane.normal);
        renderer.getViewTranform().getMatrix().multiply(plane.p);

        double z = 1 / renderer.getProjectionTranform().getMatrix().m32;
        ray.set(0, 0, 0, Mouse.x, Mouse.y, z);
        
        // find ray (mouse) and plane (pool table) intersection point in eye-space
        Vec4 ip = plane.getIntersectionPoint(ray);
        
        if (ip == null) {
            return;
        }
        
        intersectionPoint.set(ip);
        
        // convert eye-space intersection point to world space
        camera.getInvertTransform().getMatrix().multiply(intersectionPoint);
        
        transform.setIdentity();
        transform.translate(intersectionPoint.x, intersectionPoint.y + 2, intersectionPoint.z);
        transform.rotateY(angle);
        transform.scale(scale, scale, scale);
        
        setVisible(PoolTableModel.insideTable.contains(intersectionPoint.x, intersectionPoint.z) 
                && !gameModel.isBallsMoving());
        
        angle += Time.delta * 0.000000001;
        scale = 1.4 + 0.2 * Math.sin(System.nanoTime() * 0.000000003);
    }
    
    @Override
    public void updatePlaying1(Renderer renderer) {
        updatePlayerHuman(renderer);
    }

    @Override
    public void updatePlaying2(Renderer renderer) {
        if (gameModel.getMode() == Mode._1UP_2UP) {
            updatePlayerHuman(renderer);
        }
        else if (gameModel.getMode() == Mode._1UP_CPU) {
            updatePlayerCPU(renderer);
        }
    }

    private void updatePlayerHuman(Renderer renderer) {
        if (gameModel.getImpulseLength() == 0) {
            updateCursorPosition(renderer);
        }
        
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    gameModel.resetWhiteBall();
                    instructionPointer = 10;
                case 10:
                    if (Mouse.pressed1 && isVisible()) {
                        Mouse.pressedConsumed1 = true;
                        impulse.set(intersectionPoint.x, intersectionPoint.z);
                        impulse.sub(gameModel.getWhiteBall().getPosition());
                        impulse.normalize();
                        gameModel.setImpulseLength(0);
                        instructionPointer = 11;
                    }
                    break yield;
                case 11:
                    gameModel.incImpulseLength(Physics2DTime.getDelta() * 0.0000005);
                    if (gameModel.getImpulseLength() > EightBallGameModel.MAX_IMPULSE_LENGTH) {
                        gameModel.setImpulseLength(EightBallGameModel.MAX_IMPULSE_LENGTH);
                        instructionPointer = 12;
                    }
                    if (!Mouse.pressed1) {
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 13;
                    }
                    break yield;
                case 12:
                    gameModel.incImpulseLength(-Physics2DTime.getDelta() * 0.0000005);
                    if (gameModel.getImpulseLength() < 5) {
                        gameModel.setImpulseLength(5);
                        instructionPointer = 11;
                    }
                    if (!Mouse.pressed1) {
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 13;
                    }
                    break yield;
                case 13:
                    if (System.currentTimeMillis() - waitTime < 200) {
                        break yield;
                    }
                    gameModel.shot(impulse.getX(), impulse.getY(), gameModel.getImpulseLength());
                    gameModel.setImpulseLength(0);
                    instructionPointer = 1;
                case 1:
                    if (!gameModel.isBallsMoving()) {
                        instructionPointer = 2;
                    }
                    break yield;
                case 2:
                    boolean stateChanged = gameModel.evaluateTurn();
                    if (gameModel.isGameOver()) {
                        scene.setState(State.END);
                        break yield;
                    }
                    instructionPointer = 3;
                case 3:
                    if (scene.getState() == State.PLAYING_1 && gameModel.getCurrentPlayer() == 1) {
                        scene.setState(State.PLAYING_2);
                    }
                    else if (scene.getState() == State.PLAYING_2 && gameModel.getCurrentPlayer() == 0) {
                        scene.setState(State.PLAYING_1);
                    }
                    else {
                        instructionPointer = 0;
                    }
                    break yield;
            }
        }
    }

    private void updatePlayerCPU(Renderer renderer) {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    gameModel.resetWhiteBall();
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    ai.play();
                    instructionPointer = 2;
                    break yield;
                case 2:
                    if (ai.isProcessing()) {
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 10;
                case 10:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    Double resultAngle = ai.getResultAngle();
                    double arad = resultAngle;
                    double dx = Math.cos(arad);
                    double dy = Math.sin(arad);
                    gameModel.shot(dx, dy, ai.getResultingImpulseIntensity());
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    instructionPointer = 4;
                case 4:
                    if (!gameModel.isBallsMoving()) {
                        instructionPointer = 5;
                    }
                    break yield;
                case 5:
                    boolean stateChanged = gameModel.evaluateTurn();
                    if (gameModel.isGameOver()) {
                        scene.setState(State.END);
                        break yield;
                    }
                    if (gameModel.getCurrentPlayer() == 0) {
                        scene.setState(State.PLAYING_1);
                    }
                    else {
                        instructionPointer = 0;
                    }
                    break yield;
            }
        }
    }
    
    @Override
    public void preDraw(Renderer renderer) {
        renderer.setBackfaceCullingEnabled(true);
        renderer.setShader(EightBallScene.cursorShader);
        renderer.setMatrixMode(Renderer.MatrixMode.MODEL);
        renderer.setModelTransform(transform);
    }
    
    @Override
    public void draw(Renderer renderer) {
        if (scene.getState() == EightBallScene.State.PLAYING_2 
                && gameModel.getMode() == Mode._1UP_CPU) {
            return;
        }
        super.draw(renderer);
    }
    
    // broadcast messages

    @Override
    public void stateChanged() {
        setVisible(scene.getState() == EightBallScene.State.PLAYING_1
            || scene.getState() == EightBallScene.State.PLAYING_2);
        if (visible) {
            instructionPointer = 0;
        }
    }
    
}
