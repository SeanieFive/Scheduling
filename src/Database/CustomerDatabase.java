package Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduling.Customer;
import scheduling.LoginController;
import utilities.DBConnection;

public class CustomerDatabase {
    
    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static final ObservableList customerList = FXCollections.observableArrayList();
    private static String existingCustomerValue;
    private static int customerIdValue;

    // Add Customer
    public static void addCustomer(String name, int addressId) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "INSERT customer SET customerName= '" + name + "', addressId= '" + addressId + "', active = '1', createDate = CURRENT_DATE, createdBy = '" + LoginController.loggedInUser + "', lastUpdate= CURRENT_DATE, lastUpdateBy = '" + LoginController.loggedInUser + "'";
            statement.executeUpdate(query);
            customerIdValue = getCustomerId(name);
            System.out.println("Customer " + customerIdValue + " has been added by " + LoginController.loggedInUser + ".");
           statement.close();
        }
        catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    // Update Customer
    public static void updateCustomer(String name, int addressId, int id) throws IOException {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "UPDATE customer SET customerName = '" + name + "', " + "addressId = '" + addressId + "' WHERE customerId = " + id;
            statement.executeUpdate(query);
            System.out.println("Customer " + id + " has been updated.");
            statement.close();
        }
        catch(SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            }
    }
    
    // Delete Customer
    public static void deleteCustomer(int id) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "DELETE FROM customer WHERE customerId=" + id;
            statement.executeUpdate(query);
            System.out.println("Customer " + id + " was deleted.");
            statement.close();
        }
        catch(SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            }
    }
    
    
    // Retrieve All Customers
    public static ObservableList<Customer> getAllCustomers() {
        allCustomers.clear();
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT customer.customerId, customer.customerName, customer.addressId FROM customer";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                Customer customer = new Customer (
                results.getInt("customerId"), results.getString("customerName"), results.getInt("addressId"));
                allCustomers.add(customer);
            }
            statement.close();
            return allCustomers;
        }
        catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
        return null;
        }
    }
    
    // Retrieve List of Customers
    public static ObservableList getCustomerList() {
        customerList.clear();
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT customerName FROM customer";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                customerList.add(results.getString("customerName"));
            }
            statement.close();
            return customerList;
        }
        catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    } 
    
    // Convert CustomerId to Customer
    public static String getCustomerName(int id) {
    try {
        Statement statement = DBConnection.startConnection().createStatement();
        String query = "SELECT customerName FROM customer WHERE customerId=" + id;
        ResultSet results = statement.executeQuery(query);
        while(results.next()) {
            existingCustomerValue = results.getString("customerName");
        }
        statement.close();
        return existingCustomerValue;
    }
    catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    // Convert Customer to CustomerId
    public static int getCustomerId(String name) {
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT customerId FROM customer WHERE customerName = '" + name + "'";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                customerIdValue = results.getInt("customerId");
            }
            statement.close();
            return customerIdValue;
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return 0;
        }
    }
}
    
