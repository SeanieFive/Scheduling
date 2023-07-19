package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduling.Address;
import scheduling.Customer;
import scheduling.LoginController;
import utilities.DBConnection;

public class AddressDatabase {
    private static ObservableList<Address> allAddresses = FXCollections.observableArrayList();
    private static ObservableList addressList = FXCollections.observableArrayList();
    private static ObservableList conflictingCustomers = FXCollections.observableArrayList();

    private static String existingAddressValue;
    private static int addressIdValue;

    
    // Retrieve All Addresses
    public static ObservableList<Address> getAllAddresses() {
        allAddresses.clear();
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT address.addressId, address.address, address.address2, address.cityId, address.postalCode, address.phone FROM address";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                Address address = new Address (
                results.getInt("addressId"), results.getString("address"), results.getString("address2"), results.getInt("cityId"), results.getString("postalCode"), results.getString("phone"));
                allAddresses.add(address);
            }
            statement.close();
            return allAddresses;
        } catch (SQLException ex) {
        System.out.println("SQL Exception Error: " + ex.getMessage());
        return null;
        }
    }
    
    // Add Address
    public static void addAddress(String address, String address2, int cityId, String postalCode, String phone) {
        
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "INSERT address SET address= '" + address + "', address2= '" + address2 + "', cityId= '" + cityId + "', postalCode= '" + postalCode + "', phone= '" + phone + "', createDate = CURRENT_DATE, createdBy = '" + LoginController.loggedInUser + "', lastUpdate= CURRENT_DATE, lastUpdateBy = '" + LoginController.loggedInUser + "'";
            statement.executeUpdate(query);
            addressIdValue = getAddressId(address);
            System.out.println("Address " + addressIdValue + " has been added by " + LoginController.loggedInUser + ".");
            statement.close();
        } catch(SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
    }
    
    // Update Address
    public static void updateAddress(String address, String address2, int cityId, String postalCode, String phone, int id) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "UPDATE address SET address = '" + address + "', address2 = '" + address2 + "', cityId = '" + cityId + "', postalCode = '" + postalCode + "', phone = '" + phone + "' WHERE addressId = " + id;
            statement.executeUpdate(query);
            System.out.println("Address " + id + " has been updated.");
            statement.close();
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }    

    // Delete Address
    public static void deleteAddress(int id) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "DELETE FROM address WHERE addressId=" + id;
            statement.executeUpdate(query);
            statement.close();
        } catch(SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
        System.out.println("Customer " + id + " was deleted.");
    }
    
    public static Customer checkCustomerTableForAddressId(int id) {
        
        Customer customer;
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT * FROM customer WHERE addressId=" + id;
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                customer = new Customer (results.getInt("customerId"), results.getString("customerName"), results.getInt("addressId"));
                conflictingCustomers.add(customer);
                return customer;
            }
        } catch(SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
        return null;
    }
    
    
    
    // Get Address ID from Address Line 1
    public static int getAddressId(String address) {
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT addressId FROM address WHERE address = '" + address + "'";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                addressIdValue = results.getInt("addressId");
            }
            statement.close();
            return addressIdValue;
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
            return 0;
        }
    }
    
    // Convert AddressId to Address
    public static String existingAddress(int id) {
        
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT address FROM address WHERE addressId=" + id;
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                existingAddressValue = results.getString("address");
            }
            statement.close();
            return existingAddressValue;
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
            return null;
        }
    }

    // Get Address List
    public static ObservableList getAddressList() {
        addressList.clear();
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT address FROM address";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                addressList.add(results.getString("address"));
            }
            statement.close();
            return addressList ;
        } catch (SQLException ex) {
            System.out.println("SQL Exception Error: " + ex.getMessage());
            return null;
        }
    }
}
