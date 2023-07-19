package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

import Database.AddressDatabase;
import Database.CityDatabase;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddAddressController implements Initializable {

    // Add Address Fields
    @FXML private TextField address1Field;
    @FXML private TextField address2Field;
    @FXML private ComboBox cityComboBox;
    @FXML private TextField postalCodeField;
    @FXML private TextField phoneNumberField;
    
    // Add Address Buttons    
    @FXML private Button addButton;
    @FXML private Button cancelButton;

    // Add Button Selected
    @FXML
    private void addSelected(ActionEvent event) throws IOException {

        String address = address1Field.getText();
        String address2 = address2Field.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneNumberField.getText();
        int cityId = CityDatabase.getCityId(String.valueOf(cityComboBox.getValue()));
        AddressDatabase.addAddress(address, address2, cityId, postalCode, phone);
            
         // Return to Address Management
        Parent AddressManagementParent = FXMLLoader.load(getClass().getResource("AddressManagement.fxml"));
        Scene AddressManagementScene = new Scene(AddressManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AddressManagementScene);
        window.show();
    }
    
    // Cancel Button Selected
    @FXML
    public void cancelSelected(ActionEvent event) throws IOException {
        Parent AddressManagementParent = FXMLLoader.load(getClass().getResource("AddressManagement.fxml"));
        Scene AddressManagementScene = new Scene(AddressManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(AddressManagementScene);
        window.show();    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        //Load City Values in Combo List
        cityComboBox.setItems(CityDatabase.getCityList());
    }    
}
