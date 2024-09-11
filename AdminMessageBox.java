package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminMessageBox extends JFrame {
    private JList<String> empIdList;
    private JTextArea messageArea;
    private JButton selectAllButton;

    public AdminMessageBox() {
        setTitle("Send Message to Employees");
        setSize(600, 500);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Employee IDs List with Scrollbar
        JPanel empIdPanel = new JPanel(new BorderLayout());
        empIdPanel.add(new JLabel("Select Employee IDs:"), BorderLayout.NORTH);

        // Fetch employee IDs from the database
        List<String> empIds = fetchEmployeeIds();
        empIdList = new JList<>(empIds.toArray(new String[0]));
        empIdList.setVisibleRowCount(10); // Set the number of rows visible at once
        JScrollPane scrollPane = new JScrollPane(empIdList);
        empIdPanel.add(scrollPane, BorderLayout.CENTER);

        // "Select All" Button
        selectAllButton = new JButton("Select All Employees");
        selectAllButton.addActionListener(e -> empIdList.setSelectionInterval(0, empIds.size() - 1)); // Select all employees
        empIdPanel.add(selectAllButton, BorderLayout.SOUTH);

        // Message Area
        JPanel messagePanel = new JPanel(new BorderLayout());
        messageArea = new JTextArea(10, 40);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messagePanel.add(new JLabel("Message:"), BorderLayout.NORTH);
        messagePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // Send Button
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            List<String> selectedEmpIds = empIdList.getSelectedValuesList(); // Get selected employee IDs
            String message = messageArea.getText();

            if (!selectedEmpIds.isEmpty() && !message.isEmpty()) {
                sendMessageToEmployees(selectedEmpIds, message);
                JOptionPane.showMessageDialog(this, "Message sent successfully.");
                dispose(); // Close the message box
            } else {
                JOptionPane.showMessageDialog(this, "Please select employee IDs and enter a message.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(empIdPanel, BorderLayout.NORTH);
        panel.add(messagePanel, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private List<String> fetchEmployeeIds() {
        List<String> empIds = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123"; // Replace with your actual DB password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT empid FROM employee")) {

            while (rs.next()) {
                empIds.add(rs.getString("empid"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empIds;
    }

    private void sendMessageToEmployees(List<String> empIds, String message) {
        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Connected to the database!");

                // Construct SQL query for each employee ID
                for (String empId : empIds) {
                    if (isValidEmployeeId(conn, empId)) {
                        String sql = "INSERT INTO notification (empid, message, status) VALUES (?, ?, 'unread')";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, empId);
                            pstmt.setString(2, message);
                            pstmt.executeUpdate();
                        }
                    } else {
                        System.out.println("Invalid Employee ID: " + empId);
                    }
                }
                System.out.println("Messages sent successfully!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmployeeId(Connection conn, String empId) {
        String sql = "SELECT COUNT(*) FROM employee WHERE empid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminMessageBox::new);
    }
}
