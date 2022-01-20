package com.example.mvc_motors;

public class Rides {

    private String Ride, NumberOfVehicles, Price;

    public Rides() {}

    public Rides(String ride, String numberOfVehicles, String price){
        this.Ride = ride;
        this.NumberOfVehicles = numberOfVehicles;
        this.Price = price;
    }

    public String getRide() {
        return Ride;
    }

    public void setRide(String ride) {
        this.Ride = ride;
    }

    public String getNumberOfVehicles() {
        return NumberOfVehicles;
    }

    public void setNumberOfVehicles(String numberOfVehicles) {
        this.NumberOfVehicles = numberOfVehicles;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }


}
