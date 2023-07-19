package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import utilities.DBConnection;

public class ReportManagementController implements Initializable {

    // Report Buttons
    @FXML private Button generateReportButton;
    @FXML private Button backButton;
    
    // Report Elements
    @FXML private RadioButton monthlyAppointmentsButton;
    @FXML private RadioButton userSchedulesButton;
    @FXML private RadioButton customerAppointmentsButton;
    @FXML private ToggleGroup reportsToggle;
    @FXML private TextArea reportTextArea;
    
    // Determine Which Report to Generate
    @FXML
    void generateReportSelected(ActionEvent event) {
        
        if(reportsToggle.getSelectedToggle() == monthlyAppointmentsButton) {
            generateMonthlyReport();
        } if(reportsToggle.getSelectedToggle() == userSchedulesButton) {
            generateConsultantReport();
        } if(reportsToggle.getSelectedToggle() == customerAppointmentsButton) {
            generateCustomerAppointments();
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
    
    
    // Generate Monthly Report
    public void generateMonthlyReport() {
        
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT title, MONTHNAME(start) as 'Month', COUNT(*) as 'Total' FROM appointment GROUP BY title, MONTH(start)";
            ResultSet results = statement.executeQuery(query);
            StringBuilder monthlyAppointmentsString = new StringBuilder();
            monthlyAppointmentsString.append(String.format("%1$-30s %2$-30s %3$s \n", "Month", "Appointment Type", "Total Appointments"));
            monthlyAppointmentsString.append(String.join("", Collections.nCopies(110, "-")));
            monthlyAppointmentsString.append("\n");
            while(results.next()) {
                monthlyAppointmentsString.append(String.format("%1$-35s %2$-40s %3$d \n", results.getString("Month"), results.getString("title"), results.getInt("Total")));
            }
            statement.close();
            reportTextArea.clear();
            reportTextArea.setText(monthlyAppointmentsString.toString());
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
    }    

    // Generate Consultant Report
    public void generateConsultantReport() {
        
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT appointment.contact, appointment.title, customer.customerName, start, end FROM appointment JOIN customer ON customer.customerId = appointment.customerId GROUP BY appointment.contact, MONTH(start), start";
            ResultSet results = statement.executeQuery(query);
            StringBuilder ConsultantReportString = new StringBuilder();
            ConsultantReportString.append(String.format("%1$-25s %2$-30s %3$-35s %4$-35s %5$s \n", "Consultant", "Title", "Customer", "Start", "End"));
            ConsultantReportString.append(String.join("", Collections.nCopies(110, "-")));
            ConsultantReportString.append("\n");
            while(results.next()) {
                String contact = results.getString("contact");
                String title = results.getString("title");
                String customerName = results.getString("customerName");
                String start = results.getString("start");
                String end = results.getString("end");
                ConsultantReportString.append(String.format("%1$-" + (25) + "s %2$-" + (25) + "s %3$-" + (25) + "s %4$-" + (25) + "s %5$s \n", contact, title, customerName, start, end));
            }
            statement.close();
            reportTextArea.clear();
            reportTextArea.setText(ConsultantReportString.toString());
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
    }    

    // Generate Customer Appointments
    public void generateCustomerAppointments() {
        
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "SELECT customer.customerName, COUNT(*) as 'Total' FROM customer JOIN appointment ON customer.customerId = appointment.customerId GROUP BY customerName";
            ResultSet results = statement.executeQuery(query);
            StringBuilder customerReportString = new StringBuilder();
            customerReportString.append(String.format("%1$-65s %2$-65s \n", "Customer", "Total Appointments"));
            customerReportString.append(String.join("", Collections.nCopies(110, "-")));
            customerReportString.append("\n");
            while(results.next()) {
                customerReportString.append(String.format("%1$s %2$65d \n", results.getString("customerName"), results.getInt("Total")));
            }
            statement.close();
            reportTextArea.setText(customerReportString.toString());
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
    }    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
}
