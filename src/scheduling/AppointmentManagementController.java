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
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.beans.Observable;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class AppointmentManagementController implements Initializable {

    // Appointment Table
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> appointmentTitleColumn;
    
    // Appointment Fields
    @FXML private Label appointmentIdLabel;
    @FXML private ComboBox customerComboBox;
    @FXML private ComboBox userComboBox;
    @FXML private TextField descriptionField;
    @FXML private TextField titleField;
    @FXML private TextField locationField;
    @FXML private TextField contactField;
    @FXML private TextField typeField;
    @FXML private TextField urlField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox startHourBox;
    @FXML private ComboBox startMinutesBox;
    @FXML private ComboBox endHourBox;
    @FXML private ComboBox endMinutesBox;
    
    // Appointment Buttons
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button updateButton;
    @FXML private Button backButton;
    
    private Appointment selectedAppointment;

    

    // Back Button Selected
    @FXML
    void backSelected(ActionEvent event) throws IOException {
        
        Parent ScheduleManagementParent = FXMLLoader.load(getClass().getResource("ScheduleManagement.fxml"));
        Scene ScheduleManagementScene = new Scene(ScheduleManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(ScheduleManagementScene);
        window.show();    
    }
    
    // Add Button Selected
    @FXML
    void addSelected(ActionEvent event) throws IOException {
        
        Parent AddAppointmentParent = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
        Scene AddAppointmentScene = new Scene(AddAppointmentParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AddAppointmentScene);
        window.show();    
    }
    
    // Update Button Selected
    @FXML
    void updateSelected(ActionEvent event) throws IOException {
        
        verifyAppointmentIsSelected();
        int appointmentId = Integer.parseInt(appointmentIdLabel.getText());
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
            AppointmentDatabase.updateAppointment(appointmentId, customerId, userId, description, title, location, contact, type, url, start, end);
            refreshFields();
        }
    }

    // Delete Button Selected
    @FXML
    public void deleteSelected(ActionEvent event) throws IOException {
        
        verifyAppointmentIsSelected();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Appointment");
        alert.setContentText("Delete Appointment: " + selectedAppointment.getTitle() + " ?");
        alert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK) {
                AppointmentDatabase.deleteAppointment(selectedAppointment.getAppointmentId());
            }    
        }));
        refreshFields();
    }
       
    // Refresh Appointment Fields
    public void refreshFields() {
        appointmentIdLabel.setText("");
        customerComboBox.setValue(null);
        userComboBox.setValue(null);
        titleField.clear();
        descriptionField.clear();
        locationField.clear();
        contactField.clear();
        typeField.clear();
        urlField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        startHourBox.setValue(null);
        startMinutesBox.setValue(null);
        endHourBox.setValue(null);
        endMinutesBox.setValue(null);
        customerComboBox.setDisable(true);
        userComboBox.setDisable(true);
        titleField.setDisable(true);
        descriptionField.setDisable(true);
        locationField.setDisable(true);
        contactField.setDisable(true);
        typeField.setDisable(true);
        urlField.setDisable(true);
        startDatePicker.setDisable(true);
        endDatePicker.setDisable(true);
        startHourBox.setDisable(true);
        startMinutesBox.setDisable(true);
        endHourBox.setDisable(true);
        endMinutesBox.setDisable(true);
        appointmentTable.setItems(AppointmentDatabase.getAllAppointments());
    }
    
    // Verify Appointment is Selected
    public void verifyAppointmentIsSelected(){
        
        if(appointmentTable.getSelectionModel().getSelectedItem() != null) {
            selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Appointment Selected");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select a Appointment to proceed.");
            alert.showAndWait();
            return;
        }
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Load Appointment Table
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentTable.setItems(AppointmentDatabase.getAllAppointments());

        refreshFields();

        // Load Customer/User Values in ComboBoxs
        customerComboBox.setItems(CustomerDatabase.getCustomerList());
        userComboBox.setItems(UserDatabase.getUserList());
        startMinutesBox.setItems(AppointmentDatabase.getMinuteOptions());
        endMinutesBox.setItems(AppointmentDatabase.getMinuteOptions());
        startHourBox.setItems(AppointmentDatabase.getHourOptions());
        endHourBox.setItems(AppointmentDatabase.getHourOptions());
 
        // Appointment Selection Listener
        appointmentTable.getSelectionModel().selectedIndexProperty().addListener(
                (Observable event) -> {
                    
                    // Determine Selected Appointment
                    if(appointmentTable.getSelectionModel().getSelectedItem() != null) {
                        Appointment selecteditem = appointmentTable.getSelectionModel().getSelectedItem();
                                                
                        // Enable Fields
                        customerComboBox.setDisable(false);
                        userComboBox.setDisable(false);
                        titleField.setDisable(false);
                        descriptionField.setDisable(false);
                        locationField.setDisable(false);
                        contactField.setDisable(false);
                        typeField.setDisable(false);
                        urlField.setDisable(false);
                        startDatePicker.setDisable(false);
                        endDatePicker.setDisable(false);
                        startHourBox.setDisable(false);
                        startMinutesBox.setDisable(false);
                        endHourBox.setDisable(false);
                        endMinutesBox.setDisable(false);
                        
                        // Prefill Selected Customer Fields
                        appointmentIdLabel.setText(String.valueOf(selecteditem.getAppointmentId()));
                        customerComboBox.setValue(CustomerDatabase.getCustomerName(selecteditem.getCustomerId()));
                        userComboBox.setValue(UserDatabase.existingUser(selecteditem.getUserId()));
                        titleField.setText(selecteditem.getTitle());
                        descriptionField.setText(selecteditem.getDescription());
                        locationField.setText(selecteditem.getLocation());
                        contactField.setText(selecteditem.getContact());
                        typeField.setText(selecteditem.getType());
                        urlField.setText(selecteditem.getUrl());

                        // Building Start Date and Start Time
                        String startDateTime = AppointmentDatabase.getStartDateTime(selecteditem.getAppointmentId());
                        int startYear = Integer.parseInt(startDateTime.substring(0, 4));
                        int startMonth = Integer.parseInt(startDateTime.substring(5, 7));
                        int startDay = Integer.parseInt(startDateTime.substring(8, 10));
                        int startHour = Integer.parseInt(startDateTime.substring(11, 13));
                        int startMinutes = Integer.parseInt(startDateTime.substring(14, 16));
                        LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
                        LocalTime startTime = LocalTime.of(startHour, startMinutes);
                        ZoneId localTimeZone = ZoneId.of(TimeZone.getDefault().getID());
                        ZonedDateTime convertedStartLocalZDT = AppointmentDatabase.convertTimeToLocal(startDate, startTime, localTimeZone);
                        LocalDate convertedStartDate = convertedStartLocalZDT.toLocalDate();
                        int convertedStartHour = convertedStartLocalZDT.getHour();
                        int convertedStartMinutes = convertedStartLocalZDT.getMinute();
                        
                        startDatePicker.setValue(convertedStartDate);
                        startHourBox.getSelectionModel().select(convertedStartHour);

                        switch (convertedStartMinutes) {
                            case 00:
                                startMinutesBox.getSelectionModel().select("00");
                                break;
                            case 15:
                                startMinutesBox.getSelectionModel().select("15");
                                break;
                            case 30:
                                startMinutesBox.getSelectionModel().select("30");
                                break;
                            case 45:
                                startMinutesBox.getSelectionModel().select("45");
                                break;
                            default:
                                break;
                        }

                        // Building End Date and End Time
                        String endDateTime = AppointmentDatabase.getEndDateTime(selecteditem.getAppointmentId());
                        int endYear = Integer.parseInt(endDateTime.substring(0, 4));
                        int endMonth = Integer.parseInt(endDateTime.substring(5, 7));
                        int endDay = Integer.parseInt(endDateTime.substring(8, 10));
                        int endHour = Integer.parseInt(endDateTime.substring(11, 13));
                        int endMinutes = Integer.parseInt(endDateTime.substring(14, 16));
                        LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);
                        LocalTime endTime = LocalTime.of(endHour, endMinutes);
                        ZonedDateTime convertedEndLocalZDT = AppointmentDatabase.convertTimeToLocal(endDate, endTime, localTimeZone);
                        LocalDate convertedEndDate = convertedEndLocalZDT.toLocalDate();
                        int convertedEndHour = convertedEndLocalZDT.getHour();
                        int convertedEndMinutes = convertedEndLocalZDT.getMinute();

                        endDatePicker.setValue(convertedEndDate);
                        endHourBox.getSelectionModel().select(convertedEndHour);
                        
                        switch (convertedEndMinutes) {
                            case 00:
                                endMinutesBox.getSelectionModel().select("00");
                                break;
                            case 15:
                                endMinutesBox.getSelectionModel().select("15");
                                break;
                            case 30:
                                endMinutesBox.getSelectionModel().select("30");
                                break;
                            case 45:
                                endMinutesBox.getSelectionModel().select("45");
                                break;
                            default:
                                break;
                        }
                    }
                }
        );  
    }    
}
