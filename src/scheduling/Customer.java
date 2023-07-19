package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

public class Customer {


    private int customerId;
    private String customerName;
    private int customerAddressId;
    
    public Customer() {
    }
    
    public Customer(int customerId, String customerName, int customerAddressId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddressId = customerAddressId;
    }
     
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerAddressId() {
        return customerAddressId;
    }

    public void setCustomerAddressId(int customerAddressId) {
        this.customerAddressId = customerAddressId;
    }
    
}
