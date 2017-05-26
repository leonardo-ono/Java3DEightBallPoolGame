package br.ol.eightball.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Eight Ball AI class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class EightBallAI extends EightBallGameModel {
    
    public static enum Level { EASY, NORMAL, HARD };
    private Level level = Level.NORMAL;
    private EightBallGameModel gameModel;
    private boolean processing;
    private Double resultAngle;
    private Double resultingImpulseIntensity;
    
    private final List<Double> angles = new ArrayList<Double>();
    
    public EightBallAI(EightBallGameModel gameModel) {
        this.gameModel = gameModel;
        for (int a=0; a<180; a++) {
            angles.add(Math.toRadians(a * 2));
        }
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean isProcessing() {
        return processing;
    }

    public boolean isResultAngleAvailable() {
        return resultAngle != null;
    }
    
    public Double getResultAngle() {
        Double ret = resultAngle;
        int error = 0;
        switch (level) {
            case EASY:
                error = (int) (7 * Math.random()) - 3;
                break;
            case NORMAL:
                error = (int) (5 * Math.random()) - 2;
                break;
            case HARD:
                error = 0;
                break;
        }
        //System.out.println(error);
        if (ret != null) {
            ret = ret + error * Math.toRadians(0.1);
        }
        resultAngle = null;
        return ret;
    }

    public Double getResultingImpulseIntensity() {
        return resultingImpulseIntensity;
    }
    
    public void play() {
        if (processing) {
            return;
        }
        process();
    }
    
    private void process() {
        resultAngle = null;
        processing = true;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Double notSoBadResult = null;
                Collections.shuffle(angles);
                for (int a=0; a<180; a++) {
                    resetInitialState();
                    turnPocketedBalls.clear();
                    firstHitByWhiteBall = null;
                    
                    double arad = angles.get(a);
                    double dx = Math.cos(arad);
                    double dy = Math.sin(arad);
                    resultingImpulseIntensity = 200 + 100 * Math.random();
                    shot(dx, dy, resultingImpulseIntensity);

                    while (isBallsMoving()) {
                        physics2D.update();
                    }
                    
                    if (notSoBadResult == null && !isWrongFirstHitByWhiteBall()) {
                        notSoBadResult = arad;
                    }

                    switch (gameModel.getState()) {
                        case BREAK:
                        case OPEN:
                            if (!turnPocketedBalls.isEmpty() && !isBallPocketedThisTurn(16) && !isBallPocketedThisTurn(8)) {
                                //System.out.println("AI pocketed ball = " + turnPocketedBalls.get(0).getId() + " model=" + physics2D);
                                resultAngle = arad;
                                processing = false;
                                return;
                            }
                            break;
                        case TURN:
                            if (turnPocketedBalls.isEmpty() || isWrongFirstHitByWhiteBall() || isBallPocketedThisTurn(16)
                                    || (!gameModel.isRemainingJust8Ball(gameModel.getCurrentPlayer()) && isBallPocketedThisTurn(8))) {
                                // ignore
                            }
                            else if ((isBallPocketedThisTurnAllSolid() && gameModel.playersTarget[gameModel.getCurrentPlayer()] == 1) 
                                    || (isBallPocketedThisTurnAllStripes() && gameModel.playersTarget[gameModel.getCurrentPlayer()] == 2)
                                    || (gameModel.isRemainingJust8Ball(gameModel.getCurrentPlayer()) && isBallPocketedThisTurn(8))) {
                                resultAngle = arad;
                                processing = false;
                                return;
                            }
                            break;
                    }
                }
                if (notSoBadResult != null) {
                    resultAngle = notSoBadResult;
                }
                else {
                    resultAngle = (2 * Math.PI) * Math.random();
                }
                processing = false;
            }
        });
        thread.start();
    }
    
    private void resetInitialState() {
        //System.out.println(gameModel.balls);
        //System.out.println(balls);
        
        whiteBall.set(gameModel.whiteBall);
        for (BallModel ballModel : gameModel.balls) {
            int id = ballModel.getId();
            getBall(id).set(ballModel);
        }
    }

    @Override
    public boolean isWrongFirstHitByWhiteBall() {
        if (firstHitByWhiteBall == null) {
            return true;
        }
        else if (!gameModel.isRemainingJust8Ball(gameModel.getCurrentPlayer()) && firstHitByWhiteBall.getId() == 8) {
            return true;
        }
        else if (gameModel.playersTarget[gameModel.getCurrentPlayer()] == 1 && firstHitByWhiteBall.getId() > 8) {
            return true;
        }
        else if (gameModel.playersTarget[gameModel.getCurrentPlayer()] == 2 && firstHitByWhiteBall.getId() < 8) {
            return true;
        }
        return false;
    }
    
}
