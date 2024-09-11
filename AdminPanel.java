package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends JFrame {
    private JList<String> empIdList;
    private JTextField projectNameField;
    private JTextArea projectDescriptionArea;
    private JLabel fileLabel;
    private File selectedFile;

    public AdminPanel() {
        setTitle("Assign Project to Employees");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout to align components vertically

        // Employee IDs List with Scrollbar
        JPanel empIdPanel = new JPanel(new BorderLayout());
        empIdPanel.add(new JLabel("Select Employee IDs:"), BorderLayout.NORTH);

        // Fetch employee IDs from the database
        List<String> empIds = fetchEmployeeIds();
        empIdList = new JList<>(empIds.toArray(new String[0]));
        empIdList.setVisibleRowCount(10);
        JScrollPane scrollPane = new JScrollPane(empIdList);
        empIdPanel.add(scrollPane, BorderLayout.CENTER);

        // "Select All" Button
        JButton selectAllButton = new JButton("Select All Employees");
        selectAllButton.addActionListener(e -> empIdList.setSelectionInterval(0, empIds.size() - 1));
        empIdPanel.add(selectAllButton, BorderLayout.SOUTH);

        // Add employee ID panel
        panel.add(empIdPanel);

        // Project Name
        JPanel projectNamePanel = new JPanel(new BorderLayout());
        projectNamePanel.add(new JLabel("Project Name:"), BorderLayout.NORTH);
        projectNameField = new JTextField(40);
        projectNamePanel.add(projectNameField, BorderLayout.CENTER);
        panel.add(projectNamePanel); // Add Project Name panel

        // Project Description
        JPanel projectDescriptionPanel = new JPanel(new BorderLayout());
        projectDescriptionPanel.add(new JLabel("Project Description:"), BorderLayout.NORTH);
        projectDescriptionArea = new JTextArea(5, 40);
        projectDescriptionArea.setLineWrap(true);
        projectDescriptionArea.setWrapStyleWord(true);
        projectDescriptionPanel.add(new JScrollPane(projectDescriptionArea), BorderLayout.CENTER);
        panel.add(projectDescriptionPanel); // Add Project Description panel

        // File selection button
        JPanel filePanel = new JPanel(new FlowLayout());
        fileLabel = new JLabel("No file selected");
        JButton selectFileButton = new JButton("Select File");
        selectFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                fileLabel.setText("Selected file: " + selectedFile.getName());
            }
        });
        filePanel.add(selectFileButton);
        filePanel.add(fileLabel);
        panel.add(filePanel); // Add File selection panel

        // Send Project Button
        JButton sendButton = new JButton("Send Project");
        sendButton.addActionListener(e -> {
            List<String> selectedEmpIds = empIdList.getSelectedValuesList();
            String projectName = projectNameField.getText();
            String projectDescription = projectDescriptionArea.getText();

            if (!selectedEmpIds.isEmpty() && !projectName.isEmpty() && selectedFile != null) {
                sendProjectToEmployees(selectedEmpIds, projectName, projectDescription, selectedFile);
                JOptionPane.showMessageDialog(this, "Project sent successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please complete all fields and select employees.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(sendButton); // Add Send button at the bottom

        add(panel);
        setVisible(true);
    }

    private List<String> fetchEmployeeIds() {
        List<String> empIds = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123";

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

    private void sendProjectToEmployees(List<String> empIds, String projectName, String projectDescription, File file) {
        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Connected to the database!");

                for (String empId : empIds) {
                    if (isValidEmployeeId(conn, empId)) {
                        String sql = "INSERT INTO projects (empid, project_name, description, attachment_path, upload_date) VALUES (?, ?, ?, ?, NOW())";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, empId);
                            pstmt.setString(2, projectName);
                            pstmt.setString(3, projectDescription);
                            pstmt.setString(4, file.getAbsolutePath());
                            pstmt.executeUpdate();
                        }
                    } else {
                        System.out.println("Invalid Employee ID: " + empId);
                    }
                }
                System.out.println("Projects sent successfully!");
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
        SwingUtilities.invokeLater(AdminPanel::new);
    }
}
