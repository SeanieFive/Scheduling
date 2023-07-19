package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

import Database.AppointmentDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class ScheduleManagementController implements Initializable {
    
    // Schedule Management Buttons
    @FXML private Button CustomersButton;
    @FXML private Button addressesButton;
    @FXML private Button AppointmentsButton;
    @FXML private Button reportsButton;
    @FXML private Button logoutButton;
    
    // Schedule Management Fields:
    @FXML private Label usernameVariable;
    
    
    // Logout Button Selected
    @FXML
    public void LogoutSelected(ActionEvent event) throws IOException {
        
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Logout");
        exitAlert.setHeaderText("Are you sure you would like to logout?");
        Optional<ButtonType> result = exitAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent LoginParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene LoginScene = new Scene(LoginParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(LoginScene);
            window.show();
        }
    }

    // Customers Button Selected
    @FXML
    public void customersSelected(ActionEvent event) throws IOException {
                
        Parent CustomerManagementParent = FXMLLoader.load(getClass().getResource("CustomerManagement.fxml"));
        Scene CustomerManagementScene = new Scene(CustomerManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(CustomerManagementScene);
        window.show();    
    }
    
    
    // Appointments Button Selected
    @FXML
    public void appointmentsSelected(ActionEvent event) throws IOException {
                
        Parent AppointmentManagementParent = FXMLLoader.load(getClass().getResource("AppointmentManagement.fxml"));
        Scene AppointmentManagementScene = new Scene(AppointmentManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AppointmentManagementScene);
        window.show();    
    }    
    
    // Address Button Selected
    @FXML
    public void addressesSelected(ActionEvent event) throws IOException {

        Parent AddressManagementParent = FXMLLoader.load(getClass().getResource("AddressManagement.fxml"));
        Scene AddressManagementScene = new Scene(AddressManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AddressManagementScene);
        window.show();    
    }

    // Reports Button Selected
    @FXML
    public void reportsSelected(ActionEvent event) throws IOException {
        
         Parent ReportManagementParent = FXMLLoader.load(getClass().getResource("ReportManagement.fxml"));
        Scene ReportManagementScene = new Scene(ReportManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(ReportManagementScene);
        window.show(); 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Check for 15 Minute Appointment Alert
        Appointment appointment = AppointmentDatabase.determine15MinuteAlert();
        if(appointment != null) {
            String title = appointment.getTitle();
            int id = appointment.getAppointmentId();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Reminder");
            alert.setHeaderText("Appointment Within 15 Minutes");
            alert.setContentText(title + " ( Appointment ID: " + id + ") appointment is within 15 minutes.");
            alert.showAndWait();
        } 
    }    
}
