package Database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduling.Appointment;
import scheduling.LoginController;
import utilities.DBConnection;

public class AppointmentDatabase {

    private static final ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static final ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
    private static final ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();

    private static ObservableList allMinuteOptions = FXCollections.observableArrayList();
    private static ObservableList allHourOptions = FXCollections.observableArrayList();
    private static ObservableList<Integer> appointmentIdList = FXCollections.observableArrayList();

    // Convert Local Date/Time to GMT Date/Time
    public static String convertTimeToGMT(LocalDate date, LocalTime time, ZoneId zoneId) {
        
        ZonedDateTime requestedZDT = ZonedDateTime.of(date, time, zoneId);
        Instant requestedToGMT = requestedZDT.toInstant();
        ZonedDateTime convertedGMTZDT = ZonedDateTime.ofInstant(requestedToGMT, ZoneId.of("GMT"));
        String convertedDate = String.valueOf(convertedGMTZDT.getYear() + "-" + convertedGMTZDT.getMonthValue() + "-"+ convertedGMTZDT.getDayOfMonth());
        String convertedTime = String.valueOf(convertedGMTZDT.getHour() + ":" + convertedGMTZDT.getMinute());
        String convertedGMT = convertedDate + " " + convertedTime;
        return convertedGMT;
    }
    
    // Convert GMT Time to Local Date/Time
    public static ZonedDateTime convertTimeToLocal(LocalDate date, LocalTime time, ZoneId defaultZoneId) {
        
        ZoneId GMTTimeZone = ZoneId.of("GMT");
        ZonedDateTime requestedZDT = ZonedDateTime.of(date, time, GMTTimeZone);
        Instant requestedToLocal = requestedZDT.toInstant();
        ZonedDateTime convertedLocalZDT = ZonedDateTime.ofInstant(requestedToLocal, defaultZoneId);
        return convertedLocalZDT;
    }
    
    // Convert String to Local Date Time
    public static LocalDateTime convertStringToLocalDateTime(String dateString){
        
        int startYear = Integer.parseInt(dateString.substring(0, 4));
        int startMonth = Integer.parseInt(dateString.substring(5, 7));
        int startDay = Integer.parseInt(dateString.substring(8, 10));
        int startHour = Integer.parseInt(dateString.substring(11, 13));
        int startMinutes = Integer.parseInt(dateString.substring(14, 16));
        LocalDateTime dateLDT = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinutes);
        return dateLDT;
    }

    // Add Appointment
    public static void addAppointment(int customerId, int userId, String description, String title, String location, String contact, String type, String url, String start, String end) throws IOException {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "INSERT appointment SET customerId = '" + customerId + "', userId = '" + userId + "', description = '" + description + "', title = '" + title + "', location = '" + location + "', contact = '" + contact + "', type = '" + type + "', url = '" + url + "', start = '" + start + "', end = '" + end + "', createDate = CURRENT_DATE, createdBy = '" + LoginController.loggedInUser + "', lastUpdate= CURRENT_DATE, lastUpdateBy = '" + LoginController.loggedInUser + "'";
            statement.executeUpdate(query);
            System.out.println(title + " appointment has been added.");
            statement.close();
        }
        catch(SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
    }
   
    // Update Appointment
   public static void updateAppointment(int appointmentId, int customerId, int userId, String description, String title, String location, String contact, String type, String url, String start, String end) throws IOException {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "UPDATE appointment SET customerId = '" + customerId + "', userId = '" + userId + "', description = '" + description + "', title = '" + title + "', location = '" + location + "', contact = '" + contact + "', type = '" + type + "', url = '" + url + "', start = '" + start + "', end = '" + end + "' WHERE appointmentId = " + appointmentId;
            statement.executeUpdate(query);
            System.out.println("Appointment " + appointmentId + " has been updated.");
            statement.close();
        }
        catch(SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());  
        }
    }
    
   // Delete Appointment
    public static void deleteAppointment(int id) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "DELETE FROM appointment WHERE appointmentId=" + id;
            statement.executeUpdate(query);
            System.out.println("Appointment " + id + " was deleted.");
            statement.close();
        }
        catch(SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
    }
   
   // Delete All Customer Appointments
    public static void deleteCustomerAppointments(int customerId) {
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "DELETE FROM appointment WHERE customerId=" + customerId;
            statement.executeUpdate(query);
            System.out.println("All appointments for Customer " + customerId + " have been deleted.");
            statement.close();
        }
        catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    // Retrieve All Appointments
    public static ObservableList<Appointment> getAllAppointments() {
        allAppointments.clear();
        try{
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, appointment.description, appointment.location, appointment.contact, appointment.type, appointment.url, appointment.start, appointment.end FROM appointment";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                Appointment appointment = new Appointment(
                        results.getInt("appointmentId"),results.getInt("customerId"),results.getInt("userId"),results.getString("title"),results.getString("description"),results.getString("location"), results.getString("contact"), results.getString("type"), results.getString("url"), results.getString("start"),results.getString("end")
                );
                allAppointments.add(appointment);
            }
            statement.close();
            return allAppointments;
        }
        catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
            return null;
        }
    }

