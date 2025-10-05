-- AVANT Take Home Assessment Submissions --- Dhruv Kore --
Parking Lot Application

Instructions:
`mvn install`

The above should run all tests and have majority test coverage.

Design:
- The ParkingLot app consists of Spots. Vehicles can be assigned to each of the spots.

Assumptions:
- Van needs to be a vertical placement in spots
- This system acts as a base for a backend API. Further details would be displayed in the front end. For example, the entire spot is returned but in the front end, the application would only show spotID and licenseplate
- There are no considerations about billing and duration of parked vehicle

Improvements:
- Query for count scans all the spots but ideally, the count is calculated when every vehicle is parked or unparked
- Query for total parked cars can be found by count in the licensePlateToSpot HashMap
- The exceptions would ideally be thrown and handled if this were a rest API. Exceptions are usually handled at the top Rest API layer
- For a simple use case, I have used Switch Cases but for complicated cases, one my consider a rules engine design pattern.


