package testing;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
// import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;


class RoundedButton extends JButton {
    private int radius;

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
        super.paintComponent(g2d);
        g2d.dispose();
    }
}

class Card extends JPanel {
    private String title;    
    private String desc;


    Card(String t, String d) {
        title = t;
        desc = d;

        setLayout(null);
        this.setBackground(new Color(255,255,255));
        this.setPreferredSize(new Dimension(361,150));
        this.setBounds(0,50,361,150);
    }

    JPanel generateCard() {
        JLabel headingLabel = new JLabel(title);
        headingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        headingLabel.setBounds(10, 10, 361, 30);

        JTextArea descriptionTextArea = new JTextArea(desc);
        descriptionTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setOpaque(false);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setFocusable(false);
        descriptionTextArea.setBorder(null);
        descriptionTextArea.setBounds(10, 40, 341, 100);

        this.add(headingLabel);
        this.add(descriptionTextArea);

        return this;
    }
}

public class homepage extends JFrame{
 
    private StateManager state; 

    homepage(){
        
        state = StateManager.getInstance();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Transparent background
        
        setContentPane(new RoundedPanel(40));
        getContentPane().setLayout(new BorderLayout());
        

        
        //sabse bada white wala panel
        JPanel p1 = new RoundedPanel(40);
        
        p1.setBackground(new Color(245,245,245));
        p1.setBorder(new EmptyBorder(30,0,0,0));
        p1.setBounds(0,50,401,585);
        p1.setOpaque(false);

         // Create rounded buttons with a border radius of 15
        RoundedButton createButton = new RoundedButton("Create", 15);
        RoundedButton joinButton = new RoundedButton("Join", 15);


        Dimension buttonSize = new Dimension(150, 40); // Adjust the width and height as needed
        createButton.setPreferredSize(buttonSize);
        joinButton.setPreferredSize(buttonSize);

        // Set the background color of the buttons
        createButton.setBackground(new Color(0, 191, 252)); // Set blue background color
        joinButton.setBackground(new Color(0, 191, 252)); // Set blue background color

        // Set the text color of the buttons
        createButton.setForeground(Color.WHITE);
        joinButton.setForeground(Color.WHITE);

        // Add action listeners or functionality to the buttons if needed

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Adjust spacing as needed

        // Add action listeners to the buttons
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the "createroom.java" window
                createroom createRoomWindow = new createroom();
                createRoomWindow.setVisible(true);

                // Close the current "homepage.java" window
                dispose();
            }
        });

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the "joinroom.java" window
                joinroom joinRoomWindow = new joinroom();
                joinRoomWindow.setVisible(true);

                // Close the current "homepage.java" window
                dispose();
            }
        });

        // Add the rounded buttons to the button panel
        buttonPanel.add(createButton);
        buttonPanel.add(joinButton);

        int gap = 20; // Adjust the gap size as needed
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(gap, 0, gap, 0));

        // Add the button panel to p1
        p1.add(buttonPanel, BorderLayout.SOUTH);

        
        //cess X codecell wala panel
        Card codecell = new Card("Cess x Code Cell", "Cess is a committee in which students are basically for attendance purpose. The social media manager is elected by running after seniors through a junior friend.");
        JPanel codeCellCard = codecell.generateCard();

        codeCellCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Open the "Server2.java" window
                Server2 server2Window = new Server2();
                server2Window.setVisible(true);
        
                // Close the current "homepage.java" window
                dispose();
            }
        });
        p1.add(codeCellCard);
        
        
        setSize(401,635);
        getContentPane().setBackground(new Color(0,191,252));
        setUndecorated(true);
        add(p1);
        setVisible(true);
    }
    
    
    
    
    
    
    
    public static void main(String args[]){
        
        new homepage();
        
    }
        
}


class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private int cornerRadius = 15;

    public RoundedPanel(LayoutManager layout, int radius) {
        super(layout);
        cornerRadius = radius;
        setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Set a white line border
    }

    public RoundedPanel(LayoutManager layout, int radius, Color bgColor) {
        super(layout);
        cornerRadius = radius;
        backgroundColor = bgColor;
        setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Set a white line border
    }

    public RoundedPanel(int radius) {
        super();
        cornerRadius = radius;
        setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Set a white line border
    }

    public RoundedPanel(int radius, Color bgColor) {
        super();
        cornerRadius = radius;
        backgroundColor = bgColor;
        setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Set a white line border
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundColor != null) {
            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height); // Paint background
        }
    }
}


