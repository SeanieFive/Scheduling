package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

import Database.AppointmentDatabase;
import Database.CustomerDatabase;
import Database.UserDatabase;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AddAppointmentController implements Initializable {

    // Add Appointment Fields
    @FXML private TextField descriptionField;
    @FXML private TextField titleField;
    @FXML private TextField locationField;
    @FXML private TextField contactField;
    @FXML private TextField typeField;
    @FXML private TextField urlField;
    @FXML private ComboBox userComboBox;
    @FXML private ComboBox customerComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox startHourBox;
    @FXML private ComboBox startMinutesBox;
    @FXML private ComboBox endHourBox;
    @FXML private ComboBox endMinutesBox;
    
    // Add Appointment Buttons
    @FXML private Button addButton;
    @FXML private Button cancelButton;
    
    // Add Button Selected
    @FXML 
    public void addSelected(ActionEvent event) throws IOException {

        int customerId = CustomerDatabase.getCustomerId(String.valueOf(customerComboBox.getValue()));
        int userId = UserDatabase.getUserId(String.valueOf(userComboBox.getValue()));
        String description = descriptionField.getText();
        String title = titleField.getText();
        String location = locationField.getText();
        String contact = contactField.getText();
        String type = typeField.getText();
        String url = urlField.getText();
        LocalDate enteredStartDate = startDatePicker.getValue();
        LocalDate enteredEndDate = endDatePicker.getValue();
        String enteredStartTime = (String.valueOf(startHourBox.getValue()) + ":" + String.valueOf(startMinutesBox.getValue()));
        String enteredEndTime = (String.valueOf(endHourBox.getValue()) + ":" + String.valueOf(endMinutesBox.getValue()));

        // Convert Time String to LocalTime
        LocalTime enteredStartLocalTime = LocalTime.parse(enteredStartTime);
        LocalTime enteredEndLocalTime = LocalTime.parse(enteredEndTime);
        LocalDateTime enteredStartDateTime = LocalDateTime.of(enteredStartDate, enteredStartLocalTime);
        LocalDateTime enteredEndDateTime = LocalDateTime.of(enteredEndDate, enteredEndLocalTime);

        // Retrieve Local Zone Ids
        ZoneId enteredZoneId = ZoneId.of(TimeZone.getDefault().getID());

        String start = AppointmentDatabase.convertTimeToGMT(enteredStartDate , enteredStartLocalTime, enteredZoneId);
        String end = AppointmentDatabase.convertTimeToGMT(enteredEndDate , enteredEndLocalTime, enteredZoneId);

        // Set Business Hours
        LocalTime startBusinessHours = LocalTime.of(8,00);
        LocalTime endBusinessHours = LocalTime.of(17,00);
        
        // Check for Start Date greater than End Date
        if((enteredStartDateTime.isAfter(enteredEndDateTime)) || (enteredStartDateTime.isEqual(enteredEndDateTime))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Date Range");
            alert.setHeaderText("Invalid Date Range");
            alert.setContentText("Start Date/Time must be before the End Date/Time.");
            alert.showAndWait();
        }
        
        // Check Start/End is within Business Hours
        else if(enteredStartLocalTime.isBefore(startBusinessHours) || enteredEndLocalTime.isAfter(endBusinessHours) || enteredStartLocalTime.isAfter(endBusinessHours) || enteredEndLocalTime.isBefore(startBusinessHours)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Date Range");
            alert.setHeaderText("Invalid Date Range");
            alert.setContentText("Appointment times must be within the following business hours " + startBusinessHours + " - " + endBusinessHours);
            alert.showAndWait();   
        } else {
        AppointmentDatabase.addAppointment(customerId, userId, description, title, location, contact, type, url, start, end);
        
        // Return to Appointment Management
        Parent AppointmentManagementParent = FXMLLoader.load(getClass().getResource("AppointmentManagement.fxml"));
        Scene AppointmentManagementScene = new Scene(AppointmentManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AppointmentManagementScene);
        window.show();
        }        


    }
    
    // Cancel Button Selected
    @FXML
    public void cancelSelected(ActionEvent event) throws IOException {
        Parent CityManagementParent = FXMLLoader.load(getClass().getResource("CityManagement.fxml"));
        Scene CityManagementScene = new Scene(CityManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(CityManagementScene);
        window.show();
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       
        customerComboBox.setItems(CustomerDatabase.getCustomerList());
        userComboBox.setItems(UserDatabase.getUserList());
        startMinutesBox.setItems(AppointmentDatabase.getMinuteOptions());
        endMinutesBox.setItems(AppointmentDatabase.getMinuteOptions());
        startHourBox.setItems(AppointmentDatabase.getHourOptions());
        endHourBox.setItems(AppointmentDatabase.getHourOptions());
    }    
}
