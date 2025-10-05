package org.example;

public class Vehicle {
    String plateNumber;
    VehicleType vehicleType;

    public Vehicle (String vehicleType, String plateNumber) {
        switch (vehicleType.toUpperCase()) {
            case "MOTORCYCLE":
                this.vehicleType = VehicleType.MOTORCYCLE;
                break;
            case "CAR":
                this.vehicleType = VehicleType.CAR;
                break;
            case "VAN":
                this.vehicleType = VehicleType.VAN;
                break;
            default:
                System.out.println("Invalid Vehicle Type");
        }

        this.plateNumber = plateNumber;
    }
}
