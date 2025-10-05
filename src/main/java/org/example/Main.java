package org.example;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome!");

        ParkingLot parkingLot;
        List<List<String>> parkingInitialization = new ArrayList<>();

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

        // Initialize Parking Lot
        try {
            parkingLot = new ParkingLot(parkingInitialization);
            parkingLot.print();
        } catch (Exception e) {
            System.out.println("Invalid Initialization of Parking Lot structure");
        }

    }
}