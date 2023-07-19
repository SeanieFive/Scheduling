package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

import Database.AddressDatabase;
import Database.CityDatabase;
import static Database.CityDatabase.getCityId;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AddressManagementController implements Initializable {


    // Address Table
    @FXML private TableView<Address> addressTable;
    @FXML private TableColumn<Address, String> addressNameColumn;  
    
    // Elements
    @FXML private TextField addressIdField;
    @FXML private TextField address1Field;
    @FXML private TextField address2Field;
    @FXML private ComboBox cityComboBox;
    @FXML private TextField postalCodeField;
    @FXML private TextField phoneNumberField;

    // Buttons
    @FXML private Button addCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private Button backButton;

    // Address List for ComboBox
    private Address selectedAddress;
    
    

    // Add Button Selected
    @FXML
    public void addAddressSelected(ActionEvent event) throws IOException {
        
        Parent AddAddressParent = FXMLLoader.load(getClass().getResource("AddAddress.fxml"));
        Scene AddAddressScene = new Scene(AddAddressParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AddAddressScene);
        window.show();   
    }

    // Update Button Selected
    @FXML
    void updateAddressSelected(ActionEvent event) throws IOException {
                
        verifyAddressIsSelected();
        int id = Integer.parseInt(addressIdField.getText());
        String address = address1Field.getText();
        String address2 = address2Field.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneNumberField.getText();
        int cityId = getCityId(String.valueOf(cityComboBox.getValue()));
        AddressDatabase.updateAddress (address, address2, cityId, postalCode, phone, id);
        refreshAddressFields();
    }

    // Delete Button Selected
    @FXML
    public void deleteAddressSelected(ActionEvent event) throws IOException {
        
        verifyAddressIsSelected();
        int selectedId = selectedAddress.getAddressId();
        Customer customer = AddressDatabase.checkCustomerTableForAddressId(selectedId);
        if(customer == null) { 
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete Address Record");
            alert.setContentText("Delete Address: " + selectedAddress.getAddressLine1() + " ?");
            alert.showAndWait().ifPresent((response -> {
                if(response == ButtonType.OK) {
                    AddressDatabase.deleteAddress(selectedAddress.getAddressId());
                    addressTable.setItems(AddressDatabase.getAllAddresses());
                    System.out.println("Address " + selectedAddress.getAddressId() + " (" + selectedAddress.getAddressLine1() + ") was deleted.");
                }
            }));
            refreshAddressFields();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Address");
            alert.setHeaderText("Address Can't be deleted");
            alert.setContentText("Address ID: " + selectedId + " is currently already assigned to a Customer. Please update the Customer's address before deleting.");
            alert.showAndWait();
        }
    }

    // Back Button Selected
    @FXML
    void backSelected(ActionEvent event) throws IOException {
        
        Parent ScheduleManagementParent = FXMLLoader.load(getClass().getResource("ScheduleManagement.fxml"));
        Scene ScheduleManagementScene = new Scene(ScheduleManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(ScheduleManagementScene);
        window.show();    
    }
    
    // Refresh Address Fields
    public void refreshAddressFields(){
        
        addressIdField.clear();
        address1Field.clear();
        address2Field.clear();
        postalCodeField.clear();
        phoneNumberField.clear();
        cityComboBox.setValue(null);
        addressIdField.setDisable(true);
        address1Field.setDisable(true);
        address2Field.setDisable(true);
        postalCodeField.setDisable(true);
        phoneNumberField.setDisable(true);
        cityComboBox.setDisable(true);
        addressTable.setItems(AddressDatabase.getAllAddresses());
    }
    
    // Verify Address is Selected
    public void verifyAddressIsSelected(){
        
        if(addressTable.getSelectionModel().getSelectedItem() != null) {
            selectedAddress = addressTable.getSelectionModel().getSelectedItem();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Address Selected");
            alert.setHeaderText("No Address Selected");
            alert.setContentText("Please select a Address to proceed.");
            alert.showAndWait();
            return;
        }
    }
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Load Address Table
        addressNameColumn.setCellValueFactory(new PropertyValueFactory<>("addressLine1"));
        addressTable.setItems(AddressDatabase.getAllAddresses());
        
        refreshAddressFields();

        //Load City Values in Combo List
        cityComboBox.setItems(CityDatabase.getCityList());
        
        // Address Selection Listener
        addressTable.getSelectionModel().selectedItemProperty().addListener(
        event -> {

            // Determine Selected Address
            if(addressTable.getSelectionModel().getSelectedItem() != null) {
                Address selecteditem = addressTable.getSelectionModel().getSelectedItem();

                // Enable Fields for Edit
                addressIdField.setDisable(true);
                address1Field.setDisable(false);
                address2Field.setDisable(false);
                cityComboBox.setDisable(false);
                postalCodeField.setDisable(false);
                phoneNumberField.setDisable(false);

                // Prefill Selected Customer Fields
                addressIdField.setText(String.valueOf(selecteditem.getAddressId()));
                address1Field.setText(selecteditem.getAddressLine1());
                address2Field.setText(selecteditem.getAddressLine2());
                cityComboBox.setValue(CityDatabase.getCity(selecteditem.getAddressCityId()));
                postalCodeField.setText(selecteditem.getPostalCode());
                phoneNumberField.setText(selecteditem.getPhone());
            }
        });
    }    
}
