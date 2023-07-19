package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

import Database.UserDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class LoginController implements Initializable {
    
    // Login Elements
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Login Buttons
    @FXML private Button loginButton;
    @FXML private Button exitButton;
    
    // Login Labels (for Lanaguage Change)
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label loginHeader;
    
    public static String loggedInUser;
    
    // Failed Login Strings (For Language Change)
    private String failedLoginTitle;
    private String failedLoginHeader;
    private String failedLoginMessage;
    
    // Exit Confirmation Strings (For Language Change)
    private String exitTitle;
    private String exitHeader;

    
    // Exit Button Selected
    @FXML
    public void exitProgram(ActionEvent event) throws IOException {
        
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle(exitTitle);
        exitAlert.setHeaderText(exitHeader);
        Optional<ButtonType> result = exitAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);        }
    }
    
    // Initiate Login Attempt
    @FXML
    public void LoginAttempt(ActionEvent event) throws IOException {
        
        String usernameEntry = usernameField.getText();
        String passwordEntry = passwordField.getText();
        boolean userVerified = UserDatabase.login(usernameEntry, passwordEntry);
        
        if(userVerified) {
            loggedInUser = usernameEntry;
            Parent ScheduleManagementParent = FXMLLoader.load(getClass().getResource("ScheduleManagement.fxml"));
            Scene ScheduleManagementScene = new Scene(ScheduleManagementParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(ScheduleManagementScene);
            window.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(failedLoginTitle);
            alert.setHeaderText(failedLoginHeader);
            alert.setContentText(failedLoginMessage);
            alert.showAndWait();
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Check Default Lanaguage
        Locale locale = Locale.getDefault();
        System.out.println("Default Lanauge: " + locale);
        rb = ResourceBundle.getBundle("LanguageBundles/Language", locale);

        // Set Field Language Based on Default Language
        loginHeader.setText(rb.getString("Schedule") + " " + rb.getString("Login"));
        usernameLabel.setText(rb.getString("Username") + ":");
        usernameField.setPromptText(rb.getString("Username"));
        passwordLabel.setText(rb.getString("Password") + ":");
        passwordField.setPromptText(rb.getString("Password"));
        loginButton.setText(rb.getString("Login"));
        exitButton.setText(rb.getString("Exit"));
        failedLoginTitle = rb.getString("failedLoginTitle");
        failedLoginHeader = rb.getString("failedLoginHeader");
        failedLoginMessage = rb.getString("failedLoginMessage");
        exitTitle = rb.getString("exitTitle");
        exitHeader = rb.getString("exitHeader");
    }
}
