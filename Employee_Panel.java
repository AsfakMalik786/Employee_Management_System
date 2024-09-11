package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee_Panel extends JFrame implements ActionListener {

    private JButton home, employeeDetails, feedback, logout, viewMessagesButton, viewProjectsButton;
    private String empId;
    private int unreadMessageCount = 0; // To track unread message count

    Employee_Panel(String empId) {
        this.empId = empId;
        setTitle("Employee Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        Image backgroundImage = new ImageIcon(ClassLoader.getSystemResource("icons/background.jpg")).getImage();
        BackgroundPanel mainPanel = new BackgroundPanel("icons/background.jpg");
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        home = createButton("HOME");
        buttonPanel.add(home, gbc);

        gbc.gridy++;
        employeeDetails = createButton("Employee Details");
        buttonPanel.add(employeeDetails, gbc);

        gbc.gridy++;
        feedback = createButton("Message To Admin");
        buttonPanel.add(feedback, gbc);

        gbc.gridy++;
        viewMessagesButton = createButton("View Messages");
        viewMessagesButton.addActionListener(e -> {
            new EmployeeMessagePanel(empId);
            updateMessageCount();
        });
        buttonPanel.add(viewMessagesButton, gbc);

        gbc.gridy++;
        viewProjectsButton = createButton("View Projects"); // Button for viewing projects
        viewProjectsButton.addActionListener(e -> {
            new EmployeeProjectsPanel(empId); // Opens the EmployeeProjectPanel to view/download projects
        });
        buttonPanel.add(viewProjectsButton, gbc);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);
        logout = new JButton("Logout");
        logout.setPreferredSize(new Dimension(100, 30));
        logout.setBackground(Color.RED);
        logout.setForeground(Color.WHITE);
        logout.addActionListener(this);
        topRightPanel.add(logout);

        topPanel.add(topRightPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        updateMessageCount(); // Initial message count update

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 30));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(this);
        return button;
    }

    private void updateMessageCount() {
        int count = getUnreadMessageCount(); // Fetch unread message count
        if (count > 0) {
            viewMessagesButton.setText("View Messages (" + count + ")");
        } else {
            viewMessagesButton.setText("View Messages");
        }
    }

    private int getUnreadMessageCount() {
        int count = 0;
        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123"; // Replace with your actual password

        String query = "SELECT COUNT(*) FROM notification WHERE empid = ? AND status = 'unread'";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, empId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == home) {
            JOptionPane.showMessageDialog(null, "Navigate to Home");
        } else if (e.getSource() == employeeDetails) {
            new EmployeeDetailsFrame(empId);
        } else if (e.getSource() == feedback) {
            JOptionPane.showMessageDialog(null, "Provide Feedback");
        } else if (e.getSource() == logout) {
            setVisible(false);
            new EmployeeLogin();
        }
    }

    public static void main(String[] args) {
        String loggedInEmpId = "123"; // Example empId
        SwingUtilities.invokeLater(() -> new Employee_Panel(loggedInEmpId));
    }
}
