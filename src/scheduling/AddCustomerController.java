
package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

import Database.AddressDatabase;
import Database.CustomerDatabase;
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

public class AddCustomerController implements Initializable {
    
    // Add Customer Fields
    @FXML private TextField customerNameField;
    @FXML private ComboBox addressComboBox;

    // Add Customer Buttons
    @FXML private Button addButton;
    @FXML private Button cancelButton;  
    
    
    // Add Button Selected
    @FXML
    public void addSelected(ActionEvent event) throws IOException {
        
        String name = customerNameField.getText();
        int addressId = AddressDatabase.getAddressId(String.valueOf(addressComboBox.getValue()));
        CustomerDatabase.addCustomer(name, addressId);
        
        // Return to Customer Management
        Parent CustomerManagementParent = FXMLLoader.load(getClass().getResource("CustomerManagement.fxml"));
        Scene CustomerManagementScene = new Scene(CustomerManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(CustomerManagementScene);
        window.show();
    }
 
    // Cancel Button Selected
    @FXML
    public void cancelSelected(ActionEvent event) throws IOException {
        Parent CustomerManagementParent = FXMLLoader.load(getClass().getResource("CustomerManagement.fxml"));
        Scene CustomerManagementScene = new Scene(CustomerManagementParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(CustomerManagementScene);
        window.show();    
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Load Address Values in Combo List
        addressComboBox.setItems(AddressDatabase.getAddressList());
    }        
}
