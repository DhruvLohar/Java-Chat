package testing;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
// import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import testing.Server2;


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
        
        JPanel backslashButtonPanel = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column
        backslashButtonPanel.setOpaque(false);
        

        // Create the backslash label
        JLabel backslashLabel = new JLabel("<<");
        backslashLabel.setFont(new java.awt.Font("Artifakt Element Heavy", 1, 24));
        backslashLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        backslashLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backslashButtonPanel.setLayout(new GridBagLayout());
        backslashButtonPanel.setOpaque(false);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH; // Align to the top
        backslashButtonPanel.add(backslashLabel, gbc);

        backslashLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        
        backslashButtonPanel.add(backslashLabel);
        p1.add(backslashButtonPanel);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);


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

        backslashLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Close the program
                System.exit(0);
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

        // backslashButtonPanel.add(buttonPanel);

        int gap = 20; // Adjust the gap size as needed
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(gap, 0, gap, 0));

        // Add the button panel to p1
        p1.add(buttonPanel);

        
        //cess X codecell wala panel
        String title = "Cess x Code Cell";
        String desc = "Cess is a technical community that provides you with an exposure to various fields.and gives you an opportunity to develop your co-curricular skills";
        Card codecell = new Card(title, desc);
        JPanel codeCellCard = codecell.generateCard();

        codeCellCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Open the "Server2.java" window
                state.setTitle(title);
                state.setDesc(desc);
                state.setRoomCode("CESSxCODECELL");
                state.setMaxNumOfUsers(50);
                Server2 server2Window = new Server2(false);
                server2Window.setVisible(true);
        
                // Close the current "homepage.java" window
                dispose();
            }
        });
        p1.add(codeCellCard);
        


        getContentPane().setLayout(new BorderLayout());

        // Create an empty border with top padding only
        int topPadding = 50;
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(topPadding,0,0,0));

        setSize(401,635);
        getContentPane().setBackground(new Color(0,191,252));
        setUndecorated(true);
        add(p1);
        setShape(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),40,40));
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


