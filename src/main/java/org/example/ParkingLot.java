package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    List<List<Spot>> spots;
    Map<String, List<Spot>> licensePlateToSpot;

    public ParkingLot(List<List<String>> parkingSpots) throws Exception {
        this.spots = new ArrayList<>();
        for (int i = 0; i < parkingSpots.size(); i++) {
            List<String> parkingRows = parkingSpots.get(i);

            List<Spot> currentRow = new ArrayList<>();
            for (int j = 0; j < parkingSpots.get(0).size(); j++) {
                String parkingSpot = parkingRows.get(j);
                currentRow.add(new Spot(parkingSpot, i + "-" + j));
            }
            spots.add(currentRow);
        }

        this.licensePlateToSpot = new HashMap<>();
    }

    public List<Spot> addVehicle(Vehicle vehicle) {
        if (licensePlateToSpot.containsKey(vehicle.plateNumber)) { // Already Parked return existing spots
            System.out.println("\"" + vehicle.plateNumber + "\"  already parked.");
            return licensePlateToSpot.get(vehicle.plateNumber);
        }

        // Get Valid Spots to park
        List<Spot> availableSpots = findSpot(vehicle);

        if (availableSpots.isEmpty()) {
            System.out.println("Could not park \"" + vehicle.plateNumber + "\". No spot available in parkinglot.");
            return List.of();
        }

        // Park Vehicle in Spot(s)
        availableSpots.stream()
                .forEach((spot) -> {
                    spot.parkVehicle(vehicle);
                });
        licensePlateToSpot.put(vehicle.plateNumber, availableSpots);

        return availableSpots;
    }

    public List<Spot> removeVehicle(String licensePlate) {
        List<Spot> currentSpots = licensePlateToSpot.getOrDefault(licensePlate, List.of());
        currentSpots.stream().forEach((spot) -> spot.vehicle = null);

        if (currentSpots.isEmpty()) {
            System.out.println("Could not remove. Vehicle \"" + licensePlate + "\" not currently parked.");
            return List.of();
        } else {
            licensePlateToSpot.remove(licensePlate);
            System.out.println("\"" + licensePlate + "\" removed from parkinglot.");
            return currentSpots;
        }
    }

    public List<Spot> findSpot(Vehicle vehicle) {
        List<SpotType> validSpotTypes = new ArrayList<>();

        switch (vehicle.vehicleType) {
            case VehicleType.MOTORCYCLE:
                validSpotTypes.add(SpotType.COMPACT);
                validSpotTypes.add(SpotType.REGULAR);
                break;
            case VehicleType.CAR:
            case VehicleType.VAN:
                validSpotTypes.add(SpotType.REGULAR);
                break;
            default:
                System.out.println("Invalid Vehicle: " + vehicle.plateNumber);
                return List.of();
        }

        for (int i = 0; i < spots.size(); i++) {
            for (int j = 0; j < spots.get(0).size(); j++) {
                Spot currentSpot = spots.get(i).get(j);
                if (!currentSpot.isOccupied() && validSpotTypes.contains(currentSpot.spotType)) {
                    if (vehicle.vehicleType == VehicleType.VAN) {
                        Spot adjacentSpot = getAdjacentSpot(i, j);
                        if (adjacentSpot != null) {
                            return List.of(currentSpot, adjacentSpot);
                        }
                    } else { // Adjacent not required for non-Van
                        return List.of(currentSpot);
                    }
                }
            }
        }

        return List.of();
    }

    private Spot getAdjacentSpot(int i, int j) {
        // Assumption Two contiguous spaces means vertically connecting because VAN is longer
        if (i < spots.size() - 1) {
            Spot adjacentSpot = spots.get(i + 1).get(j); // Get spot below
            if (!adjacentSpot.isOccupied() && adjacentSpot.spotType == SpotType.REGULAR) {
                return adjacentSpot;
            }
        }

        return null;
    }

    public void print() {
        System.out.println();

        for (List<Spot> row : spots) {
            for (Spot s : row) {

                String output = "";
                if (s.vehicle != null) {
                    switch (s.vehicle.vehicleType) {
                        case VehicleType.MOTORCYCLE:
                            output = "*M*";
                            break;
                        case VehicleType.CAR:
                            output = "*C*";
                            break;
                        case VehicleType.VAN:
                            output = "*V*";
                            break;
                        default:
                            break;
                    }
                } else {
                    output = s.spotType == SpotType.REGULAR ? "Reg" : "Com";
                }

                System.out.print(output + " | ");
            }
            System.out.println();
        }
    }

    public Integer totalNumberOfSpots() {
        return spots.size() * spots.get(0).size();
    }

    public Map<SpotType, Integer> totalNumberOfSpotsByType(List<SpotType> spotTypes, Boolean isOccupied) {
        HashMap<SpotType, Integer> countOfSpots = new HashMap<>();
        for (SpotType spotType : spotTypes) {
            countOfSpots.put(spotType, 0);
        }

        spots.stream()
                .forEach((spotRow) -> spotRow.forEach((spot) ->
                {
                    if (spot.isOccupied() == isOccupied) {
                        Integer currentCount = countOfSpots.get(spot.spotType);
                        countOfSpots.put(spot.spotType, currentCount + 1);
                    }
                }));

        return countOfSpots;
    }

    public Map<VehicleType, Integer> totalNumberOfSpotsByVehicleType() {
        List<VehicleType> vehicleTypes = List.of(VehicleType.CAR, VehicleType.VAN, VehicleType.MOTORCYCLE);
        HashMap<VehicleType, Integer> countOfSpots = new HashMap<>();
        for (VehicleType vehicleType : vehicleTypes) {
            countOfSpots.put(vehicleType, 0);
        }

        spots.stream()
                .forEach((spotRow) -> spotRow.forEach((spot) ->
                {
                    if (spot.isOccupied() && vehicleTypes.contains(spot.vehicle.vehicleType)) {
                        Integer currentCount = countOfSpots.get(spot.vehicle.vehicleType);
                        countOfSpots.put(spot.vehicle.vehicleType, currentCount + 1);
                    }
                }));

        return countOfSpots;
    }

    public Boolean isLotCompletelyFull() {
        Integer occupiedCount = totalNumberOfSpotsByType(List.of(SpotType.REGULAR, SpotType.COMPACT), true)
                .values()
                .stream()
                .mapToInt(integer -> integer)
                .sum();

        return occupiedCount.equals(totalNumberOfSpots());
    }

    public Boolean isLotCompletelyEmpty() {
        Integer emptyCount = totalNumberOfSpotsByType(List.of(SpotType.REGULAR, SpotType.COMPACT), false)
                .values()
                .stream()
                .mapToInt(integer -> integer)
                .sum();

        return emptyCount.equals(totalNumberOfSpots());
    }
}
