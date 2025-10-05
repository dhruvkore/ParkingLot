package org.example;

public class Spot {
    public String spotID;
    public SpotType spotType;
    public Vehicle vehicle;

    public Spot(String parkingSpot, String spotID) {
        this.spotID = spotID;
        switch (parkingSpot.toUpperCase()) {
            case "REGULAR":
                this.spotType = SpotType.REGULAR;
                break;
            case "COMPACT":
                this.spotType = SpotType.COMPACT;
                break;
            default:
//                System.out.println("Invalid Parking Spot Type");
//                System.out.println(parkingSpot.toUpperCase());
                System.out.println("Invalid Parking Spot Type:" + parkingSpot);
        }
    }

    public Boolean isOccupied() {
        return this.vehicle != null;
    }

    public void parkVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
