import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Mypanel extends JPanel implements MouseListener, MouseMotionListener {
    private final int width = 700;
    private final int height = 700;

    double centerX = -0.5;
    double centerY = 0.0;
    double zoom = 1.0;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    Point lastDragPoint; // Stores the last mouse position during drag

    Mypanel() {
        this.setPreferredSize(new Dimension(width, height));
        generateBlackImage();

        setFocusable(true);
        requestFocusInWindow();

        addMouseListener(this);
        addMouseMotionListener(this);

        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            double factor = (notches > 0) ? 0.5 : 2.0;
            zoom *= factor;
            generateBlackImage();
            repaint();
        });
    }

    public double map(double value, double inMin, double inMax, double outMin, double outMax) {
        return outMin + (value - inMin) * (outMax - outMin) / (inMax - inMin);
    }

    public void generateBlackImage() {
        double scale = 1.5 / zoom;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double real = centerX + map(x, 0, width, -scale, scale);
                double imag = centerY + map(y, 0, height, scale, -scale);

                int count = (int) mandelbrot(real, imag);

                if (count >= 500) {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    float hue = (float) (Math.log(count + 1) / Math.log(600));
                    Color color = Color.getHSBColor(hue, 1.0f, 1.0f);
                    image.setRGB(x, y, color.getRGB());
                }
            }
        }
    }

    public double mandelbrot(double Ca, double Cb) {
        int count = 0;
        double Za = Ca;
        double Zb = Cb;

        while ((Za * Za + Zb * Zb) < 4 && count <= 500) {
            double tempZa = Za * Za - Zb * Zb + Ca;
            Zb = 2 * Za * Zb + Cb;
            Za = tempZa;
            count++;
        }
        return count;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double scale = 1.5 / zoom;
        double newCenterX = centerX + map(e.getX(), 0, width, -scale, scale);
        double newCenterY = centerY + map(e.getY(), 0, height, scale, -scale);
        centerX = newCenterX;
        centerY = newCenterY;
        generateBlackImage();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastDragPoint = e.getPoint(); // Store where the drag started
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastDragPoint = null; // Stop tracking drag
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (lastDragPoint != null) {
            Point currentPoint = e.getPoint();
            
            // Calculate how much the mouse moved
            int dx = currentPoint.x - lastDragPoint.x;
            int dy = currentPoint.y - lastDragPoint.y;

            // Adjust the center based on drag (scaled by zoom)
            double scale = 1.5 / zoom;
            centerX -= dx * (scale / width);  // Move opposite to drag direction
            centerY += dy * (scale / height);

            lastDragPoint = currentPoint; // Update last position

            generateBlackImage();
            repaint();
        }
    }

    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mandelbrot Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new Mypanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
