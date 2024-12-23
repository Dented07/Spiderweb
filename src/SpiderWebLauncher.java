import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpiderWebLauncher extends JFrame {
    private int spiderX = 200;
    private int spiderY = 80;
    private int spiderSize = 20;
    
    public SpiderWebLauncher() {
        setTitle("SpiderWeb Launcher");
        setSize(400, 400);  // Made taller for spider
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Custom panel for spider drawing
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.BLACK);
                
                // Draw decorative web lines
                g.setColor(new Color(30, 30, 30));
                for (int i = 0; i < 8; i++) {
                    double angle = Math.PI * 2 * i / 8;
                    int endX = spiderX + (int)(Math.cos(angle) * 150);
                    int endY = spiderY + (int)(Math.sin(angle) * 150);
                    g.drawLine(spiderX, spiderY, endX, endY);
                }
                
                // Draw spider
                Graphics2D g2d = (Graphics2D)g;
                g2d.setStroke(new BasicStroke(2));
                
                // Body
                g.setColor(Color.DARK_GRAY);
                g.fillOval(spiderX - spiderSize, spiderY - spiderSize, spiderSize*2, spiderSize*2);
                g.fillOval(spiderX - spiderSize/2, spiderY - spiderSize*4/3, spiderSize, spiderSize);
                
                // Legs
                for (int i = 0; i < 8; i++) {
                    double angle = Math.PI * 2 * i / 8;
                    int legLength = spiderSize + 15;
                    int endX = spiderX + (int)(Math.cos(angle) * legLength);
                    int endY = spiderY + (int)(Math.sin(angle) * legLength);
                    g.drawLine(spiderX, spiderY, endX, endY);
                }
                
                // Eyes
                g.setColor(Color.RED);
                g.fillOval(spiderX - spiderSize/3, spiderY - spiderSize, 6, 6);
                g.fillOval(spiderX + spiderSize/3 - 6, spiderY - spiderSize, 6, 6);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Title Label
        JLabel titleLabel = new JLabel("SpiderWeb Launcher");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(120, 50, 20, 50));
        
        // SpiderWeb 1 Button
        JButton classicButton = createGameButton("SpiderWeb 1", "Classic Mode");
        classicButton.addActionListener(e -> {
            dispose();
            new SpiderWeb().setVisible(true);
        });
        
        // SpiderWeb Pro Button
        JButton proButton = createGameButton("SpiderWeb 1 Pro", "Coming Soon!");
        proButton.setEnabled(false);
        
        // Exit Button
        JButton exitButton = createGameButton("Exit", "Close Launcher");
        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(classicButton);
        buttonPanel.add(proButton);
        buttonPanel.add(exitButton);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Version label
        JLabel versionLabel = new JLabel("v1.0");
        versionLabel.setForeground(Color.GRAY);
        versionLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(versionLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add spider animation
        Timer animationTimer = new Timer(50, e -> {
            spiderY += (Math.random() - 0.5) * 2;  // Subtle up/down movement
            repaint();
        });
        animationTimer.start();
    }
    
    private JButton createGameButton(String title, String tooltip) {
        JButton button = new JButton(title);
        button.setBackground(new Color(40, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setToolTipText(tooltip);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(60, 60, 60));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(40, 40, 40));
            }
        });
        
        return button;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SpiderWebLauncher().setVisible(true);
        });
    }
} 