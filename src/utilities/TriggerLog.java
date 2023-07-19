package utilities;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TriggerLog {
    
    public TriggerLog() {
    }
    
    // Cature Log Entry
    public static void log (String userName, boolean success) {
        Logger log = Logger.getLogger("log.txt");
        
        try {
            FileHandler fileHandler = new FileHandler("log.txt", true);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            log.addHandler(fileHandler); 
        } catch (IOException | SecurityException ex) {
            Logger.getLogger("log.txt").log(Level.SEVERE, null, ex);
        }

        if(success) {
            Logger.getLogger("log.txt").log(Level.INFO,"Login Attempt: UserName: " + userName + " has been verified");
        }else {
            Logger.getLogger("log.txt").log(Level.INFO,"Failed Login Attempt: UserName: " + userName);
        }
    }
}
