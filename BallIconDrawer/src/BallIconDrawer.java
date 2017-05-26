
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 *
 * @author leonardo
 */
public class BallIconDrawer {
    
    private final Stroke stroke = new BasicStroke(3);
    private final Color pocketedColor = new Color(150, 20, 20);

    // number 1~15 16=white 17=?
    public void drawBall(Graphics g, int number, int x, int y) {
        String n = number + "";
        int size = 17;
        g.setColor(Color.BLACK);
        g.fillOval(x - size / 2 - 2, y - size / 2 - 2, size + 4, size + 4);
        g.setColor(Color.WHITE);
        g.fillOval(x - size / 2 - 1, y - size / 2 - 1, size + 2, size + 2);
        if (number > 8 && number < 16) {
            g.setColor(Color.BLACK);
            g.fillRect(x - size / 2, y - size / 2 + 4, size + 1, size - 6);
        }
        else if (number != 16) {
            g.setColor(Color.BLACK);
            g.fillOval(x - size / 2, y - size / 2, size, size);
        }
        g.setColor(Color.WHITE);
        if (number == 17) {
            n = "?";
        }
        if (number != 16) {
            int tw = g.getFontMetrics().stringWidth(n);
            g.drawString(n, x - tw / 2 + 1, y + size / 4 + 2);
        }
    }

    public void drawPocketed(Graphics2D g, int x, int y) {
        int size = 17;
        int offset = size / 2;
        
        g.setColor(pocketedColor);
        Stroke originalStroke = g.getStroke();
        g.setStroke(stroke);
        g.drawLine(x - offset, y - offset, x + 17 - offset, y + 17 - offset);
        g.drawLine(x + 17 - offset, y - offset, x - offset, y + 17 - offset);
        g.setStroke(originalStroke);
    }
    
}
