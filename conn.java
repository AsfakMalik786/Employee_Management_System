package employee.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class conn {

    private Connection connection;
    private Statement statement;

    public conn() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeemanagement?useSSL=false", "root", "Asfak@123");
            // Create a statement object
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Please include the JDBC driver in your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database. Check your URL, username, and password.");
            e.printStackTrace();
        }
    }

    // Getter for the connection
    public Connection getConnection() {
        return connection;
    }

    // Getter for the statement
    public Statement getStatement() {
        return statement;
    }

    // Close resources
    public void close() {
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
