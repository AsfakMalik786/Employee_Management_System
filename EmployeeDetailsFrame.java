package employee.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeeDetailsFrame extends JFrame {

    public EmployeeDetailsFrame(String empId) {
        setTitle("Employee Details");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        BackgroundPanel backgroundPanel = new BackgroundPanel("icons/background.jpg");
        backgroundPanel.setLayout(new BorderLayout());

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        backgroundPanel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        String url = "jdbc:mysql://localhost:3306/employeemanagement";
        String user = "root";
        String password = "Asfak@123";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM employee WHERE empid = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, empId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String details = String.format(
                        "Name: %s\nFather Name: %s\nDOB: %s\nSalary: %s\nAddress: %s\nPhone: %s\nEmail: %s\nDesignation: %s\nAadhar: %s\nEmployee ID: %s\n",
                        rs.getString("name"),
                        rs.getString("fname"),
                        rs.getString("dob"),
                        rs.getString("salary"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("designation"),
                        rs.getString("aadhar"),
                        rs.getString("empid")
                );
                detailsArea.setText(details);
            } else {
                detailsArea.setText("Employee details not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            detailsArea.setText("An error occurred while loading employee details.");
        }

        add(backgroundPanel);
        setVisible(true);
    }
}
