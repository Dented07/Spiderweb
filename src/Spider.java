import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Spider {
    private int x, y;
    private List<Point> webs;
    private int size = 30;
    public Spider(int x, int y) {
        this.x = x;
        this.y = y;
        webs = new ArrayList<>();
    }
    
    public void shootWeb(int targetX, int targetY) {
        if (webs.size() < 3) {
            webs.add(new Point(targetX, targetY));
        }
    }
    
    public List<Point> getWebs() {
        return webs;
    }
    
    public Point getPosition() {
        return new Point(x, y);
    }
    
    public void draw(Graphics2D g) {
        // Draw spider body
        g.setColor(Color.DARK_GRAY);
        g.fillOval(x - size, y - size, size*2, size*2);  // Body
        g.fillOval(x - size/2, y - size*4/3, size, size);  // Head
        
        // Draw legs
        g.setStroke(new BasicStroke(3));
        for (int i = 0; i < 8; i++) {
            double angle = Math.PI * 2 * i / 8;
            int legLength = size + 20;
            int endX = x + (int)(Math.cos(angle) * legLength);
            int endY = y + (int)(Math.sin(angle) * legLength);
            g.drawLine(x, y, endX, endY);
        }
        
        // Draw eyes
        g.setColor(Color.RED);
        g.fillOval(x - size/3, y - size, 8, 8);
        g.fillOval(x + size/3 - 8, y - size, 8, 8);
        
        // Draw webs
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(3));
        for (Point web : webs) {
            g.drawLine(x, y, web.x, web.y);
            g.fillOval(web.x - 5, web.y - 5, 10, 10);
        }
    }
}