package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeProjectsPanel extends JFrame {

    private JTable projectsTable;
    private String empId;
    private JButton downloadButton;
    private JButton deleteButton;

    private ArrayList<Project> projectList = new ArrayList<>();

    public EmployeeProjectsPanel(String empId) {
        this.empId = empId;
        setTitle("Your Projects");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        loadEmployeeProjects();

        String[] columnNames = {"Project Name", "Description", "Attachment", "Upload Date"};
        String[][] projectData = new String[projectList.size()][4]; // Update size to 4

        for (int i = 0; i < projectList.size(); i++) {
            Project project = projectList.get(i);
            projectData[i][0] = project.getProjectName();
            projectData[i][1] = project.getDescription();
            projectData[i][2] = project.getAttachmentPath() == null ? "No Attachment" : "Download";
            projectData[i][3] = project.getUploadDate() != null ? project.getUploadDate().toString() : ""; // Show timestamp
        }

        projectsTable = new JTable(projectData, columnNames);
        JScrollPane scrollPane = new JScrollPane(projectsTable);
        add(scrollPane, BorderLayout.CENTER);

        downloadButton = new JButton("Download Attachment");
        downloadButton.addActionListener(e -> downloadAttachment());
        add(downloadButton, BorderLayout.SOUTH);

        deleteButton = new JButton("Delete Project");
        deleteButton.addActionListener(e -> deleteProject());
        add(deleteButton, BorderLayout.NORTH); // Place above the download button

        setVisible(true);
    }

    private void deleteProject() {
        int selectedRow = projectsTable.getSelectedRow();

        if (selectedRow != -1) {
            String projectName = projectList.get(selectedRow).getProjectName();

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this project?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String url = "jdbc:mysql://localhost:3306/employeemanagement";
                String user = "root";
                String password = "Asfak@123";
                String query = "DELETE FROM projects WHERE project_name = ? AND empid = ?";

                try (Connection conn = DriverManager.getConnection(url, user, password);
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, projectName);
                    stmt.setString(2, empId);
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Project deleted successfully.");
                        projectList.remove(selectedRow);
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error deleting project.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a project first.");
        }
    }

    private void refreshTable() {
        String[][] projectData = new String[projectList.size()][4];
        for (int i = 0; i < projectList.size(); i++) {
            Project project = projectList.get(i);
            projectData[i][0] = project.getProjectName();
            projectData[i][1] = project.getDescription();
            projectData[i][2] = project.getAttachmentPath() == null ? "No Attachment" : "Download";
            projectData[i][3] = project.getUploadDate() != null ? project.getUploadDate().toString() : "";
        }

        projectsTable.setModel(new javax.swing.table.DefaultTableModel(projectData, new String[]{"Project Name", "Description", "Attachment", "Upload Date"}));
    }


    private void loadEmployeeProjects() {
        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123";
        String query = "SELECT project_name, description, attachment_path, upload_date " +
                "FROM projects " +
                "WHERE empid = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, empId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project project = new Project(
                        rs.getString("project_name"),
                        rs.getString("description"),
                        rs.getString("attachment_path"),
                        rs.getTimestamp("upload_date") // Include timestamp
                );
                projectList.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Handling file download for selected project
    private void downloadAttachment() {
        int selectedRow = projectsTable.getSelectedRow();

        if (selectedRow != -1) {
            String attachmentPath = projectList.get(selectedRow).getAttachmentPath();

            // Proceed only if there is an attachment
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(attachmentPath));
                int returnVal = fileChooser.showSaveDialog(this);

                // If the user approves the save location
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File destinationFile = fileChooser.getSelectedFile();
                    try (InputStream in = new FileInputStream(attachmentPath);
                         FileOutputStream out = new FileOutputStream(destinationFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                        JOptionPane.showMessageDialog(this, "File downloaded successfully.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error downloading file.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No attachment to download.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a project first.");
        }
    }

    public static void main(String[] args) {
        String loggedInEmpId = "123"; // Replace with actual empId
        SwingUtilities.invokeLater(() -> new EmployeeProjectsPanel(loggedInEmpId));
    }
}

// Project class to hold project details
class Project {
    private String projectName;
    private String description;
    private String attachmentPath;
    private Timestamp uploadDate; // Add this field

    public Project(String projectName, String description, String attachmentPath, Timestamp uploadDate) {
        this.projectName = projectName;
        this.description = description;
        this.attachmentPath = attachmentPath;
        this.uploadDate = uploadDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getDescription() {
        return description;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }
}
