package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

import Database.AddressDatabase;
import Database.AppointmentDatabase;
import Database.CustomerDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CustomerManagementController implements Initializable {

    // Customer Table
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> customerName;
    
    // Appointment Table
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> appointmentTitle;
    @FXML private TableColumn<Appointment, String> appointmentStart;
    @FXML private TableColumn<Appointment, String> appointmentEnd;
        
    // Customer Fields
    @FXML private TextField customerIdField;
    @FXML private TextField customerNameField;
    @FXML private ComboBox addressComboBox;

    // Address and AddressId variables
    private Customer selectedCustomer;
    
    // Screen Buttons
    @FXML private Button addCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private RadioButton monthlyRadioButton;
    @FXML private RadioButton weeklyRadioButton;
    @FXML private ToggleGroup monthlyWeeklyToggle;
    @FXML private Button manageAppointmentsButton;
    @FXML private Button manageAddressesButton;
    @FXML private Button backButton;

    
    
        // Add Customer Selected
    @FXML
    public void addCustomerSelected(ActionEvent event) throws IOException {
        
        Parent AddCustomerParent = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        Scene AddCustomerScene = new Scene(AddCustomerParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AddCustomerScene);
        window.show();   
    }
    
    // Update Customer Selected
    @FXML
    public void updateCustomerSelected(ActionEvent event) throws IOException {
        
        verifyCustomerIsSelected();
        int id = Integer.parseInt(customerIdField.getText());
        String name = customerNameField.getText();
        int addressId = AddressDatabase.getAddressId(String.valueOf(addressComboBox.getValue()));
        CustomerDatabase.updateCustomer(name, addressId, id);
        refreshCustomerFields();
    }

    // Delete Customer Selected
    @FXML
    public void deleteCustomerSelected(ActionEvent event) throws IOException {
        
        if(verifyCustomerIsSelected()){
            
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Customer Record");
        alert.setContentText("Delete Customer: " + selectedCustomer.getCustomerName() + " ?");
        alert.showAndWait().ifPresent((response -> {
            if(response == ButtonType.OK) {
                AppointmentDatabase.deleteCustomerAppointments(selectedCustomer.getCustomerId());
                CustomerDatabase.deleteCustomer(selectedCustomer.getCustomerId());
                refreshCustomerFields();
            }
        }));
        }

    }
    
    // Back Button Selected
    @FXML
    public void BackSelected(ActionEvent event) throws IOException {
        
        Parent ScheduledManagementParent = FXMLLoader.load(getClass().getResource("ScheduleManagement.fxml"));
        Scene ScheduledManagementScene = new Scene(ScheduledManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(ScheduledManagementScene);
        window.show();  
    }
    
    // Manage Appointments Selected
    @FXML
    public void manageAppointmentsSelected(ActionEvent event) throws IOException {
        
        Parent AppointmentManagementParent = FXMLLoader.load(getClass().getResource("AppointmentManagement.fxml"));
        Scene AppointmentManagementScene = new Scene(AppointmentManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AppointmentManagementScene);
        window.show();
    }    
    
    // Manage Addresses Selected
    @FXML
    public void manageAddressesSelected(ActionEvent event) throws IOException {
        
        Parent AddressManagementParent = FXMLLoader.load(getClass().getResource("AddressManagement.fxml"));
        Scene AddressManagementScene = new Scene(AddressManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AddressManagementScene);
        window.show();
    }    
    
    // Refresh Customer Fields and Tables
    public void refreshCustomerFields(){
        
        customerIdField.clear();
        customerNameField.clear();
        addressComboBox.setValue(null);
        customerIdField.setDisable(true);
        customerNameField.setDisable(true);
        addressComboBox.setDisable(true);
        customerTable.setItems(CustomerDatabase.getAllCustomers());
        customerTable.getSelectionModel().clearSelection();
        
        appointmentTable.setItems(null);
    }
    
        
    // Verify Customer is Selected
    public Boolean verifyCustomerIsSelected(){
        
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Customer Selected");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer to proceed.");
            alert.showAndWait();
            return false;
        }
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Generate Customer Table
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerTable.setItems(CustomerDatabase.getAllCustomers());

        // Generate Appointment Table
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        monthlyWeeklyToggle.selectToggle(monthlyRadioButton);

        refreshCustomerFields();

        // Generate Address List for Address ComboBox

        // Monthly/Weekly Calendar Selection (With Lambda Expression)
        monthlyWeeklyToggle.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if(customerTable.getSelectionModel().getSelectedItem() != null) {
                Customer selecteditem = customerTable.getSelectionModel().getSelectedItem();
                if (monthlyWeeklyToggle.getSelectedToggle() != null) {
                    if(monthlyRadioButton.isSelected()) {
                        appointmentTable.setItems(AppointmentDatabase.getMonthlyAppointments(selecteditem.getCustomerId()));
                    } else if(weeklyRadioButton.isSelected()) {
                        appointmentTable.setItems(AppointmentDatabase.getWeeklyAppoinments(selecteditem.getCustomerId()));
                    }
                }
            }
        });

        // Customer Selection Listener (With Lambda Expression)
        customerTable.getSelectionModel().selectedItemProperty().addListener( event -> {
            if(customerTable.getSelectionModel().getSelectedItem() != null) {
                Customer selecteditem = customerTable.getSelectionModel().getSelectedItem();

                //Enable Fields
                customerIdField.setDisable(true);
                customerNameField.setDisable(false);
                addressComboBox.setDisable(false);
            
                // Prefill Selected Customer Fields
                customerIdField.setText(String.valueOf(selecteditem.getCustomerId()));
                customerNameField.setText(selecteditem.getCustomerName());
                addressComboBox.setValue(AddressDatabase.existingAddress(selecteditem.getCustomerAddressId()));
                
                // Monthly/Weekly Calendar Selection (With Lambda Expression)
                if (monthlyWeeklyToggle.getSelectedToggle() != null) {
                    if(monthlyRadioButton.isSelected()) {
                        appointmentTable.setItems(AppointmentDatabase.getMonthlyAppointments(selecteditem.getCustomerId()));
                    } else if(weeklyRadioButton.isSelected()) {
                        appointmentTable.setItems(AppointmentDatabase.getWeeklyAppoinments(selecteditem.getCustomerId()));
                    }
                }
                
        
            }
        });
    }
}
