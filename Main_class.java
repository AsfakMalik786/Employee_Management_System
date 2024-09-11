package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main_class extends JFrame {

    private String empId;

    Main_class(String empId) {
        this.empId = empId; // Initialize empId
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a custom panel with a background image
        BackgroundPanel backgroundPanel = new BackgroundPanel("icons/background.jpg");
        backgroundPanel.setLayout(new BorderLayout()); // Use BorderLayout
        add(backgroundPanel);

        // Create a header panel for the heading and logout button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false); // Make the panel transparent

        // Heading
        JLabel heading = new JLabel("Employee Management System", JLabel.CENTER);
        heading.setFont(new Font("Raleway", Font.BOLD, 25));
        heading.setForeground(Color.WHITE); // Set text color to contrast with background
        headerPanel.add(heading, BorderLayout.CENTER);

        // Logout Button
        JButton logout = new JButton("Logout");
        logout.setPreferredSize(new Dimension(100, 40));
        logout.setForeground(Color.WHITE);
        logout.setBackground(Color.BLACK);
        logout.addActionListener(e -> {
            setVisible(false);
            new EmployeeLogin();
        });
        headerPanel.add(logout, BorderLayout.EAST);

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);

        // Create a panel for the main content and set GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Make the panel transparent
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        // Create a GridBagConstraints object for component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridwidth = 1;
        gbc.gridy = 0;

        JButton add = createButton("Add Employee", e -> {
            new AddEmployee();
            setVisible(false);
        });
        gbc.gridx = 0;
        mainPanel.add(add, gbc);

        JButton view = createButton("View Employee", e -> {
            new View_Employee();
            setVisible(false);
        });
        gbc.gridx = 1;
        mainPanel.add(view, gbc);

        JButton rem = createButton("Remove Employee", e -> {
            new RemoveEmployee();
            setVisible(false);
        });
        gbc.gridx = 2;
        mainPanel.add(rem, gbc);

        // Add Send Message Button
        JButton sendMessage = createButton("Send Message", e -> {
            new AdminMessageBox();  // Opens the Admin Message Box to send a message
        });
        gbc.gridx = 3;
        mainPanel.add(sendMessage, gbc);

        JButton uploadProject = createButton("Upload Project", e -> {
            new AdminPanel();  // Opens the Upload Project window
        });
        gbc.gridx = 4;  // Adjust the position for the new button
        mainPanel.add(uploadProject, gbc);

        // Set JFrame properties
        setSize(1120, 630);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Main_class() {
        this(null);

    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.addActionListener(actionListener);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK);
            }
        });

        return button;
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(ClassLoader.getSystemResource(imagePath)).getImage();
            } catch (Exception e) {
                System.err.println("Error loading image: " + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Scale the background image to fit the panel size
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        // Example: After successful login, use the employee ID
        String loggedInEmpId = "123"; // This should be dynamically set after login
        SwingUtilities.invokeLater(() -> new Main_class(loggedInEmpId));
    }
}
