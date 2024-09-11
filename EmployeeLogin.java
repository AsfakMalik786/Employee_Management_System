package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class EmployeeLogin extends JFrame implements ActionListener {

    private JTextField tempid;
    private JPasswordField tpassword;
    private JButton login, back, admin, createNewPassword;
    private JPanel imagePanel;

    public EmployeeLogin() {
        setTitle("Employee Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(163, 255, 188));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Heading
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel heading = new JLabel("Employee Panel", JLabel.CENTER);
        heading.setFont(new Font("Serif", Font.BOLD, 20));
        mainPanel.add(heading, gbc);

        // Employee ID Label
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel empIdLabel = new JLabel("Employee ID");
        mainPanel.add(empIdLabel, gbc);

        // Employee ID Text Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        tempid = new JTextField(15);
        mainPanel.add(tempid, gbc);

        // Password Label
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passwordLabel = new JLabel("Password");
        mainPanel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        tpassword = new JPasswordField(15);
        mainPanel.add(tpassword, gbc);

        // Login Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        login = new JButton("LOGIN");
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        mainPanel.add(login, gbc);

        // Back Button
        gbc.gridx = 1;
        back = new JButton("BACK");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        mainPanel.add(back, gbc);

        // Admin Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        admin = new JButton("ADMIN");
        admin.setBackground(Color.BLACK);
        admin.setForeground(Color.WHITE);
        admin.addActionListener(this);
        mainPanel.add(admin, gbc);

        // Create New Password Button
        gbc.gridy++;
        createNewPassword = new JButton("Create New Password");
        createNewPassword.setBackground(Color.BLACK);
        createNewPassword.setForeground(Color.WHITE);
        createNewPassword.addActionListener(this);
        mainPanel.add(createNewPassword, gbc);

        // Add main panel to the frame
        add(mainPanel, BorderLayout.CENTER);



        // Set the frame to be resizable
        setResizable(true);
        setSize(600, 350);
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            handleLogin();
        } else if (e.getSource() == back) {
            handleBack();
        } else if (e.getSource() == createNewPassword) {
            handleCreateNewPassword();
        } else if (e.getSource() == admin) {
            handleAdmin();
        }
    }

    private void handleLogin() {
        try {
            String empId = tempid.getText();
            String password = new String(tpassword.getPassword());

            conn conn = new conn();
            String query = "SELECT * FROM employee WHERE empid = ?";
            PreparedStatement pstmt = conn.getConnection().prepareStatement(query);
            pstmt.setString(1, empId);
            ResultSet resultset = pstmt.executeQuery();

            if (resultset.next()) {
                String storedHashedPassword = resultset.getString("password");
                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    setVisible(false);
                    new Employee_Panel(empId);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Employee ID or Password");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Employee ID or Password");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleBack() {
        setVisible(false);
        new Login();
    }

    private void handleCreateNewPassword() {
        setVisible(false);
        new CreateNewPassword();
    }

    private void handleAdmin() {
        new Login();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeLogin::new);
    }
}
