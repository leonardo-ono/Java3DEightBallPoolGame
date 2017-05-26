import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author leonardo
 */
public class View extends JPanel {
    
    private BufferedImage offscreen;

    private Stroke stroke = new BasicStroke(3);
    private Color pocketedColor = new Color(150, 20, 20);
    
    public View() {
        offscreen = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(offscreen.createGraphics());
        g.drawImage(offscreen, 0, 0, 800, 600, 0, 0, 400, 300, null);
    }
    
    private void draw(Graphics2D g) {
        g.drawLine(0, 0, getWidth(), getHeight());
        
        Font font = new Font("Arial", Font.BOLD, 12);
        g.setFont(font);
        
        for (int n=1; n<16; n++) {
            drawBall(g, n, 25 * n, 150);
            drawPocketed(g, 25 * n, 150);
        }

        for (int n=16; n<18; n++) {
            drawBall(g, n, 25 * (n - 15), 200);
        }
        
    }
    
    private void drawBall(Graphics g, int number, int x, int y) {
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

    private void drawPocketed(Graphics2D g, int x, int y) {
        int size = 17;
        int offset = size / 2;
        
        g.setColor(pocketedColor);
        Stroke originalStroke = g.getStroke();
        g.setStroke(stroke);
        g.drawLine(x - offset, y - offset, x + 17 - offset, y + 17 - offset);
        g.drawLine(x + 17 - offset, y - offset, x - offset, y + 17 - offset);
        g.setStroke(originalStroke);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setTitle("Test");
                frame.getContentPane().add(new View());
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setVisible(true);
            }
        });
    }
    
}
