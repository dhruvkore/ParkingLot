package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {

    ParkingLot parkingLot;
    List<List<String>> parkingInitialization;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        parkingInitialization = new ArrayList<>();

        // Row 1 -> R R C R
        List<String> parkinglotRow = new ArrayList<>();
        parkinglotRow.add("regular");
        parkinglotRow.add("ReGuLaR");
        parkinglotRow.add("COMPACT");
        parkinglotRow.add("Regular");

        parkingInitialization.add(parkinglotRow);

        // Row 2 -> R R R R
        parkinglotRow = new ArrayList<>();
        parkinglotRow.add("regular");
        parkinglotRow.add("regular");
        parkinglotRow.add("regular");
        parkinglotRow.add("Regular");

        parkingInitialization.add(parkinglotRow);

        // Row 3 -> R C R C
        parkinglotRow = new ArrayList<>();
        parkinglotRow.add("regular");
        parkinglotRow.add("compact");
        parkinglotRow.add("regular");
        parkinglotRow.add("compact");

        parkingInitialization.add(parkinglotRow);

        assertDoesNotThrow(() -> parkingLot = new ParkingLot(parkingInitialization));
    }

    void assertVehicleInCorrectPlace(int i, int j, SpotType spotType, String licensePlate, VehicleType vehicleType) {
        Spot currentSpot = parkingLot.spots.get(i).get(j);
        assertTrue(currentSpot.isOccupied());
        assertTrue(currentSpot.spotType == spotType);
        Vehicle currentVehicle = currentSpot.vehicle;

        assertTrue(currentVehicle.plateNumber.equals(licensePlate));
        assertTrue(currentVehicle.vehicleType == vehicleType);
    }

    void assertVehicleRemoved(int i, int j, String licensePlate) {
        assertFalse(parkingLot.licensePlateToSpot.containsKey(licensePlate));
        assertNull(parkingLot.spots.get(i).get(j).vehicle);
    }

    @Test
    void PopulateParkingLotTest() {
        // Before each populates parkinglot
        assertTrue(parkingLot.isLotCompletelyEmpty());
        parkingLot.print();
    }

    @Test
    void PopulateFullParkingLot() {
        IntStream.range(0, 12).forEach((i) -> parkingLot.addVehicle(new Vehicle("MOTORCYCLE", Integer.valueOf(i).toString())));

        assertTrue(parkingLot.isLotCompletelyFull());
    }

    @Test
    void AddThenRemoveMotorcycleToParkingLot() {
        parkingLot.print();

        parkingLot.addVehicle(new Vehicle("MOTORCYCLE", "1234"));
        parkingLot.print();

        assertVehicleInCorrectPlace(0, 0, SpotType.REGULAR, "1234", VehicleType.MOTORCYCLE);

        parkingLot.removeVehicle("1234");
        parkingLot.print();
    }

    @Test
    void AddThenRemoveCarToParkingLot() {
        parkingLot.print();

        parkingLot.addVehicle(new Vehicle("CAR", "4321"));
        parkingLot.print();

        assertVehicleInCorrectPlace(0, 0, SpotType.REGULAR, "4321", VehicleType.CAR);

        parkingLot.removeVehicle("4321");
        parkingLot.print();
    }

    @Test
    void AddThenRemoveVanToParkingLot() {
        parkingLot.print();

        parkingLot.addVehicle(new Vehicle("VAN", "3213"));
        parkingLot.print();

        assertVehicleInCorrectPlace(0, 0, SpotType.REGULAR, "3213", VehicleType.VAN);
        assertVehicleInCorrectPlace(1, 0, SpotType.REGULAR, "3213", VehicleType.VAN);

        parkingLot.removeVehicle("3213");
        parkingLot.print();
    }


    @Test
    void AddManyVehiclesToParkingLot() {
        parkingLot.print();

        parkingLot.addVehicle(new Vehicle("CAR", "1234"));
        parkingLot.addVehicle(new Vehicle("VAN", "1236"));
        parkingLot.addVehicle(new Vehicle("CAR", "1237"));
        parkingLot.addVehicle(new Vehicle("MOTORCYCLE", "1238"));
        parkingLot.addVehicle(new Vehicle("MOTORCYCLE", "4321"));
        parkingLot.print();

        assertVehicleInCorrectPlace(0, 0, SpotType.REGULAR, "1234", VehicleType.CAR);
        assertVehicleInCorrectPlace(0, 1, SpotType.REGULAR, "1236", VehicleType.VAN);
        assertVehicleInCorrectPlace(1, 1, SpotType.REGULAR, "1236", VehicleType.VAN);
        assertVehicleInCorrectPlace(0, 3, SpotType.REGULAR, "1237", VehicleType.CAR);
        assertVehicleInCorrectPlace(0, 2, SpotType.COMPACT, "1238", VehicleType.MOTORCYCLE);
        assertVehicleInCorrectPlace(1, 0, SpotType.REGULAR, "4321", VehicleType.MOTORCYCLE); // Motorcycle in Regular spot


        // Get Occupied Spot Counts
        Map<SpotType, Integer> occupiedCounts = parkingLot.totalNumberOfSpotsByType(List.of(SpotType.REGULAR, SpotType.COMPACT, SpotType.COMPACT), true);
        assertEquals(5, occupiedCounts.get(SpotType.REGULAR));
        assertEquals(1, occupiedCounts.get(SpotType.COMPACT));

        // Get Remaining Spot Counts
        Map<SpotType, Integer> emptyCounts = parkingLot.totalNumberOfSpotsByType(List.of(SpotType.REGULAR, SpotType.COMPACT, SpotType.COMPACT), false);
        assertEquals(4, emptyCounts.get(SpotType.REGULAR));
        assertEquals(2, emptyCounts.get(SpotType.COMPACT));

        // Validate counts by vehicle type

        Map<VehicleType, Integer> occupiedCountsByVehicleType = parkingLot.totalNumberOfSpotsByVehicleType();
        assertEquals(2, occupiedCountsByVehicleType.get(VehicleType.CAR));
        assertEquals(2, occupiedCountsByVehicleType.get(VehicleType.VAN)); // 2 spots for 1 VAN
        assertEquals(2, occupiedCountsByVehicleType.get(VehicleType.MOTORCYCLE));

        parkingLot.print();

        parkingLot.removeVehicle("1236"); // Remove Van from 2 spots
        assertVehicleRemoved(0, 1, "1236");
        assertVehicleRemoved(1, 1, "1236");

        parkingLot.removeVehicle("1238"); // Remove Motorcycle
        assertVehicleRemoved(0, 2, "1238");

        parkingLot.print();
    }

    @Test
    void AddMoreVehiclesThanSpots() {
        parkingLot.print();

        parkingLot.addVehicle(new Vehicle("VAN", "1234"));
        parkingLot.addVehicle(new Vehicle("VAN", "1236"));
        parkingLot.addVehicle(new Vehicle("VAN", "1237"));
        parkingLot.addVehicle(new Vehicle("VAN", "1238"));

        parkingLot.addVehicle(new Vehicle("VAN", "4321")); // Cannot park this Car
        assertFalse(parkingLot.licensePlateToSpot.containsKey("4321")); // Assert not parked
    }

    @Test
    void RemoveNonExistantVehicle() {
        parkingLot.removeVehicle("1234567890");
    }

    @Test
    void TotalNumberOfSpots() {
        assertEquals(parkingLot.totalNumberOfSpots(), 12);
    }
}