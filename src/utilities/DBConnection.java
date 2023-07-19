
package utilities;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U06e0o";
    
    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    // Driver and Connection Interfact reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    
    // Username and Password
    private static final String  username = "U06e0o";
    private static String password = "53688741927";
    
    
    // Start Database Connection
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        } return conn;
    }
    
    
    // Close Database Connection
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed");
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
    }
    
    
    // Check Database Connection
    public static Connection checkConnection() {
        return conn;
    }
}