//    //Retrieve Customer Specific Appointments
//    public static ObservableList<Appointment> getCustomerAppointments(int customerId) {
//        customerAppointments.clear();
//        try{
//            Statement statement = DBConnection.checkConnection().createStatement();
//            String query = "SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, appointment.description, appointment.location, appointment.contact, appointment.type, appointment.url, appointment.start, appointment.end FROM appointment WHERE customerId =" + customerId;
//            ResultSet results = statement.executeQuery(query);
//            while(results.next()) {
//                Appointment appointment = new Appointment(
//                    results.getInt("appointmentId"),results.getInt("customerId"),results.getInt("userId"),results.getString("title"),results.getString("description"),results.getString("location"), results.getString("contact"), results.getString("type"), results.getString("url"), results.getString("start"),results.getString("end")
//                );
//                customerAppointments.add(appointment);
//            }
//            statement.close();
//            return customerAppointments;
//        }
//        catch (SQLException e) {
//            System.out.println("SQLException: " + e.getMessage());
//            return null;
//        }
//    }
    
    //Retrieve User Specific Appointments
    public static ObservableList<Appointment> getUserAppointments(int userId) {
        userAppointments.clear();
        try{
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT appointment.appointmentId, appointment.customerId, appointment.userId, appointment.title, appointment.description, appointment.location, appointment.contact, appointment.type, appointment.url, appointment.start, appointment.end FROM appointment WHERE userId =" + userId;
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                Appointment appointment = new Appointment(
                    results.getInt("appointmentId"),results.getInt("customerId"),results.getInt("userId"),results.getString("title"),results.getString("description"),results.getString("location"), results.getString("contact"), results.getString("type"), results.getString("url"), results.getString("start"),results.getString("end")
                );
                userAppointments.add(appointment);
            }
            statement.close();
            return userAppointments;
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
    
    // Get List Of Appointment IDs
    public static ObservableList<Integer> getListOfAppointmentIds() {
        appointmentIdList.clear();
        try{
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT appointment.appointmentId FROM appointment";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                    int appointmentId = results.getInt("appointmentId");
                
                appointmentIdList.add(appointmentId);
                System.out.println(appointmentId + " was added to list");
            }
            statement.close();
            return appointmentIdList;
        }
        catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
            return null;
        }
    }
    
    //Get Start Date and Start Time
    public static String getStartDateTime(int appointmentId) {
        String start = "";
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT start FROM appointment WHERE appointmentId = " + appointmentId;
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                start = results.getString("start");
            }
            statement.close();
            return start;
        }
        catch(SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                return null;
            }
    }
    
    // Get End Date and End Time
    public static String getEndDateTime(int appointmentId) {
        String end = "";
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT end FROM appointment WHERE appointmentId = " + appointmentId;
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                end = results.getString("end");
            }
            statement.close();
            return end; 
        }
        catch(SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
            return null;
        }
    }
    
    // Create Appointment Minute Options
    public static ObservableList getMinuteOptions() {
        allMinuteOptions.clear();
        allMinuteOptions.add("00");
        allMinuteOptions.add("15");
        allMinuteOptions.add("30");
        allMinuteOptions.add("45");
        return allMinuteOptions;  
    }

    // Create Appointment Hour Options
    public static ObservableList getHourOptions() {
        allHourOptions.clear();
        allHourOptions.add("00");
        allHourOptions.add("01");
        allHourOptions.add("02");
        allHourOptions.add("03");
        allHourOptions.add("04");
        allHourOptions.add("05");
        allHourOptions.add("06");
        allHourOptions.add("07");
        allHourOptions.add("08");
        allHourOptions.add("09");
        allHourOptions.add("10");
        allHourOptions.add("11");
        allHourOptions.add("12");
        allHourOptions.add("13");
        allHourOptions.add("14");
        allHourOptions.add("15");
        allHourOptions.add("16");
        allHourOptions.add("17");
        allHourOptions.add("18");
        allHourOptions.add("19");
        allHourOptions.add("20");
        allHourOptions.add("21");
        allHourOptions.add("22");
        allHourOptions.add("23");
        return allHourOptions;  
    }   
    
    // Determine 15 Minute Alert
    public static Appointment determine15MinuteAlert(){
        
    Appointment appointment;
    LocalDateTime now = LocalDateTime.now();
    ZoneId zid = ZoneId.systemDefault();
    ZonedDateTime zdt = now.atZone(zid);
    LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    LocalDateTime ldt2 = ldt.plusMinutes(15);
    String user = UserDatabase.getCurrentUser().getUsername();
    int userId = UserDatabase.getUserId(user);

        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT * FROM appointment WHERE start BETWEEN '" + ldt + "' AND '" + ldt2 + "' AND userId='" + userId + "'";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                appointment = new Appointment (results.getInt("appointmentId"),results.getInt("customerId"),results.getInt("userId"),results.getString("title"),results.getString("description"),results.getString("location"), results.getString("contact"), results.getString("type"), results.getString("url"), results.getString("start"),results.getString("end"));
                return appointment;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
        }
        return null;
        } 
    
    
    public static ObservableList<Appointment> getMonthlyAppointments (int id) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Appointment appointment;
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT * FROM appointment WHERE customerId = '" + id + "' AND " + 
                "start >= '" + start + "' AND start <= '" + end + "'"; 
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                appointment = new Appointment (results.getInt("appointmentId"),results.getInt("customerId"),results.getInt("userId"),results.getString("title"),results.getString("description"),results.getString("location"), results.getString("contact"), results.getString("type"), results.getString("url"), results.getString("start"),results.getString("end"));
                appointments.add(appointment);
            }
            statement.close();
            return appointments;
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
            return null;
        }
    } 
    
    public static ObservableList<Appointment> getWeeklyAppoinments(int id) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Appointment appointment;
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now().plusWeeks(1);
        try {
            Statement statement = DBConnection.checkConnection().createStatement();
            String query = "SELECT * FROM appointment WHERE customerId = '" + id + "' AND " + 
                "start >= '" + begin + "' AND start <= '" + end + "'";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                appointment = new Appointment (results.getInt("appointmentId"),results.getInt("customerId"),results.getInt("userId"),results.getString("title"),results.getString("description"),results.getString("location"), results.getString("contact"), results.getString("type"), results.getString("url"), results.getString("start"),results.getString("end"));
                appointments.add(appointment);
            }
            statement.close();
            return appointments;
        } catch (SQLException e) {
            System.out.println("SQL Exception Error: " + e.getMessage());
            return null;
        }
    }
    
    
}
 

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    public static ObservableList<Appointment> getMonthlyAppointments (int id) {
//        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
//        Appointment appointment;
//        LocalDate begin = LocalDate.now();
//        LocalDate end = LocalDate.now().plusMonths(1);
//        try {
//            Statement statement = DBConnection.checkConnection().createStatement();
//            String query = "SELECT * FROM appointment WHERE customerId'" + id + "'";
//            ResultSet results = statement.executeQuery(query);
//            while(results.next()) {
//                appointment = new Appointment(results.getInt("appointmentId"), results.getInt("customerId"), results.getString("start"),
//                    results.getString("end"), results.getString("title"), results.getString("description"),
//                    results.getString("location"), results.getString("contact"));
//                appointments.add(appointment);
//            }
//            statement.close();
//            return appointments;
//        } catch (SQLException e) {
//            System.out.println("SQLException: " + e.getMessage());
//            return null;
//        }
//        
//    }
//    public static ObservableList<Appointment> getWeeklyAppoinments(int id) {
//        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
//        Appointment appointment;
//        LocalDate begin = LocalDate.now();
//        LocalDate end = LocalDate.now().plusWeeks(1);
//        try {
//            Statement statement = DBConnection.checkConnection().createStatement();
//            String query = "SELECT * FROM appointment WHERE customerId = '" + id + "' AND " + 
//                "start >= '" + begin + "' AND start <= '" + end + "'";
//            ResultSet results = statement.executeQuery(query);
//            while(results.next()) {
//                appointment = new Appointment(results.getInt("appointmentId"), results.getInt("customerId"), results.getString("start"),
//                    results.getString("end"), results.getString("title"), results.getString("description"),
//                    results.getString("location"), results.getString("contact"));
//                appointments.add(appointment);
//            }
//            statement.close();
//            return appointments;
//        } catch (SQLException e) {
//            System.out.println("SQLException: " + e.getMessage());
//            return null;
//        }
//    }
//public static Appointment appointmentIn15Min() {
//        Appointment appointment;
//        LocalDateTime now = LocalDateTime.now();
//        ZoneId zid = ZoneId.systemDefault();
//        ZonedDateTime zdt = now.atZone(zid);
//        LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
//        LocalDateTime ldt2 = ldt.plusMinutes(15);
//        String user = UserDatabase.getCurrentUser().getUsername();
//        try {
//            Statement statement = DBConnection.checkConnection().createStatement();
//            String query = "SELECT * FROM appointment WHERE start BETWEEN '" + ldt + "' AND '" + ldt2 + "' AND " + 
//                "contact='" + user + "'";
//            ResultSet results = statement.executeQuery(query);
//            if(results.next()) {
//                appointment = new Appointment(results.getInt("appointmentId"), results.getInt("customerId"), results.getString("start"),
//                    results.getString("end"), results.getString("title"), results.getString("description"),
//                    results.getString("location"), results.getString("contact"));
//                return appointment;
//            }
//        } catch (SQLException e) {
//            System.out.println("SQLException: " + e.getMessage());
//        }
//        return null;
//    }
//    


//    
//    public static boolean overlappingAppointment(int id, String location, String date, String time) {
//        String start = createTimeStamp(date, time, location, true);
//        try {
//            Statement statement = DBConnection.checkConnection().createStatement();
//            String query = "SELECT * FROM appointment WHERE start = '" + start + "' AND location = '" + location + "'";
//            ResultSet results = statement.executeQuery(query);
//            if(results.next()) {
//                if(results.getInt("appointmentId") == id) {
//                    statement.close();
//                    return false;
//                }
//                statement.close();
//                return true;
//            } else {
//                statement.close();
//                return false;
//            }
//        } catch (SQLException e) {
//            System.out.println("SQLExcpection: " + e.getMessage());
//            return true;
//        }
//    }

//    
//    public static String createTimeStamp(String date, String time, String location, boolean startMode) {
//        String h = time.split(":")[0];
//        int rawH = Integer.parseInt(h);
//        if(rawH < 9) {
//            rawH += 12;
//        }
//        if(!startMode) {
//            rawH += 1;
//        }
//        String rawD = String.format("%s %02d:%s", date, rawH, "00");
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
//        LocalDateTime ldt = LocalDateTime.parse(rawD, df);
//        ZoneId zid;
//        if(location.equals("New York")) {
//            zid = ZoneId.of("America/New_York");
//        } else if(location.equals("Phoenix")) {
//            zid = ZoneId.of("America/Phoenix");
//        } else {
//            zid = ZoneId.of("Europe/London");
//        }
//        ZonedDateTime zdt = ldt.atZone(zid);
//        ZonedDateTime utcDate = zdt.withZoneSameInstant(ZoneId.of("UTC"));
//        ldt = utcDate.toLocalDateTime();
//        Timestamp ts = Timestamp.valueOf(ldt); 
//        return ts.toString();
//    }
    

