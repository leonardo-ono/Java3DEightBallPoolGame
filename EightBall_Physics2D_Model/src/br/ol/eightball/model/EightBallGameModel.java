package br.ol.eightball.model;

import br.ol.eightball.physics2d.Circle;
import br.ol.eightball.physics2d.CollisionDetection;
import br.ol.eightball.physics2d.Contact;
import br.ol.eightball.physics2d.ContactListener;
import br.ol.eightball.physics2d.Physics2D;
import br.ol.eightball.physics2d.RigidBody;
import br.ol.eightball.physics2d.StaticLine;
import br.ol.eightball.physics2d.Vec2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Eight Ball Game Model (with 2D physics) class.
 * 
 * Basic rules from: http://www.wikihow.com/Play-8-BallModel-Pool
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class EightBallGameModel implements ContactListener {
    
    public static enum Mode { _1UP_2UP, _1UP_CPU };
    public static enum State {
        NONE, BREAK, OPEN, TURN, WIN_1UP, WIN_2UP
    };
    protected final List<EightBallModelListener> listeners = new ArrayList<EightBallModelListener>();
    protected final Physics2D physics2D = new Physics2D();
    protected Mode mode = Mode._1UP_CPU;
    protected State state = State.NONE;
    protected int breakPlayer; // 0 or 1
    protected boolean whiteBallMovable; // allow player to move white ball in the beginning of turn
    protected PoolTableModel poolTable;
    protected final List<BallModel> balls = new ArrayList<BallModel>();
    protected BallModel ball8;
    protected BallModel whiteBall;
    protected BallModel firstHitByWhiteBall;
    protected final Set<BallModel> pocketedBalls = new HashSet<BallModel>();
    protected final List<BallModel> turnPocketedBalls = new ArrayList<BallModel>();
    protected String[] players = new String[2];
    protected final int[] playersTarget = new int[2]; // 1=1~7 2=9~15
    protected int currentPlayer; // 0 or 1
    
    public static final int MAX_IMPULSE_LENGTH = 300;
    protected double impulseLength;
    
    public EightBallGameModel() {
        createAllObjects();
        physics2D.addContactListener(this);
    }

    public double getImpulseLength() {
        return impulseLength;
    }

    public void setImpulseLength(double impulseLength) {
        this.impulseLength = impulseLength;
    }

    public void incImpulseLength(double impulseLength) {
        this.impulseLength += impulseLength;
    }
    
    public void addModelListener(EightBallModelListener listener) {
        listeners.add(listener);
    }
    
    public Physics2D getPhysics2D() {
        return physics2D;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    private void createAllObjects() {
        poolTable = new PoolTableModel(this);
        for (int i=1; i<=16; i++) {
            BallModel ball = new BallModel(this, i, 10 * i, 0, 8);
            physics2D.getRigidBodies().add(ball);
            balls.add(ball);
            if (i == 8) {
                ball8 = ball;
            }
            else if (i == 16) {
                whiteBall = ball;
                balls.remove(whiteBall);
            }
        }
        restart();
    }
    
    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            for (EightBallModelListener listener : listeners) {
                listener.stateChanged(state);
            }
        }
    }

    public int getBreakPlayer() {
        return breakPlayer;
    }

    public void setBreakPlayer(int breakPlayer) {
        this.breakPlayer = breakPlayer;
    }
    
    public boolean isPlayerWin(int player) { // 0 or 1
        return (player == 0 && state == State.WIN_1UP) 
                || (player == 1 && state == State.WIN_2UP);
    }
    
    public boolean isGameOver() {
        return state == State.NONE || state == State.WIN_1UP || state == State.WIN_2UP;
    }
    
    public boolean isWhiteBallMovable() {
        return whiteBallMovable;
    }

    public PoolTableModel getPoolTable() {
        return poolTable;
    }
    
    public BallModel getBall(int id) { // 1~16 (16 = white ball)
        if (id == 16) {
            return whiteBall;
        }
        for (BallModel ball : balls) {
            if (ball.getId() == id) {
                return ball;
            }
        }
        return null;
    }
    
    public void addPocketedBall(BallModel ball) {
        pocketedBalls.add(ball);
        turnPocketedBalls.add(ball);
        // System.out.println("Pocketed " + ball.getId() + " ball ...");
        for (EightBallModelListener listener : listeners) {
            listener.ballPocketed(ball);
        }
    }

    public BallModel getWhiteBall() {
        return whiteBall;
    }
    
    public String[] getPlayers() {
        return players;
    }
    
    public String getPlayer(int id) {
        return players[id] == null ? "" : players[id];
    }
    
    public void setPlayer(int id, String name) {
        players[id] = name;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getPlayerTargetInt(int player) {
        return playersTarget[player];
    }
    
    public String getPlayerTarget(int player) {
        if (playersTarget[player] == 1) {
            return "SOLID";
        }
        else if (playersTarget[player] == 2) {
            return "STRIPES";
        }
        return "?";
    }

    // --- ContactListener implementation ---

    @Override
    public void onCollisionEnter(Contact contact) {
        if (contact.getRba() == whiteBall && contact.getRbb() instanceof BallModel && firstHitByWhiteBall == null) {
            firstHitByWhiteBall = (BallModel) contact.getRbb();
            //System.out.println("onCollisionEnter " + firstHitByWhiteBall.getId());
        }
        if (contact.getRbb() == whiteBall && contact.getRba() instanceof BallModel &&firstHitByWhiteBall == null) {
            firstHitByWhiteBall = (BallModel) contact.getRba();
            //System.out.println("onCollisionEnter " + firstHitByWhiteBall.getId());
        }
    }

    @Override
    public void onCollision(Contact contact) {
    }

    @Override
    public void onCollisionOut(Contact contact) {
    }    
    
    // ---
    
    public boolean isBallsMoving() {
        if (whiteBall.getVelocity().getX() != 0 || whiteBall.getVelocity().getY() != 0) {
            return true;
        }
        for (BallModel ball : balls) {
            if (ball.getVelocity().getX() != 0 || ball.getVelocity().getY() != 0) {
                return true;
            }
        }
        return false;
    }
    
    public void resetWhiteBall() {
        if (!whiteBall.isVisible()) {
            whiteBall.getPosition().set(-100, 0);
            whiteBall.getVelocity().set(0, 0);
            whiteBall.setVisible(true);
        }
    }
    
    public void restart() {
        state = State.BREAK;
        whiteBallMovable = true;
        currentPlayer = breakPlayer;
        playersTarget[0] = playersTarget[1] = 0;
        
        firstHitByWhiteBall = null;
        pocketedBalls.clear();
        turnPocketedBalls.clear();
        
        // triangulate balls
        Collections.shuffle(balls);
        balls.remove(ball8);
        balls.add(4, ball8);
        int n = 0;
        for (int x=0; x<5; x++) {
            for (int y=0; y<=x; y++) {
                BallModel ball = balls.get(n++);
                ball.setVisible(true);
                ball.getPosition().set(50 + x * 16, y * 16 - (x + 1) * 8);
                ball.getVelocity().set(0, 0);
            }
        }
        whiteBall.getVelocity().set(0, 0);
        whiteBall.getPosition().set(-100, 0);
    }
    
    // return state changed
    public boolean evaluateTurn() {
        boolean stateChanged = false;
        boolean changePlayer = true;
        
        // check 8 ball
        if (isBallPocketed(8)) {
            boolean leftJust8Ball = isRemainingJust8Ball(currentPlayer);
            boolean whiteBallPocketed = isBallPocketed(16);
            if (whiteBallPocketed) {
                pocketedBalls.remove(getBall(16));
            }
            
            if (currentPlayer == 0) {
                state = leftJust8Ball && !whiteBallPocketed ? State.WIN_1UP : State.WIN_2UP;
            }
            else if (currentPlayer == 1) {
                state = leftJust8Ball && !whiteBallPocketed ? State.WIN_2UP : State.WIN_1UP;
            }
            stateChanged = true;
        }
        
        // break or open_table
        else if (state == State.BREAK || state == State.OPEN) {
            
            if (isBallPocketed(16)) {
                whiteBallMovable = true; // fool
                pocketedBalls.remove(getBall(16));
                state = State.OPEN;
                stateChanged = true;
            }
            else if (isBallPocketedThisTurnAllSolid()) {
                if (currentPlayer == 0) {
                    playersTarget[0] = 1;
                    playersTarget[1] = 2;
                }
                else if (currentPlayer == 1) {
                    playersTarget[0] = 2;
                    playersTarget[1] = 1;
                }
                state = State.TURN;
                changePlayer = false;
                stateChanged = true;
            }
            else if (isBallPocketedThisTurnAllStripes()) {
                if (currentPlayer == 0) {
                    playersTarget[0] = 2;
                    playersTarget[1] = 1;
                }
                else if (currentPlayer == 1) {
                    playersTarget[0] = 1;
                    playersTarget[1] = 2;
                }
                state = State.TURN;
                changePlayer = false;
                stateChanged = true;
            }
            else {
                state = State.OPEN;
                stateChanged = true;
            }
        }
        
        // turn
        else if (state == State.TURN) {
            
            if (isBallPocketed(16)) {
                whiteBallMovable = true; // fool
                pocketedBalls.remove(getBall(16));
            }
            else if (isWrongFirstHitByWhiteBall()) {
                changePlayer = true;
            }
            else if (playersTarget[currentPlayer] == 1 && isBallPocketedThisTurnAllSolid()) {
                changePlayer = false;
            }
            else if (playersTarget[currentPlayer] == 2 && isBallPocketedThisTurnAllStripes()) {
                changePlayer = false;
            }
            
        }
        
        if (changePlayer) {
            currentPlayer = currentPlayer == 0 ? 1 : 0;
        }
        firstHitByWhiteBall = null;
        turnPocketedBalls.clear();
        return stateChanged;
    }
    
    public boolean isWrongFirstHitByWhiteBall() {
        if (firstHitByWhiteBall == null) {
            return true;
        }
        else if (!isRemainingJust8Ball(currentPlayer) && firstHitByWhiteBall.getId() == 8) {
            return true;
        }
        else if (playersTarget[currentPlayer] == 1 && firstHitByWhiteBall.getId() > 8) {
            return true;
        }
        else if (playersTarget[currentPlayer] == 2 && firstHitByWhiteBall.getId() < 8) {
            return true;
        }
        return false;
    }
    
    public boolean isBallPocketedThisTurnAllSolid() { // 1~7
        for (BallModel ball : turnPocketedBalls)  {
            if (ball.getId() > 8) {
                return false;
            }
        }
        return !turnPocketedBalls.isEmpty();
    }

    public boolean isBallPocketedThisTurnAllStripes() { // 9~15
        for (BallModel ball : turnPocketedBalls)  {
            if (ball.getId() < 8) {
                return false;
            }
        }
        return !turnPocketedBalls.isEmpty();
    }
    
    public boolean isBallPocketedThisTurn(int id) {
        BallModel ball = getBall(id);
        return turnPocketedBalls.contains(ball);
    }
    
    public boolean isBallPocketed(int id) {
        BallModel ball = getBall(id);
        return pocketedBalls.contains(ball);
    }
    
    public boolean isRemainingJust8Ball(int player) {
        if (playersTarget[player] == 0)  {
            return false;
        }
        else if (playersTarget[player] == 1)  { // 1~7
            for (int id = 1; id <= 7; id++) {
                if (!isBallPocketed(id)) {
                    return false;
                }
            }
            return true;
        }
        else if (playersTarget[player] == 2)  { // 9~15
            for (int id = 9; id <= 15; id++) {
                if (!isBallPocketed(id)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private final Vec2 impulse = new Vec2();
    public void shot(double dx, double dy, double impulseIntensity) {
        impulse.set(dx, dy);
        impulse.normalize();
        impulse.scale(impulseIntensity);
        whiteBall.applyImpulse(impulse);
        
    }
    private final BallModel provWhiteBall = new BallModel(this, 17, 0, 0, 8);
    private final Vec2 shootDirection = new Vec2();
    
    public Vec2 calculateShotHitPosition(Vec2 shootPosition) {
        shootDirection.set(shootPosition);
        shootDirection.sub(whiteBall.getPosition());
        shootDirection.normalize();
        shootDirection.scale(1);
        provWhiteBall.getPosition().set(whiteBall.getPosition());
        boolean keep = true;
        while (keep) {
            provWhiteBall.getPosition().add(shootDirection);
            // handle onCollision & onCollisionEnter 
            BallModel rb1 = provWhiteBall;
            for (RigidBody rb2 : getPhysics2D().getRigidBodies()) {
                if (rb1 != rb2 && rb1.isVisible() && rb2.isVisible() && rb2 != whiteBall) {
                    Contact contact = getPhysics2D().getContactFromCache(rb1, rb2);

                    if (rb2.getShape() instanceof Circle 
                            && CollisionDetection.checkCollisionCircleCircle(rb1, rb2, contact)) {
                        getPhysics2D().saveContactToCache(contact);
                        keep = false;
                    }
                    else if (rb2.getShape() instanceof StaticLine 
                            && CollisionDetection.checkCollisionCircleStaticLine(rb1, rb2, contact)) {
                        getPhysics2D().saveContactToCache(contact);
                        keep = false;
                    }
                }
            }
        }
        return provWhiteBall.getPosition();
    }
    
}
