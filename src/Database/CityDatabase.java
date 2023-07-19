package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduling.City;
import scheduling.LoginController;
import utilities.DBConnection;

public class CityDatabase {

    private static ObservableList<City> allCities = FXCollections.observableArrayList();
    private static ObservableList cityList = FXCollections.observableArrayList();

    // Get All Cities
    public static ObservableList<City> getAllCities() {
        allCities.clear();
        try{
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT city.cityId, city.city, country.countryId FROM city INNER JOIN country ON city.countryId = country.countryId";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                City city = new City(
                        results.getInt("cityId"),
                        results.getString("city"),
                        results.getInt("countryId"));
                allCities.add(city);
            }
            statement.close();
            return allCities;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    // Add City
    public static void addCity(String city, int countryId) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "INSERT city SET city= '" + city + "', countryId= '" + countryId + "', createDate = CURRENT_DATE, createdBy = '" + LoginController.loggedInUser + "', lastUpdate= CURRENT_DATE, lastUpdateBy = '" + LoginController.loggedInUser + "'";

            statement.executeUpdate(query);
            int cityIdValue = getCityId(city);
        
            System.out.println("City " + cityIdValue + " has been added by " + LoginController.loggedInUser + ".");
            statement.close();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    } 
    
    // Update City
    public static void updateCity(String city, int countryId, int id) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "UPDATE city SET city = '" + city + "', countryId = '" + countryId + "' WHERE cityId = " + id;
            statement.executeUpdate(query);
            System.out.println("City " + id + " has been updated.");
            
            statement.close();

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    // Delete City
    public static void deleteCity(int id) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "DELETE FROM city WHERE cityId=" + id;

            statement.executeUpdate(query);

            System.out.println("City " + id + "(" + getCity(id) + " was deleted.");
            
            statement.close();
        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }
    
    // Convert City to CityId
    public static int getCityId(String city) {
        int existingCityIdValue = 0;
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT cityId FROM city WHERE city=" + "'" + city + "'";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                existingCityIdValue = results.getInt("cityId");
            }
            statement.close();
            return existingCityIdValue;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return 0;
        }
    }
    
    // Convert City ID to City
    public static String getCity(int id) {
        String existingCityValue = "";
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT city FROM city WHERE cityId=" + id;
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                existingCityValue = results.getString("city");
            }
            statement.close();
            return existingCityValue;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    // Create List of Cities
    public static ObservableList getCityList() {
        cityList.clear();
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT city FROM city";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                cityList.add(results.getString("city"));
            }
            statement.close();
            return cityList;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }
}
    

