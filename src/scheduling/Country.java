package scheduling;

/*
* Author: Sean Kenney
* Student ID: 001041212
* GZP1 TASK 1: JAVA APPLICATION DEVELOPMENT
*/

public class Country {
    private int countryId;
    private String country;
    
    public Country() {
    
}

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Country(int countryId) {
        this.countryId = countryId;
    }
    
    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }
}
