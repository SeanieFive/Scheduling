package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

public final class Address {
    private int addressId;
    private String addressLine1;
    private String addressLine2;
    private int addressCityId;
    private String postalCode;
    private String phone;
    
    public Address() {   
    }

    public Address(int id, String address, String address2, int cityId, String postalCode, String phone) {
        this.addressId = id;
        this.addressLine1 = address;
        this.addressLine2 = address2;
        this.addressCityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
    }
    
    
    public int getAddressId() {
        return addressId;
    }
    
    public void setAddressId(int id) {
        this.addressId = id;
    }
    
    public String getAddressLine1() {
        return addressLine1;
    }
    
    public void setAddressLine1(String address) {
        this.addressLine1 = (address);
    }

    public String getAddressLine2() {
        return addressLine2;
    }
    
    public void setAddressLine2(String address2) {
        this.addressLine2 = address2;
    }
    
    public int getAddressCityId() {
        return addressCityId;
    }
    
    public void setAddressCityId(int cityId) {
        this.addressCityId = cityId;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
