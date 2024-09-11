package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

public class CreateNewPassword extends JFrame implements ActionListener {

    JTextField tempid;
    JPasswordField tnewPassword, tconfirmPassword;
    JButton createPassword, back;

    // Custom JPanel to paint background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String fileName) {
            try {
                backgroundImage = new ImageIcon(ClassLoader.getSystemResource(fileName)).getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    CreateNewPassword() {
        setTitle("Create New Password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel("icons/background.jpg");
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Heading
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel heading = new JLabel("Create New Password", JLabel.CENTER);
        heading.setFont(new Font("Serif", Font.BOLD, 20));
        heading.setForeground(Color.WHITE); // Set text color to contrast with background
        backgroundPanel.add(heading, gbc);

        // Employee ID
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel empIdLabel = new JLabel("Employee ID");
        empIdLabel.setForeground(Color.WHITE); // Set text color to contrast with background
        backgroundPanel.add(empIdLabel, gbc);

        gbc.gridx = 1;
        tempid = new JTextField(15);
        backgroundPanel.add(tempid, gbc);

        // New Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setForeground(Color.WHITE); // Set text color to contrast with background
        backgroundPanel.add(newPasswordLabel, gbc);

        gbc.gridx = 1;
        tnewPassword = new JPasswordField(15);
        backgroundPanel.add(tnewPassword, gbc);

        // Confirm Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setForeground(Color.WHITE); // Set text color to contrast with background
        backgroundPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        tconfirmPassword = new JPasswordField(15);
        backgroundPanel.add(tconfirmPassword, gbc);

        // Create Password Button
        gbc.gridy++;
        gbc.gridx = 0;
        createPassword = new JButton("CREATE");
        createPassword.setBackground(Color.BLACK);
        createPassword.setForeground(Color.WHITE);
        createPassword.addActionListener(this);
        backgroundPanel.add(createPassword, gbc);

        // Back Button
        gbc.gridx = 1;
        back = new JButton("BACK");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        backgroundPanel.add(back, gbc);

        // Add background panel to the frame
        setContentPane(backgroundPanel);

        setSize(600, 400);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createPassword) {
            String empId = tempid.getText();
            String newPassword = new String(tnewPassword.getPassword());
            String confirmPassword = new String(tconfirmPassword.getPassword());

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match");
            } else {
                try {
                    conn conn = new conn();
                    String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                    String query = "UPDATE employee SET password = ? WHERE empid = ?";
                    PreparedStatement pstmt = conn.getConnection().prepareStatement(query);
                    pstmt.setString(1, hashedPassword);
                    pstmt.setString(2, empId);
                    int updated = pstmt.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(null, "Password updated successfully");
                        setVisible(false);
                        new EmployeeLogin();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Employee ID");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == back) {
            setVisible(false);
            new EmployeeLogin();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreateNewPassword::new);
    }
}
