package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame implements ActionListener {

    private JTextField tusername;
    private JPasswordField tpassword;
    private JButton login, back, employee;

    Login() {
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Background panel with custom painting
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon(ClassLoader.getSystemResource("icons/background.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Heading
        JLabel heading = new JLabel("<html><span style='font-size:20px; font-weight:bold;'>Admin Panel</span></html>");
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(heading, gbc);

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        backgroundPanel.add(usernameLabel, gbc);

        tusername = new JTextField(15);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 1;
        backgroundPanel.add(tusername, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.gridx = 0;
        gbc.gridy = 2;
        backgroundPanel.add(passwordLabel, gbc);

        tpassword = new JPasswordField(15);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 1;
        backgroundPanel.add(tpassword, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        login = new JButton("LOGIN");
        login.setBackground(Color.black);
        login.setForeground(Color.white);
        login.addActionListener(this);
        buttonPanel.add(login);

        back = new JButton("BACK");
        back.setBackground(Color.black);
        back.setForeground(Color.white);
        back.addActionListener(this);
        buttonPanel.add(back);

        employee = new JButton("EMPLOYEE");
        employee.setBackground(Color.black);
        employee.setForeground(Color.white);
        employee.addActionListener(this);
        buttonPanel.add(employee);

        backgroundPanel.add(buttonPanel, gbc);
        gbc.gridy = 3;
        backgroundPanel.add(buttonPanel, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            try {
                String username = tusername.getText();
                String password = new String(tpassword.getPassword());

                conn dbConnection = new conn();
                String query = "SELECT * FROM login WHERE username = ? AND password = ?";
                PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet resultset = pstmt.executeQuery();

                if (resultset.next()) {
                    setVisible(false); // Close the login window
                    new Main_class();  // Create and show Main_class window
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username Or Password");
                }

                pstmt.close();
                dbConnection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } else if (e.getSource() == back) {
            System.exit(0);
        } else if (e.getSource() == employee) {
            setVisible(false);
            new EmployeeLogin();  // This opens EmployeeLogin panel (assuming it's defined elsewhere)
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
