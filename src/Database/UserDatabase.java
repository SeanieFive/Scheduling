package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduling.User;
import utilities.DBConnection;
import utilities.TriggerLog;

public class UserDatabase {
    
    private static User currentUser;
    private static String existingCustomerValue;
    private static int userIdValue;
    private static final ObservableList userList = FXCollections.observableArrayList();
    
    public static Boolean login(String usernameEntry, String passwordEntry) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT * FROM user WHERE username='" + usernameEntry + "' AND password='" + passwordEntry + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                currentUser = new User();
                currentUser.setUsername(results.getString("userName"));
                System.out.println(usernameEntry + " has been verified");
                TriggerLog.log(usernameEntry, true);
                statement.close();

                return true;
            } else {
                System.out.println("UserName and Password failed verificaiton");
                TriggerLog.log(usernameEntry, false);
                return false;
            }
            
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }

    }
    
    // Create List of Users
    public static ObservableList getUserList() {
        userList.clear();
        
        // Retrieve User list from User Table (Returns userList)
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT userName FROM user";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                userList.add(results.getString("userName"));
            }
            statement.close();
            return userList;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    } 
    
    // Convert UserId to User
    public static String existingUser(int id) {
    try {
        Statement statement = DBConnection.startConnection().createStatement();
        String query = "SELECT userName FROM user WHERE userId=" + id;
        ResultSet results = statement.executeQuery(query);
        while(results.next()) {
            existingCustomerValue = results.getString("userName");
        }
        statement.close();
        return existingCustomerValue;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }    
    
    public static User getCurrentUser(){
        return currentUser;
    }
    
    // UserName to UserId
    public static int getUserId(String userName) {
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT userId FROM user WHERE userName = '" + userName + "'";

            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                userIdValue = results.getInt("userId");
            }
            statement.close();
            return userIdValue;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return 0;
        }
    }
}
