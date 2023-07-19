/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import scheduling.Country;
import scheduling.LoginController;
import utilities.DBConnection;

/**
 *
 * @author welea
 */
public class CountryDatabase {
    
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private static ObservableList countryList = FXCollections.observableArrayList();
    
    // Get All Countries
    public static ObservableList<Country> getAllCountries() {
        allCountries.clear();
        try{
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT country.countryId, country.country FROM country";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                Country country = new Country(
                        results.getInt("countryId"),
                        results.getString("country"));
                allCountries.add(country);
            }
            statement.close();
            return allCountries;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    // Add Country
    public static void addCountry(String country) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "INSERT country SET country= '" + country + "', createDate = CURRENT_DATE, createdBy = '" + LoginController.loggedInUser + "', lastUpdate= CURRENT_DATE, lastUpdateBy = '" + LoginController.loggedInUser + "'";
            
            statement.executeUpdate(query);
            int countryIdValue = getCountryId(country);

            System.out.println("Country " + countryIdValue + " has been added by " + LoginController.loggedInUser + ".");
            statement.close();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    // Update Country
    public static void updateCountry(String country, int id) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "UPDATE country SET country = '" + country + "' WHERE countryId = " + id;
            statement.executeUpdate(query);
            System.out.println("Country " + id + " has been updated.");
            statement.close();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }  
    
    // Delete Country
    public static void deleteCountry(int id) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "DELETE FROM country WHERE countryId=" + id;
            statement.executeUpdate(query);
            System.out.println("City " + id + "'" + getCountry(id) + ") was deleted.");
            statement.close();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    // Convert Country to CountryId
    public static int getCountryId(String country) {
        int existingCountryIdValue = 0;
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT countryId FROM country WHERE country=" + "'" + country + "'";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                existingCountryIdValue = results.getInt("countryId");
            }
            statement.close();
            return existingCountryIdValue;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return 0;
        }
    }
    
    // Convert Country ID to Country
    public static String getCountry(int id) {
        String existingCountryValue = "";
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT country FROM country WHERE countryId=" + id;
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                existingCountryValue = results.getString("country");
            }
            statement.close();
            return existingCountryValue;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    
    // Create List of Countries
    public static ObservableList getCountryList() {
        countryList.clear();
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT country FROM country";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                countryList.add(results.getString("country"));
            }
            statement.close();
            return countryList;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }
}
