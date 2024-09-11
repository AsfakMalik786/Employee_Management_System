package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeMessagePanel extends JFrame {

    private JTable messageTable;
    private String empId;
    private JLabel messageCountLabel;

    public EmployeeMessagePanel(String empId) {
        this.empId = empId;

        setTitle("Messages for Employee ID: " + empId);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize the message panel
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        // Fetch and display messages for the employee
        ArrayList<Message> messages = fetchMessagesForEmployee(empId);

        if (!messages.isEmpty()) {
            // Set up the table model
            String[] columnNames = {"Message", "Time & Date"};
            Object[][] data = new Object[messages.size()][2];

            for (int i = 0; i < messages.size(); i++) {
                data[i][0] = messages.get(i).getMessage();
                data[i][1] = messages.get(i).getTimestamp();
            }

            // Create JTable to display messages
            messageTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(messageTable);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Add message count label
            messageCountLabel = new JLabel("Unread Messages: " + getUnreadMessageCount());
            panel.add(messageCountLabel, BorderLayout.NORTH);

            // Add "Mark as All Read" button
            JButton markAsReadButton = new JButton("Mark as All Read");
            markAsReadButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    markAllAsRead();
                    refreshMessageTable();
                    messageCountLabel.setText("Unread Messages: " + getUnreadMessageCount());
                }
            });
            panel.add(markAsReadButton, BorderLayout.SOUTH);
        } else {
            JLabel noMessageLabel = new JLabel("No messages found for you.", SwingConstants.CENTER);
            panel.add(noMessageLabel, BorderLayout.CENTER);
        }

        setVisible(true);
    }

    // Fetch messages from the database for the given employee ID
    private ArrayList<Message> fetchMessagesForEmployee(String empId) {
        ArrayList<Message> messages = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123"; // Replace with your actual password

        String query = "SELECT message, timestamp FROM notification WHERE empid = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, empId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String messageText = rs.getString("message");
                Timestamp timestamp = rs.getTimestamp("timestamp");

                // Initialize a Message object
                Message message = new Message(messageText, timestamp);
                messages.add(message); // Adding message to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages; // Returning the list of messages
    }

    // Mark all messages as read
    private void markAllAsRead() {
        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123"; // Replace with your actual password

        String deleteQuery = "DELETE FROM notification WHERE empid = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setString(1, empId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Refresh the message table after marking as read
    private void refreshMessageTable() {
        // Clear the current content of the frame
        getContentPane().removeAll();

        // Recreate the panel and components
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        // Fetch and display updated messages for the employee
        ArrayList<Message> messages = fetchMessagesForEmployee(empId);

        if (!messages.isEmpty()) {
            // Set up the table model
            String[] columnNames = {"Message", "Time & Date"};
            Object[][] data = new Object[messages.size()][2];

            for (int i = 0; i < messages.size(); i++) {
                data[i][0] = messages.get(i).getMessage();
                data[i][1] = messages.get(i).getTimestamp();
            }

            // Create JTable to display messages
            messageTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(messageTable);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Add message count label
            messageCountLabel = new JLabel("Unread Messages: " + getUnreadMessageCount());
            panel.add(messageCountLabel, BorderLayout.NORTH);

            // Add "Mark as All Read" button
            JButton markAsReadButton = new JButton("Mark as All Read");
            markAsReadButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    markAllAsRead();
                    refreshMessageTable();
                }
            });
            panel.add(markAsReadButton, BorderLayout.SOUTH);
        } else {
            JLabel noMessageLabel = new JLabel("No messages found for you.", SwingConstants.CENTER);
            panel.add(noMessageLabel, BorderLayout.CENTER);
        }

        // Refresh the display
        revalidate();
        repaint();
    }

    // Get the count of unread messages
    private int getUnreadMessageCount() {
        int count = 0;
        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123"; // Replace with your actual password

        String countQuery = "SELECT COUNT(*) FROM notification WHERE empid = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement pstmt = conn.prepareStatement(countQuery);
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

    public static void main(String[] args) {
        String empId = "123"; // Test with a sample employee ID
        SwingUtilities.invokeLater(() -> new EmployeeMessagePanel(empId));
    }
}

// Message class to hold message details
class Message {
    private String message;
    private Timestamp timestamp;

    public Message(String message, Timestamp timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
