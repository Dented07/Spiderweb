import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SpiderWeb extends JFrame {
    private Spider spider;
    private JPanel gamePanel;
    private boolean showGrid = true;
    private int gridSize = 50; // Size of grid squares
    private boolean isDragging = false;
    private Point dragStart = null;
    private Timer closeTimer;
    private JLabel messageLabel;
    private boolean webShotDuringWarning = false;
    private Timer finalTimer;
    
    public SpiderWeb() {
        setTitle("SpiderWeb 1");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        spider = new Spider(400, 300);
        
        // Create game panel first
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.BLACK);
                
                // Draw grid
                if (showGrid) {
                    g.setColor(new Color(50, 50, 50));
                    for (int x = 0; x < getWidth(); x += gridSize) {
                        g.drawLine(x, 0, x, getHeight());
                    }
                    for (int y = 0; y < getHeight(); y += gridSize) {
                        g.drawLine(0, y, getWidth(), y);
                    }
                }
                
                spider.draw((Graphics2D)g);
                
                // Draw drag preview
                if (isDragging && dragStart != null) {
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(3));
                    Point mousePoint = getMousePosition();
                    if (mousePoint != null) {
                        int snapX = Math.round(mousePoint.x / (float)gridSize) * gridSize;
                        g2d.drawLine(spider.getPosition().x, spider.getPosition().y, snapX, dragStart.y);
                        g2d.fillOval(snapX - 5, dragStart.y - 5, 10, 10);
                    }
                }
            }
        };
        
        gamePanel.setOpaque(true);
        
        // Add mouse listeners
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point spiderPos = spider.getPosition();
                dragStart = new Point(
                    spiderPos.x,
                    Math.round(e.getY() / (float)gridSize) * gridSize
                );
                isDragging = true;
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging && dragStart != null) {
                    int endX = Math.round(e.getX() / (float)gridSize) * gridSize;
                    int endY = dragStart.y;
                    Point spiderPos = spider.getPosition();
                    
                    if (endY == dragStart.y && Math.abs(endX - spiderPos.x) > gridSize) {
                        spider.shootWeb(endX, endY);
                        messageLabel.setText("WARNING: Shoot a web in 2 seconds or game closes!");
                        messageLabel.setForeground(Color.RED);
                        webShotDuringWarning = false;
                        closeTimer.start();
                    } else {
                        spider.shootWeb(endX, endY);
                        webShotDuringWarning = true;
                        checkTriangle();
                    }
                }
                isDragging = false;
                dragStart = null;
                repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isDragging) {
                    int snapX = Math.round(e.getX() / (float)gridSize) * gridSize;
                    int snapY = Math.round(e.getY() / (float)gridSize) * gridSize;
                    spider.shootWeb(snapX, snapY);
                    webShotDuringWarning = true;
                    checkTriangle();
                    repaint();
                }
            }
        });
        
        gamePanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    repaint();
                }
            }
        });
        
        // Add the game panel first
        add(gamePanel, BorderLayout.CENTER);
        
        // Create and add message label
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.YELLOW);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(messageLabel, BorderLayout.NORTH);
        
        // Create checkbox and button panel
        JCheckBox gridCheckbox = new JCheckBox("Show Grid");
        gridCheckbox.setSelected(true);
        gridCheckbox.setForeground(Color.WHITE);
        gridCheckbox.setBackground(Color.BLACK);
        gridCheckbox.addActionListener(e -> {
            showGrid = gridCheckbox.isSelected();
            repaint();
        });
        
        JButton resetButton = new JButton("Reset");
        resetButton.setFocusPainted(false);
        resetButton.setBackground(new Color(70, 70, 70));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> {
            spider = new Spider(400, 300);
            messageLabel.setText("");
            repaint();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(resetButton);
        buttonPanel.add(gridCheckbox);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Create timer last
        closeTimer = new Timer(2000, e -> {
            messageLabel.setText("Game closing in 2 seconds!");
            messageLabel.setForeground(Color.RED);
            
            finalTimer = new Timer(2000, e2 -> {
                if (!webShotDuringWarning) {
                    dispose();
                    System.exit(0);
                } else {
                    messageLabel.setText("Nice save!");
                    messageLabel.setForeground(Color.GREEN);
                }
            });
            finalTimer.setRepeats(false);
            finalTimer.start();
        });
        closeTimer.setRepeats(false);
    }
    
    private void checkTriangle() {
        List<Point> webs = spider.getWebs();
        if (webs.size() == 3) {
            // Calculate sides
            double[] sides = new double[3];
            for (int i = 0; i < 3; i++) {
                int next = (i + 1) % 3;
                double dx = webs.get(i).x - webs.get(next).x;
                double dy = webs.get(i).y - webs.get(next).y;
                sides[i] = Math.sqrt(dx * dx + dy * dy);
            }
            
            // Check if it's a good triangle (all sides within 5% of each other)
            double tolerance = sides[0] * 0.05;  // 5% tolerance
            if (Math.abs(sides[0] - sides[1]) < tolerance &&
                Math.abs(sides[1] - sides[2]) < tolerance &&
                Math.abs(sides[2] - sides[0]) < tolerance) {
                messageLabel.setText("Perfect Triangle! Great job!");
                messageLabel.setForeground(Color.GREEN);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SpiderWeb().setVisible(true);
        });
    }
} 